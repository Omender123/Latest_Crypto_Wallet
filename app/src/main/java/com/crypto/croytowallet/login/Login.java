package com.crypto.croytowallet.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.CrashDataModel;
import com.crypto.croytowallet.SharedPrefernce.CreshSharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.signup.Referral_code;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    private long timeCountInMilliSeconds = 1 * 60000;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;


    Button login;
TextView forget_password,signup,unlock;
EditText username,password,otp;
TextInputLayout layout_otp;
    KProgressHUD progressDialog;
    String url="http://13.233.136.56:8080/api/user/login";
    ConstraintLayout linearLayout;
    Animation fade_in,blink;
    FusedLocationProviderClient fusedLocationProviderClient;
    String locations,ipAddress,os,id,Devicetoken;
    LinearLayout linearotp;
    TextView timer_txt,resendOtp;
    private static CountDownTimer countDownTimer;
    Boolean click=false,click1=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login =findViewById(R.id.login1);
        signup =findViewById(R.id.signup);
        layout_otp =findViewById(R.id.otp);
        unlock = findViewById(R.id.unlock);
        linearotp = findViewById(R.id.linearotp);
        timer_txt = findViewById(R.id.timer);
        resendOtp = findViewById(R.id.resendOtp);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);


        //input
        username = findViewById(R.id.ed_username1);
        password = findViewById(R.id.ed_password1);
        otp = findViewById(R.id.ed_otp1);
       // animation
        linearLayout= findViewById(R.id.login_layout);
        fade_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        blink= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        linearLayout.startAnimation(fade_in);

        forget_password=findViewById(R.id.forget);
        listener();

        getAllDetails();
        getosName();
        //if the user is already logged in we will directly start the profile activity



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              signIn(v);
              //  Toast.makeText(Login.this, ""+os+locations+ipAddress+Devicetoken, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void signIn(View view) {
        if (validate() == false) {
            onSignupFailed();
            return;
        }
        saveToServerDB(view);

    }
    public void onSignupFailed() {
     //   Toast.makeText(getBaseContext(), "Please fill all requirement ", Toast.LENGTH_LONG).show();
        Snackbar warningSnackBar = Snacky.builder()
                .setActivity(Login.this)
                .setText("Please fill all requirement")
                .setTextColor(getResources().getColor(R.color.white))
                .setDuration(Snacky.LENGTH_SHORT)
                .warning();
        warningSnackBar.show();
        login.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String passwords = password.getText().toString().trim();
        String usernames = username.getText().toString().trim();


        if (passwords.isEmpty() || passwords.length() < 8) {
            password.setError("please enter your password is more then 8 digit");
            requestFocus(password);
            valid = false;
        } else {
            password.setError(null);
        }

        if (usernames.isEmpty()) {
            username.setError("Please enter username");
            requestFocus(username);
            valid = false;
        } else {
            username.setError(null);
        }

        return valid;
    }

    private void saveToServerDB(View view) {

        String passwords = password.getText().toString().trim();
        String usernames = username.getText().toString().trim();
        String otp1 = otp.getText().toString().trim();

        CrashDataModel crashDataModel = new CrashDataModel(usernames,passwords);
        CreshSharedPrefManager.getInstance(getApplicationContext()).SetCrashData(crashDataModel);

        progressDialog = KProgressHUD.create(Login.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call =RetrofitClient.getInstance().getApi()
                .Login(usernames,passwords,otp1,locations,os,ipAddress);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s=null;
                hideKeyboard(view);
                hidepDialog();
                if (response.code()==200){

                    try {
                        s=response.body().string();
                        JSONObject object= new JSONObject(s);
                        String result =object.getString("result");
                        String token =object.getString("token");
                    //    updateCoin(token);
                        JSONObject  object1 = new JSONObject(result);
                        id= object1.getString("_id");
                        String name = object1.getString("name");
                        String email = object1.getString("email");
                        String phone = object1.getString("phone");
                        String username = object1.getString("username");
                        String mnemonic = object1.getString("mnemonic");
                        String Referral_code = object1.getString("myReferalcode");
                        String transaction_Pin = object1.getString("transactionPin");
                        String ETH = object1.getString("ethAddress");
                        String BTC = object1.getString("bitcoinAddress");
                        String LITE = object1.getString("litecoinAddress");
                        String XRP = object1.getString("xrpAddress");

                        String security=object1.getString("securityEnable");
                        JSONObject object2=new JSONObject(security);
                        String EMAIL2FA = object2.getString("email2fa");


                        UserData userData=new UserData(id,name,email,phone,username,mnemonic,Referral_code,transaction_Pin,token
                                ,ETH,BTC,LITE,XRP,EMAIL2FA);

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(userData);
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                       Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();



                        new Handler().postDelayed(new Runnable() {


                            @Override
                            public void run() {
                                // This method will be executed once the timer is over
                                sendEmail();

                            }
                        }, 500);

                       // Toast.makeText(Login.this, ""+s, Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==400) {
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");
                        if(error.equals("Incorrect otp")){
                            if (click1==true){
                                Snacky.builder()
                                        .setActivity(Login.this)
                                        .setText(" Incorrect otp ")
                                        .setDuration(Snacky.LENGTH_SHORT)
                                        .setActionText(android.R.string.ok)
                                        .error()
                                        .show();
                                layout_otp.setVisibility(View.VISIBLE);
                                linearotp.setVisibility(View.VISIBLE);

                            }else{
                                resendOTP();
                                layout_otp.setVisibility(View.VISIBLE);
                                linearotp.setVisibility(View.VISIBLE);
                                startCountDownTimer();

                            }


                        }else if(error.equals("Transaction Pin not set")){

                            startActivity(new Intent(getApplicationContext(),CreashSetTransactionPin.class));
                        }else if(error.equals("Your email is not verified at the time of signup")){
                            startActivity(new Intent(getApplicationContext(),CrashOtpActivity.class));
                            resendOTP();
                        }else{

                            Snacky.builder()
                                    .setActivity(Login.this)
                                    .setText(error)
                                    .setDuration(Snacky.LENGTH_SHORT)
                                    .setActionText(android.R.string.ok)
                                    .error()
                                    .show();
                        }
                    //    Toast.makeText(Login.this, ""+s, Toast.LENGTH_SHORT).show();
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
                        .setText(t.getLocalizedMessage())
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

    public void sendEmail(){

        Call<ResponseBody>call =RetrofitClient.getInstance().getApi().Send_Email(id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s=null;
                if (response.code()==200){

                    Log.d("res","send");
                }else if(response.code()==400){
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Log.d("error1",error);



                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("error",t.getMessage());
            }
        });
    }

    public void resendOTP() {

        String usernames = username.getText().toString().trim();


        progressDialog = KProgressHUD.create(Login.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call=  RetrofitClient
                .getInstance()
                .getApi().sendOtp(usernames);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hidepDialog();

                String s=null;
                if (response.code()==200){

                    Snacky.builder()
                            .setActivity(Login.this)
                            .setText("Otp send in your register Email")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();
                    click1=true;

                      //  OTPexpire();
                }else if(response.code()==400){
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(Login.this)
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
                        .setActivity(Login.this)
                        .setText("Please Check Your Internet Connection")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }


    public void getAllDetails(){

        StringRequest stringRequest =new StringRequest(Request.Method.GET, "http://ip-api.com/json", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object =new JSONObject(response);
                    ipAddress = object.getString("query");
                    locations = object.getString("city");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

 //               Toast.makeText(Login.this, ""+error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    public void getDetails(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(Login.this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getLocation();

        }else{
            ActivityCompat.requestPermissions(Login.this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}
                    ,44);
        }
        getosName();
        Ipaddress();
    }
    public void getosName(){
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                os= String.valueOf(builder.append(fieldName));
                //    builder.append("sdk=").append(fieldValue);

            }
        }
    }
    public void Ipaddress(){
        try {
            //permition
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL myIp = new URL("https://checkip.amazonaws.com/");
            URLConnection c = myIp.openConnection();
            c.setConnectTimeout(1000);
            c.setReadTimeout(1000);

            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            // textView.setText(in.readLine());
            ipAddress=in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                // Initialize Location
                Location location =task.getResult();
                if (location!=null){
                    try {
                        // Initialize geoCoder
                        Geocoder geocoder = new Geocoder(Login.this, Locale.getDefault());
                        // Initialize addressList
                        List<Address> addresses =geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );
                        locations=addresses.get(0).getSubLocality()+","+addresses.get(0).getLocality();
                       /* textView1.setText(Html.fromHtml("<font color='#6200EE'><b>Latitude : </b><br></font>"
                                +addresses.get(0).getLocality()));*//* +addresses.get(0).getLatitude()+"<br>"+"<font color='#6200EE'><b>Longitude : </b><br></font> "
                            +addresses.get(0).getLongitude()));
 *//*               //    Toast.makeText(MainActivity.this, ""+addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
                         */  }catch (Exception e){
                    }

                }
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


public void listener(){
    forget_password.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), ForgetPassword.class).putExtra("options","0"));

        }
    });
    signup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), Referral_code.class));
        }
    });

    unlock.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), ForgetPassword.class).putExtra("options","1"));
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
                            resendOTP();
                            reset();
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

    public void updateCoin(String token){

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().UpdateCoin(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s= null;
                if (response.isSuccessful()){

                    try {
                        s= response.body().string();
                        //  Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else if(response.code()==400||response.code()==401){
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");
                        Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}