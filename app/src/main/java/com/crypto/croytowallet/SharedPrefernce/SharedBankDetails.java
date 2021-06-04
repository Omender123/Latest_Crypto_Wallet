package com.crypto.croytowallet.SharedPrefernce;

import android.content.Context;
import android.content.SharedPreferences;

import com.crypto.croytowallet.Extra_Class.ApiResponse.ResponseBankDetails;

public class SharedBankDetails {
    private static final String SHARED_PREF_NAME = "Prebhat";
    private static final String KEY_USERNAME = "keybankname";
    private static final String KEY_MNEMONIC = "keyAccountNo";
    private static final String KEY_GOOGLE_AUTH_KEY = "Ifsc Code";
    private static final String KEY_Email = "Key_HorlderName";
    private static final String KEY_Amount = "Key_Amount";

    private static SharedBankDetails mInstance;
    private static Context mCtx;

    private SharedBankDetails(Context context) {
        mCtx = context;
    }

    public static synchronized SharedBankDetails getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedBankDetails(context);
        }
        return mInstance;
    }

    public void SetBankDetails(ResponseBankDetails user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME,user.getAccountName() );
        editor.putString(KEY_MNEMONIC, user.getAccountNo());
        editor.putString(KEY_GOOGLE_AUTH_KEY, user.getBankName());
        editor.putString(KEY_Email, user.getIFSCcode());
        editor.apply();
    }

    //this method will give the logged in user
    public ResponseBankDetails getBankDetails() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new ResponseBankDetails(
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_MNEMONIC, null),
                sharedPreferences.getString(KEY_GOOGLE_AUTH_KEY, null),
                sharedPreferences.getString(KEY_Email, null)
        );
    }

}
