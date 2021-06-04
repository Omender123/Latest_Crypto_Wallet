package com.crypto.croytowallet.SetTransactionPin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.crypto.croytowallet.Activity.Security;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
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

public class CuurentPin extends AppCompatActivity {
    String currentPin,pin;
    PinView pinView;
    UserData userData;
    Button confirm;
    KProgressHUD progressDialog;
    Animation slide_up,slide_right;
    TextView textView,textView1,textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuurent_pin);
        confirm = findViewById(R.id.skip_btn);
        pinView = findViewById(R.id.enterCurrent_pin);
        textView = findViewById(R.id.ent_cur);
        textView1 = findViewById(R.id.kind);
        textView2 = findViewById(R.id.forget);


        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        pin = userData.getTransaction_Pin();

        slide_up = AnimationUtils.loadAnimation(CuurentPin.this, R.anim.silde_up);
        slide_right =AnimationUtils.loadAnimation(CuurentPin.this,R.anim.slide_in_right);

        textView.startAnimation(slide_right);
        textView1.startAnimation(slide_right);
        textView2.startAnimation(slide_right);
        confirm.startAnimation(slide_right);
        pinView.startAnimation(slide_right);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPin = pinView.getText().toString();
                if (currentPin.isEmpty()){
                    Snacky.builder()
                            .setActivity(CuurentPin.this)
                            .setText("Please Enter Current Transaction Pin")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else if(!pin.equals(currentPin)){
                    Snacky.builder()
                            .setActivity(CuurentPin.this)
                            .setText(" Transaction Pin Not Match")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else{
                 resendOTP();

                   }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    public void back(View view) {
        onBackPressed();
    }

    public void resendOTP() {

        String usernames = userData.getUsername();


        progressDialog = KProgressHUD.create(CuurentPin.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().sendOtp(usernames);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hidepDialog();

                String s = null;
                if (response.code() == 200) {

                    Intent intent = new Intent(getApplicationContext(),EnterDetails.class);
                    intent.putExtra("CurrentPin",currentPin);
                    intent.putExtra("Type","resetPin");
                    intent.putExtra("mnemonic","");
                    startActivity(intent);

                    Toast.makeText(CuurentPin.this, "Otp send in your register Email", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 400) {
                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(CuurentPin.this)
                                .setText(error)
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
                        .setActivity(CuurentPin.this)
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

    public void forgetPIN(View view) {
        startActivity(new Intent(getApplicationContext(),EnterMnemonices.class));
    }
}