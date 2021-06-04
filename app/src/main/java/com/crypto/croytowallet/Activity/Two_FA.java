package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.zcw.togglebutton.ToggleButton;

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

public class Two_FA extends AppCompatActivity {
    ImageView imageView;
    ToggleButton google_to_fa,email_to_fa;
    KProgressHUD progressDialog;
    UserData userData;
    SharedPreferences sharedPreferences = null;
    SharedPreferences sharedPreferences1 = null;
    Boolean booleanValue,booleanValue1,email2f,googl2f;
    String id;
    CardView g2a,e2a;
    Animation enterright;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two__f);
        imageView =findViewById(R.id.back);

       google_to_fa=findViewById(R.id.toogle1);
        email_to_fa=findViewById(R.id.toogle2);

        g2a = findViewById(R.id.g2a);
        e2a = findViewById(R.id.e2a);
        enterright = AnimationUtils.loadAnimation(Two_FA.this, R.anim.slide_in_left);
        g2a.startAnimation(enterright);
        e2a.startAnimation(enterright);



        userData= SharedPrefManager.getInstance(getApplicationContext()).getUser();


         id=userData.getId();

         // for Google 2FA
        sharedPreferences = getSharedPreferences("google",0);

        // for google2fa
        booleanValue1 = sharedPreferences.getBoolean("google2fa",false);
        if (booleanValue1){
            google_to_fa.setToggleOn(true);
        }

        googl2f = sharedPreferences.getBoolean("google2fa",false);


        google_to_fa.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("google2fa",true);
                    editor.commit();
                    google_to_fa.setToggleOn(true);

                    googl2f = sharedPreferences.getBoolean("google2fa",true);
                    GoogleFA_Enable(id,googl2f,email2f);


                }
                else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("google2fa",false);
                    editor.commit();
                    googl2f = sharedPreferences.getBoolean("google2fa",false);
                    google_to_fa.setToggleOff(true);

                    TwoFA_Disable(id,googl2f,email2f);

                    }
            }
        });

        // for Email 2fa
        sharedPreferences1 = getSharedPreferences("email",0);
        email2f = sharedPreferences1.getBoolean("email2fa",false);

        // for email2fa
        booleanValue = sharedPreferences1.getBoolean("email2fa",false);
        if (booleanValue){
            email_to_fa.setToggleOn(true);
        }


        email_to_fa.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on){
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.putBoolean("email2fa",true);
                    editor.commit();
                    email_to_fa.setToggleOn(true);
                    email2f = sharedPreferences1.getBoolean("email2fa",true);
                    EmailFA_Enable(id,googl2f,email2f);

                }
                else{

                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.putBoolean("email2fa",false);
                    editor.commit();
                    email2f = sharedPreferences1.getBoolean("email2fa",false);
                    email_to_fa.setToggleOff(true);

                    TwoFA_Disable(id,googl2f,email2f);

                }
            }
        });

        back();
        get2fa();




    }

    public void get2fa(){

        String token = userData.getToken();
        StringRequest request=new StringRequest(Request.Method.GET, URLs.URL_GET_2FA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object =new JSONObject(response);
                    String result =  object.getString("result");
                    JSONObject object1 = new JSONObject(result);

                    booleanValue = object1.getBoolean("email2fa");
                    booleanValue1 = object1.getBoolean("google2fa");


                    } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                parseVolleyError(error);

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


    public void EmailFA_Enable(String id, Boolean google2fa,Boolean email2fa){

        progressDialog = KProgressHUD.create(Two_FA.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody>call = RetrofitClient.getInstance().getApi().TwoFA(id,google2fa,email2fa);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s =null;
                hidepDialog();
                if (response.code()==200){


                        Snacky.builder()
                                .setActivity(Two_FA.this)
                                .setText("successful Turn ON ")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .success()
                                .show();


                }else if (response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Two_FA.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                hidepDialog();
                Snacky.builder()
                        .setActivity(Two_FA.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }
    public void GoogleFA_Enable(String id, Boolean google2fa,Boolean email2fa){

        progressDialog = KProgressHUD.create(Two_FA.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody>call = RetrofitClient.getInstance().getApi().TwoFA(id,google2fa,email2fa);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s =null;
                hidepDialog();
                if (response.code()==200){


                        Snacky.builder()
                                .setActivity(Two_FA.this)
                                .setText("successful Turn ON ")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .success()
                                .show();


                }else if (response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Two_FA.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                hidepDialog();
                Snacky.builder()
                        .setActivity(Two_FA.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }
    public void TwoFA_Disable(String id, Boolean google2fa,Boolean email2fa){

        progressDialog = KProgressHUD.create(Two_FA.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody>call = RetrofitClient.getInstance().getApi().TwoFA(id,google2fa,email2fa);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s =null;
                hidepDialog();
                if (response.code()==200){

                                Snacky.builder()
                                .setActivity(Two_FA.this)
                                .setText("successful Turn OFF ")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .success()
                                .show();



                }else if (response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Two_FA.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                hidepDialog();
                Snacky.builder()
                        .setActivity(Two_FA.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }


    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            String message=data.getString("error");
            Snacky.builder()
                    .setActivity(Two_FA.this)
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

       /* Intent intent = new Intent(Two_FA.this, Security.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();*/

        onSaveInstanceState(new Bundle());
    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(Two_FA.this, Security.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
                onBackPressed();
            }
        });

    }

    public void Obtain_2fa(View view) {
        resendOTP();
    }

    public void resendOTP() {
        UserData userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String usernames = userData.getUsername();

        progressDialog = KProgressHUD.create(Two_FA.this)
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

                    startActivity(new Intent(Two_FA.this,CheckGoogle2FA.class));
                    Toast.makeText(getApplicationContext(), "Otp send in your register Email", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 400) {
                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(Two_FA.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(Two_FA.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }


}