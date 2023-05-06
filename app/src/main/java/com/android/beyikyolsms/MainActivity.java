package com.android.beyikyolsms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.beyikyolsms.Database.SMS;
import com.android.beyikyolsms.Database.SmSDB;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rec;
    private Context context=this;
    private EditText editText;
    private static MainActivity INSTANCE;
    private SmSDB db;
    private ArrayList<SMS> smsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // In Activity's onCreate() for instance
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        db = new SmSDB(this);
        rec=findViewById(R.id.rec);
        editText=findViewById(R.id.ipe);
        INSTANCE=this;
        askPerm();
        getAllSmsHistory(1);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                APIClient.setPreference("ip",s.toString(),context);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(!APIClient.getSharedPreference(context,"ip").isEmpty()){
            editText.setText(APIClient.getSharedPreference(context,"ip"));
        }
    }

    public static MainActivity get(){
        return INSTANCE;
    }

    private void askPerm() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS},
                    321);
        }
    }
    public void getAllSmsHistory(int page) {
        MainActivity.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = db.getAllDate();
                if (cursor.getCount() == 0) {
                    // No data
                } else {
                    smsList.clear();
                    while (cursor.moveToNext()) {
                        smsList.add(new SMS(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5)));
                    }
                    if (page == 0) {
                        rec.setLayoutManager(new LinearLayoutManager(context));
                        rec.setAdapter(new SmsAdapter(smsList, context));
                    } else {
                        try {
                            rec.getAdapter().notifyDataSetChanged();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rec.setLayoutManager(new LinearLayoutManager(context));
                            rec.setAdapter(new SmsAdapter(smsList, context));
                        }
                    }
                }
            }
        });


    }
}