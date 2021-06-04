package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.croytowallet.Adapter.Ticket_Adapter;
import com.crypto.croytowallet.Model.CrptoInfoModel;
import com.crypto.croytowallet.Model.TicketModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.login.Login;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Ticket extends AppCompatActivity {
RecyclerView recyclerView;
TextView add_ticket_btn,ticket_history;
ImageView imageView;
    KProgressHUD progressDialog;
    UserData userData;

    ArrayList<TicketModel> ticketModels;
    Ticket_Adapter ticket_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        recyclerView = findViewById(R.id.Recycler_Add_Ticket);
        imageView =findViewById(R.id.back);
        add_ticket_btn=findViewById(R.id.add_more_ticket);
        ticket_history =findViewById(R.id.txt_list_is_empty);
        ticketModels =new ArrayList<TicketModel>();
            userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        getActiveDetails();


               back();

    }

    private void getActiveDetails() {

        String Token= userData.getToken();

        progressDialog = KProgressHUD.create(Ticket.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        StringRequest stringRequest =new StringRequest(Request.Method.GET, URLs.URL_GET_TIKECT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                hidepDialog();

                try {
                    JSONObject object = new JSONObject(response);
                    String result = object.getString("result");
                    JSONArray jsonArray =new JSONArray(result);

                    for (int i=0;i<=jsonArray.length();i++){

                        TicketModel ticketModel1 =new TicketModel();
                        JSONObject jsonObject =jsonArray.getJSONObject(i);

                        String status =jsonObject.getString("status");
                        String userId =jsonObject.getString("_id");
                        String subject =jsonObject.getString("subject");
                        String date =jsonObject.getString("date");
                        String description =jsonObject.getString("description");

                        ticketModel1.setStatus(status);
                        ticketModel1.setDate(date);
                        ticketModel1.setUserId(userId);
                        ticketModel1.setSubject(subject);
                        ticketModel1.setDescription(description);

                        ticketModels.add(ticketModel1);
                        Collections.reverse(ticketModels);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(ticketModels!=null && ticketModels.size()>0){
                    ticket_adapter =new Ticket_Adapter(ticketModels,getApplicationContext());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(ticket_adapter);
                }else{
                    ticket_history.setVisibility(View.VISIBLE);
                }

                // Toast.makeText(Ticket.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hidepDialog();

               // Toast.makeText(Ticket.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Token);

                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
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
                //Intent intent = new Intent(Ticket.this, Support.class);
                //startActivity(intent);
                onBackPressed();
            }
        });

        add_ticket_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Ticket.this,Add_Ticket.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onSaveInstanceState(new Bundle());

    }
}