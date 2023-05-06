package com.android.beyikyolsms;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.android.beyikyolsms.Database.SMS;
import com.android.beyikyolsms.Database.SmSDB;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySmsReceiver extends BroadcastReceiver {
    private static final String TAG =
            MySmsReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM =
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show.
                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                strMessage += " :" + msgs[i].getMessageBody() + "\n";
                // Log and display the SMS message.
                Log.d(TAG, "onReceive: " + strMessage);
                long sms_id = System.currentTimeMillis();
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = sdf.format(new Date());

                    SMS sms = new SMS(1, msgs[i].getOriginatingAddress(), msgs[i].getMessageBody(), CONSTANT.PENDING, sms_id + "",date);
                    new SmSDB(context).insertSms(sms);
                    MainActivity.get().getAllSmsHistory(3);
                } catch (Exception ex){
                    ex.printStackTrace();
                }

                try {
                    acceptNumber(msgs[i].getOriginatingAddress(),context,sms_id+"");
                } catch (Exception ex){}
            }
        }
    }

    private void acceptNumber(String number,Context context,String smsid){
        Call<AcceptNumber> call = APIClient.getClient(context).create(ApiInterface.class).acceptNumber(new AcceptNumber(number));
        call.enqueue(new Callback<AcceptNumber>() {
            @Override
            public void onResponse(Call<AcceptNumber> call, Response<AcceptNumber> response) {
                if(response.isSuccessful()){
                    try {
                        new SmSDB(context).updateStatus(CONSTANT.DELIVERY,smsid);
                        MainActivity.get().getAllSmsHistory(3);
                    } catch (Exception ex){}
                } else {
                    try {
                        new SmSDB(context).updateStatus(CONSTANT.FAILED,smsid);
                        MainActivity.get().getAllSmsHistory(3);
                    } catch (Exception ex){}
                }
                Toast.makeText(context, ""+response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AcceptNumber> call, Throwable t) {
                try {
                    new SmSDB(context).updateStatus(CONSTANT.FAILED,smsid);
                    MainActivity.get().getAllSmsHistory(3);
                } catch (Exception ex){}
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
