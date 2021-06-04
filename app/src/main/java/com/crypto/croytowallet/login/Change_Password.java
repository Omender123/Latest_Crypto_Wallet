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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;

public class
Change_Password extends AppCompatActivity {
Button done;
    KProgressHUD progressDialog;
    EditText enter_username,enter_password,enter_Cpass,enter_mnemonic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        done=findViewById(R.id.done);
        enter_username = findViewById(R.id.enter_username1);
        enter_password = findViewById(R.id.enter_new_password);
        enter_Cpass = findViewById(R.id.confirm_password);
        enter_mnemonic = findViewById(R.id.enter_mnemonic);
        Bundle bundle = getIntent().getExtras();
        String otp = bundle.getString("otp");
         Toast.makeText(this, ""+otp, Toast.LENGTH_SHORT).show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  startActivity(new Intent(getApplicationContext(),Login.class));*/
                ChangePassword(v);
                hideKeyboard(v);
            }
        });
    }

    public void ChangePassword(View view) {
        if (validate() == false) {
            onOTPFailed();
            return;
        }
        saveToServerDB(view);

    }
    public void onOTPFailed() {
        // Toast.makeText(getBaseContext(), "Please fill all requirement ", Toast.LENGTH_LONG).show();
        Snackbar warningSnackBar = Snacky.builder()
                .setActivity(Change_Password.this)
                .setText("Please fill all requirement")
                .setTextColor(getResources().getColor(R.color.white))
                .setDuration(Snacky.LENGTH_SHORT)
                .warning();
        warningSnackBar.show();

        done.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String usernames = enter_username.getText().toString().trim();
        String password = enter_password.getText().toString().trim();
        String mnemonic = enter_mnemonic.getText().toString().trim();
        String c_pass = enter_Cpass.getText().toString().trim();

        if (usernames.isEmpty()) {
            enter_username.setError("Please enter username");
            requestFocus(enter_username);
            valid = false;
        } else {
            enter_username.setError(null);
        }

        if (mnemonic.isEmpty()) {
            enter_mnemonic.setError("Please enter Mnemonic");
            requestFocus(enter_mnemonic);
            valid = false;
        } else {
            enter_mnemonic.setError(null);
        }
        if (password.isEmpty() || password.length() < 8 ) {
            enter_password.setError("please enter your password is more then 8 digit");
            requestFocus(enter_password);
            valid = false;
        } else {
            enter_password.setError(null);
        }

        if (c_pass.isEmpty() || c_pass.length() < 4  || !(c_pass.equals(password))) {
            enter_Cpass.setError("Password Do not match");
            requestFocus(enter_Cpass);
            valid = false;
        } else {
            enter_Cpass.setError(null);
        }

        return valid;
    }

    private void saveToServerDB(View view) {

        String usernames = enter_username.getText().toString().trim();
        String password = enter_password.getText().toString().trim();
        String mnemonic = enter_mnemonic.getText().toString().trim();
        Bundle bundle = getIntent().getExtras();
        String otp = bundle.getString("otp");


        progressDialog = KProgressHUD.create(Change_Password.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        StringRequest request=new StringRequest(Request.Method.PUT, URLs.URL_CHANGE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();


                Toast.makeText(Change_Password.this, "Password changed Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                parseVolleyError(error);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username",usernames);
                params.put("mnemonic",mnemonic);
                params.put("password",password);
                params.put("otp",otp);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();



                return headers;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);


    }

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);

            String message=data.getString("error");
            Snacky.builder()
                    .setActivity(Change_Password.this)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_SHORT)
                    .setActionText(android.R.string.ok)
                    .error()
                    .show();
          //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        }
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