package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.Payment.Pay_money;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;

public class ManualEnterUserName extends AppCompatActivity {
    ImageView imageView;
    EditText ed_username,ed_userid;
    String username,userId,token;
    CardView comfirm;
    UserData userData;
    KProgressHUD progressDialog;
    SharedPreferences sharedPreferences;
    TextInputLayout textInputLayout;
    Animation slide_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_enter_user_name);
        imageView =findViewById(R.id.back);
        ed_username = findViewById(R.id.ed_username1);
        textInputLayout = findViewById(R.id.user);
       // ed_userid  = findViewById(R.id.ed_userID);
        comfirm  = findViewById(R.id.confirm);

        slide_right = AnimationUtils.loadAnimation(ManualEnterUserName.this,R.anim.slide_in_right);
        //set Animatoin
        textInputLayout.startAnimation(slide_right);
        comfirm.startAnimation(slide_right);


        userData= SharedPrefManager.getInstance(this).getUser();


        sharedPreferences=getSharedPreferences("walletScan", Context.MODE_PRIVATE);

        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username =ed_username.getText().toString().trim();
           //     userId =ed_userid.getText().toString().trim();

                if (username.isEmpty()){
                    ed_username.requestFocus();
                    Snackbar warningSnackBar = Snacky.builder()
                            .setActivity(ManualEnterUserName.this)
                            .setText(" Please Enter username ")
                            .setTextColor(getResources().getColor(R.color.white))
                            .setDuration(Snacky.LENGTH_SHORT)
                            .warning();
                    warningSnackBar.show();
                }else{
                    userDetails();

                }
            }
        });
        back();
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
               /* Intent intent = new Intent(ManualEnterUserName.this, WalletScan.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

          */
            onBackPressed();
            }
        });

    }

    public void userDetails(){

        progressDialog = KProgressHUD.create(ManualEnterUserName.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();


      token=userData.getToken();



        StringRequest request=new StringRequest(Request.Method.POST, URLs.URL_USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();

                try {
                    JSONObject object =new JSONObject(response);
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String username = object.getString("username");
                    String userid = object.getString("userId");

                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("name",name);
                    editor.putString("email",email);
                    editor.putString("username",username);
                    editor.putString("id",userid);
                    editor.commit();

                    startActivity(new Intent(ManualEnterUserName.this, Pay_money.class));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  Toast.makeText(ManualEnterUserName.this, ""+response, Toast.LENGTH_SHORT).show();

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
               params.put("username",username);

               return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", token);

                return headers;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
     /*   RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);*/

    }

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);

            String message=data.getString("error");
            Snackbar warningSnackBar = Snacky.builder()
                    .setActivity(ManualEnterUserName.this)
                    .setText(message)
                    .setTextColor(getResources().getColor(R.color.white))
                    .setDuration(Snacky.LENGTH_SHORT)
                    .error();
            warningSnackBar.show();
         //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
}