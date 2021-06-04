package com.crypto.croytowallet.CoinTransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chaos.view.PinView;
import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.ImtSmart.SwapAcknowledgement;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.Model.SwapModel;
import com.crypto.croytowallet.Model.SwapRespoinseModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SwapResponsePrefernce;
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
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.mateware.snacky.Snacky;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Payout_verification extends AppCompatActivity {
    private long timeCountInMilliSeconds = 1 * 60000;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;

    String result,Amount,Token,enterPin,cryptoCurrency,otp,AuthToken,email2fa,google2fa,CoinType;
    SharedPreferences preferences;
    KProgressHUD progressDialog;
    UserData userData;
    TextView next,email_otp,googleToken,btn_sendOtp,timer_txt;
    PinView pinView;
    EditText ed_token,ed_otp;
    ImageView imageView;
    TextInputLayout lyt_emiail,lyt_Google;
    SwapModel swapModel;
    private static CountDownTimer countDownTimer;
    Boolean click=false;
    String transIDs,statuss;
    private Socket mSocket;
    {
        try {
           mSocket = IO.socket("https://api.imx.global");

           // mSocket = IO.socket("http://13.233.136.56:8080");

        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_verification2);

        next = findViewById(R.id.Next_btn);
        ed_otp = findViewById(R.id.enter_otp);
        ed_token = findViewById(R.id.enter_GoogleToken);
        pinView = findViewById(R.id.enter_pin);
        imageView =findViewById(R.id.back);

        email_otp = findViewById(R.id.Email_payout);
        googleToken = findViewById(R.id.Gmail_payout);
        lyt_emiail = findViewById(R.id.pass);
        lyt_Google = findViewById(R.id.pass1);
        btn_sendOtp=findViewById(R.id.btn_sendOtp);
        timer_txt = findViewById(R.id.timer);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        swapModel = SwapSharedPrefernce.getInstance(getApplicationContext()).getSwapData();

        cryptoCurrency = swapModel.getSendData();
        result = swapModel.getReceivedData();
        Amount = swapModel.getEnterAmount();
        CoinType = swapModel.getCoinType();



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterPin = pinView.getText().toString();
                Token = ed_token.getText().toString().trim();
                otp = ed_otp.getText().toString().trim();

                String trans = userData.getTransaction_Pin();

                if (enterPin.isEmpty()) {
                    Snacky.builder()
                            .setView(v)
                            .setText("Please enter transaction pin")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .warning()
                            .show();


                } else if (!enterPin.equals(trans)) {
                    Snacky.builder()
                            .setView(v)
                            .setText("Transaction Pin incorrect")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .error()
                            .show();
                }else{

                  //  Log.d("coinTranser",cryptoCurrency+result+Amount+enterPin);
                 // Toast.makeText(Payout_verification.this, ""+cryptoCurrency+result+Amount+enterPin+CoinType, Toast.LENGTH_SHORT).show();
                  sendcoin();
                }

            }
        });

        get2fa();
        back();

        mSocket.connect();
       // mSocket.on("hello",helloEvent);
        mSocket.on("pendingReport",PendingReport);
        mSocket.on("cryptoError",CryptoError);

    }

    public void get2fa(){

        String token = userData.getToken();
        StringRequest request=new StringRequest(Request.Method.GET, URLs.URL_GET_2FA, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object =new JSONObject(response);
                    String result =  object.getString("result");
                    JSONObject object1 = new JSONObject(result);

                    String    email2fa1 = object1.getString("email2fa");
                    String  google2fa1 = object1.getString("google2fa");


                    if (email2fa1.equals("true") && google2fa1.equals("false") ){

                        email_otp.setVisibility(View.VISIBLE);
                        lyt_emiail.setVisibility(View.VISIBLE);
                        btn_sendOtp.setVisibility(View.VISIBLE);
                        timer_txt.setVisibility(View.VISIBLE);
                        progressBarCircle.setVisibility(View.VISIBLE);
                        googleToken.setVisibility(View.GONE);
                        lyt_Google.setVisibility(View.GONE);
                         startCountDownTimer();

                    }else  if (email2fa1.equals("false") && google2fa1.equals("true")){
                        googleToken.setVisibility(View.VISIBLE);
                        lyt_Google.setVisibility(View.VISIBLE);
                        progressBarCircle.setVisibility(View.GONE);
                        btn_sendOtp.setVisibility(View.GONE);
                        timer_txt.setVisibility(View.GONE);
                        email_otp.setVisibility(View.GONE);
                        lyt_emiail.setVisibility(View.GONE);

                    }else if (email2fa1.equals("true") && google2fa1.equals("true")){
                        email_otp.setVisibility(View.VISIBLE);
                        lyt_emiail.setVisibility(View.VISIBLE);
                        btn_sendOtp.setVisibility(View.VISIBLE);
                        timer_txt.setVisibility(View.VISIBLE);
                        progressBarCircle.setVisibility(View.VISIBLE);
                        googleToken.setVisibility(View.VISIBLE);
                        lyt_Google.setVisibility(View.VISIBLE);

                        startCountDownTimer();

                    }else {
                        email_otp.setVisibility(View.GONE);
                        lyt_emiail.setVisibility(View.GONE);
                        btn_sendOtp.setVisibility(View.GONE);
                        timer_txt.setVisibility(View.GONE);
                        googleToken.setVisibility(View.GONE);
                        lyt_Google.setVisibility(View.GONE);
                        progressBarCircle.setVisibility(View.GONE);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Toast.makeText(Two_FA.this, ""+response, Toast.LENGTH_SHORT).show();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



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

    private Emitter.Listener helloEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                 //   Log.d("hellooooo",data.toString());
                  //  Toast.makeText(Payout_verification.this, ""+data.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    };
    private Emitter.Listener PendingReport = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    if(data==null){
                        transIDs = "Not found";
                        statuss = "Pending";
                    }else{

                        try {
                            transIDs = data.getString("transactionHash");
                            statuss = data.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                  //  Log.d("hellooooo",data.toString());

                   // Toast.makeText(Payout_verification.this, ""+data.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    };

    private Emitter.Listener CryptoError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];


                    AppUtils.showMessageOKCancel(data.toString(), Payout_verification.this,false, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }
                    });

                }
            });


        }
    };


    public void sendcoin(){



        progressDialog = KProgressHUD.create(Payout_verification.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        AuthToken =userData.getToken();

        Call<ResponseBody>call = RetrofitClient.getInstance().getApi().coinTransfer(AuthToken,CoinType,cryptoCurrency,"1",Token,otp,Amount,enterPin,result);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s =null;
                hidepDialog();
                if (response.code()==200){

                    try {
                        s = response.body().string();

                        JSONObject object = new JSONObject(s);
                        String responses = object.getString("response");

                        if (responses.equalsIgnoreCase("null")){
                            String transId = transIDs;
                            String status = "true";
                            SwapRespoinseModel swapRespoinseModel = new SwapRespoinseModel(transId,status);
                            SwapResponsePrefernce.getInstance(getApplicationContext()).SetData(swapRespoinseModel);

                            startActivity(new Intent(getApplicationContext(),SwapAcknowledgement.class));

                        }else{
                            JSONObject  object1 = new JSONObject(responses);

                            String transId = object1.getString("transactionHash");
                            String status = object1.getString("status");

                            SwapRespoinseModel swapRespoinseModel = new SwapRespoinseModel(transId,status);
                            SwapResponsePrefernce.getInstance(getApplicationContext()).SetData(swapRespoinseModel);

                            startActivity(new Intent(getApplicationContext(),SwapAcknowledgement.class));


                        }

                        // Toast.makeText(SwapEnterPin.this, ""+s, Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }else if (response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Payout_verification.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();
                        pinView.setLineColor(getResources().getColor(R.color.light_gray));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code()==401){
                    Snacky.builder()
                            .setActivity(Payout_verification.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                    pinView.setLineColor(getResources().getColor(R.color.light_gray));
                }else if (response.code()==504 ){

                    SwapRespoinseModel swapRespoinseModel = new SwapRespoinseModel(transIDs,statuss);
                    SwapResponsePrefernce.getInstance(getApplicationContext()).SetData(swapRespoinseModel);

                    Intent intent = new Intent(Payout_verification.this,SwapAcknowledgement.class);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();

                SwapRespoinseModel swapRespoinseModel = new SwapRespoinseModel(transIDs,statuss);
                SwapResponsePrefernce.getInstance(getApplicationContext()).SetData(swapRespoinseModel);

                Intent intent = new Intent(Payout_verification.this,SwapAcknowledgement.class);
                startActivity(intent);
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


    public void sendOtpAgain(View view) {
        String username = userData.getUsername();
        progressDialog = KProgressHUD.create(Payout_verification.this)
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


                 //   OTPexpire();
                    Snacky.builder()
                            .setView(view)
                            .setText("Otp resend in your register Email")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();
                    reset();
                }else if(response.code()==400){
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(Payout_verification.this)
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
                        .setActivity(Payout_verification.this)
                        .setText("Please Check Your Internet Connection")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

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
                btn_sendOtp.setAlpha(0.4f);

            }

            @Override
            public void onFinish() {

                click = true;
                timer_txt.setText("60s");
                // call to initialize the progress bar values
                setProgressBarValues();
                timerStatus = TimerStatus.STOPPED;
                btn_sendOtp.setAlpha(0.9f);

                btn_sendOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // resendOTP(v);
                        if(click==true){
                            sendOtpAgain(v);
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
                    Toast.makeText(Payout_verification.this, "Your Otp is expire", Toast.LENGTH_SHORT).show();

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