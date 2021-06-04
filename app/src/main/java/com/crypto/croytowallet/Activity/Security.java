package com.crypto.croytowallet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SetTransactionPin.CuurentPin;
import com.crypto.croytowallet.SetTransactionPin.EnterMnemonices;
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

public class Security extends AppCompatActivity implements View.OnClickListener {
    ActionBar actionBar;
    Toolbar toolbar;
    ImageView imageView;
    CardView Two_FA1, card_passcode,backUp,resetPin,forgetPin;
    CheckBox passcode;
    KProgressHUD progressDialog;

    SharedPreferences sharedPreferences = null;
    UserData userData;
    Animation slide_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        imageView = findViewById(R.id.back);
        Two_FA1 = findViewById(R.id.tofa);
        backUp = findViewById(R.id.backUp);
        card_passcode = findViewById(R.id.card_passcode);
        passcode = findViewById(R.id.checkbox);
        resetPin = findViewById(R.id.resetPin);
        forgetPin = findViewById(R.id.forgetPin);



        slide_up = AnimationUtils.loadAnimation(Security.this, R.anim.silde_up);
        backUp.startAnimation(slide_up);
        Two_FA1.startAnimation(slide_up);
        card_passcode.startAnimation(slide_up);
        resetPin.startAnimation(slide_up);
        forgetPin.startAnimation(slide_up);

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        sharedPreferences = getSharedPreferences("myKey1", 0);

        Boolean booleanValue = sharedPreferences.getBoolean("passcode", false);
        if (booleanValue) {
            passcode.setChecked(true);
        }

        passcode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("passcode", true);
                    editor.commit();

                    passcode.setChecked(true);
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("passcode", false);
                    editor.commit();

                    passcode.setChecked(false);
                }
            }
        });

        Two_FA1.setOnClickListener(this);
        backUp.setOnClickListener(this);
        resetPin.setOnClickListener(this);
        forgetPin.setOnClickListener(this);

        back();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
       /* Intent intent = new Intent(Security.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();*/
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(Security.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void actionBarSetup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

            //   actionBar.setTitle("Price Action Strategy ");

        }
    }

    public void back() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onSaveInstanceState(new Bundle());
                /*Intent intent = new Intent(Security.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/

                onBackPressed();
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.tofa:
                startActivity(new Intent(Security.this, Two_FA.class));

                break;
            case R.id.backUp:
                resendOTP();
                startActivity(new Intent(getApplicationContext(), BackVerification.class));
                finish();
                break;

            case R.id.resetPin:
                startActivity(new Intent(getApplicationContext(), CuurentPin.class));
                break;
            case R.id.forgetPin:
               startActivity(new Intent(getApplicationContext(), EnterMnemonices.class));
                break;
        }
    }

    public void resendOTP() {

        String usernames = userData.getUsername();


        progressDialog = KProgressHUD.create(Security.this)
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

                    Toast.makeText(Security.this, "Otp send in your register Email", Toast.LENGTH_SHORT).show();
                    // OTPexpire();
                } else if (response.code() == 400) {
                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(Security.this)
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
                        .setActivity(Security.this)
                        .setText("Please Check Your Internet Connection")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }


    public void OTPexpire() {
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                expire();
            }
        }, 60000);
    }

    public void expire() {
        String usernames = userData.getUsername();


        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().expireOtp(usernames);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hidepDialog();

                String s = null;
                if (response.code() == 200) {
                    Toast.makeText(Security.this, "Your Otp is expire", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
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

}