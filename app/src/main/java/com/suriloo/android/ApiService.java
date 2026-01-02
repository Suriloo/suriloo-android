package com.suriloo.android;

import com.suriloo.android.model.JwtResponse;
import com.suriloo.android.model.LoginRequest;
import com.suriloo.android.model.SignUpRequest;
import com.suriloo.android.model.TokenRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/auth/google")
    Call<JwtResponse> sendToken(@Body TokenRequest tokenRequest);

    @POST("/auth/api/user/login")
    Call<JwtResponse> login(@Body LoginRequest loginRequest);

    @POST("/auth/api/user/signup")
    Call<ResponseBody> signup(@Body SignUpRequest signUpRequest);

}
