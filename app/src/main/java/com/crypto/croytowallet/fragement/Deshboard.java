package com.crypto.croytowallet.fragement;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
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
import com.crypto.croytowallet.Activity.New_Currency;
import com.crypto.croytowallet.Activity.WalletBalance;
import com.crypto.croytowallet.Activity.WalletReceive;
import com.crypto.croytowallet.Activity.WalletScan;
import com.crypto.croytowallet.Adapter.Crypto_currencyInfo;
import com.crypto.croytowallet.Adapter.OverViewAdapter;
import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.ImtSmart.ImtSmartGraphLayout;
import com.crypto.croytowallet.Interface.CryptoClickListner;
import com.crypto.croytowallet.Interface.OverViewClickListner;
import com.crypto.croytowallet.Model.CrptoInfoModel;
import com.crypto.croytowallet.Model.OverViewModel;
import com.crypto.croytowallet.TopUp.Top_up_Money;
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

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 */
public class
Deshboard extends Fragment implements View.OnClickListener, CryptoClickListner, OverViewClickListner {
    ArrayList<CrptoInfoModel> crptoInfoModels;
    ArrayList<OverViewModel> overViewModels;
    RecyclerView cryptoInfoRecyclerView, overviewRecycler;
    RequestQueue requestQueue;
    Crypto_currencyInfo crypto_currencyInfo;
    OverViewAdapter overViewAdapter;
    LinearLayout lytscan, lytPay, lytWalletBalance, lytaddMoney, amt_pic;
    SharedPreferences sharedPreferences, sharedPreferences1;
    TextView textView, textView1,new_currency;
    CardView imtsmart, multi_option,card_imt;
    TextView add_currency, increaseRate, null1, imtPrice,increaseRate2, null2, Price;
    String imtPrices, increaseRate1;
    KProgressHUD progressDialog;
    String currency2, CurrencySymbols;
    UserData userData;
    Animation enterright, rightin, right;
    ArrayList<String> strings;
    ImageView ImtChart,ImtChart1;

    public Deshboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deshboard, container, false);
        cryptoInfoRecyclerView = view.findViewById(R.id.deshboardRecyclerView);

        lytscan = view.findViewById(R.id.lytScan);
        lytPay = view.findViewById(R.id.lytPay);
        lytWalletBalance = view.findViewById(R.id.lytwallet);
        lytaddMoney = view.findViewById(R.id.lytaddMoney);
        imtsmart = view.findViewById(R.id.ImtSmart);
        add_currency = view.findViewById(R.id.Add_more_Currency);
        overviewRecycler = view.findViewById(R.id.overviewRecycler);
        multi_option = view.findViewById(R.id.multi_option);
        amt_pic = view.findViewById(R.id.amt_pic);
        new_currency = view.findViewById(R.id.newCoin);

        /*-----------------------CryptoInfo---------------*/
        textView = view.findViewById(R.id.balance);
        textView1 = view.findViewById(R.id.balance1);
        increaseRate = view.findViewById(R.id.increaseRate);
        null1 = view.findViewById(R.id.null1);
        imtPrice = view.findViewById(R.id.coinrate);
        ImtChart = view.findViewById(R.id.chart);

        /*-----------------------overView---------------*/
        increaseRate2 = view.findViewById(R.id.increaseRate1);
        null2 = view.findViewById(R.id.null2);
        Price = view.findViewById(R.id.price);
        card_imt = view.findViewById(R.id.card);
        ImtChart1 = view.findViewById(R.id.imageView3);



         sharedPreferences1 = getActivity().getSharedPreferences("imtInfo", Context.MODE_PRIVATE);// for imt price
        sharedPreferences1.getString("price", null);

        sharedPreferences = getActivity().getSharedPreferences("currency", 0);// for currency
        currency2 = sharedPreferences.getString("currency1", "usd");
        CurrencySymbols = sharedPreferences.getString("Currency_Symbols", "$");

        userData = SharedPrefManager.getInstance(getContext()).getUser();

        crptoInfoModels = new ArrayList<CrptoInfoModel>();
        overViewModels = new ArrayList<OverViewModel>();
        strings = new ArrayList<>();
        CryptoInfoRecyclerView();



        // Animation
        enterright = AnimationUtils.loadAnimation(getContext(), R.anim.enter_right);
        rightin = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        right = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);

        //set animation
        multi_option.startAnimation(right);
        amt_pic.startAnimation(right);

        /*-------------------setClickListner------------*/
        lytscan.setOnClickListener(this);
        lytPay.setOnClickListener(this);
        lytWalletBalance.setOnClickListener(this);
        lytaddMoney.setOnClickListener(this);
        imtsmart.setOnClickListener(this);
        add_currency.setOnClickListener(this);
        card_imt.setOnClickListener(this);
        new_currency.setOnClickListener(this);

        getImtDetails();
        AirDropBalance();
        getAllCoins();
        return view;
    }

    public void CryptoInfoRecyclerView() {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=" + currency2 + "&ids=bitcoin%2Cethereum%2Ctether%2Cripple%2Clitecoin%2Cusd-coin&order=market_cap_desc&sparkline=false&price_change_percentage=24h";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                //   hidepDialog();
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
                        String high_price = jsonObject1.getString("high_24h");
                        String low_price = jsonObject1.getString("low_24h");


                        crptoInfoModel1.setId(id);
                        crptoInfoModel1.setImage(image);
                        crptoInfoModel1.setName(name);
                        crptoInfoModel1.setCurrencyRate(rate);
                        crptoInfoModel1.setCurrentPrice(price);
                        crptoInfoModel1.setHigh_price(high_price);
                        crptoInfoModel1.setLow_price(low_price);
                        crptoInfoModel1.setSymbol(symbol);
                        crptoInfoModels.add(crptoInfoModel1);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                crypto_currencyInfo = new Crypto_currencyInfo(crptoInfoModels, getContext(), Deshboard.this::onCryptoItemClickListener);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                cryptoInfoRecyclerView.setLayoutManager(mLayoutManager);
                cryptoInfoRecyclerView.setItemAnimator(new DefaultItemAnimator());
                cryptoInfoRecyclerView.setAdapter(crypto_currencyInfo);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    public void AirDropBalance() {
        String token = userData.getToken();

        String currency = currency2.toUpperCase();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().Balance(token, "airdrop","airdrop", currency);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = null;


                if (response.code() == 200) {
                    try {
                        s = response.body().string();

                        JSONObject object = new JSONObject(s);
                        String balance = object.getString("balance");
                        String cal = object.getString("calculationPrice");
                        JSONObject object1 = new JSONObject(cal);
                        String calBalance = object1.getString("calculation");

                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(2);

                        if (calBalance.equals("null")) {
                            double balance2 = Double.parseDouble(balance);
                            textView.setText("" + df.format(balance2));
                            textView1.setText(CurrencySymbols + "0");
                        } else {

                            double balance2 = Double.parseDouble(balance);
                            double calBalance2 = Double.parseDouble(calBalance);

                            String bal = String.valueOf(df.format(balance2));
                            String cal_bal = String.valueOf( df.format(calBalance2));

                            textView.setText("" + df.format(balance2));
                            textView1.setText(CurrencySymbols + df.format(calBalance2));
                            try {
                                MyPreferences.getInstance(getActivity()).putString(PrefConf.USER_BALANCE,bal);
                                MyPreferences.getInstance(getActivity()).putString(PrefConf.CAL_USER_BALANCE, cal_bal);

                            }catch (Exception e){}

                        }


                        //  Log.d("airDrop",s);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 400) {
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");


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

                } else if (response.code() == 401) {

                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Snacky.builder()
                        .setActivity(getActivity())
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        deepChangeTextColor(1);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.lytScan) {
            deepChangeTextColor(1);
            startActivity(new Intent(getContext(), WalletScan.class));

        } else if (id == R.id.lytPay) {
            deepChangeTextColor(2);
            startActivity(new Intent(getContext(), WalletReceive.class));


        } else if (id == R.id.lytwallet) {
            deepChangeTextColor(3);
            startActivity(new Intent(getContext(), WalletBalance.class));


        } else if (id == R.id.lytaddMoney) {
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putString("price", imtPrices);
            editor.putString("chanage", increaseRate1);
            editor.commit();
            deepChangeTextColor(4);
            startActivity(new Intent(getContext(), Top_up_Money.class));


        }else if(id == R.id.card){
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putString("price", imtPrices);
            editor.putString("chanage", increaseRate1);
            editor.commit();

            startActivity(new Intent(getContext(), ImtSmartGraphLayout.class));

        }else if(id == R.id.Add_more_Currency){
            startActivity(new Intent(getContext(), Add_Currency.class));
        }else if(id == R.id.ImtSmart){
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putString("price", imtPrices);
            editor.putString("chanage", increaseRate1);
            editor.commit();

            startActivity(new Intent(getContext(), ImtSmartGraphLayout.class));

        }else if(id ==R.id.newCoin){
            startActivity(new Intent(getContext(), New_Currency.class));
        }

    }

    public void deepChangeTextColor(int changeId) {
        for (int i = 1; i <= 4; i++) {
            int img = getResources().getIdentifier("img" + i, "id", getActivity().getPackageName());
            int txt = getResources().getIdentifier("txt" + i, "id", getActivity().getPackageName());

            TextView textView = getView().findViewById(txt);
            ImageView imageView = getView().findViewById(img);

            if (changeId == i) {
                imageView.setColorFilter(getResources().getColor(R.color.purple_500));
                textView.setTextColor(getResources().getColor(R.color.purple_500));
                //  textView.setVisibility(View.VISIBLE);
            } else {
                imageView.setColorFilter(getResources().getColor(R.color.toolbar_text_color));
                textView.setTextColor(getResources().getColor(R.color.toolbar_text_color));
                //  textView.setVisibility(View.GONE);
            }

        }
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


    public void getImtDetails() {

        UserData userData = SharedPrefManager.getInstance(getContext()).getUser();

        String Token = userData.getToken();
        String currency = currency2.toUpperCase();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getIMTDetails(Token, currency);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = null;


                if (response.code() == 200) {
                    try {
                        s = response.body().string();

                        JSONArray jsonArray = new JSONArray(s);

                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            imtPrices = object.getString("price");
                            // imtPrices1 =object.getString("price");
                            increaseRate1 = object.getString("percent_change_24h");

                            imtPrice.setText(CurrencySymbols + imtPrices);
                            Price.setText(CurrencySymbols + imtPrices);
                            increaseRate.setText(increaseRate1);
                            increaseRate2.setText(increaseRate1);

                            SharedPreferences.Editor editor = sharedPreferences1.edit();
                            editor.putString("imtPrices", imtPrices);
                            editor.commit();

                            try {
                                increaseRate.setTextColor(increaseRate1.contains("-") ?
                                        getContext().getResources().getColor(R.color.red) : getContext().getResources().getColor(R.color.green));

                                null1.setTextColor(increaseRate1.contains("-") ?
                                        getContext().getResources().getColor(R.color.red) : getContext().getResources().getColor(R.color.green));
                                if (increaseRate1.contains("-")) {
                                    increaseRate.setText(increaseRate1);
                                     ImtChart.setImageDrawable(getResources().getDrawable(R.drawable.ic_down_blue));
                                 } else {
                                    increaseRate.setText("+" + increaseRate1);
                                     ImtChart.setImageDrawable(getResources().getDrawable(R.drawable.ic_up_blue));
                                  }
                            } catch (Exception e) { }

                            try {
                                increaseRate2.setTextColor(increaseRate1.contains("-") ?
                                        getContext().getResources().getColor(R.color.red) : getContext().getResources().getColor(R.color.green));

                                null2.setTextColor(increaseRate1.contains("-") ?
                                        getContext().getResources().getColor(R.color.red) : getContext().getResources().getColor(R.color.green));
                                if (increaseRate1.contains("-")) {
                                     increaseRate2.setText(increaseRate1);
                                    ImtChart1.setImageDrawable(getResources().getDrawable(R.drawable.ic_orange_down));
                                } else {
                                    increaseRate2.setText("+" + increaseRate1);
                                    ImtChart1.setImageDrawable(getResources().getDrawable(R.drawable.ic_orange_up));
                                }
                            } catch (Exception e) { }

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 400) {
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");


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

                } else if (response.code() == 401) {

                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snacky.builder()
                        .setActivity(getActivity())
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


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

                            if (enabled == true) {
                                String name = object1.getString("name");

                                if (name.equalsIgnoreCase("imt")) {
                                    card_imt.setVisibility(View.VISIBLE);
                                } else {
                                    strings.add(name);
                                }

                            }


                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    String coinId = String.join(",", strings);
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

    public void overViewData(String CoinId) {

        Call<ResponseBody> call = RetrofitGraph.getInstance().getApi().getAllCoin(CoinId, currency2.toLowerCase());

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

                            // Log.d("data",id+symbol+image+name+rate+price);

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

                        //  Toast.makeText(Add_Currency.this, ""+s, Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                    overViewAdapter = new OverViewAdapter(overViewModels, getContext(),Deshboard.this::onOverViewItemClickListener);
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

