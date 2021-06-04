
package com.crypto.croytowallet.fragement;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.croytowallet.Activity.Add_Currency;
import com.crypto.croytowallet.Activity.Graph_layout;
import com.crypto.croytowallet.Adapter.Add_Currency_Adapter;
import com.crypto.croytowallet.Adapter.Crypto_currencyInfo;
import com.crypto.croytowallet.Adapter.OverViewAdapter;
import com.crypto.croytowallet.Interface.CryptoClickListner;
import com.crypto.croytowallet.Interface.OverViewClickListner;
import com.crypto.croytowallet.Model.CrptoInfoModel;
import com.crypto.croytowallet.Model.Model_Class_Add_Currency;
import com.crypto.croytowallet.Model.OverViewModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.Updated_data;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.database.RetrofitGraph;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 */
public class Wallet extends Fragment implements CryptoClickListner, OverViewClickListner {
    ArrayList<CrptoInfoModel> crptoInfoModels;
    ArrayList<OverViewModel> overViewModels;
    RecyclerView WalletRecyclerView, overviewRecycler;
    RequestQueue requestQueue;
    Crypto_currencyInfo crypto_currencyInfo;
    OverViewAdapter overViewAdapter;
    SharedPreferences sharedPreferences;
    KProgressHUD progressDialog;
    String currency2;
    TextView add_currency;
    UserData userData;
    ArrayList<String >strings;

