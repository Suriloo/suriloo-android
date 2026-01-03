package com.suriloo.android;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL="http://192.168.1.29:8080";
    private static Retrofit retrofit = null;

    public static Retrofit getInstance(Context context){
        if(retrofit==null){
            HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient=new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context.getApplicationContext())) // Use application context
                    .addInterceptor(httpLoggingInterceptor)
                    .build();

            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static class AuthInterceptor implements Interceptor {
        private final Context context;

        public AuthInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
            String token = prefs.getString("jwt", null);

            Request.Builder requestBuilder = chain.request().newBuilder();
            if (token != null) {
                requestBuilder.addHeader("Authorization", "Bearer " + token);
            }

            return chain.proceed(requestBuilder.build());
        }
    }
}
