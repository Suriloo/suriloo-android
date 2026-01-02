package com.suriloo.android;

import com.suriloo.android.model.JwtResponse;
import com.suriloo.android.model.TokenRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/google")
    Call<JwtResponse> sendToken(@Body TokenRequest tokenRequest);
}
