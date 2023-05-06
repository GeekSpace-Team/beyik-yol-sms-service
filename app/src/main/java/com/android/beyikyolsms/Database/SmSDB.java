package com.android.beyikyolsms.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SmSDB extends SQLiteOpenHelper {
    private static final String DB_NAME="sms_db";
    private static final String TB_NAME="sms_table";
    public SmSDB(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TB_NAME+" (id integer primary key, phone text,sms text,status text,sms_id text,sms_date text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(sqLiteDatabase);
    }

    public boolean insertSms (SMS sms) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", sms.getPhone_number());
        contentValues.put("sms", sms.getSms());
        contentValues.put("status", sms.getStatus());
        contentValues.put("sms_id", sms.getSms_id());
        contentValues.put("sms_date", sms.getSms_date());
        db.insert(TB_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TB_NAME+" where id="+id+"", null );
        return res;
    }

    public Cursor getAllDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TB_NAME+" ORDER BY id DESC", null );
        return res;
    }

    public boolean updateStatus(String status,String sms_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        db.update(TB_NAME, contentValues, "sms_id = ? ", new String[] { sms_id } );
        return true;
    }

    public boolean updateContact (SMS sms) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", sms.getPhone_number());
        contentValues.put("sms", sms.getSms());
        contentValues.put("status", sms.getStatus());
        contentValues.put("sms_id", sms.getSms_id());
        contentValues.put("sms_date", sms.getSms_date());
        db.update(TB_NAME, contentValues, "id = ? ", new String[] { Integer.toString(sms.getId()) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TB_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

}
