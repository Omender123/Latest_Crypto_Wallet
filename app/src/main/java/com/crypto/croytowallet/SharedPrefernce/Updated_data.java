package com.crypto.croytowallet.SharedPrefernce;

import android.content.Context;
import android.content.SharedPreferences;

public class Updated_data {

    private static Updated_data minst;
    private static Context mct;
    private static final String LATITUDE="LATITUDE";
    private static final String LONGITUDE="LONGITUDE";
    private static final String SHARD_PERFNAME="myshardperf624";
    private static final String KEY_ID="id";

    private static final String KEY_Coin_ID="Coin_id";

    private static final String KEY_TripID="tid";

    private static final String Device="divce";
    private static final String KEY_PICTURE="picture";
    private static final String KEY_USERNAME="name";
    private static final String KEY_BOOKING_ID="booking_id";
    private static final String KEY_USER_ID="user_id";
    private static final String KEY_EMAIL="email";
    private static final String NAME="first_name";
    private static final String KEY_LAST_NAME="last_name";
    private static final String KEY_MOBILE="mobile";

    private static final String ADDHOME="setAddHome";


    private static final String  PRICE="12";
    private static final String IMAGE="Image";
    private static final String CHANGE="change";




    private Updated_data(Context context){
        mct=context;
    }
    public static synchronized Updated_data getInstans(Context context){
        if (minst==null){
            minst=new Updated_data(context);
        }
        return minst;
    }


    public boolean userLogin(int id, String coinname, String symbol,String image,String change,String  price,String CoinId){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(KEY_ID,id);
        editor.putString(NAME,coinname);
        editor.putString(KEY_MOBILE,symbol);
        editor.putString(IMAGE,image);
        editor.putString(CHANGE,change);
        editor.putString(PRICE,price);
        editor.putString(KEY_Coin_ID,CoinId);




        editor.apply();
        return true;
    }

    public boolean Service_id(String id){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(KEY_Coin_ID,id);



        editor.apply();
        return true;
    }


    public boolean tripid(String id){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(KEY_TripID,id);



        editor.apply();
        return true;
    }



    public boolean Devicetoken(String id){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Device,id);



        editor.apply();
        return true;
    }
    public boolean fatchdata( String mobile ){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString(KEY_MOBILE,mobile);



        editor.apply();
        return true;
    }

    public boolean fatchimage ( String image ){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString(IMAGE,image);
        editor.apply();
        return true;
    }

    public boolean fatchchange ( String change ){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString(CHANGE,change);
        editor.apply();
        return true;
    }

    public boolean fatchprice ( Integer price ){

        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putInt(PRICE,price);
        editor.apply();
        return true;
    }


    public static boolean getInstance(String setAddHome){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(ADDHOME,setAddHome);


        return true;
    }




    public boolean FatchTrips(String booking_id, String user_id){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString(KEY_BOOKING_ID,booking_id);
        editor.putString(KEY_USER_ID,user_id);



        editor.apply();
        return true;
    }





    public boolean isLogin(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID, null) != null;
    }
    public boolean logout(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;

    }




    public int getUserId(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID,-1);

    }

    public String getCoinId(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_Coin_ID,null);

    }

    public String getUsername(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(NAME,null);

    }

    public String  getprice(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PRICE,"");

    }
    public String getChange(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CHANGE,null);

    }
    public String getImage(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(IMAGE,null);

    }

    public String getmobile(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_MOBILE,null);

    }




}