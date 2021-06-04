package com.crypto.croytowallet.SharedPrefernce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.crypto.croytowallet.login.Login;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "Prebhat";
    private static final String KEY_NAME = "keyname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_MOBILE = "keyMobile";
    private static final String KEY_ID = "keyid";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_MNEMONIC = "keymnemonic";
    private static final String KEY_REFERRAL_CODE = "keyreferral_code";
    private static final String KEY_TRANSACTION_PIN = "keytransaction_pin";
    private static final String KEY_TOKEN = "keytoken";
    private static final String KEY_ETHADDRESS = "key_ETH";
    private static final String KEY_BTCADDRESS = "key_BTC";
    private static final String KEY_LITEADDRESS = "key_LITE";
    private static final String KEY_XRPADDRESS = "key_XRP";
    private static final String KEY_EMAIL2FA = "key_EMAIL_2FA";
    private static SharedPrefManager mInstance;

    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(UserData user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getId());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_MOBILE, user.getMobile());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_MNEMONIC, user.getMnemonic());
        editor.putString(KEY_REFERRAL_CODE, user.getReferral_code());
        editor.putString(KEY_TRANSACTION_PIN, user.getTransaction_Pin());
        editor.putString(KEY_TOKEN, user.getToken());
        editor.putString(KEY_ETHADDRESS, user.getETH());
        editor.putString(KEY_BTCADDRESS, user.getBTC());
        editor.putString(KEY_LITEADDRESS, user.getLITE());
        editor.putString(KEY_XRPADDRESS, user.getXRP());

        editor.putString(KEY_EMAIL2FA, user.getEMAIL2FA());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, null) != null;
    }

    //this method will give the logged in user
    public UserData getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new UserData(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_MOBILE, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_MNEMONIC, null),
                sharedPreferences.getString(KEY_REFERRAL_CODE, null),
                sharedPreferences.getString(KEY_TRANSACTION_PIN, null),
                sharedPreferences.getString(KEY_TOKEN, null),
                sharedPreferences.getString(KEY_ETHADDRESS, null),
                sharedPreferences.getString(KEY_BTCADDRESS, null),
                sharedPreferences.getString(KEY_LITEADDRESS, null),
                sharedPreferences.getString(KEY_XRPADDRESS, null),
                sharedPreferences.getString(KEY_EMAIL2FA, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(mCtx, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mCtx.startActivity(intent);




/*        mCtx.startActivity(new Intent(mCtx, Login_Activity.class));
        mCtx.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
 */
    }
}