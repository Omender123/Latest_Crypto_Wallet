package com.crypto.croytowallet.SharedPrefernce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.crypto.croytowallet.Model.SwapRespoinseModel;
import com.crypto.croytowallet.login.Login;

public class SwapResponsePrefernce {
    private static final String SHARED_PREF_NAME = "IMX";
    private static final String KEY_TransID = "keyTransID";
    private static final String KEY_Status= "keyStatus";
    private static SwapResponsePrefernce mInstance;
    private static Context mCtx;

    private SwapResponsePrefernce(Context context) {
        mCtx = context;
    }

    public static synchronized SwapResponsePrefernce getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SwapResponsePrefernce(context);
        }
        return mInstance;
    }
    public void SetData(SwapRespoinseModel user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TransID, user.getTransId());
        editor.putString(KEY_Status, user.getStatus());
        editor.apply();
    }

    public SwapRespoinseModel getData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new SwapRespoinseModel(
                sharedPreferences.getString(KEY_TransID, null),
                sharedPreferences.getString(KEY_Status, null)
        );
    }

    public void ClearData(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }
}
