package com.crypto.croytowallet.database;

import android.content.Context;

import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;

import java.io.IOException;


import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
   private static final String BASE_URL="https://api.imx.global/api/";
  // private static final String BASE_URL="http://13.233.136.56:8080/api/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;
    private Context context;


    public RetrofitClient() {
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public  static synchronized RetrofitClient getInstance()
    {
        if (mInstance==null)
        {
            mInstance=new RetrofitClient();

        }
        return mInstance;
    }
    public Api getApi()
    {
        return retrofit.create(Api.class);

    }

}
