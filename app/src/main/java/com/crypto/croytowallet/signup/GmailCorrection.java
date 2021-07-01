package com.crypto.croytowallet.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Activity.Kyc;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SignUpData;
import com.crypto.croytowallet.SharedPrefernce.SignUpRefernace;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.TransactionPin.EnterConfirmMnemonic;
import com.crypto.croytowallet.TransactionPin.TransactionPin;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.login.CrashOtpActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GmailCorrection extends AppCompatActivity {
    Button confirm;
    EditText Ed_Email;
    String oldEmail, username, check = "1", newEmail, token;
    ImageView editBtn;
    UserData userData;
    KProgressHUD progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail_correction);
        confirm = findViewById(R.id.confirm_btn);
        Ed_Email = findViewById(R.id.oldGmail);
        editBtn = findViewById(R.id.editEmail);
        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        oldEmail = userData.getEmail();
        username = userData.getUsername();
        token = userData.getToken();
        Ed_Email.setText(oldEmail);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.equalsIgnoreCase("0")) {
                    newEmail = Ed_Email.getText().toString().trim();
                    if (newEmail.isEmpty()) {
                        Snackbar warningSnackBar = Snacky.builder()
                                .setActivity(GmailCorrection.this)
                                .setText("Please New Correct Email")
                                .setTextColor(getResources().getColor(R.color.white))
                                .setDuration(Snacky.LENGTH_SHORT)
                                .warning();
                        warningSnackBar.show();

                    } else {

                        putApi(username, token, newEmail);
                    }

                } else if (check.equalsIgnoreCase("1")) {
                    sendOTP(username);
                }

            }
        });

    }

    public void putApi(String username, String token, String newEmail) {


        progressDialog = KProgressHUD.create(GmailCorrection.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        JsonObject bodyParameters = new JsonObject();
        bodyParameters.addProperty("username", username);
        bodyParameters.addProperty("newEmail", newEmail);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().EmailCorrection(token, bodyParameters);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();
                String s = null;

                if (response.code() == 200) {
                    try {
                        s = response.body().string();

                        JSONObject object = new JSONObject(s);
                        String result = object.getString("result");
                        JSONObject object1 = new JSONObject(result);
                        String email = object1.getString("email");
                        Ed_Email.setEnabled(false);
                        Ed_Email.setText(email);

                        check = "1";
                        editBtn.setVisibility(View.VISIBLE);

                        Snacky.builder()
                                .setActivity(GmailCorrection.this)
                                .setText("Successful Updated Your Email")
                                .setTextColor(getResources().getColor(R.color.white))
                                .setDuration(Snacky.LENGTH_SHORT)
                                .success()
                                .show();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 400 || response.code() == 401) {
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");
                        Snacky.builder()
                                .setActivity(GmailCorrection.this)
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
                        .setActivity(GmailCorrection.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }

    public void Edit_Btn(View view) {
        editBtn.setVisibility(View.GONE);
        Ed_Email.setEnabled(true);
        Ed_Email.setText("");
        check = "0";


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void sendOTP(String username) {


        progressDialog = KProgressHUD.create(GmailCorrection.this)
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

                    startActivity(new Intent(getApplicationContext(), CrashOtpActivity.class));
                    Toast.makeText(getApplicationContext(), "Otp send in your registered Email", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 400) {
                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(GmailCorrection.this)
                                .setText(" Oops Username Not Found !!!!!")
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
                        .setActivity(GmailCorrection.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

    }
}