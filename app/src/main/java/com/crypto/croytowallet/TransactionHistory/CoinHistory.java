package com.crypto.croytowallet.TransactionHistory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.crypto.croytowallet.Adapter.CoinSpinnerAdapter;
import com.crypto.croytowallet.Adapter.Coin_History_Adapter;
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

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinHistory extends AppCompatActivity implements HistoryClickLister {
    RecyclerView recyclerView;
    ArrayList<CoinModal> coinModals;
    Coin_History_Adapter coin_history_adapter;
    KProgressHUD progressDialog;
    TextView history_Empty;
    Spinner coinSpinner,coinReceived;
    String sendData,receivedData;
    String [] coinName ={"ImSmart","Bitcoin","Ethereum","Tether","XRP","Litecoin","USD Coin"};
    String [] coinSymbols ={"imt","btc","eth","usdt","xrp","ltc","usdc"};
    String [] coinImage ={"https://assets.coingecko.com/coins/images/11770/large/qUaSWQys_400x400.png?1593744621",
            "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
            "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880",
            "https://assets.coingecko.com/coins/images/325/large/Tether-logo.png?1598003707",
            "https://assets.coingecko.com/coins/images/44/large/xrp-symbol-white-128.png?1605778731",
            "https://assets.coingecko.com/coins/images/2/large/litecoin.png?1547033580",
            "https://assets.coingecko.com/coins/images/6319/large/USD_Coin_icon.png?1547042389"};

    String [] coinName1 ={"Bitcoin","Ethereum","XRP","Litecoin","ALT-Coin"};
    String [] coinSymbols1 ={"btc","eth","xrp","ltc","erc20"};
    String [] coinImage1 ={"https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
            "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880",
            "https://assets.coingecko.com/coins/images/44/large/xrp-symbol-white-128.png?1605778731",
            "https://assets.coingecko.com/coins/images/2/large/litecoin.png?1547033580",
           "https://assets.coingecko.com/coins/images/11770/large/qUaSWQys_400x400.png?1593744621"};

    TextView sendTxt,receivedTxt;
    LinearLayout lyt_send,lyt_received;

    ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_history);
        recyclerView=findViewById(R.id.recyclerCoin);
        coinSpinner = findViewById(R.id.coinSpinner);
        coinReceived= findViewById(R.id.coinReceived);
        history_Empty =findViewById(R.id.txt_list_is_empty);

        sendTxt = findViewById(R.id.textSend);
        receivedTxt  = findViewById(R.id.textReceived);
        lyt_received = findViewById(R.id.received);
        lyt_send = findViewById(R.id.send);

        toggleButton = findViewById(R.id.toogle);
        coinModals =new ArrayList<CoinModal>();

        CoinSpinnerAdapter coinSpinnerAdapter =new CoinSpinnerAdapter(getApplicationContext(),coinImage,coinName,coinSymbols);
        coinSpinner.setAdapter(coinSpinnerAdapter);
        coinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              sendData=coinSymbols[position];

              try {
                  getSendCoinHistory();
                  coinModals.clear();
                  coin_history_adapter.notifyDataSetChanged();

                  new Handler().postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          if (coinModals!=null && coinModals.size()>0){
                           //   history_Empty.setVisibility(View.GONE);


                          }else {
                                         Snacky.builder()
                                                  .setActivity(CoinHistory.this)
                                                  .setText("No Transaction History Available")
                                                  .setDuration(Snacky.LENGTH_SHORT)
                                                  .setActionText(android.R.string.ok)
                                                  .success()
                                                  .show();
                                      }
                      }
                  },500);

              }catch (Exception e){

              }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
              //  getSendCoinHistory();
            }
        });

        CoinSpinnerAdapter coinSpinnerAdapter1 =new CoinSpinnerAdapter(getApplicationContext(),coinImage1,coinName1,coinSymbols1);
        coinReceived.setAdapter(coinSpinnerAdapter1);
         coinReceived.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                receivedData=coinSymbols1[position];
                  try {
                      getReceivedCoinHistory();
                  coinModals.clear();
                  coin_history_adapter.notifyDataSetChanged();

                  new Handler().postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          if (coinModals!=null && coinModals.size()>0){
                              //  history_Empty.setVisibility(View.GONE);
                          }else {
                              //   history_Empty.setVisibility(View.VISIBLE);
                              Snacky.builder()
                                      .setActivity(CoinHistory.this)
                                      .setText("No Transaction History Available")
                                      .setDuration(Snacky.LENGTH_SHORT)
                                      .setActionText(android.R.string.ok)
                                      .success()
                                      .show();
                          }
                      }
                  },500);
              }catch (Exception e){

              }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
              //  getSendCoinHistory();
            }
        });


        toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on){

                    lyt_received.setBackground(getResources().getDrawable(R.drawable.background_border3));
                    receivedTxt.setTextColor(getResources().getColor(R.color.black));
                    sendTxt.setTextColor(getResources().getColor(R.color.purple_500));
                    lyt_send.setBackground(getResources().getDrawable(R.drawable.background_broder));
                    coinSpinner.setVisibility(View.GONE);
                    coinReceived.setVisibility(View.VISIBLE);
                  //  getReceivedCoinHistory();
                    try {

                        getReceivedCoinHistory();
                        coinModals.clear();
                        coin_history_adapter.notifyDataSetChanged();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (coinModals!=null && coinModals.size()>0){
                                    //  history_Empty.setVisibility(View.GONE);
                                }else {
                                    //   history_Empty.setVisibility(View.VISIBLE);
                                    Snacky.builder()
                                            .setActivity(CoinHistory.this)
                                            .setText("No Transaction History Available")
                                            .setDuration(Snacky.LENGTH_SHORT)
                                            .setActionText(android.R.string.ok)
                                            .success()
                                            .show();
                                }
                            }
                        },500);

                    }catch (Exception e){

                    }



                }else{

                    lyt_received.setBackground(getResources().getDrawable(R.drawable.background_broder));
                    receivedTxt.setTextColor(getResources().getColor(R.color.purple_500));
                    sendTxt.setTextColor(getResources().getColor(R.color.black));
                    lyt_send.setBackground(getResources().getDrawable(R.drawable.background_border3));
                    coinSpinner.setVisibility(View.VISIBLE);
                    coinReceived.setVisibility(View.GONE);

                  //  getSendCoinHistory();
                    // getSendCoinHistory();
                    try {
                        getSendCoinHistory();
                        coinModals.clear();
                        coin_history_adapter.notifyDataSetChanged();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (coinModals!=null && coinModals.size()>0){
                                  //  history_Empty.setVisibility(View.GONE);
                                }else {
                                 //   history_Empty.setVisibility(View.VISIBLE);
                                    Snacky.builder()
                                            .setActivity(CoinHistory.this)
                                            .setText("No Transaction History Available")
                                            .setDuration(Snacky.LENGTH_SHORT)
                                            .setActionText(android.R.string.ok)
                                            .success()
                                            .show();
                                }
                            }
                        },500);

                    }catch (Exception e){

                    }

                }

            }
        });

          }
    public void getReceivedCoinHistory() {

        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

       /* progressDialog = KProgressHUD.create(CoinHistory.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
*/
       // showpDialog();

        Call<ResponseBody> call= RetrofitClient.getInstance().getApi().get_ReceivedHistory(token,receivedData);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               // hidepDialog();
                String s=null;
                if (response.code()==200){

                    coinModals.clear();
                    try {
                        s=response.body().string();

                        JSONArray jsonArray = new JSONArray(s);
                        for (int i=0;i<=jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            CoinModal modal = new CoinModal();
                            String transactionHash= object.getString("hash");
                            String form= object.getString("from");
                            String date= object.getString("timeStamp");
                            String value= object.getString("value");

                            modal.setUsername(form);
                            modal.setTime(date);
                            modal.setAmount(value);
                            modal.setType("Received");
                            modal.setId(transactionHash);
                            coinModals.add(modal);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    if(coinModals!=null && coinModals.size()>0) {
                        coin_history_adapter = new Coin_History_Adapter(coinModals, getApplicationContext(),CoinHistory.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(coin_history_adapter);

                    } else{
                      /*  Snacky.builder()
                                .setActivity(CoinHistory.this)
                                .setText("No Transaction History Available")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .success()
                                .show();
                    */}

                }else if (response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


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

                } else if (response.code()==401){
                    Snacky.builder()
                            .setActivity(CoinHistory.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
               // hidepDialog();
            }
        });
    }

    public void getSendCoinHistory() {

        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        progressDialog = KProgressHUD.create(CoinHistory.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call= RetrofitClient.getInstance().getApi().get_SendHistory(token,sendData);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();
                String s=null;
                if (response.code()==200){
                     coinModals.clear();
                    try {
                        s=response.body().string();
                        JSONObject object  = new JSONObject(s);
                        String result =object.getString("result");
                        JSONArray jsonArray  =  new JSONArray(result);
                        for (int i=0;i<=jsonArray.length();i++){
                            JSONObject object1 = jsonArray.getJSONObject(i);

                            CoinModal modal = new CoinModal();
                            String type = object1.getString("cryptoCurrency");
                            String amount = object1.getString("amtOfCrypto");
                            String id = object1.getString("transactionHash");
                            String date = object1.getString("createdAt");
                            String userData = object1.getString("userId");
                            //  String receiver = object1.getString("receiver");



                            JSONObject object2 = new JSONObject(userData);
                            String username =object2.getString("username");



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
                    if(coinModals!=null && coinModals.size()>0){
                        coin_history_adapter = new Coin_History_Adapter(coinModals,getApplicationContext(),CoinHistory.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(coin_history_adapter);
                    }else{

                       /* Snacky.builder()
                                .setActivity(CoinHistory.this)
                                .setText("No Transaction History Available")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .success()
                                .show();
*/

                        // history_Empty.setVisibility(View.VISIBLE);

                    }

                }else if (response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


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

                } else if (response.code()==401){
                    Snacky.builder()
                            .setActivity(CoinHistory.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
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


    @Override
    public void onHistoryItemClickListener(int position) {
        String username =coinModals.get(position).getUsername();
        String transaction =coinModals.get(position).getId();
        String type =coinModals.get(position).getType();
        String date =coinModals.get(position).getTime();
        String amount =coinModals.get(position).getAmount();

        Transaction_HistoryModel historyModel=new Transaction_HistoryModel(transaction,"Success",amount,type,username,date,null,"coinTransfer");

        //storing the user in shared preferences
        TransactionHistorySharedPrefManager.getInstance(getApplicationContext()).Transaction_History_Data(historyModel);


        Intent intent = new Intent(CoinHistory.this,Full_Transaction_History.class);
        startActivity(intent);


    }
}