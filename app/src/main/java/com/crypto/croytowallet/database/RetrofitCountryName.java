package com.crypto.croytowallet.database;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCountryName {
    private static final String BASE_URL="https://restcountries.eu/rest/v2/";
    private static RetrofitCountryName mInstance;
    private Retrofit retrofit;
    private Context context;



    public RetrofitCountryName() {
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public  static synchronized RetrofitCountryName getInstance()
    {
        if (mInstance==null)
        {
            mInstance=new RetrofitCountryName();

        }
        return mInstance;
    }
    public Api getApiCountryName()
    {
        return retrofit.create(Api.class);

    }


}
