package com.android.beyikyolsms;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;

    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(1800, TimeUnit.SECONDS)
            .writeTimeout(1800,TimeUnit.SECONDS)
            .connectTimeout(1800, TimeUnit.SECONDS)
            .build();


    public static Retrofit getClient(Context context) {
        return new Retrofit.Builder()
                .baseUrl("https://beyikyol.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static void setPreference(String name, String value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, MODE_PRIVATE).edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String getSharedPreference(Context context, String name) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(name, MODE_PRIVATE);
            String value = prefs.getString(name, "");
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }
}