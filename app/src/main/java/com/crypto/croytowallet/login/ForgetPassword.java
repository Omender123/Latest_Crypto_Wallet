package com.crypto.croytowallet.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.database.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {
    Button next1;
    KProgressHUD progressDialog;
    EditText enter_username;
    TextView welcome;
    String options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        next1=findViewById(R.id.next1);
        enter_username = findViewById(R.id.enter_user);
        welcome = findViewById(R.id.welcome);


        Bundle bundle = getIntent().getExtras();
        options = bundle.getString("options");
        if (options.equals("1")){
            welcome.setText(R.string.un_lock);
        }

        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernames = enter_username.getText().toString().trim();



                sendOtp(v);
            }
        });
    }

    public void sendOtp(View view) {
        if (validate() == false) {
            onOTPFailed();
            return;
        }
        saveToServerDB(view);

    }
    public void onOTPFailed() {
       // Toast.makeText(getBaseContext(), "Please fill all requirement ", Toast.LENGTH_LONG).show();
        Snackbar warningSnackBar = Snacky.builder()
                .setActivity(ForgetPassword.this)
                .setText("Please enter the username")
                .setTextColor(getResources().getColor(R.color.white))
                .setDuration(Snacky.LENGTH_SHORT)
                .warning();
               warningSnackBar.show();

        next1.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String usernames = enter_username.getText().toString().trim();


        if (usernames.isEmpty()) {
          //  enter_username.setError("");
            requestFocus(enter_username);
            valid = false;
        } else {
            enter_username.setError(null);
        }

        return valid;
    }

    private void saveToServerDB(View view) {

        String usernames = enter_username.getText().toString().trim();

        progressDialog = KProgressHUD.create(ForgetPassword.this)
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
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();

                String s=null;
                if (response.code()==200){
                    hideKeyboard(view);

                    if (options.equals("0")){
                        Intent intent= new Intent(getApplicationContext(), OTP_Activity.class);
                        intent.putExtra("username",usernames);
                        intent.putExtra("options","0");
                        startActivity(intent);
                        finish();


                    }else if (options.equals("1")  ) {
                        Intent intent = new Intent(getApplicationContext(), Unlock_Account.class);
                        intent.putExtra("username", usernames);
                        intent.putExtra("options","1");
                        startActivity(intent);
                        finish();

                    }
                        Toast.makeText(getApplicationContext(), "Otp send in your registered Email", Toast.LENGTH_SHORT).show();

                }else if(response.code()==400){
                    hideKeyboard(view);
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

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


}