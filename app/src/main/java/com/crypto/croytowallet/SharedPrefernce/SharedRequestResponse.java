package com.crypto.croytowallet.SharedPrefernce;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedRequestResponse {
    private static SharedRequestResponse minst;
    private static Context mct;
    private static final String Transact_ID="Transact_ID";
    private static final String Request_ID="Request_ID";
    private static final String SHARD_PERFNAME="myshardperf624";
    private static final String Currency_type="Currency_type";
    private static final String Enter_Amount="Enter_Amount";
    private static final String IMT_U_Amount="IMT_U";
    private static final String STATUS="STATUS";
    private static final String REWARDS="REWARDS";
    private SharedRequestResponse(Context context){
        mct=context;
    }
    public static synchronized SharedRequestResponse getInstans(Context context){
        if (minst==null){
            minst=new SharedRequestResponse(context);
        }
        return minst;
    }

    public boolean SetData( String TransId, String RequestId,String EnterAmount,String IMT_amount,String  Status,String Curreny_type,String Rewards){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Transact_ID,TransId);
        editor.putString(Request_ID,RequestId);
        editor.putString(Enter_Amount,EnterAmount);
        editor.putString(IMT_U_Amount,IMT_amount);
        editor.putString(STATUS,Status);
        editor.putString(Currency_type,Curreny_type);
        editor.putString(REWARDS,Rewards);


        editor.apply();
        return true;
    }

    public boolean RemoveData(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;

    }
    public String getTransferId(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Transact_ID,null);

    }

    public String getRequest_ID(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Request_ID,null);

    }

    public String  getEnter_Amount(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Enter_Amount,"");

    }
    public String getCurrency_type(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Currency_type,null);

    }
    public String getStatus(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(STATUS,null);

    }

    public String getIMT_U_Amount(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(IMT_U_Amount,null);

    }

    public String getRewards(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(REWARDS,null);

    }
}
