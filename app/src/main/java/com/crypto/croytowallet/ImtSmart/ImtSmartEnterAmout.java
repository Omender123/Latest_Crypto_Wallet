package com.crypto.croytowallet.ImtSmart;

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
import android.view.inputmethod.InputMethodManager;
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
import com.crypto.croytowallet.CoinTransfer.Pay_Coin;
import com.crypto.croytowallet.CoinTransfer.Payout_verification;
import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.Model.SwapModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SwapSharedPrefernce;
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

public class ImtSmartEnterAmout extends AppCompatActivity {
    String result,Amount,cryptoCurrency="imt", email2fa1,google2fa1;
    TextView toolbar_title,text_send;
    ImageView imageView;
    EditText enterAmount,token;
    TextInputLayout enterAmount1,token1;
    Button next;
    UserData userData;
    KProgressHUD progressDialog;
    SharedPreferences sharedPreferences,sharedPreferences1,sharedPreferences2;
    String currency2,CurrencySymbols,userBalance,imtPrice,coinSymbol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imt_smart_enter_amout);
        imageView =findViewById(R.id.back);
        enterAmount=findViewById(R.id.ed_enter_amount);
        next=findViewById(R.id.next);
        text_send = findViewById(R.id.txt_send_amount);
        userData= SharedPrefManager.getInstance(getApplicationContext()).getUser();
        sharedPreferences  =getApplicationContext().getSharedPreferences("currency",0);
        sharedPreferences1 = getApplicationContext().getSharedPreferences("imtInfo", Context.MODE_PRIVATE);
        sharedPreferences2=getSharedPreferences("ImtScan", Context.MODE_PRIVATE);

        currency2 =sharedPreferences.getString("currency1","usd");
        CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");
        imtPrice = sharedPreferences1.getString("imtPrices", "0.09");
        result =sharedPreferences2.getString("Imtaddress","");


        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Amount = enterAmount.getText().toString().trim();
                if (Amount.isEmpty()){
                    enterAmount.setError("Please enter amount");
                    enterAmount.requestFocus();
                }else {

                    AirDropBalance(userData.getToken(),"erc","imt",currency2);

                    AppUtils.hideKeyboard(v,ImtSmartEnterAmout.this);



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
                    coinprices=Double.parseDouble(imtPrice);
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

    public void AirDropBalance(String token,String coinType,String coinSymbol,String currency){

        progressDialog = KProgressHUD.create(ImtSmartEnterAmout.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().Balance(token,coinType,coinSymbol,currency);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hidepDialog();
                String s =null;
                if (response.code()==200){
                    try {
                        s=response.body().string();

                        JSONObject object = new JSONObject(s);
                        userBalance = object.getString("balance");

                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(8);

                        Double coinprices,enterAmount,totalAmoumt;
                        coinprices=Double.parseDouble(imtPrice);
                        enterAmount=Double.parseDouble(Amount);

                        totalAmoumt = enterAmount*coinprices;

                        String coinAmount = String.valueOf(df.format(totalAmoumt));

                        SwapModel swapModel = new SwapModel(cryptoCurrency,result,imtPrice,currency2,CurrencySymbols,coinAmount,Amount,userBalance,coinAmount,1,"CoinTransfer",coinType);
                        SwapSharedPrefernce.getInstance(getApplicationContext()).SetData(swapModel);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), SwapConfirmation.class);
                                startActivity(intent);
                            }
                        },500);



                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(ImtSmartEnterAmout.this)
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
                            .setActivity(ImtSmartEnterAmout.this)
                            .setText("unAuthorization Request")
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
                        .setActivity(ImtSmartEnterAmout.this)
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
        progressDialog = KProgressHUD.create(ImtSmartEnterAmout.this)
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
                    google2fa1 = object1.getString("google2fa");


                    if (email2fa1.equals("true")){
                        sendOTP();
                    }else {
                      //  Toast.makeText(ImtSmartEnterAmout.this, "Your Email 2FA OFF", Toast.LENGTH_SHORT).show();
                    }


                    //   Toast.makeText(Pay_Coin.this, ""+email2fa1+google2fa1, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
              //  Toast.makeText(ImtSmartEnterAmout.this, ""+response, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();

               // Toast.makeText(ImtSmartEnterAmout.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
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
                    .setActivity(ImtSmartEnterAmout.this)
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
        progressDialog = KProgressHUD.create(ImtSmartEnterAmout.this)
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

                  /*  Snacky.builder()
                            .setView(view)
                            .setText("Otp send in your register Email")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();*/
                 //   OTPexpire();
                    Toast.makeText(ImtSmartEnterAmout.this, "Otp send in your registered Email", Toast.LENGTH_SHORT).show();

                }else if(response.code()==400){
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(ImtSmartEnterAmout.this)
                                .setText(" Oops Username Not Found !!!!!")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();
                        // Toast.makeText(SignUp.this, jsonObject1.getString("error")+"", Toast.LENGTH_SHORT).show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(ImtSmartEnterAmout.this)
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
    public void OTPexpire(){
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                expire();
            }
        }, 60000);
    }

    public void expire(){
        String username = userData.getUsername();


        Call<ResponseBody> call=  RetrofitClient
                .getInstance()
                .getApi().expireOtp(username);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hidepDialog();

                String s=null;
                if (response.code()==200){
                    Toast.makeText(ImtSmartEnterAmout.this, "Your Otp is expire", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

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