package com.crypto.croytowallet.SharedPrefernce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.crypto.croytowallet.Model.SwapModel;
import com.crypto.croytowallet.login.Login;

public class SwapSharedPrefernce {
    private static SwapSharedPrefernce mInstance;
    private static Context mCtx;
    private static final String SHARED_PREF_NAME = "swap";
    private static final String KEY_SEND = "keysend";
    private static final String KEY_RECEIVED= "keyreceived";
    private static final String KEY_COINPRICE = "keycoinPrice";
    private static final String KEY_CURRENCY_TYPE = "keycurrency_type";
    private static final String KEY_CURRENCY_SYMBOLS = "keycurrency_Symbol";
    private static final String KEY_COIN_AMOUNT = "keycoinAmount";
    private static final String KEY_ENTER_AMOUNT = "keyenterAmount";
    private static final String KEY_VALUE = "keyValue";
    private static final String KEY_userBalance = "keyUserBalance";
    private static final String KEY_Total = "keyTotal";
    private static final String KEY_TYPE = "KeyType";
    private static final String KEY_COIN_TYPE = "KEY_COIN_TYPE";

    private SwapSharedPrefernce(Context context) {
        mCtx = context;
    }

    public static synchronized SwapSharedPrefernce getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SwapSharedPrefernce(context);
        }
        return mInstance;
    }

    public void SetData(SwapModel user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SEND, user.getSendData());
        editor.putString(KEY_RECEIVED, user.getReceivedData());
        editor.putString(KEY_COINPRICE, user.getCoinPrice());
        editor.putString(KEY_CURRENCY_TYPE, user.getCurrencyType());
        editor.putString(KEY_CURRENCY_SYMBOLS, user.getCurrencySymbol());
        editor.putString(KEY_COIN_AMOUNT, user.getCoinAmount());
        editor.putString(KEY_ENTER_AMOUNT, user.getEnterAmount());
        editor.putString(KEY_userBalance, user.getUserBalance());
        editor.putString(KEY_Total, user.getTotalAmount());
        editor.putInt(KEY_VALUE, user.getValue());
        editor.putString(KEY_TYPE, user.getType());
        editor.putString(KEY_COIN_TYPE, user.getCoinType());
        editor.apply();
    }


    public SwapModel getSwapData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new SwapModel(
                sharedPreferences.getString(KEY_SEND, null),
                sharedPreferences.getString(KEY_RECEIVED, null),
                sharedPreferences.getString(KEY_COINPRICE, null),
                sharedPreferences.getString(KEY_CURRENCY_TYPE, null),
                sharedPreferences.getString(KEY_CURRENCY_SYMBOLS, null),
                sharedPreferences.getString(KEY_COIN_AMOUNT, null),
                sharedPreferences.getString(KEY_ENTER_AMOUNT, null),
                sharedPreferences.getString(KEY_userBalance, null),
                sharedPreferences.getString(KEY_Total, null),
                sharedPreferences.getInt(KEY_VALUE, 1),
                sharedPreferences.getString(KEY_TYPE, null),
                sharedPreferences.getString(KEY_COIN_TYPE, null)

                );
    }
    public void ClearSwapData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }

}
