package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.Adapter.SelectCurrencyAdapter;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.Model.CurrencyModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectCurrency extends AppCompatActivity implements HistoryClickLister {
    ImageView imageView;
    RecyclerView recyclerView;
    KProgressHUD progressDialog;
    UserData userData;
    ArrayList<CurrencyModel> currencyModels;
    SelectCurrencyAdapter selectCurrencyAdapter;
    SharedPreferences sharedPreferences;
    EditText search_input;

    CharSequence search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_currency);
        imageView = findViewById(R.id.back);
        search_input = findViewById(R.id.search_currency);
        recyclerView = findViewById(R.id.recyclerCurrency);
        currencyModels = new ArrayList<CurrencyModel>();
        sharedPreferences = getSharedPreferences("currency", Context.MODE_PRIVATE);

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        back();
        getCurrency();
    }


    public void getCurrency() {
        String token = userData.getToken();

        progressDialog = KProgressHUD.create(SelectCurrency.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GET_CURRENCY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();

                try {
                    JSONObject object = new JSONObject(response);
                    String result = object.getString("currency");
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i <= jsonArray.length(); i++) {
                        CurrencyModel currencyModel1 = new CurrencyModel();
                        JSONObject object1 = jsonArray.getJSONObject(i);
                        String countryName = object1.getString("countryName");
                        String currency = object1.getString("currency");
                        String symbols = object1.getString("symbol");

                        currencyModel1.setCountryName(countryName);
                        currencyModel1.setCurrency(currency);
                        currencyModel1.setSymbols(symbols);

                        currencyModels.add(currencyModel1);

                        //    Toast.makeText(SelectCurrency.this, ""+countryName+currency+symbols, Toast.LENGTH_SHORT).show();

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                selectCurrencyAdapter = new SelectCurrencyAdapter(currencyModels, getApplicationContext(), SelectCurrency.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(selectCurrencyAdapter);

                search_input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        selectCurrencyAdapter.getFilter().filter(s);
                        search = s;
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                // Toast.makeText(getApplicationContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), Setting.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    public void back() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCurrency.this, Setting.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onHistoryItemClickListener(int position) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        String currency = currencyModels.get(position).getCurrency();
        String symbols = currencyModels.get(position).getSymbols();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("currency1", currency);
        editor.putString("Currency_Symbols", symbols);
        editor.commit();

    }
}


