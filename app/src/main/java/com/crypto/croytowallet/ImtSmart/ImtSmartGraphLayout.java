package com.crypto.croytowallet.ImtSmart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

import com.crypto.croytowallet.ImtSmart.ImtSmartGraphLayout;
import com.crypto.croytowallet.Extra_Class.ApiResponse.SendCoinHistoryResponse;
import com.crypto.croytowallet.Extra_Class.MyMarkerView;
import com.crypto.croytowallet.Extra_Class.MyMarkerView1;
import com.crypto.croytowallet.Adapter.Coin_Send_History_Adapter;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.Model.CoinModal;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.TransactionHistorySharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.Transaction_HistoryModel;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.TransactionHistory.CoinHistory;
import com.crypto.croytowallet.TransactionHistory.Full_Transaction_History;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.database.RetrofitGraph;
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

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImtSmartGraphLayout extends AppCompatActivity implements View.OnClickListener, Coin_Send_History_Adapter.OnSendCoinItemListener {
    ImageView back, received, send;
    TextView price, balances, coinprice, increaseRate, null1,  text_activity, text_about, text_video, text_blog, text_description;
    KProgressHUD progressDialog;
    UserData userData;
    private LineChart chart;
    TextView swap;
    String balance1, price1;
    SharedPreferences sharedPreferences1, sharedPreferences;
    String currency2, CurrencySymbols;
    LinearLayout h_24, d_7, m_1, m_3, m_6, y_1;
    ArrayList<CoinModal> coinModals;
    Coin_Send_History_Adapter coin_Send_history_adapter;
    RecyclerView recyclerView;
    TextView history_Empty;
    ArrayList<Entry> yvalue;
    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imt_smart_graph_layout);
        chart = findViewById(R.id.cubiclinechart);
        swap = findViewById(R.id.swap_btc_btn);
        back = findViewById(R.id.back);
        received = findViewById(R.id.receive_coin);
        price = findViewById(R.id.price);
        balances = findViewById(R.id.balance);
        coinprice = findViewById(R.id.coinPrice);
        recyclerView = findViewById(R.id.recyclerView);
        history_Empty = findViewById(R.id.txt_list_is_empty);
        text_activity = findViewById(R.id.text_activity);
        text_about = findViewById(R.id.text_about);
        text_video = findViewById(R.id.text_video);
        text_blog = findViewById(R.id.text_blog);
        text_description = findViewById(R.id.text_description);
        constraintLayout = findViewById(R.id.constraint);

        increaseRate = findViewById(R.id.increaseRate);
        null1 = findViewById(R.id.null1);
        h_24 = findViewById(R.id.h_24);
        d_7 = findViewById(R.id.d_7);
        m_1 = findViewById(R.id.m_1);
        m_3 = findViewById(R.id.m_3);
        m_6 = findViewById(R.id.m_6);
        y_1 = findViewById(R.id.y_1);

        yvalue = new ArrayList<>();

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
        text_activity.setOnClickListener(this);
        text_about.setOnClickListener(this);
        text_video.setOnClickListener(this);
        text_blog.setOnClickListener(this);




        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        sharedPreferences1 = getSharedPreferences("imtInfo", Context.MODE_PRIVATE);
        coinModals = new ArrayList<CoinModal>();
        sharedPreferences = getApplicationContext().getSharedPreferences("currency", 0);
        currency2 = sharedPreferences.getString("currency1", "usd");
        CurrencySymbols = sharedPreferences.getString("Currency_Symbols", "$");


        price1 = sharedPreferences1.getString("price", null);
        String increaseRate1 = sharedPreferences1.getString("chanage", null);
        increaseRate.setText(increaseRate1);
        price.setText(CurrencySymbols + price1);

        try {
            increaseRate.setTextColor(increaseRate1.contains("-") ?
                    getApplicationContext().getResources().getColor(R.color.red) : getApplicationContext().getResources().getColor(R.color.green));

            null1.setTextColor(increaseRate1.contains("-") ?
                    getApplicationContext().getResources().getColor(R.color.red) : getApplicationContext().getResources().getColor(R.color.green));

            if (increaseRate1.contains("-")) {
                increaseRate.setText(increaseRate1);
            } else {
                increaseRate.setText("+" + increaseRate1);
            }
        } catch (Exception e) {

        }
        getBalance();

        GetImtGraph1d("imt", currency2, 24);
        getSendCoinHistory(userData.getToken(),"imt");
        chart.setNoDataText("Loading......................");
        chart.setNoDataTextColor(getResources().getColor(R.color.toolbar_text_color));

        chart.setBackgroundColor(getResources().getColor(R.color.graph));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;

            case R.id.receive_coin:
                Intent intent = new Intent(getApplicationContext(), ImtSmartRecevied.class);
                startActivity(intent);
                break;

            case R.id.send_coin:
                Intent intent1 = new Intent(getApplicationContext(), ImtSmartCoinScan.class);
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
                GetImtGraph1d("imt", currency2, 24);
                break;

            case R.id.d_7:
                h_24.setBackgroundColor(getResources().getColor(R.color.background));
                d_7.setBackground(getResources().getDrawable(R.drawable.graph_box));
                m_1.setBackgroundColor(getResources().getColor(R.color.background));
                m_3.setBackgroundColor(getResources().getColor(R.color.background));
                m_6.setBackgroundColor(getResources().getColor(R.color.background));
                y_1.setBackgroundColor(getResources().getColor(R.color.background));
                getGraphData("moneytoken", currency2, "7", "");
                break;

            case R.id.m_1:
                h_24.setBackgroundColor(getResources().getColor(R.color.background));
                d_7.setBackgroundColor(getResources().getColor(R.color.background));
                m_1.setBackground(getResources().getDrawable(R.drawable.graph_box));
                m_3.setBackgroundColor(getResources().getColor(R.color.background));
                m_6.setBackgroundColor(getResources().getColor(R.color.background));
                y_1.setBackgroundColor(getResources().getColor(R.color.background));
                getGraphData("moneytoken", currency2, "30", "daily");
                break;

            case R.id.m_3:
                h_24.setBackgroundColor(getResources().getColor(R.color.background));
                d_7.setBackgroundColor(getResources().getColor(R.color.background));
                m_1.setBackgroundColor(getResources().getColor(R.color.background));
                m_3.setBackground(getResources().getDrawable(R.drawable.graph_box));
                m_6.setBackgroundColor(getResources().getColor(R.color.background));
                y_1.setBackgroundColor(getResources().getColor(R.color.background));
                getGraphData("moneytoken", currency2, "90", "daily");
                break;

            case R.id.m_6:
                h_24.setBackgroundColor(getResources().getColor(R.color.background));
                d_7.setBackgroundColor(getResources().getColor(R.color.background));
                m_1.setBackgroundColor(getResources().getColor(R.color.background));
                m_3.setBackgroundColor(getResources().getColor(R.color.background));
                m_6.setBackground(getResources().getDrawable(R.drawable.graph_box));
                y_1.setBackgroundColor(getResources().getColor(R.color.background));

                getGraphData("moneytoken", currency2, "180", "daily");
                break;

            case R.id.y_1:
                h_24.setBackgroundColor(getResources().getColor(R.color.background));
                d_7.setBackgroundColor(getResources().getColor(R.color.background));
                m_1.setBackgroundColor(getResources().getColor(R.color.background));
                m_3.setBackgroundColor(getResources().getColor(R.color.background));
                m_6.setBackgroundColor(getResources().getColor(R.color.background));
                y_1.setBackground(getResources().getDrawable(R.drawable.graph_box));
                getGraphData("moneytoken", currency2, "365", "daily");
               
                break;

            case R.id.text_activity:
                text_activity.setTextColor(getResources().getColor(R.color.toolbar_text_color));
                text_about.setTextColor(getResources().getColor(R.color.toolbar_text_unSelectcolor));
                text_video.setTextColor(getResources().getColor(R.color.toolbar_text_unSelectcolor));
                text_blog.setTextColor(getResources().getColor(R.color.toolbar_text_unSelectcolor));
                constraintLayout.setVisibility(View.VISIBLE);
                text_description.setVisibility(View.GONE);
                getSendCoinHistory(userData.getToken(),"imt");


                break;
            case R.id.text_about:
                text_about.setTextColor(getResources().getColor(R.color.toolbar_text_color));
                text_activity.setTextColor(getResources().getColor(R.color.toolbar_text_unSelectcolor));
                text_video.setTextColor(getResources().getColor(R.color.toolbar_text_unSelectcolor));
                text_blog.setTextColor(getResources().getColor(R.color.toolbar_text_unSelectcolor));
                constraintLayout.setVisibility(View.GONE);
                text_description.setVisibility(View.VISIBLE);
                text_description.setText("Descriptions");

                break;
            case R.id.text_video:
                text_about.setTextColor(getResources().getColor(R.color.toolbar_text_unSelectcolor));
                text_activity.setTextColor(getResources().getColor(R.color.toolbar_text_unSelectcolor));
                text_video.setTextColor(getResources().getColor(R.color.toolbar_text_color));
                text_blog.setTextColor(getResources().getColor(R.color.toolbar_text_unSelectcolor));
                constraintLayout.setVisibility(View.VISIBLE);
                text_description.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                history_Empty.setVisibility(View.VISIBLE);
                history_Empty.setText("No Videos are Available");

                break;
            case R.id.text_blog:
                text_about.setTextColor(getResources().getColor(R.color.toolbar_text_unSelectcolor));
                text_activity.setTextColor(getResources().getColor(R.color.toolbar_text_unSelectcolor));
                text_video.setTextColor(getResources().getColor(R.color.toolbar_text_unSelectcolor));
                text_blog.setTextColor(getResources().getColor(R.color.toolbar_text_color));
                constraintLayout.setVisibility(View.VISIBLE);
                text_description.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                history_Empty.setVisibility(View.VISIBLE);
                history_Empty.setText("No Blog are Available");
                break;



        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onSaveInstanceState(new Bundle());
    }

    public void getBalance() {

        String token = userData.getToken();


        String currency = currency2.toUpperCase();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().Balance(token, "imt","imt", currency);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                            coinprice.setText("" + df.format(balance2));
                            balances.setText(CurrencySymbols + "0");
                        } else {

                            double balance2 = Double.parseDouble(balance);
                            double calBalance2 = Double.parseDouble(calBalance);

                            coinprice.setText("" + df.format(balance2));
                            balances.setText(CurrencySymbols + df.format(calBalance2));
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
                                    .setActivity(ImtSmartGraphLayout.this)
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





                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 401) {

                    Snacky.builder()
                            .setActivity(ImtSmartGraphLayout.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }

            }

            //"Failed to Load Balance kindly try again."
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snacky.builder()
                        .setActivity(ImtSmartGraphLayout.this)
                        .setText(t.getLocalizedMessage())
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



    public void getGraphData(String id, String currency, String days, String interval) {

        progressDialog = KProgressHUD.create(ImtSmartGraphLayout.this)
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
                    MyMarkerView mv = new MyMarkerView(ImtSmartGraphLayout.this, R.layout.custom_marker_view);

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
                        Drawable drawable = ContextCompat.getDrawable(ImtSmartGraphLayout.this, R.drawable.gradient1);
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
                                .setActivity(ImtSmartGraphLayout.this)
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
                        .setActivity(ImtSmartGraphLayout.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

    }

    public void GetImtGraph1d(String coinName, String currency2, int hour) {

        progressDialog = KProgressHUD.create(ImtSmartGraphLayout.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitGraph.RetrofitGraph1.getInstance().getApi1().ImtGraph1d(coinName, currency2, hour);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                hidepDialog();

                if (response.isSuccessful()) {
                    ArrayList<Entry> yvalue = new ArrayList<>();
                    yvalue.clear();
                    try {
                        s = response.body().string();

                        JSONObject object = new JSONObject(s);
                        String response1 = object.getString("Response");

                        if (response1.equals("Error")) {
                            String Message = object.getString("Message");
                            Snacky.builder()
                                    .setActivity(ImtSmartGraphLayout.this)
                                    .setText(Message)
                                    .setDuration(Snacky.LENGTH_SHORT)
                                    .setActionText(android.R.string.ok)
                                    .error()
                                    .show();
                        } else {
                            String Data = object.getString("Data");

                            JSONObject object1 = new JSONObject(Data);
                            String Data1 = object1.getString("Data");

                            JSONArray jsonArray = new JSONArray(Data1);
                            for (int i = 0; i <= jsonArray.length(); i++) {
                                JSONObject object2 = jsonArray.getJSONObject(i);
                                String high = object2.getString("high");
                                Float aFloat = Float.parseFloat(high);
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
                    MyMarkerView1 mv = new MyMarkerView1(ImtSmartGraphLayout.this, R.layout.custom_marker_view);

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
                        Drawable drawable = ContextCompat.getDrawable(ImtSmartGraphLayout.this, R.drawable.gradient1);
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

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(ImtSmartGraphLayout.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }

    public void getSendCoinHistory(String token, String symbol) {




        Call<SendCoinHistoryResponse> call = RetrofitClient.getInstance().getApi().get_SendHistory(token, symbol);

        call.enqueue(new Callback<SendCoinHistoryResponse>() {
            @Override
            public void onResponse(Call<SendCoinHistoryResponse> call, Response<SendCoinHistoryResponse> response) {
                String s = null;
                if (response.code() == 200  && response.body()!=null) {
                    if (response.body().getResult()!=null && response.body().getResult().size()>0){
                        coin_Send_history_adapter = new Coin_Send_History_Adapter(response.body(), getApplicationContext(), ImtSmartGraphLayout.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(coin_Send_history_adapter);
                        history_Empty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                    }else{
                        history_Empty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } else if (response.code() == 400 || response.code() == 401) {
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(ImtSmartGraphLayout.this)
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
            public void onFailure(Call<SendCoinHistoryResponse> call, Throwable t) {
                Snacky.builder()
                        .setActivity(ImtSmartGraphLayout.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

            }
        });
    }



    @Override
    public void onSendCoinItemClickListener(SendCoinHistoryResponse sendCoinHistoryResponse, int position) {
        String username = sendCoinHistoryResponse.getResult().get(position).getUserId().getUsername();
        String transaction = sendCoinHistoryResponse.getResult().get(position).getTransactionHash();
        String type = sendCoinHistoryResponse.getResult().get(position).getType();
        String date = sendCoinHistoryResponse.getResult().get(position).getCreatedAt();
        String amount = sendCoinHistoryResponse.getResult().get(position).getAmtOfCrypto();

        Transaction_HistoryModel historyModel = new Transaction_HistoryModel(transaction, "Success", amount, "type", username, date, null, type);

        //storing the user in shared preferences
        TransactionHistorySharedPrefManager.getInstance(getApplicationContext()).Transaction_History_Data(historyModel);


        Intent intent = new Intent(ImtSmartGraphLayout.this, Full_Transaction_History.class);
        startActivity(intent);

    }
   /* @Override
    public void onHistoryItemClickListener(int position) {
        String username = coinModals.get(position).getUsername();
        String transaction = coinModals.get(position).getId();
        String type = coinModals.get(position).getType();
        String date = coinModals.get(position).getTime();
        String amount = coinModals.get(position).getAmount();
        String Type = "coinTransfer";
        Transaction_HistoryModel historyModel = new Transaction_HistoryModel(transaction, "imt", amount, "type", username, date,null,type);

        //storing the user in shared preferences
        TransactionHistorySharedPrefManager.getInstance(getApplicationContext()).Transaction_History_Data(historyModel);


        Intent intent = new Intent(ImtSmartGraphLayout.this, Full_Transaction_History.class);
        startActivity(intent);
    }
*/}