    public Wallet() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mywallet, container, false);

        WalletRecyclerView = view.findViewById(R.id.walletRecyclerView);
        overviewRecycler = view.findViewById(R.id.overviewRecycler);
        add_currency = view.findViewById(R.id.Add_more_Currency);
        crptoInfoModels = new ArrayList<CrptoInfoModel>();
        overViewModels = new ArrayList<OverViewModel>();
        userData = SharedPrefManager.getInstance(getContext()).getUser();

        sharedPreferences = getActivity().getSharedPreferences("currency", 0);
        currency2 = sharedPreferences.getString("currency1", "usd");

       strings = new ArrayList<String>();

        add_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Add_Currency.class));

            }
        });


        getAllCoins();
        CryptoInfoRecyclerView();

        return view;
    }


    public void CryptoInfoRecyclerView() {

        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=" + currency2 + "&ids=bitcoin%2Cethereum%2Ctether%2Cripple%2Clitecoin%2Cusd-coin&order=market_cap_desc&sparkline=false&price_change_percentage=24h";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //     hidepDialog();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i <= jsonArray.length(); i++) {
                        CrptoInfoModel crptoInfoModel1 = new CrptoInfoModel();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String symbol = jsonObject1.getString("symbol");
                        String image = jsonObject1.getString("image");
                        String name = jsonObject1.getString("name");
                        String rate = jsonObject1.getString("price_change_percentage_24h");
                        String price = jsonObject1.getString("current_price");

                        crptoInfoModel1.setId(id);
                        crptoInfoModel1.setImage(image);
                        crptoInfoModel1.setName(name);
                        crptoInfoModel1.setCurrencyRate(rate);
                        crptoInfoModel1.setCurrentPrice(price);
                        crptoInfoModel1.setSymbol(symbol);
                        crptoInfoModels.add(crptoInfoModel1);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                crypto_currencyInfo = new Crypto_currencyInfo(crptoInfoModels, getContext(), Wallet.this::onCryptoItemClickListener);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                WalletRecyclerView.setLayoutManager(mLayoutManager);
                WalletRecyclerView.setItemAnimator(new DefaultItemAnimator());
                WalletRecyclerView.setAdapter(crypto_currencyInfo);
                //  Toast.makeText(getContext(), ""+response.toString(), Toast.LENGTH_SHORT).show();

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // hidepDialog();
                //  Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onCryptoItemClickListener(int position) {
        String CoinID = crptoInfoModels.get(position).getId();
        String result = crptoInfoModels.get(position).getSymbol();
        String price = crptoInfoModels.get(position).getCurrentPrice();
        String image = crptoInfoModels.get(position).getImage();
        String coinName = crptoInfoModels.get(position).getName();
        String change = crptoInfoModels.get(position).getCurrencyRate();

        Updated_data.getInstans(getContext()).userLogin(position, coinName, result, image, change, price, CoinID);

        Intent intent = new Intent(getContext(), Graph_layout.class);
        startActivity(intent);

    }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void getAllCoins() {


        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getAllCoinDataBase(userData.getToken());

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = null;

                if (response.isSuccessful() && response.code() == 200 && response.body() != null) {

                    try {
                        s = response.body().string();

                        JSONObject object = new JSONObject(s);

                        String account = object.getString("Account");
                        JSONArray jsonArray = new JSONArray(account);
                        for (int i = 0; i <= jsonArray.length(); i++) {
                           JSONObject object1 = jsonArray.getJSONObject(i);
                              Boolean enabled = object1.getBoolean("enabled");

                            if (enabled==true){
                                String name = object1.getString("name");

                                if (name.equalsIgnoreCase("imt")){

                                }else{
                                    strings.add(name);
                                }

                            }



                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    String coinId=String.join(",",strings);
                    if (!coinId.isEmpty()){
                        overViewData(coinId);
                    }else {
                        Snacky.builder()
                                .setActivity(getActivity())
                                .setText("No Coin Enabled")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .success()
                                .show();

                    }

                } else if (response.code() == 401 || response.code() == 400) {

                    try {
                        s = response.errorBody().string();
                        JSONObject object = new JSONObject(s);
                        String error = object.getString("error");

                        Snacky.builder()
                                .setActivity(getActivity())
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
                        .setActivity(getActivity())
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }

    public void overViewData(String coinId) {


        Call<ResponseBody> call = RetrofitGraph.getInstance().getApi().getAllCoin(coinId, currency2.toLowerCase());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = null;
                overViewModels.clear();
                if (response.isSuccessful()) {
                    try {
                        s = response.body().string();
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            OverViewModel overViewModel1 = new OverViewModel();

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String symbol = jsonObject1.getString("symbol");
                            String image = jsonObject1.getString("image");
                            String name = jsonObject1.getString("name");
                            String rate = jsonObject1.getString("price_change_percentage_24h");
                            String price = jsonObject1.getString("current_price");
                            String high_price = jsonObject1.getString("high_24h");
                            String low_price = jsonObject1.getString("low_24h");


                            overViewModel1.setId(id);
                            overViewModel1.setImage(image);
                            overViewModel1.setName(name);
                            overViewModel1.setCurrencyRate(rate);
                            overViewModel1.setCurrentPrice(price);
                            overViewModel1.setHigh_price(high_price);
                            overViewModel1.setLow_price(low_price);
                            overViewModel1.setSymbol(symbol);
                            overViewModels.add(overViewModel1);

                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                    overViewAdapter = new OverViewAdapter(overViewModels, getContext(),Wallet.this::onOverViewItemClickListener);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    overviewRecycler.setLayoutManager(mLayoutManager);
                    overviewRecycler.setItemAnimator(new DefaultItemAnimator());
                    overviewRecycler.setAdapter(overViewAdapter);


                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    @Override
    public void onOverViewItemClickListener(int position) {
        String CoinID = overViewModels.get(position).getId();
        String result = overViewModels.get(position).getSymbol();
        String price = overViewModels.get(position).getCurrentPrice();
        String image = overViewModels.get(position).getImage();
        String coinName = overViewModels.get(position).getName();
        String change = overViewModels.get(position).getCurrencyRate();
        Updated_data.getInstans(getContext()).userLogin(position, coinName, result, image, change, price, CoinID);

        Intent intent = new Intent(getContext(), Graph_layout.class);
        startActivity(intent);
    }
}
