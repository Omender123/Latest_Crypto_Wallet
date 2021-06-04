package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crypto.croytowallet.Adapter.Transaaction_history_adapter;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TransactionHistoryResponse;
import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.TransactionHistorySharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.Transaction_HistoryModel;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.TransactionHistory.Full_Transaction_History;
import com.crypto.croytowallet.TransactionHistory.Transaction_history;
import com.crypto.croytowallet.database.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.mateware.snacky.Snacky;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletBalance extends AppCompatActivity implements HistoryClickLister {
    ImageView imageView,sreach;
    TextView textView,textView1,more;
    RecyclerView recyclerView;
    KProgressHUD progressDialog;
    TextView history_Empty;
   SharedPreferences sharedPreferences;
    private ArrayList<TransactionHistoryResponse.Result> data;
    Transaaction_history_adapter transaaction_history_adapter;

    String CurrencySymbols,currency2,balance,cal_balance;
   LinearLayout linearLayout;
    private ShimmerFrameLayout mShimmerViewContainer,mShimmerViewContainer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_balance);
        imageView =findViewById(R.id.back);
        sreach =findViewById(R.id.sreach);
        back();
        textView  =findViewById(R.id.balance);
        textView1  =findViewById(R.id.balance1);

        more=findViewById(R.id.moretransactions);
        recyclerView=findViewById(R.id.recyclerViewAddBalance);
        history_Empty =findViewById(R.id.txt_list_is_empty);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer1 = findViewById(R.id.shimmer_view_container1);
        linearLayout =  findViewById(R.id.linear);

        data = new  ArrayList<TransactionHistoryResponse.Result>();

        sharedPreferences =getApplicationContext().getSharedPreferences("currency",0);
       currency2 =sharedPreferences.getString("currency1","usd");
        CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");


        balance = MyPreferences.getInstance(getApplicationContext()).getString(PrefConf.USER_BALANCE,"0");
        cal_balance =MyPreferences.getInstance(getApplicationContext()).getString(PrefConf.CAL_USER_BALANCE,"0");

        textView.setText(balance);
        textView1.setText(CurrencySymbols+cal_balance);




        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletBalance.this, Transaction_history.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        sreach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletBalance.this, Transaction_history.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        GetAllTransactionHistory();

    }



    private void GetAllTransactionHistory() {
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        progressDialog = KProgressHUD.create(WalletBalance.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<TransactionHistoryResponse> call = RetrofitClient.getInstance().getApi().GetAllTransactionHistory(token);

        call.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call, Response<TransactionHistoryResponse> response) {
                hidepDialog();
                String s= null;
                if (response.isSuccessful()){
                    TransactionHistoryResponse transactionHistoryResponse = response.body();
                    data = new ArrayList<TransactionHistoryResponse.Result>(Arrays.asList(transactionHistoryResponse.getResults()));

                    if (data!=null && data.size()>0){
                        transaaction_history_adapter = new Transaaction_history_adapter(data,getApplicationContext(),WalletBalance.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(transaaction_history_adapter);
                    }else{

                        history_Empty.setVisibility(View.VISIBLE);

                    }




                }else{
                    try{
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(WalletBalance.this)
                                .setText(error)
                                .setTextColor(getResources().getColor(R.color.white))
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                mShimmerViewContainer1.stopShimmerAnimation();
                mShimmerViewContainer1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {
                hidepDialog();

                Snacky.builder()
                        .setActivity(WalletBalance.this)
                        .setText(t.getLocalizedMessage())
                        .setTextColor(getResources().getColor(R.color.white))
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
    public void onBackPressed() {
        super.onBackPressed();

        onSaveInstanceState(new Bundle());
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
    public void onHistoryItemClickListener(int position) {


        String id = data.get(position).getId();
        String sendername=data.get(position).getSenderName();
        String receviername=data.get(position).getReceiverName();
        String amount=data.get(position).getAmount();
        String status =data.get(position).getStatus();
        String time=data.get(position).getCreatedAt();
        String type = data.get(position).getType();

        if (data.get(position).getEarnedReward()==null){
            String Rewards = "0.0";
            Transaction_HistoryModel historyModel=new Transaction_HistoryModel(id,"Done",amount,sendername,receviername,time,Rewards,type);

            TransactionHistorySharedPrefManager.getInstance(getApplicationContext()).Transaction_History_Data(historyModel);

        }else{
            String Rewards=data.get(position).getEarnedReward();

            Transaction_HistoryModel historyModel=new Transaction_HistoryModel(id,"Done",amount,sendername,receviername,time,Rewards,type);
            //storing the user in shared preferences
            TransactionHistorySharedPrefManager.getInstance(getApplicationContext()).Transaction_History_Data(historyModel);

        }


        Intent intent = new Intent(WalletBalance.this,Full_Transaction_History.class);
        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer1.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer1.stopShimmerAnimation();
        super.onPause();
    }
}