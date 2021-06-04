package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;
import java.util.Map;


public class Add_Ticket extends AppCompatActivity {
    ImageView imageView;
    EditText ed_subject,ed_description;
    String subject,description;
    TextView addButton;
    KProgressHUD progressDialog;
    UserData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__ticket);
        imageView =findViewById(R.id.back);
        ed_subject = findViewById(R.id.subject);
        ed_description = findViewById(R.id.description);
        addButton = findViewById(R.id.add_ticket);

        data = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject = ed_subject.getText().toString().trim();
                description = ed_description.getText().toString().trim();
                if (subject.isEmpty()){
                    ed_subject.requestFocus();
                    ed_subject.setError("please Enter Subject");
                } else if(description.isEmpty()) {
                    ed_description.requestFocus();
                    ed_description.setError("please Enter Query");
                }else {
                    addTicket();
                }

            }
        });


        back();
    }

    private void addTicket() {

        String token = data.getToken();
        progressDialog = KProgressHUD.create(Add_Ticket.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.URL_ADD_TIKECT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();

                startActivity(new Intent(getApplicationContext(),Ticket.class));
                Toast.makeText(Add_Ticket.this, "Successfully added Your Tickect", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hidepDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("subject", subject);
                params.put("description", description);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", token);

                return headers;
            }


        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

       }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }
}