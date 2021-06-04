package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.TransactionHistorySharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Threat_Mode extends AppCompatActivity {
EditText ed_pass,ed_otp,ed_pin;
String password,otp,pin;
CardView submit;
    KProgressHUD progressDialog;
    SharedPreferences sharedPreferences1,sharedPreferences2;
    Animation slide_up;
    TextInputLayout enterpss,enterotp,enterpin;
    TextView textt,textC;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threat_mode);
        ed_pass = findViewById(R.id.enter_pass);
        ed_otp = findViewById(R.id.enter_otp);
        ed_pin = findViewById(R.id.enter_pin);
        submit = findViewById(R.id.submit);

        imageView = findViewById(R.id.image);

        // textLyout input

        enterpss = findViewById(R.id.enterpss);
        enterotp = findViewById(R.id.enterotp);
        enterpin = findViewById(R.id.enterpin);
        textt = findViewById(R.id.textt);
        textC = findViewById(R.id.textC);

        sharedPreferences1 =getApplicationContext().getSharedPreferences("currency",0);
        sharedPreferences2 =getApplicationContext().getSharedPreferences("PROJECT_NAME",0);
        slide_up = AnimationUtils.loadAnimation(Threat_Mode.this, R.anim.silde_up);

        enterpss.setAnimation(slide_up);
        enterotp.setAnimation(slide_up);
        enterpin.setAnimation(slide_up);
        textt.setAnimation(slide_up);
        textC.setAnimation(slide_up);
        submit.setAnimation(slide_up);

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
                 threat_mode_Api();
                }
            }
        });


    }

    public void threat_mode_Api() {

        UserData userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        String token = userData.getToken();

        progressDialog = KProgressHUD.create(Threat_Mode.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();


        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().Threat_mode_Api(token,password,otp,pin);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();
                String s=null;
                if (response.code()==200){

                    SharedPrefManager.getInstance(getApplicationContext()).logout();
                    TransactionHistorySharedPrefManager.getInstance(getApplicationContext()).clearPearData();
                    sharedPreferences1.edit().clear().commit();
                    sharedPreferences2.edit().clear().commit();
                    finish();

                }else if(response.code()==400){
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(Threat_Mode.this)
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
                            .setActivity(Threat_Mode.this)
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
                        .setActivity(Threat_Mode.this)
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