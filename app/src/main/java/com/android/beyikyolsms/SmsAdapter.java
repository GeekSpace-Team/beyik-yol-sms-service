package com.android.beyikyolsms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.android.beyikyolsms.Database.SMS;

import java.util.ArrayList;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {
    private ArrayList<SMS> sms = new ArrayList<>();
    private Context context;

    public SmsAdapter(ArrayList<SMS> sms, Context context) {
        this.sms = sms;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.sms_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SMS sm = sms.get(position);
        holder.phone.setText(sm.getPhone_number());
        holder.sms.setText(sm.getSms());
        holder.status.setText(sm.getStatus());
        holder.smsDate.setText(sm.getSms_date());
        if(position%2==0){
            holder.bg.setBackgroundColor(context.getResources().getColor(R.color.inactive));
        } else{
            holder.bg.setBackgroundColor(context.getResources().getColor(R.color.blackTransParent));
        }

        if (sm.getStatus().equals(CONSTANT.PENDING)) {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.inactive));
        }
        else if (sm.getStatus().equals(CONSTANT.SENT)) {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.sent));
        }
        else if (sm.getStatus().equals(CONSTANT.NOT_DELIVERY)) {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.textColor));
        }
        else if (sm.getStatus().equals(CONSTANT.DELIVERY)) {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.second));
        }
        else if (sm.getStatus().equals(CONSTANT.FAILED)) {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.error));
        }
        else {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.error));
        }
    }

    @Override
    public int getItemCount() {
        return sms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView phone, sms, status,smsDate;
        RelativeLayout bg;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.bg);
            phone = itemView.findViewById(R.id.phone);
            sms = itemView.findViewById(R.id.sms);
            status = itemView.findViewById(R.id.status);
            card = itemView.findViewById(R.id.card);
            smsDate = itemView.findViewById(R.id.smsDate);
        }
    }
}
