package com.crypto.croytowallet.SharedPrefernce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.crypto.croytowallet.Model.OfferModel;
import com.crypto.croytowallet.login.Login;

public class OfferSharedPrefManager {
    private static final String SHARED_PREF_NAME = "Prebhat";
    private static final String KEY_NAME = "Offer_name";
    private static final String KEY_Short = "ShortOffer_Des";
    private static final String KEY_LONG = "LongOffer_Des";
    private static final String KEY_Title = "Offer_title";
    private static final String KEY_Image = "OfferImage";
    private static final String KEY_Date = "OfferDate";
      private static OfferSharedPrefManager mInstance;

    private static Context mCtx;

    private OfferSharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized OfferSharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new OfferSharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void AddOfferData(OfferModel user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, user.getOffer_name());
        editor.putString(KEY_Short, user.getShortDescription());
        editor.putString(KEY_LONG, user.getLongDescription());
        editor.putString(KEY_Date, user.getDate());
        editor.putString(KEY_Image, user.getImageUrl());
        editor.putString(KEY_Title, user.getOffer_tittle());


          editor.apply();
    }

    //this method will give the logged in user
    public OfferModel GetOfferData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new OfferModel(
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_Short, null),
                sharedPreferences.getString(KEY_LONG, null),
                sharedPreferences.getString(KEY_Date, null),
                sharedPreferences.getString(KEY_Image, null),
                sharedPreferences.getString(KEY_Title, null)

        );
    }

    //this method will logout the user
    public void ClearData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();


/*        mCtx.startActivity(new Intent(mCtx, Login_Activity.class));
        mCtx.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
 */
    }

}
