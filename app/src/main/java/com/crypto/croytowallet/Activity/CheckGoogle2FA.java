package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.TransactionHistorySharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckGoogle2FA extends AppCompatActivity {
    EditText ed_pass,ed_otp,ed_pin;
    String password,otp,pin;
    CardView submit;
    KProgressHUD progressDialog;
    SharedPreferences sharedPreferences1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_google2_f);
        ed_pass = findViewById(R.id.enter_pass);
        ed_otp = findViewById(R.id.enter_otp);
        ed_pin = findViewById(R.id.enter_pin);
        submit = findViewById(R.id.submit);

        sharedPreferences1 = getSharedPreferences("GoogleKey", MODE_PRIVATE);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = ed_pass.getText().toString().trim();
                otp = ed_otp.getText().toString().trim();
                pin = ed_pin.getText().toString().trim();

                if (password.isEmpty()){
                    ed_pass.setError("Please enter password");
                    ed_pass.requestFocus();

                }else if(otp.isEmpty()){
                    ed_otp.setError("Please enter otp");
                    ed_otp.requestFocus();

                }else if(pin.isEmpty()){
                    ed_pin.setError("Please enter transaction pin");
                    ed_pin.requestFocus();

                }else {
                    Google_Option();
                }
            }
        });


    }

    public void Google_Option() {

        UserData userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        String token = userData.getToken();

        progressDialog = KProgressHUD.create(CheckGoogle2FA.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();


        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().Google_Obtain(token,password,otp,pin);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();
                String s=null;
                if (response.code()==200){

                    try {
                        s = response.body().string();
                        JSONObject object = new JSONObject(s);
                        String result = object.getString("result");
                        JSONObject object1 = new JSONObject(result);
                        String key= object1.getString("key");
                        String oauthKey = object1.getString("otpauth_url");

                        SharedPreferences.Editor myEdit = sharedPreferences1.edit();

                        // write all the data entered by the user in SharedPreference and apply
                        myEdit.putString("oauthKey",oauthKey );
                        myEdit.putString("key",key );
                        myEdit.apply();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                startActivity(new Intent(CheckGoogle2FA.this,ShowGoogle2fa_BarCode.class));
                            }
                        },1000);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }


                }else if(response.code()==400){
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(CheckGoogle2FA.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();
                        // Toast.makeText(SignUp.this, jsonObject1.getString("error")+"", Toast.LENGTH_SHORT).show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                } else if(response.code()==401){
                    Snacky.builder()
                            .setActivity(CheckGoogle2FA.this)
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
                        .setActivity(CheckGoogle2FA.this)
                        .setText(t.getMessage())
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

    public void back(View view) {
        onBackPressed();
    }
}