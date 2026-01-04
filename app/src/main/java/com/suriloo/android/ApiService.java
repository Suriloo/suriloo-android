package com.suriloo.android;

import com.suriloo.android.home.Content;
import com.suriloo.android.model.JwtResponse;
import com.suriloo.android.model.LoginRequest;
import com.suriloo.android.model.SignUpRequest;
import com.suriloo.android.model.TokenRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("/auth/google")
    Call<JwtResponse> sendToken(@Body TokenRequest tokenRequest);

    @POST("/auth/api/user/login")
    Call<JwtResponse> login(@Body LoginRequest loginRequest);

    @POST("/auth/api/user/signup")
    Call<ResponseBody> signup(@Body SignUpRequest signUpRequest);

    @GET("/api/home/popular-choice")
    Call<List<Content>> getPopularContent();

    @GET("/api/home/recommended-for-you")
    Call<List<Content>> getRecommendedContent();

    @GET("/api/home/new-arrivals")
    Call<List<Content>> getNewArrivalsContent();

    @GET("/api/explore/{category}")
    Call<List<Content>> getContentByCategory(@Path("category") String category);

    @GET("/api/home/recently-watched")
    Call<List<Content>> getMostRecentlyWatched();

    @POST("/api/home/recently-watched")
    Call<ResponseBody> markAsWatched(@Body Content content);
}
