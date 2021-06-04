package com.crypto.croytowallet.Rewards;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.crypto.croytowallet.Adapter.Rewards_HistoryAdapter;
import com.crypto.croytowallet.Adapter.Product_adapter;
import com.crypto.croytowallet.Adapter.Transaaction_history_adapter;
import com.crypto.croytowallet.Extra_Class.ApiResponse.RewardHistoryResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TransactionHistoryResponse;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.TransactionHistory.Transaction_history;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rewards extends AppCompatActivity implements HistoryClickLister {
    RecyclerView recyclerView,history_recyclerView;
    Product_adapter product_adapter;
    Rewards_HistoryAdapter rewards_historyAdapter;
    String [] price = {"50$","50$","50$","50$","50$","50$"};
    private ArrayList<RewardHistoryResponse.Result> data;
    TextView imx_point;
    KProgressHUD progressDialog;
    UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        //recyclerView = findViewById(R.id.product_recycler);
        history_recyclerView= findViewById(R.id.history_recycler);
        imx_point = findViewById(R.id.imx_point);

        userData = SharedPrefManager.getInstance(this).getUser();

        data = new  ArrayList<RewardHistoryResponse.Result>();

       // getAllprouct();
        getAllprouctHistory();

        getRewards(userData.getToken());


    }

    private void getRewards(String token) {


        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().GetRewards(token);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String  s = null;
                if (response.isSuccessful()&&response.code()==200){

                    try {
                        s=response.body().string();
                        JSONObject object = new JSONObject(s);
                        Double reward = object.getDouble("reward");

                      imx_point.setText(new DecimalFormat("##.####").format(reward));

                       // imx_point.setText(re);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }else  if(response.code()==401||response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Rewards.this)
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
                        .setActivity(Rewards.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

            }
        });
    }

    private void getAllprouctHistory() {
        progressDialog = KProgressHUD.create(Rewards.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<RewardHistoryResponse> call = RetrofitClient.getInstance().getApi().GetHistoryRewards(userData.getToken());
        call.enqueue(new Callback<RewardHistoryResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<RewardHistoryResponse> call, Response<RewardHistoryResponse> response) {
                String  s = null;
                hidepDialog();
                if (response.isSuccessful()&&response.code()==200){
                    RewardHistoryResponse rewardHistoryResponse = response.body();
                    data = new ArrayList<RewardHistoryResponse.Result>(Arrays.asList(rewardHistoryResponse.getResults()));
                    if (data!=null && data.size()>0){
                        rewards_historyAdapter = new Rewards_HistoryAdapter(data,getApplicationContext(), Rewards.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                        history_recyclerView.setLayoutManager(mLayoutManager);
                        history_recyclerView.setItemAnimator(new DefaultItemAnimator());
                        history_recyclerView.setAdapter(rewards_historyAdapter);
                    }else{

                        Snacky.builder()
                                .setActivity(Rewards.this)
                                .setText("No Rewards Transaction History")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();



                    }


                }else  if(response.code()==401||response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Rewards.this)
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
            public void onFailure(Call<RewardHistoryResponse> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(Rewards.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

            }
        });
   

    }

    private void getAllprouct() {
        product_adapter = new Product_adapter(price,Rewards.this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(Rewards.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(product_adapter);
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    public void OpenRedeem(View view) {
        startActivity(new Intent(getApplicationContext(),Redeem.class));
    }
    public void Categories_btn(View view) {
        startActivity(new Intent(getApplicationContext(),Product_Categories.class));
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
    public void onHistoryItemClickListener(int position) {
      RewardHistoryResponse.Result  result = new RewardHistoryResponse.Result();
       String trans_id = data.get(position).getId();
        String Rewards = data.get(position).getEarnedReward();
        String airdrop_amount = data.get(position).getAmount();
        String Date_and_Time = data.get(position).getCreatedAt();
        result.setId(trans_id);
        result.setEarnedReward(Rewards);
        result.setAmount(airdrop_amount);
        result.setCreatedAt(Date_and_Time);

      startActivity(new Intent(getApplicationContext(),Reward_History.class).putExtra("result",result));

    }
}