package com.crypto.croytowallet.SharedPrefernce;

import android.content.Context;
import android.content.SharedPreferences;

public class SignUpRefernace {
    private static final String SHARED_PREF_NAME = "Prebhat";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_MNEMONIC = "keymnemonic";
    private static final String KEY_GOOGLE_AUTH_KEY = "keyGoogle";
    private static final String KEY_Email = "keyEmail";

    private static SignUpRefernace mInstance;
    private static Context mCtx;

    private SignUpRefernace(Context context) {
        mCtx = context;
    }

    public static synchronized SignUpRefernace getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SignUpRefernace(context);
        }
        return mInstance;
    }

    public void UserSignUP(SignUpData user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_MNEMONIC, user.getMnemonic());
        editor.putString(KEY_GOOGLE_AUTH_KEY, user.getGoogleAuthKey());
        editor.putString(KEY_Email, user.getGmail());
        editor.apply();
    }

    //this method will give the logged in user
    public SignUpData getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new SignUpData(
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_MNEMONIC, null),
                sharedPreferences.getString(KEY_GOOGLE_AUTH_KEY, null),
                sharedPreferences.getString(KEY_Email, null)
                );
    }
}

