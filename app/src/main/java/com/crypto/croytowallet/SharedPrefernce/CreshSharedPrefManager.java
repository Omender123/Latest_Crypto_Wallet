package com.crypto.croytowallet.SharedPrefernce;

import android.content.Context;
import android.content.SharedPreferences;

public class CreshSharedPrefManager {
    private static final String SHARED_PREF_NAME = "Carsh";
    private static final String KEY_USERNAME = "keyuserName";
    private static final String KEY_PASSWORD = "keypassword";
    private static CreshSharedPrefManager mInstance;

    private static Context mCtx;
    private CreshSharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized CreshSharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CreshSharedPrefManager(context);
        }
        return mInstance;
    }

    public void SetCrashData(CrashDataModel user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_PASSWORD, user.getPassword());
           editor.apply();
    }
    public CrashDataModel getCreshData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new CrashDataModel(
                 sharedPreferences.getString(KEY_USERNAME, null),
                 sharedPreferences.getString(KEY_PASSWORD, null)
                 );
    }

}
