package com.crypto.croytowallet.TransactionPin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SignUpData;
import com.crypto.croytowallet.SharedPrefernce.SignUpRefernace;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.signup.GmailCorrection;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class EnterConfirmMnemonic extends AppCompatActivity implements View.OnClickListener {
TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,tt1,tt2,tt3,tt4,tt5,tt6,tt7,tt8,tt9,tt10,tt11,tt12;
String text1,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,text12;
    SharedPreferences preferences;
    SignUpData user;
    KProgressHUD progressDialog;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_confirm_mnemonic);
        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);
        t4=findViewById(R.id.t4);
        t5=findViewById(R.id.t5);
        t6=findViewById(R.id.t6);
        t7=findViewById(R.id.t7);
        t8=findViewById(R.id.t8);
        t9=findViewById(R.id.t9);
        t10=findViewById(R.id.t10);
        t11=findViewById(R.id.t11);
        t12=findViewById(R.id.t12);

         user = SignUpRefernace.getInstance(this).getUser();


        tt1=findViewById(R.id.tt1);
        tt2=findViewById(R.id.tt2);
        tt3=findViewById(R.id.tt3);
        tt4=findViewById(R.id.tt4);
        tt5=findViewById(R.id.tt5);
        tt6=findViewById(R.id.tt6);
        tt7=findViewById(R.id.tt7);
        tt8=findViewById(R.id.tt8);
        tt9=findViewById(R.id.tt9);
        tt10=findViewById(R.id.tt10);
        tt11=findViewById(R.id.tt11);
        tt12=findViewById(R.id.tt12);
        
    
        inti();
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);
        t7.setOnClickListener(this);
        t8.setOnClickListener(this);
        t9.setOnClickListener(this);
        t10.setOnClickListener(this);
        t11.setOnClickListener(this);
        t12.setOnClickListener(this);

        tt1.setOnClickListener(this);
        tt2.setOnClickListener(this);
        tt3.setOnClickListener(this);
        tt4.setOnClickListener(this);
        tt5.setOnClickListener(this);
        tt6.setOnClickListener(this);
        tt7.setOnClickListener(this);
        tt8.setOnClickListener(this);
        tt9.setOnClickListener(this);
        tt10.setOnClickListener(this);
        tt11.setOnClickListener(this);
        tt12.setOnClickListener(this);

        preferences=getApplicationContext().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String transaction = preferences.getString("transaction","");
        Toast.makeText(this, ""+transaction, Toast.LENGTH_SHORT).show();
    }


    public void Done(View view) {
        String a=tt1.getText().toString();
        String b=tt2.getText().toString();
        String c=tt3.getText().toString();
        String d=tt4.getText().toString();
        String e=tt5.getText().toString();
        String f=tt6.getText().toString();
        String g=tt7.getText().toString();
        String h=tt8.getText().toString();
        String i=tt9.getText().toString();
        String j=tt10.getText().toString();
        String k=tt11.getText().toString();
        String l=tt12.getText().toString();


        String   Mnemonic=a+" "+b+" "+c+" "+d+" "+e+" "+f+" "+g+" "+h+" "+i+" "+j+" "+k+" "+l;
       // String text= user.getMnemonic();


           progressDialog = KProgressHUD.create(EnterConfirmMnemonic.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait.....")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();

            showpDialog();

            String transaction = preferences.getString("transaction","");
            String username=   user.getUsername();

        StringRequest request=new StringRequest(Request.Method.POST, URLs.URL_SET_TRANSACTIONPin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                 startActivity(new Intent(EnterConfirmMnemonic.this, GmailCorrection.class));

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
                params.put("mnemonic",Mnemonic);
                params.put("transactionPin",transaction);
                params.put("username",username);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                // headers.put("Authorization", "Bearer "+Token);

                return headers;
            }

        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);


    }

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);

            String message=data.getString("error");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        }
    }



  /*  private void saveToServerDB() {

        progressDialog = KProgressHUD.create(EnterConfirmMnemonic.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();
        String transaction = preferences.getString("transaction","");
        String username=   user.getUsername();

    }*/
    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
        public void inti(){

            SignUpData user = SignUpRefernace.getInstance(this).getUser();
            String text= user.getMnemonic();

            String[] s= text.split(" ");

            text1 =s[0];
            text11 =s[1];
            text9 =s[2];
            text7 =s[3];
            text5 =s[4];
            text3 =s[5];
            text8 =s[6];
            text4 =s[7];
            text2 =s[8];
            text10 =s[9];
            text6 =s[10];
            text12 =s[11];



            t1.setText(text1);
        t2.setText(text11);
        t3.setText(text9);
        t4.setText(text7);
        t5.setText(text5);
        t6.setText(text3);
        t7.setText(text8);
        t8.setText(text4);
        t9.setText(text2);
        t10.setText(text10);
        t11.setText(text6);
        t12.setText(text12);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.t1:
                t1.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt1.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt1.setText(text1);
                tt1.setVisibility(View.VISIBLE);
                break;

            case R.id.t2:
                t2.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt2.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt2.setText(text11);
                tt2.setVisibility(View.VISIBLE);
                break;

            case R.id.t3:
                t3.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt3.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt3.setText(text9);
                tt3.setVisibility(View.VISIBLE);
                break;

            case R.id.t4:
                t4.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt4.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt4.setVisibility(View.VISIBLE);
                tt4.setText(text7);
                break;

            case R.id.t5:
                t5.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt5.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt5.setText(text5);
                tt5.setVisibility(View.VISIBLE);
                break;

            case R.id.t6:
                t6.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt6.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt6.setText(text3);
                tt6.setVisibility(View.VISIBLE);
                break;

            case R.id.t7:
                t7.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt7.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt7.setText(text8);
                tt7.setVisibility(View.VISIBLE);
                break;

            case R.id.t8:
                t8.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt8.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt8.setText(text4);
                tt8.setVisibility(View.VISIBLE);
                break;

            case R.id.t9:
                t9.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt9.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt9.setText(text2);
                tt9.setVisibility(View.VISIBLE);
                break;

            case R.id.t10:
                t10.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt10.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt10.setText(text10);
                tt10.setVisibility(View.VISIBLE);
                break;

                case R.id.t11:
                    t11.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    tt11.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    tt11.setText(text6);
                    tt11.setVisibility(View.VISIBLE);
                break;

            case R.id.t12:
                t12.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt12.setBackgroundColor(getResources().getColor(R.color.purple_500));
                tt12.setText(text12);
                tt12.setVisibility(View.VISIBLE);
                break;

            case R.id.tt1:
                t1.setBackgroundDrawable(getResources().getDrawable(R.drawable.textviewborder));
                tt1.setVisibility(View.GONE);
                break;

            case R.id.tt2:
                t2.setBackgroundDrawable(getResources().getDrawable(R.drawable.textviewborder));
                tt2.setVisibility(View.GONE);
                break;

            case R.id.tt3:
                t3.setBackgroundDrawable(getResources().getDrawable(R.drawable.textviewborder));
                tt3.setVisibility(View.GONE);
                break;

            case R.id.tt4:
                t4.setBackgroundDrawable(getResources().getDrawable(R.drawable.textviewborder));
                tt4.setVisibility(View.GONE);
                break;

            case R.id.tt5:
                t5.setBackgroundDrawable(getResources().getDrawable(R.drawable.textviewborder));
                tt5.setVisibility(View.GONE);
                break;

            case R.id.tt6:
                t6.setBackgroundDrawable(getResources().getDrawable(R.drawable.textviewborder));
                tt6.setVisibility(View.GONE);
                break;

            case R.id.tt7:
                t7.setBackgroundDrawable(getResources().getDrawable(R.drawable.textviewborder));
                tt7.setVisibility(View.GONE);
                break;

            case R.id.tt8:
                t8.setBackgroundDrawable(getResources().getDrawable(R.drawable.textviewborder));
                tt8.setVisibility(View.GONE);
                break;

            case R.id.tt9:
                t9.setBackgroundDrawable(getResources().getDrawable(R.drawable.textviewborder));
                tt9.setVisibility(View.GONE);
                break;

            case R.id.tt10:
                t10.setBackgroundDrawable(getResources().getDrawable(R.drawable.textviewborder));
                tt10.setVisibility(View.GONE);
                break;

            case R.id.tt11:
                t11.setBackgroundDrawable(getResources().getDrawable(R.drawable.textviewborder));
                tt11.setVisibility(View.GONE);
                break;

            case R.id.tt12:
                t12.setBackgroundDrawable(getResources().getDrawable(R.drawable.textviewborder));
                tt12.setVisibility(View.GONE);
                break;


        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }
}