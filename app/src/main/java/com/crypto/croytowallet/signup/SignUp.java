package com.crypto.croytowallet.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.crypto.croytowallet.Extra_Class.InitApplication;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SignUpData;
import com.crypto.croytowallet.SharedPrefernce.SignUpRefernace;
import com.crypto.croytowallet.TransactionPin.TransactionPin;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.login.Change_Password;
import com.crypto.croytowallet.login.Login;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    Animation fade_in;
    ConstraintLayout constraintLayout;
    Button ready_to1;
    EditText name1, username1, email1, phoneno1, pass1, repass1;
    KProgressHUD progressDialog;
    String url = "http://13.233.136.56:8080/api/user/register";
    SharedPreferences sharedPreferences;
    Spinner countryCode;
    ArrayList<String> code;
    String codeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // animation
        constraintLayout = findViewById(R.id.signup1);
        fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        code = new ArrayList<String>();

        name1 = findViewById(R.id.name1);
        username1 = findViewById(R.id.username1);
        email1 = findViewById(R.id.email1);
        phoneno1 = findViewById(R.id.phone1);
        pass1 = findViewById(R.id.pass1);
        repass1 = findViewById(R.id.re_pass1);
        countryCode = findViewById(R.id.countryCode);

        ready_to1 = findViewById(R.id.ready_to12);
        ready_to1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(v);
                hideKeyboard((Button) v);
            }
        });

        getCountryCode();

    }

    public void getCountryCode() {

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getCountryCode();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;

                if (response.code() == 200) {
                    try {
                        s = response.body().string();

                        JSONObject object = new JSONObject(s);

                        String result = object.getString("result");

                        JSONArray jsonArray = new JSONArray(result);

                        for (int i = 0; i <= jsonArray.length(); i++) {
                            code.add(jsonArray.getString(i));
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayAdapter<String> adp = new ArrayAdapter<String>(SignUp.this, R.layout.spinner_list, code);
                    countryCode.setAdapter(adp);

                  //  adp.setDropDownViewResource(R.layout.spinner_list);

                    countryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            codeName = parent.getItemAtPosition(position).toString();
                            // Toast.makeText(parent.getContext(), city, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } else if (response.code() == 400) {


                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(SignUp.this)
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
                Snacky.builder()
                        .setActivity(SignUp.this)
                        .setText("Internet problem")
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

    public void signup(View parentView) {
        if (validate() == false) {
            onSignupFailed();
            return;
        }
        saveToServerDB(parentView);
    }


    public void onSignupFailed() {
        //   Toast.makeText(getBaseContext(), "Please fill all requirement ", Toast.LENGTH_LONG).show();

        Snackbar warningSnackBar = Snacky.builder()
                .setActivity(SignUp.this)
                .setText("Please fill all requirement")
                .setTextColor(getResources().getColor(R.color.white))
                .setDuration(Snacky.LENGTH_SHORT)
                .warning();
        warningSnackBar.show();
        ready_to1.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = name1.getText().toString();
        String username = username1.getText().toString();
        String email = email1.getText().toString();
        String mobile = phoneno1.getText().toString();
        String password = pass1.getText().toString();
        String reEnterPassword = repass1.getText().toString();

        if (name.isEmpty()) {
            name1.setError("Please enter your name");
            requestFocus(name1);
            valid = false;
        } else {
            name1.setError(null);
        }
        if (username.isEmpty()) {
            username1.setError("Please enter your username");
            requestFocus(username1);
            valid = false;
        } else {
            username1.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email1.setError("enter a valid email address");
            requestFocus(email1);
            valid = false;
        } else {
            email1.setError(null);
        }
        if (mobile.isEmpty() || mobile.length() < 10) {
            phoneno1.setError("enter a Mobile");
            requestFocus(phoneno1);
            valid = false;
        } else {
            phoneno1.setError(null);
        }

        if (password.isEmpty() || password.length() < 8) {
            pass1.setError("please enter your password is more then 8 digit");
            requestFocus(pass1);
            valid = false;
        } else {
            pass1.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || !(reEnterPassword.equals(password))) {
            repass1.setError("Password Do not match");
            requestFocus(repass1);
            valid = false;
        } else {
            repass1.setError(null);
        }

        return valid;
    }

    private void saveToServerDB(View parentView) {

        progressDialog = KProgressHUD.create(SignUp.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        String name = name1.getText().toString();
        String username = username1.getText().toString();
        String email = email1.getText().toString();
        String mobile = phoneno1.getText().toString();
        String password = pass1.getText().toString();
        Bundle bundle = getIntent().getExtras();
        String refercode = bundle.getString("referral_code");

        Log.d("referral_Code", refercode);

        String number = codeName + mobile;
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().register(name, email, username, password, refercode, number);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                hidepDialog();

                if (response.code() == 200) {

                    try {
                        s = response.body().string();
                        JSONObject object = new JSONObject(s);
                        String result = object.getString("result");
                        JSONObject object1 = new JSONObject(result);

                        String googlekey = object1.getString("googleAuthenticatorKey");

                        JSONObject object2 = new JSONObject(googlekey);
                        SignUpData user = new SignUpData(
                                object1.getString("username"),
                                object1.getString("mnemonic"),
                                object2.getString("key"),
                                object1.getString("email")
                        );

                        //storing the user in shared preferences
                        SignUpRefernace.getInstance(getApplicationContext()).UserSignUP(user);

                        Intent intent = new Intent(SignUp.this, TransactionPin.class);
                        // Intent intent = new Intent(getApplicationContext(), Google_auth.class);
                        startActivity(intent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 400) {

                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");

                        Snackbar snackbar = Snackbar
                                .make(parentView, "Oops username or email address is already exist", Snackbar.LENGTH_LONG)
                                .setAction("ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Snackbar snackbar1 = Snackbar.make(parentView, " " + error, Snackbar.LENGTH_SHORT);
                                        snackbar1.show();
                                    }
                                });

                        snackbar.show();
                        // Toast.makeText(SignUp.this, jsonObject1.getString("error")+"", Toast.LENGTH_SHORT).show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                    //    Toast.makeText(ContactNumberActivity.this, "Please enter your details", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(SignUp.this)
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());

    }

    public void signup_login(View view) {
       /* Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
*/
    }

    public void signup_signup(View view) {
      /*  Intent intent = new Intent(getApplicationContext(), Referral_code.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
    }



}