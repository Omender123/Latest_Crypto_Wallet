package com.crypto.croytowallet.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.CrashDataModel;
import com.crypto.croytowallet.SharedPrefernce.CreshSharedPrefManager;
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

public class CreashSetTransactionPin extends AppCompatActivity {
   TextView sumbit;
    PinView enter_pin,comfirm_Pin;
    String str_enten_Pin,str_confirm_Pin;
    KProgressHUD progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creash_set_transaction_pin);
        enter_pin = findViewById(R.id.enter_pin);
        comfirm_Pin = findViewById(R.id.enter_pin1);
        sumbit = findViewById(R.id.submit_btn);

        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_enten_Pin = enter_pin.getText().toString();
                str_confirm_Pin = comfirm_Pin.getText().toString();

                if (str_enten_Pin.isEmpty() || str_confirm_Pin.isEmpty()){
                    Snacky.builder()
                            .setView(v)
                            .setText("Please filled All requirements ")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .setTextColor(getResources().getColor(R.color.white))
                            .warning()
                            .show();
                } else if (str_confirm_Pin.equals(str_enten_Pin)){
                    enter_pin.setLineColor(getResources().getColor(R.color.green));
                    comfirm_Pin.setLineColor(getResources().getColor(R.color.green));

                    setTransactionPin();
                }else {
                      new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    enter_pin.setLineColor(getResources().getColor(R.color.light_gray));
                    comfirm_Pin.setLineColor(getResources().getColor(R.color.light_gray));
                }
            },500);
                    enter_pin.setLineColor(getResources().getColor(R.color.red));
                   comfirm_Pin.setLineColor(getResources().getColor(R.color.red));
                }
            }
        });


    }

    private void setTransactionPin() {
        CrashDataModel crashDataModel = CreshSharedPrefManager.getInstance(getApplicationContext()).getCreshData();

        String username =crashDataModel.getUsername();
        String password = crashDataModel.getPassword();

        progressDialog = KProgressHUD.create(CreashSetTransactionPin.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().setTransactionPin(username,str_enten_Pin,password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s =null;
                hidepDialog();
                if (response.code()==200){

                    try {
                        s=response.body().string();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        Toast.makeText(CreashSetTransactionPin.this, " Successfully set Transaction Pin", Toast.LENGTH_SHORT).show();

                        enter_pin.setLineColor(getResources().getColor(R.color.light_gray));
                        comfirm_Pin.setLineColor(getResources().getColor(R.color.light_gray));
                        // Toast.makeText(Payout_verification.this, ""+s, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(CreashSetTransactionPin.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();
                        enter_pin.setLineColor(getResources().getColor(R.color.light_gray));
                        comfirm_Pin.setLineColor(getResources().getColor(R.color.light_gray));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
                }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(CreashSetTransactionPin.this)
                        .setText("Please Check Your Internet Connection")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

            }
        });

    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}