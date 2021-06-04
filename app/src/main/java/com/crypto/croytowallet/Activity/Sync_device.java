package com.crypto.croytowallet.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.Adapter.ActiveDeviceAdapter;
import com.crypto.croytowallet.Extra_Class.ApiResponse.ActiveDeviceResponse;
import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sync_device extends AppCompatActivity implements HistoryClickLister {
    ImageView imageView;
    RecyclerView recyclerView;
    private ArrayList<ActiveDeviceResponse.Result> data;
    KProgressHUD progressDialog;
    ActiveDeviceAdapter activeDeviceAdapter;
    TextView balances ,textView1;
    SharedPreferences sharedPreferences;
    String CurrencySymbols,currency2,balance,cal_balance;
    String jwt_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_device);
        imageView = findViewById(R.id.back);
        balances = findViewById(R.id.balance);
        textView1  =findViewById(R.id.balance1);

        data = new  ArrayList<ActiveDeviceResponse.Result>();

        recyclerView = findViewById(R.id.active_device_recyclerView);
        back();

        sharedPreferences =getApplicationContext().getSharedPreferences("currency",0);
        currency2 =sharedPreferences.getString("currency1","usd");
        CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");


        balance = MyPreferences.getInstance(getApplicationContext()).getString(PrefConf.USER_BALANCE,"0");
        cal_balance =MyPreferences.getInstance(getApplicationContext()).getString(PrefConf.CAL_USER_BALANCE,"0");

        balances.setText(balance);
        textView1.setText(CurrencySymbols+cal_balance);

       getActiveDeviceDetails();
    }

    public void getActiveDeviceDetails(){
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String username=user.getUsername();
        String token = user.getToken();

        progressDialog = KProgressHUD.create(Sync_device.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ActiveDeviceResponse>call = RetrofitClient.getInstance().getApi().ActiveDevice(username);
        call.enqueue(new Callback<ActiveDeviceResponse>() {
            @Override
            public void onResponse(Call<ActiveDeviceResponse> call, Response<ActiveDeviceResponse> response) {
             hidepDialog();
              String s= null;
                if (response.isSuccessful()){
                    ActiveDeviceResponse activeDeviceResponse = response.body();
                   data = new ArrayList<ActiveDeviceResponse.Result>(Arrays.asList(activeDeviceResponse.getResults()));

                    activeDeviceAdapter =new ActiveDeviceAdapter(data,getApplicationContext(),Sync_device.this);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(activeDeviceAdapter);

                }else{
                    try{
                     s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Sync_device.this)
                                .setText(error)
                                .setTextColor(getResources().getColor(R.color.white))
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ActiveDeviceResponse> call, Throwable t) {
                hidepDialog();

                Snacky.builder()
                        .setActivity(Sync_device.this)
                        .setText(t.getLocalizedMessage())
                        .setTextColor(getResources().getColor(R.color.white))
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }


    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onSaveInstanceState(new Bundle());
    }

    public void back() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

    }

    @Override
    public void onHistoryItemClickListener(int position) {

        jwt_token =data.get(position).getJwt();
        AlertDialogBox();

    }
    public void AlertDialogBox(){

        //Logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Sync_device.this);

        // set title
        alertDialogBuilder.setTitle(R.string.app_name);

        // set dialog message
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher_round);
        alertDialogBuilder
                .setMessage(R.string.log_text)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Remove_Jwt();

                    }
                })
                .setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void Remove_Jwt() {
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String username=user.getUsername();
        String token = user.getToken();

        progressDialog = KProgressHUD.create(Sync_device.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().remove_JWT(token,username,jwt_token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hidepDialog();
                String s =null;
                hidepDialog();
                if (response.code()==200){

                    try {
                        s=response.body().string();

                        Snacky.builder()
                                .setActivity(Sync_device.this)
                                .setText("Successfully remove")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setTextColor(getResources().getColor(R.color.white))
                                .setActionText(android.R.string.ok)
                                .success()
                                .show();

                        getActiveDeviceDetails();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Sync_device.this)
                                .setText(error)
                                .setTextColor(getResources().getColor(R.color.white))
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code()==401){
                    Snacky.builder()
                            .setActivity(Sync_device.this)
                            .setText("unAuthorization Request")
                            .setTextColor(getResources().getColor(R.color.white))
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();

                Snacky.builder()
                        .setActivity(Sync_device.this)
                        .setText("Internet Problem")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

    }




}
