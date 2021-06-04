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
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SignUpData;
import com.crypto.croytowallet.SharedPrefernce.SignUpRefernace;
import com.crypto.croytowallet.TransactionPin.EnterConfirmMnemonic;
import com.crypto.croytowallet.TransactionPin.TransactionPin;
import com.crypto.croytowallet.database.RetrofitClient;
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
TextView txt_oldEmail;
EditText Ed_newEmail,Ed_password;
String oldEmail,newEmail,password,check="2",username;
ImageView editBtn;
TextInputLayout txt_newEmail,text_pass;
SignUpData signUpData;
    KProgressHUD progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail_correction);
        confirm = findViewById(R.id.confirm_btn);
        txt_oldEmail = findViewById(R.id.oldGmail);
        Ed_newEmail = findViewById(R.id.newEmail);
        Ed_password = findViewById(R.id.userPassword);
        editBtn = findViewById(R.id.editEmail);
        txt_newEmail = findViewById(R.id.text_newEmail);
        text_pass = findViewById(R.id.text_pass);

        signUpData = SignUpRefernace.getInstance(getApplicationContext()).getUser();
        oldEmail = signUpData.getGmail();
        username = signUpData.getUsername();
        txt_oldEmail.setText(oldEmail);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.equalsIgnoreCase("0")){
                    newEmail = Ed_newEmail.getText().toString().trim();
                    password = Ed_password.getText().toString().trim();
                    if (newEmail.isEmpty() || password.isEmpty()){
                        Snackbar warningSnackBar = Snacky.builder()
                                .setActivity(GmailCorrection.this)
                                .setText("Please fill all requirement")
                                .setTextColor(getResources().getColor(R.color.white))
                                .setDuration(Snacky.LENGTH_SHORT)
                                .warning();
                        warningSnackBar.show();

                    }else{

                        putApi(username,password,newEmail);
                          }

                }else if (check.equalsIgnoreCase("1")){
                     startActivity(new Intent(GmailCorrection.this, Add_Verification.class));
                }else if(check.equalsIgnoreCase("2")){
                  startActivity(new Intent(GmailCorrection.this, Add_Verification.class));

                }

            }
        });

    }

    public void putApi(String username, String password, String newEmail) {

     /*
*/
        progressDialog = KProgressHUD.create(GmailCorrection.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        JsonObject bodyParameters = new JsonObject();
        bodyParameters.addProperty("username",username);
        bodyParameters.addProperty("password",password);
        bodyParameters.addProperty("newEmail",newEmail);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().EmailCorrection(bodyParameters);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();
                String s=null;

                if (response.code()==200){
                    try {
                        s= response.body().string();

                        JSONObject object = new JSONObject(s);
                        String result = object.getString("result");
                        JSONObject object1 = new JSONObject(result);
                        String email=object1.getString("email");
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

                        check="1";
                        txt_oldEmail.setVisibility(View.VISIBLE);
                        editBtn.setVisibility(View.VISIBLE);
                        txt_newEmail.setVisibility(View.GONE);
                        text_pass.setVisibility(View.GONE);
                        txt_oldEmail.setText(email);



                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }else if(response.code()==400){
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
        txt_oldEmail.setVisibility(View.GONE);
        editBtn.setVisibility(View.GONE);
        txt_newEmail.setVisibility(View.VISIBLE);
        text_pass.setVisibility(View.VISIBLE);

        check="0";


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

}