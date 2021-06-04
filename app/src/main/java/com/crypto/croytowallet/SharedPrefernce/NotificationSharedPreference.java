package com.crypto.croytowallet.SharedPrefernce;

import android.content.Context;
import android.content.SharedPreferences;

import com.crypto.croytowallet.Model.NoticationModel;
import com.crypto.croytowallet.Model.OfferModel;

public class NotificationSharedPreference {

    private static final String SHARED_PREF_NAME = "Prebhat";
    private static final String KEY_LANDIMAGE = "Offer_name";
    private static final String KEY_IMAGE = "ShortOffer_Des";
    private static final String KEY_DETAILS = "LongOffer_Des";
     private static NotificationSharedPreference mInstance;

    private static Context mCtx;

    private NotificationSharedPreference(Context context) {
        mCtx = context;
    }

    public static synchronized NotificationSharedPreference getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NotificationSharedPreference(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void AddNotificationData(NoticationModel user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LANDIMAGE, user.getLandingImages());
        editor.putString(KEY_IMAGE, user.getImages());
        editor.putString(KEY_DETAILS, user.getDetails());


        editor.apply();
    }

    //this method will give the logged in user
    public NoticationModel GetNotificationData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new NoticationModel(
                sharedPreferences.getString(KEY_LANDIMAGE, null),
                sharedPreferences.getString(KEY_IMAGE, null),
                sharedPreferences.getString(KEY_DETAILS, null)
        );
    }

    //this method will logout the user
    public void ClearData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();



    }

}
