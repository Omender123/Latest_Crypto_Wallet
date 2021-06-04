package com.crypto.croytowallet.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.CrashDataModel;
import com.crypto.croytowallet.SharedPrefernce.CreshSharedPrefManager;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.signup.GmailVerfiyOtp;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrashOtpActivity extends AppCompatActivity {
    private long timeCountInMilliSeconds = 1 * 60000;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;

    Boolean click=false;


    KProgressHUD progressDialog;
    EditText enter_otp;
    CrashDataModel user;
    Button next2;
    TextView timer_txt,resendOtp;
    private static CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_otp);

        user = CreshSharedPrefManager.getInstance(getApplicationContext()).getCreshData();

        next2 = findViewById(R.id.next2);
        enter_otp = findViewById(R.id.enter_otp);
        timer_txt = findViewById(R.id.timer);
        resendOtp = findViewById(R.id.resendOtp);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);



        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = enter_otp.getText().toString().trim();

                if (otp.isEmpty()) {

                    hideKeyboard(v);
                    enter_otp.requestFocus();
                    Snackbar warningSnackBar = Snacky.builder()
                            .setActivity(CrashOtpActivity.this)
                            .setText("please enter One Time Password")
                            .setTextColor(getResources().getColor(R.color.white))
                            .setDuration(Snacky.LENGTH_SHORT)
                            .warning();
                    warningSnackBar.show();
                } else {
                    verifyOTP(v);


                }
            }
        });

      //  timer();
        startCountDownTimer();
    }

    public  void verifyOTP(View view){
        String otp = enter_otp.getText().toString().trim();
        String username=   user.getUsername();

        progressDialog = KProgressHUD.create(CrashOtpActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().OtpVerify(username,otp);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();

                String s = null;
                if (response.code() == 200) {
                    hideKeyboard(view);
                startActivity(new Intent(getApplicationContext(),Login.class));

                } else if (response.code() == 400) {
                    hideKeyboard(view);
                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");

                       Snacky.builder()
                                .setView(view)
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
                hideKeyboard(view);
                Snacky.builder()
                        .setView(view)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }

    public void resendOTP(View view) {
        String username=   user.getUsername();
        progressDialog = KProgressHUD.create(CrashOtpActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().sendOtp(username);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();

                String s = null;
                if (response.code() == 200) {
                    hideKeyboard(view);
                    Snacky.builder()
                            .setView(view)
                            .setText("Otp resend in your register Email")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();

                    reset();
                //   OTPexpire();
                } else if (response.code() == 400) {
                    hideKeyboard(view);
                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");

                        Snacky.builder()
                                .setView(view)
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
                hideKeyboard(view);
                Snacky.builder()
                        .setView(view)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
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
        String username=   user.getUsername();

        Call<ResponseBody> call=  RetrofitClient
                .getInstance()
                .getApi().expireOtp(username);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // hidepDialog();

                String s=null;
                if (response.code()==200){
                    Toast.makeText(CrashOtpActivity.this, "Your Otp is expire", Toast.LENGTH_SHORT).show();

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
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
    public void timer(){
        countDownTimer =   new CountDownTimer(60000, 1000){
            public void onTick(long millisUntilFinished){
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d",  TimeUnit.MILLISECONDS.toSeconds(millis) );
                timer_txt.setText(hms+"s  ");

            }
            public  void onFinish(){
                timer_txt.setVisibility(View.GONE);
                resendOtp.setAlpha(0.9f);
                countDownTimer = null;
                resendOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resendOTP(v);
                    }
                });
            }
        }.start();
    }
    private void reset() {
        stopCountDownTimer();
        startCountDownTimer();

    }

    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                click =false;
                timer_txt.setText(hmsTimeFormatter(millisUntilFinished)+"s");

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
                resendOtp.setAlpha(0.4f);

            }

            @Override
            public void onFinish() {

                click = true;
                timer_txt.setText("60s");
                // call to initialize the progress bar values
                setProgressBarValues();
                timerStatus = TimerStatus.STOPPED;
                resendOtp.setAlpha(0.9f);

                resendOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // resendOTP(v);
                        if(click==true){
                            resendOTP(v);
                        }

                    }
                });
            }

        }.start();
        countDownTimer.start();
    }

    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds/ 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d",  TimeUnit.MILLISECONDS.toSeconds(milliSeconds) );
        return hms;


    }


}