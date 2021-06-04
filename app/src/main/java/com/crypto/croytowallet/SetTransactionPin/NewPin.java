package com.crypto.croytowallet.SetTransactionPin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.TransactionHistorySharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPin extends AppCompatActivity {
PinView newPin,confirmPin;
Button confirm;
String newPintxt,confirmPintxt,currentPin,password,otp,type,mnemonic;
    KProgressHUD progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pin);
        newPin = findViewById(R.id.enterNew_pin);
        confirmPin = findViewById(R.id.enter_confirm_pin);
        confirm = findViewById(R.id.confirm_btn);

       try {
           Bundle bundle = getIntent().getExtras();
           currentPin = bundle.getString("CurrentPin1");
           type = bundle.getString("Type1");
           password = bundle.getString("password");
           otp = bundle.getString("otp");
           mnemonic = bundle.getString("mnemonic1");

       }catch (Exception e){

       }



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPintxt = newPin.getText().toString();
                confirmPintxt = confirmPin.getText().toString();

                if (newPintxt.isEmpty() || confirmPintxt.isEmpty()){
                    Snacky.builder()
                            .setActivity(NewPin.this)
                            .setText("Please Enter all Details")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else if(!newPintxt.equals(confirmPintxt)){
                    Snacky.builder()
                            .setActivity(NewPin.this)
                            .setText(" New Transaction Pin Not Match")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else{

                    checkType();

                }
            }
        });
    }


    public void checkType(){
        Bundle bundle = getIntent().getExtras();
        currentPin = bundle.getString("CurrentPin1");
        type = bundle.getString("Type1");
        password = bundle.getString("password");
        otp = bundle.getString("otp");
        mnemonic = bundle.getString("mnemonic1");

        if (type.equalsIgnoreCase("resetPin")){
            ResetPinApi(currentPin,password,otp,newPintxt);
           // Toast.makeText(NewPin.this, ""+currentPin+password+otp+type+newPintxt, Toast.LENGTH_SHORT).show();
        }else{
            ForgetPinApi(mnemonic,password,otp,newPintxt);
           // Toast.makeText(NewPin.this, ""+mnemonic+password+otp+type+newPintxt, Toast.LENGTH_SHORT).show();
        }
    }

    private void ResetPinApi(String currentPin, String password, String otp, String newPintxt) {
        progressDialog = KProgressHUD.create(NewPin.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        UserData user = SharedPrefManager.getInstance(this).getUser();
        String token=user.getToken();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().resetTransactionPin(token,otp,currentPin,password,newPintxt);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s=null;
                hidepDialog();
                if (response.code()==200){
                    startActivity(new Intent(getApplicationContext(),CompleteScreen.class));
                }else if(response.code()==400){

                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(NewPin.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();
                        // Toast.makeText(SignUp.this, jsonObject1.getString("error")+"", Toast.LENGTH_SHORT).show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }else if(response.code()==401){
                    Snacky.builder()
                            .setActivity(NewPin.this)
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
                        .setActivity(NewPin.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

    }

    private void ForgetPinApi(String mnemonic, String password, String otp, String newPintxt) {

        progressDialog = KProgressHUD.create(NewPin.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        UserData user = SharedPrefManager.getInstance(this).getUser();
        String token=user.getToken();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().forgetTransactionPin(token,otp,mnemonic,password,newPintxt);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s=null;
                hidepDialog();
                if (response.code()==200){
                    startActivity(new Intent(getApplicationContext(),CompleteScreen.class));
                }else if(response.code()==400){

                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(NewPin.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();
                        // Toast.makeText(SignUp.this, jsonObject1.getString("error")+"", Toast.LENGTH_SHORT).show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }else if(response.code()==401){
                    Snacky.builder()
                            .setActivity(NewPin.this)
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
                        .setActivity(NewPin.this)
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

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    onSaveInstanceState(new Bundle());
    }
}