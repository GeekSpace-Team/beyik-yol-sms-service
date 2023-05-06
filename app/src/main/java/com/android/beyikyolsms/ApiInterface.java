package com.android.beyikyolsms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("mobile-auth/accept-number")
    Call<AcceptNumber> acceptNumber(@Body AcceptNumber body);
}
