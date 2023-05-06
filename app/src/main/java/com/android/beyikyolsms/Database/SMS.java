package com.android.beyikyolsms.Database;

public class SMS {
    public Integer id;
    public String phone_number;
    public String sms;
    public String status;
    public String sms_id;
    public String sms_date;

    public SMS(Integer id, String phone_number, String sms, String status, String sms_id, String sms_date) {
        this.id = id;
        this.phone_number = phone_number;
        this.sms = sms;
        this.status = status;
        this.sms_id = sms_id;
        this.sms_date = sms_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSms_id() {
        return sms_id;
    }

    public void setSms_id(String sms_id) {
        this.sms_id = sms_id;
    }

    public String getSms_date() {
        return sms_date;
    }

    public void setSms_date(String sms_date) {
        this.sms_date = sms_date;
    }
}
