package com.crypto.croytowallet.CoinTransfer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.Activity.Graph_layout;
import com.crypto.croytowallet.ImtSmart.SwapConfirmation;
import com.crypto.croytowallet.Model.SwapModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SwapSharedPrefernce;
import com.crypto.croytowallet.SharedPrefernce.Updated_data;
import com.crypto.croytowallet.SharedPrefernce.UserData;

import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.database.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Pay_Coin extends AppCompatActivity {
    int position;
    String result,Amount,cryptoCurrency, email2fa1,userBalance,currency2,CurrencySymbols,price;
    TextView toolbar_title,text_send;
    ImageView imageView;
    EditText enterAmount,token;
    TextInputLayout enterAmount1,token1;
    Button next;
    UserData userData;
    KProgressHUD progressDialog;
    SharedPreferences preferences,sharedPreferences1;


    private AppCompatActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay__coin);
        imageView =findViewById(R.id.back);
        toolbar_title=findViewById(R.id.toolbar_title);
        enterAmount=findViewById(R.id.ed_enter_amount);
        text_send = findViewById(R.id.txt_send_amount);
      //  token =findViewById(R.id.ed_token);
        enterAmount1=findViewById(R.id.user);

        next=findViewById(R.id.next);
        preferences=getSharedPreferences("coinScan", Context.MODE_PRIVATE);
        result = preferences.getString("address","");

        sharedPreferences1 =getApplicationContext().getSharedPreferences("currency",0);
        currency2 =sharedPreferences1.getString("currency1","usd");
        CurrencySymbols =sharedPreferences1.getString("Currency_Symbols","$");





        cryptoCurrency = Updated_data.getInstans(getApplicationContext()).getmobile();
        price =Updated_data.getInstans(getApplicationContext()).getprice();

        userData= SharedPrefManager.getInstance(getApplicationContext()).getUser();

        toolbar_title.setText("Send "+cryptoCurrency);


        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Amount = enterAmount.getText().toString().trim();
                if (Amount.isEmpty()){
                    enterAmount.setError("Please enter amount");
                    enterAmount.requestFocus();
                }else {


                    geTypeToken(userData.getToken(),cryptoCurrency);

                }


            }
        });


        enterAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String msg = s.toString();

                if (msg.isEmpty()){
                        text_send.setText(" ");
                    }else {
                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(8);

                        Double coinprices,enterAmount,totalAmoumt;
                        coinprices=Double.parseDouble(price);
                        enterAmount=Double.parseDouble(msg);

                        totalAmoumt = enterAmount*coinprices;

                  text_send.setText(msg +" "+cryptoCurrency.toUpperCase() +"="+df.format(totalAmoumt)+" " +currency2.toUpperCase() );
                    }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




         back();
       get2fa();

    }
    private void geTypeToken(String token, String symbol) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getToken(token,symbol);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = null;
                if (response.code()==200){
                    try {
                        s = response.body().string();
                        JSONObject object = new JSONObject(s);
                        String CoinType = object.getString("token");
                        getBalance(token,CoinType,symbol,"usd");
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }else if (response.code()==400){
                    getBalance(token,symbol,symbol,"usd");

                }else if (response.code()==401){
                    Snacky.builder()
                            .setActivity(Pay_Coin.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snacky.builder()
                        .setActivity(Pay_Coin.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

            }
        });

    }

    public void getBalance(String Auth_token,String coinType,String coinSymbols,String currency){


        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().Balance(Auth_token,coinType,coinSymbols,currency);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s =null;
                if (response.code()==200){
                    try {
                        s=response.body().string();

                        JSONObject object = new JSONObject(s);
                        userBalance = object.getString("balance");

                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(8);

                        Double coinprices,enterAmount,totalAmoumt;
                        coinprices=Double.parseDouble(price);
                        enterAmount=Double.parseDouble(Amount);

                        totalAmoumt = enterAmount*coinprices;

                        String coinAmount = String.valueOf(df.format(totalAmoumt));

                        SwapModel swapModel = new SwapModel(cryptoCurrency,result,price,currency2,CurrencySymbols,coinAmount,Amount,userBalance,coinAmount,1,"CoinTransfer",coinType);
                        SwapSharedPrefernce.getInstance(getApplicationContext()).SetData(swapModel);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), SwapConfirmation.class);
                                startActivity(intent);
                            }
                        },500);

                      //  Toast.makeText(Pay_Coin.this, ""+userBalance, Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        userBalance="0";

                        Snacky.builder()
                                .setActivity(Pay_Coin.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==401){

                    Snacky.builder()
                            .setActivity(Pay_Coin.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snacky.builder()
                        .setActivity(Pay_Coin.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }


    public void get2fa(){

        String token = userData.getToken();
        progressDialog = KProgressHUD.create(Pay_Coin.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        StringRequest request=new StringRequest(Request.Method.GET, URLs.URL_GET_2FA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                try {
                    JSONObject object =new JSONObject(response);
                    String result =  object.getString("result");
                    JSONObject object1 = new JSONObject(result);

                    email2fa1 = object1.getString("email2fa");


                   if (email2fa1.equals("true")){
                       sendOTP();
                    }else {
                     //  Toast.makeText(Pay_Coin.this, "Your Email 2FA OFF", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
//                parseVolleyError(error);

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", token);

                return headers;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);


    }


    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            String message=data.getString("error");
            Snacky.builder()
                    .setActivity(Pay_Coin.this)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_SHORT)
                    .setActionText(android.R.string.ok)
                    .error()
                    .show();
            //      Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        }
    }


    public void sendOTP(){
        String username = userData.getUsername();
        progressDialog = KProgressHUD.create(Pay_Coin.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call=  RetrofitClient
                .getInstance()
                .getApi().sendOtp(username);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hidepDialog();

                String s=null;
                if (response.code()==200){

                    Toast.makeText(Pay_Coin.this, "Otp send in your registered Email", Toast.LENGTH_SHORT).show();

                }else if(response.code()==400){
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(Pay_Coin.this)
                                .setText(" Oops Username Not Found !!!!!")
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(Pay_Coin.this)
                        .setText("Please Check Your Internet Connection")
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

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }



    }



