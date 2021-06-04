package com.crypto.croytowallet.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crypto.croytowallet.Adapter.Coin_History_Adapter;
import com.crypto.croytowallet.CoinTransfer.CoinScan;
import com.crypto.croytowallet.CoinTransfer.Received_Coin;
import com.crypto.croytowallet.Extra_Class.MyMarkerView;
import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.ImtSmart.imtSwap;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.Model.CoinModal;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.TransactionHistorySharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.Transaction_HistoryModel;
import com.crypto.croytowallet.SharedPrefernce.Updated_data;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.TransactionHistory.CoinHistory;
import com.crypto.croytowallet.TransactionHistory.Full_Transaction_History;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.database.RetrofitGraph;
import com.crypto.croytowallet.fragement.Exchange;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Graph_layout extends AppCompatActivity implements View.OnClickListener, HistoryClickLister {

    TextView swap, price, balance, coinname, coinsymbols, coinprice, sync, increaseRate, null1, more;
    LinearLayout h_24, d_7, m_1, m_3, m_6, y_1;
    private Exchange exchange;
    int position;
    String symbol, image, coinName, change, CurrencySymbols, coinId, currency2;
    ImageView back, received, send;
    KProgressHUD progressDialog;
    SharedPreferences preferences, sharedPreferences;
    private LineChart chart;
    UserData userData;
    CircleImageView circleImageView;
    String balance1, price1,token;
    ArrayList<CoinModal> coinModals;
    Coin_History_Adapter coin_history_adapter;
    RecyclerView recyclerView;
    TextView history_Empty;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_layout);
        chart = findViewById(R.id.cubiclinechart);
        swap = findViewById(R.id.swap_btc_btn);
        back = findViewById(R.id.back);
        received = findViewById(R.id.receive_coin);
        price = findViewById(R.id.price);
        balance = findViewById(R.id.balance);
        coinname = findViewById(R.id.coinName);
        coinsymbols = findViewById(R.id.coinsymbols);
        coinprice = findViewById(R.id.coinPrice);
        circleImageView = findViewById(R.id.coinImage);
        // sync = findViewById(R.id.sync);
        increaseRate = findViewById(R.id.increaseRate);
        recyclerView = findViewById(R.id.recyclerView);
        history_Empty = findViewById(R.id.txt_list_is_empty);
        more = findViewById(R.id.moretransactions);

        null1 = findViewById(R.id.null1);
        h_24 = findViewById(R.id.h_24);
        d_7 = findViewById(R.id.d_7);
        m_1 = findViewById(R.id.m_1);
        m_3 = findViewById(R.id.m_3);
        m_6 = findViewById(R.id.m_6);
        y_1 = findViewById(R.id.y_1);
        more.setOnClickListener(this);
        send = findViewById(R.id.send_coin);
        swap.setOnClickListener(this);
        back.setOnClickListener(this);
        received.setOnClickListener(this);
        send.setOnClickListener(this);
        h_24.setOnClickListener(this);
        d_7.setOnClickListener(this);
        m_1.setOnClickListener(this);
        m_3.setOnClickListener(this);
        m_6.setOnClickListener(this);
        y_1.setOnClickListener(this);

        coinModals = new ArrayList<CoinModal>();

        position = Updated_data.getInstans(getApplicationContext()).getUserId();
        price1 = Updated_data.getInstans(getApplicationContext()).getprice();
        symbol = Updated_data.getInstans(getApplicationContext()).getmobile();
        image = Updated_data.getInstans(getApplicationContext()).getImage();
        coinName = Updated_data.getInstans(getApplicationContext()).getUsername();
        change = Updated_data.getInstans(getApplicationContext()).getChange();

        //  Toast.makeText(this, ""+symbol, Toast.LENGTH_SHORT).show();

        sharedPreferences = getApplicationContext().getSharedPreferences("currency", 0);

        CurrencySymbols = sharedPreferences.getString("Currency_Symbols", "$");
        coinId = Updated_data.getInstans(getApplicationContext()).getCoinId();
        currency2 = sharedPreferences.getString("currency1", "usd");

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        token  = userData.getToken();

        Glide.with(this).load(image).into(circleImageView);

        try {
            double pric = Double.parseDouble(price1);
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(5);

            price.setText(CurrencySymbols + df.format(pric));

        } catch (Exception e) {
        }

        coinname.setText(coinName);
        coinsymbols.setText("(" + symbol + ")");
        //   sync.setText(symbol+" Price");
        increaseRate.setText(change);
        increaseRate.setTextColor(change.contains("-") ?
                getApplicationContext().getResources().getColor(R.color.red) : getApplicationContext().getResources().getColor(R.color.green));

        null1.setTextColor(change.contains("-") ?
                getApplicationContext().getResources().getColor(R.color.red) : getApplicationContext().getResources().getColor(R.color.green));

        if (change.contains("-")) {
            increaseRate.setText(change);
        } else {
            increaseRate.setText("+" + change);
        }
       // getBalance();

        getGraphData(coinId, currency2, "1", "");
        geTypeToken(token,symbol);


        getSendCoinHistory();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;

            case R.id.receive_coin:
                Intent intent = new Intent(getApplicationContext(), Received_Coin.class);
                // intent.putExtra("position",position);
                startActivity(intent);
                break;

            case R.id.send_coin:
                Intent intent1 = new Intent(getApplicationContext(), CoinScan.class);
                //  intent1.putExtra("position",position);
                startActivity(intent1);
                break;
            case R.id.swap_btc_btn:
                Intent intent2 = new Intent(getApplicationContext(), imtSwap.class);
                startActivity(intent2);
                break;

            case R.id.moretransactions:
                Intent intent3 = new Intent(getApplicationContext(), CoinHistory.class);
                startActivity(intent3);
                break;

            case R.id.h_24:
                h_24.setBackground(getResources().getDrawable(R.drawable.graph_box));
                d_7.setBackgroundColor(getResources().getColor(R.color.background));
                m_1.setBackgroundColor(getResources().getColor(R.color.background));
                m_3.setBackgroundColor(getResources().getColor(R.color.background));
                m_6.setBackgroundColor(getResources().getColor(R.color.background));
                y_1.setBackgroundColor(getResources().getColor(R.color.background));
                getGraphData(coinId, currency2, "1", "");

                break;

            case R.id.d_7:
                h_24.setBackgroundColor(getResources().getColor(R.color.background));
                d_7.setBackground(getResources().getDrawable(R.drawable.graph_box));
                m_1.setBackgroundColor(getResources().getColor(R.color.background));
                m_3.setBackgroundColor(getResources().getColor(R.color.background));
                m_6.setBackgroundColor(getResources().getColor(R.color.background));
                y_1.setBackgroundColor(getResources().getColor(R.color.background));

                getGraphData(coinId, currency2, "7", "daily");

                break;

            case R.id.m_1:
                h_24.setBackgroundColor(getResources().getColor(R.color.background));
                d_7.setBackgroundColor(getResources().getColor(R.color.background));
                m_1.setBackground(getResources().getDrawable(R.drawable.graph_box));
                m_3.setBackgroundColor(getResources().getColor(R.color.background));
                m_6.setBackgroundColor(getResources().getColor(R.color.background));
                y_1.setBackgroundColor(getResources().getColor(R.color.background));
                getGraphData(coinId, currency2, "30", "daily");

                break;

            case R.id.m_3:
                h_24.setBackgroundColor(getResources().getColor(R.color.background));
                d_7.setBackgroundColor(getResources().getColor(R.color.background));
                m_1.setBackgroundColor(getResources().getColor(R.color.background));
                m_3.setBackground(getResources().getDrawable(R.drawable.graph_box));
                m_6.setBackgroundColor(getResources().getColor(R.color.background));
                y_1.setBackgroundColor(getResources().getColor(R.color.background));

                getGraphData(coinId, currency2, "90", "daily");


                break;

            case R.id.m_6:
                h_24.setBackgroundColor(getResources().getColor(R.color.background));
                d_7.setBackgroundColor(getResources().getColor(R.color.background));
                m_1.setBackgroundColor(getResources().getColor(R.color.background));
                m_3.setBackgroundColor(getResources().getColor(R.color.background));
                m_6.setBackground(getResources().getDrawable(R.drawable.graph_box));
                y_1.setBackgroundColor(getResources().getColor(R.color.background));

                getGraphData(coinId, currency2, "180", "daily");


                break;

            case R.id.y_1:
                h_24.setBackgroundColor(getResources().getColor(R.color.background));
                d_7.setBackgroundColor(getResources().getColor(R.color.background));
                m_1.setBackgroundColor(getResources().getColor(R.color.background));
                m_3.setBackgroundColor(getResources().getColor(R.color.background));
                m_6.setBackgroundColor(getResources().getColor(R.color.background));
                y_1.setBackground(getResources().getDrawable(R.drawable.graph_box));
                getGraphData(coinId, currency2, "365", "daily");
                break;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onSaveInstanceState(new Bundle());
    }

    private void geTypeToken(String token, String symbol) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getToken(token,symbol);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                if (response.code()==200){
                    try {
                        s = response.body().string();
                        JSONObject object = new JSONObject(s);
                        String token1 = object.getString("token");
                        getBalance(token,token1,symbol,"usd");
                        MyPreferences.getInstance(getApplicationContext()).putString(PrefConf.TOKEN_TYPE,token1);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }else if (response.code()==400){
                    getBalance(token,symbol,symbol,"usd");
                    MyPreferences.getInstance(getApplicationContext()).putString(PrefConf.TOKEN_TYPE,symbol);
                }else if (response.code()==401){
                    Snacky.builder()
                            .setActivity(Graph_layout.this)
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
                        .setActivity(Graph_layout.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

            }
        });

    }

    public void getBalance(String token,String type,String name,String currency) {


        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().Balance(token,type,name ,currency);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                if (response.code() == 200) {
                    try {
                        s = response.body().string();

                        JSONObject jsonObject = new JSONObject(s);


                        balance1 = jsonObject.getString("balance");


                        if (!balance1.equalsIgnoreCase("null")){
                            double balance2 = Double.parseDouble(balance1);
                            double price = Double.parseDouble(price1);
                            double total = balance2 * price;
                            DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(2);

                            balance.setText(CurrencySymbols + df.format(total));
                            coinprice.setText("" + df.format(balance2));

                        }else {
                            balance.setText(CurrencySymbols + "0");
                            coinprice.setText("0");
                        }




                        // Log.d("bal",df.format(total));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 400) {
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");

                        if (error.equalsIgnoreCase("Account not found.")) {
                            Snacky.builder()
                                    .setActivity(Graph_layout.this)
                                    .setText("You need to activate the Ripple account by transferring the 25 XRP")
                                    .setDuration(Snacky.LENGTH_LONG)
                                    .setActionText(android.R.string.ok)
                                    .error()
                                    .show();

                        } else {
                            Snacky.builder()
                                    .setActivity(Graph_layout.this)
                                    .setText("Failed to Load Balance kindly try again.")
                                    .setDuration(Snacky.LENGTH_LONG)
                                    .setActionText(android.R.string.ok)
                                    .setActionClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Snacky.builder()
                                                    .setView(v)
                                                    .setText(error)
                                                    .setDuration(Snacky.LENGTH_SHORT)
                                                    .error()
                                                    .show();
                                        }
                                    })
                                    .error()
                                    .show();
                            // Toast.makeText(Graph_layout.this, ""+error, Toast.LENGTH_SHORT).show();
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 401) {

                    Snacky.builder()
                            .setActivity(Graph_layout.this)
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
                        .setActivity(Graph_layout.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

                //    Log.d("errrrp",t.getLocalizedMessage());

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

    public void getGraphData(String id, String currency, String days, String interval) {

        progressDialog = KProgressHUD.create(Graph_layout.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();


        Call<ResponseBody> call = RetrofitGraph.getInstance().getApi().getGraphData1(id, currency, days, interval);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                hidepDialog();
                if (response.code() == 200) {
                    ArrayList<Entry> yvalue = new ArrayList<>();
                    yvalue.clear();
                    try {
                        s = response.body().string();

                        JSONObject object = new JSONObject(s);

                        String prices = object.getString("prices");
                        JSONArray jsonArray = new JSONArray(prices);

                        for (int i = 0; i <= jsonArray.length(); i++) {

                            String peice1 = jsonArray.getString(i);

                            JSONArray jsonArray1 = new JSONArray(peice1);
                            for (int j = 0; j <= jsonArray1.length(); j++) {
                                String lowPrices = jsonArray1.getString(1);

                                Float aFloat = Float.parseFloat(lowPrices);
                                yvalue.add(new Entry(i, aFloat));
                            }

                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    chart.setDragEnabled(true);
                    chart.setScaleEnabled(true);
                    chart.setPinchZoom(true);
                    /* create marker to display box when values are selected */
                    MyMarkerView mv = new MyMarkerView(Graph_layout.this, R.layout.custom_marker_view);

                    // Set the marker to the chart
                    mv.setChartView(chart);
                    chart.setMarker(mv);
                    chart.animateXY(2000, 200);
                    chart.getXAxis().setDrawGridLines(false);
                    chart.getAxisLeft().setDrawGridLinesBehindData(false);
                    chart.getAxisLeft().setDrawGridLines(false);
                    chart.getAxisRight().setDrawGridLines(false);
                    chart.getDescription().setEnabled(false);
                    chart.getAxisLeft().setDrawLabels(false);
                    chart.getAxisRight().setDrawLabels(false);
                    chart.getXAxis().setDrawLabels(false);
                    chart.getLegend().setEnabled(false);
                    YAxis y = chart.getAxisRight();
                    y.setEnabled(false);
                    y.setDrawAxisLine(false);
                    YAxis y1 = chart.getAxisLeft();
                    y1.setDrawAxisLine(false);

                    XAxis x = chart.getXAxis();
                    x.setDrawAxisLine(false);
                    x.setDrawGridLines(false);


                    LineDataSet set1 = new LineDataSet(yvalue, "");
                    set1.setFillAlpha(110);
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);
                    LineData data = new LineData(dataSets);
                    chart.setData(data);

                    if (Utils.getSDKInt() >= 18) {
                        Drawable drawable = ContextCompat.getDrawable(Graph_layout.this, R.drawable.gradient1);
                        set1.setFillDrawable(drawable);
                    } else {
                        set1.setFillColor(Color.rgb(229, 146, 19));
                    }
                    set1.setMode(LineDataSet.Mode.LINEAR);
                    set1.setLineWidth(2f);
                    set1.setColor(getResources().getColor(R.color.graph_line));
                    set1.setDrawValues(!set1.isDrawValuesEnabled());
                    set1.setDrawFilled(true);
                    set1.setDrawCircles(false);
                    chart.setBackgroundColor(getResources().getColor(R.color.graph));


                } else if (response.code() == 400) {
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");
                        Snacky.builder()
                                .setActivity(Graph_layout.this)
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
                        .setActivity(Graph_layout.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

    }

    public void getSendCoinHistory() {

        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token = user.getToken();


        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().get_SendHistory(token, symbol.toLowerCase());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();
                String s = null;
                if (response.code() == 200) {
                    coinModals.clear();
                    try {
                        s = response.body().string();
                        JSONObject object = new JSONObject(s);
                        String result = object.getString("result");
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);

                            CoinModal modal = new CoinModal();
                            String type = object1.getString("cryptoCurrency");
                            String amount = object1.getString("amtOfCrypto");
                            String id = object1.getString("transactionHash");
                            String date = object1.getString("createdAt");
                            String userData = object1.getString("userId");


                            JSONObject object2 = new JSONObject(userData);
                            String username = object2.getString("username");


                            modal.setUsername(username);
                            modal.setTime(date);
                            modal.setAmount(amount);
                            modal.setType(type.toUpperCase());
                            modal.setId(id);
                            coinModals.add(modal);


                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    if (coinModals != null && coinModals.size() > 0) {
                        coin_history_adapter = new Coin_History_Adapter(coinModals, getApplicationContext(), Graph_layout.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(coin_history_adapter);
                    } else {


                        history_Empty.setVisibility(View.VISIBLE);

                    }

                } else if (response.code() == 400) {
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Graph_layout.this)
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
                            .setActivity(Graph_layout.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                coinModals.clear();
                Snacky.builder()
                        .setActivity(Graph_layout.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

            }
        });
    }

    @Override
    public void onHistoryItemClickListener(int position) {
        String username = coinModals.get(position).getUsername();
        String transaction = coinModals.get(position).getId();
        String type = coinModals.get(position).getType();
        String date = coinModals.get(position).getTime();
        String amount = coinModals.get(position).getAmount();
        String Type = "coinTransfer";

        Transaction_HistoryModel historyModel = new Transaction_HistoryModel(transaction, coinId, amount, type, username, date,null,Type);

        //storing the user in shared preferences
        TransactionHistorySharedPrefManager.getInstance(getApplicationContext()).Transaction_History_Data(historyModel);


        Intent intent = new Intent(Graph_layout.this, Full_Transaction_History.class);
        startActivity(intent);

    }
}