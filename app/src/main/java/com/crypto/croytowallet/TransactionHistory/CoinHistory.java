package com.crypto.croytowallet.TransactionHistory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Adapter.CoinSpinnerAdapter;
import com.crypto.croytowallet.Adapter.Coin_Send_History_Adapter;
import com.crypto.croytowallet.Adapter.ReceviedCoinHistoryApdapter;
import com.crypto.croytowallet.Extra_Class.ApiResponse.ReceviedCoinHistoryResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.SendCoinHistoryResponse;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.Model.CoinModal;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.TransactionHistorySharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.Transaction_HistoryModel;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.zcw.togglebutton.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinHistory extends AppCompatActivity implements Coin_Send_History_Adapter.OnSendCoinItemListener, ReceviedCoinHistoryApdapter.OnReceviedCoinItemListener, View.OnClickListener {
    RecyclerView recyclerView;
    Coin_Send_History_Adapter coin_send_history_adapter;
    KProgressHUD progressDialog;
    TextView history_Empty;
    UserData user;
    Spinner coinSpinner;
    String sendData, ReceivedData, auth_token;
    String[] coinName = {"ImSmart", "Bitcoin", "Ethereum", "Tether", "XRP", "Litecoin", "USD Coin"};
    String[] coinSymbols = {"imt", "btc", "eth", "usdt", "xrp", "ltc", "usdc"};
    String[] coinReceviedSymbols = {"erc20", "btc", "eth", "erc20", "xrp", "ltc", "erc20"};
    String[] coinImage = {"https://assets.coingecko.com/coins/images/11770/large/qUaSWQys_400x400.png?1593744621",
            "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
            "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880",
            "https://assets.coingecko.com/coins/images/325/large/Tether-logo.png?1598003707",
            "https://assets.coingecko.com/coins/images/44/large/xrp-symbol-white-128.png?1605778731",
            "https://assets.coingecko.com/coins/images/2/large/litecoin.png?1547033580",
            "https://assets.coingecko.com/coins/images/6319/large/USD_Coin_icon.png?1547042389"};

    TextView sendTxt, receivedTxt;
    LinearLayout lyt_send, lyt_received;

    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_history);
        recyclerView = findViewById(R.id.recyclerCoin);
        coinSpinner = findViewById(R.id.coinSpinner);
        history_Empty = findViewById(R.id.txt_list_is_empty);

        sendTxt = findViewById(R.id.textSend);
        receivedTxt = findViewById(R.id.textReceived);
        lyt_received = findViewById(R.id.received);
        lyt_send = findViewById(R.id.send);

        lyt_send.setOnClickListener(this);
        lyt_received.setOnClickListener(this);

        user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        auth_token = user.getToken();

        CoinSpinnerAdapter coinSpinnerAdapter = new CoinSpinnerAdapter(getApplicationContext(), coinImage, coinName, coinSymbols);
        coinSpinner.setAdapter(coinSpinnerAdapter);
        coinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sendData = coinSymbols[position];
                ReceivedData = coinReceviedSymbols[position];


                if (check == false) {

                    getSendCoinHistory(auth_token, sendData);
                } else {

                    getReceivedCoinHistory(auth_token, ReceivedData);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void getReceivedCoinHistory(String token, String receivedData) {


        progressDialog = KProgressHUD.create(CoinHistory.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<List<ReceviedCoinHistoryResponse>> call = RetrofitClient.getInstance().getApi().get_ReceivedHistory(token, receivedData);

        call.enqueue(new Callback<List<ReceviedCoinHistoryResponse>>() {
            @Override
            public void onResponse(Call<List<ReceviedCoinHistoryResponse>> call, Response<List<ReceviedCoinHistoryResponse>> response) {
                hidepDialog();
                String s = null;
                if (response.code() == 200) {

                    if (response.body() != null && response.body().size() > 0) {
                        ReceviedCoinHistoryApdapter receviedCoinHistoryApdapter = new ReceviedCoinHistoryApdapter(response.body(), getApplicationContext(), CoinHistory.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(receviedCoinHistoryApdapter);
                        history_Empty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                    } else {
                        history_Empty.setVisibility(View.VISIBLE);
                        history_Empty.setText("No Received Coin History are Available");
                        recyclerView.setVisibility(View.GONE);
                    }
                } else if (response.code() == 400 || response.code() == 401) {
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(CoinHistory.this)
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
            public void onFailure(Call<List<ReceviedCoinHistoryResponse>> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(CoinHistory.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }

    public void getSendCoinHistory(String token, String symbol) {


        progressDialog = KProgressHUD.create(CoinHistory.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<SendCoinHistoryResponse> call = RetrofitClient.getInstance().getApi().get_SendHistory(token, symbol);

        call.enqueue(new Callback<SendCoinHistoryResponse>() {
            @Override
            public void onResponse(Call<SendCoinHistoryResponse> call, Response<SendCoinHistoryResponse> response) {
                hidepDialog();
                String s = null;
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getResult() != null && response.body().getResult().size() > 0) {
                        coin_send_history_adapter = new Coin_Send_History_Adapter(response.body(), getApplicationContext(), CoinHistory.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(coin_send_history_adapter);
                        history_Empty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                    } else {
                        history_Empty.setVisibility(View.VISIBLE);
                        history_Empty.setText("No Send Coin History are Available");
                        recyclerView.setVisibility(View.GONE);
                    }
                } else if (response.code() == 400 || response.code() == 401) {
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(CoinHistory.this)
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
                hidepDialog();

                Snacky.builder()
                        .setActivity(CoinHistory.this)
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CoinHistory.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }


    public void back(View view) {
        onBackPressed();
    }


   /* @Override
    public void onHistoryItemClickListener(int position) {
        String username = coinModals.get(position).getUsername();
        String transaction = coinModals.get(position).getId();
        String type = coinModals.get(position).getType();
        String date = coinModals.get(position).getTime();
        String amount = coinModals.get(position).getAmount();

        Transaction_HistoryModel historyModel = new Transaction_HistoryModel(transaction, "Success", amount, "type", username, date, null, type);

        //storing the user in shared preferences
        TransactionHistorySharedPrefManager.getInstance(getApplicationContext()).Transaction_History_Data(historyModel);


        Intent intent = new Intent(CoinHistory.this, Full_Transaction_History.class);
        startActivity(intent);


    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                lyt_received.setBackground(getResources().getDrawable(R.drawable.background_broder));
                receivedTxt.setTextColor(getResources().getColor(R.color.purple_500));
                sendTxt.setTextColor(getResources().getColor(R.color.black));
                lyt_send.setBackground(getResources().getDrawable(R.drawable.background_border3));
                check = false;
                getSendCoinHistory(auth_token, sendData);
                break;

            case R.id.received:
                lyt_received.setBackground(getResources().getDrawable(R.drawable.background_border3));
                receivedTxt.setTextColor(getResources().getColor(R.color.black));
                sendTxt.setTextColor(getResources().getColor(R.color.purple_500));
                lyt_send.setBackground(getResources().getDrawable(R.drawable.background_broder));
                check = true;
                getReceivedCoinHistory(auth_token, ReceivedData);
                break;
        }
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


        Intent intent = new Intent(CoinHistory.this, Full_Transaction_History.class);
        startActivity(intent);

    }

    @Override
    public void onReceviedCoinItemClickListener(List<ReceviedCoinHistoryResponse> receviedCoinHistoryResponses, int position) {

        String username = receviedCoinHistoryResponses.get(position).getFrom();
        String transaction = receviedCoinHistoryResponses.get(position).getHash();
        String type = "Received";
        String date = receviedCoinHistoryResponses.get(position).getTimeStamp();
        String amount = receviedCoinHistoryResponses.get(position).getValue();

        Transaction_HistoryModel historyModel = new Transaction_HistoryModel(transaction, "Success", amount, "type", username, date, null, type);

        //storing the user in shared preferences
        TransactionHistorySharedPrefManager.getInstance(getApplicationContext()).Transaction_History_Data(historyModel);


        Intent intent = new Intent(CoinHistory.this, Full_Transaction_History.class);
        startActivity(intent);
    }
}