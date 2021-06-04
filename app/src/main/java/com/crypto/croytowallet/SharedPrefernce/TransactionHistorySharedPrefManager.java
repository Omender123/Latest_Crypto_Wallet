package com.crypto.croytowallet.SharedPrefernce;

import android.content.Context;
import android.content.SharedPreferences;

public class TransactionHistorySharedPrefManager {
    private static final String SHARED_PREF_NAME = "Prebhat";
    private static final String KEY_ID = "keyid";
    private static final String KEY_STATUS = "keystatus";
    private static final String KEY_AMTOFCRYPTO = "keyamount";
    private static final String KEY_SENDER_NAME = "keySenderName";
    private static final String KEY_RECEVIER_NAME = "keyReciverName";
    private static final String KEY_TIME= "keyTime";
    private static final String KEY_REWARDS= "KEY_REWARDS";
    private static final String KEY_TYPE= "KEY_TYPE";

    private static TransactionHistorySharedPrefManager mInstance;
    private static Context mCtx;

    private TransactionHistorySharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized TransactionHistorySharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TransactionHistorySharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void Transaction_History_Data(Transaction_HistoryModel user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getId());
        editor.putString(KEY_STATUS, user.getStatus());
        editor.putString(KEY_AMTOFCRYPTO, user.getAmtOfCrypto());
        editor.putString(KEY_SENDER_NAME, user.getSenderName());
        editor.putString(KEY_RECEVIER_NAME, user.getReciverName());
        editor.putString(KEY_TIME, user.getTime());
        editor.putString(KEY_REWARDS, user.getRewards());
        editor.putString(KEY_TYPE, user.getType());

        editor.apply();
    }


    //this method will give the logged in user
    public Transaction_HistoryModel getTransaction_History() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return new Transaction_HistoryModel(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_STATUS, null),
                sharedPreferences.getString(KEY_AMTOFCRYPTO, null),
                sharedPreferences.getString(KEY_SENDER_NAME, null),
                sharedPreferences.getString(KEY_RECEVIER_NAME, null),
                sharedPreferences.getString(KEY_TIME, null),
                sharedPreferences.getString(KEY_REWARDS, null),
                sharedPreferences.getString(KEY_TYPE, null)
        );
    }

    //this method will logout the user
    public void clearPearData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();


/*        mCtx.startActivity(new Intent(mCtx, Login_Activity.class));
        mCtx.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
 */
    }
}
