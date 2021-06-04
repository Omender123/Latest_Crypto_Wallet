package com.crypto.croytowallet.TransactionHistory;

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
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Adapter.Transaaction_history_adapter;
import com.crypto.croytowallet.Extra_Class.ApiResponse.FilterBody;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TransactionHistoryResponse;
import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.Rewards.BottomSheetTC;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.TransactionHistorySharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.Transaction_HistoryModel;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import de.mateware.snacky.Snacky;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transaction_history extends AppCompatActivity implements HistoryClickLister {
    ImageView imageView;
    RecyclerView recyclerView;
    private ArrayList<TransactionHistoryResponse.Result> data;
    Transaaction_history_adapter transaaction_history_adapter;
    KProgressHUD progressDialog;
    SharedPreferences sharedPreferences;
    TextView history_Empty;
    EditText search_input;

    CharSequence search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        imageView =findViewById(R.id.back);
        search_input = findViewById(R.id.search_currency);

        back();
        recyclerView=findViewById(R.id.recyclerTransation);

        history_Empty =findViewById(R.id.txt_list_is_empty);


        data = new  ArrayList<TransactionHistoryResponse.Result>();
        sharedPreferences=getSharedPreferences("transaction", Context.MODE_PRIVATE);
      //  getHistory();

        GetAllTransactionHistory();

    }

    private void GetAllTransactionHistory() {
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        progressDialog = KProgressHUD.create(Transaction_history.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        String Check = MyPreferences.getInstance(Transaction_history.this).getString(PrefConf.CHECK,"false");

        Call<TransactionHistoryResponse> call;

        if (Check.equalsIgnoreCase("false")){
            call  = RetrofitClient.getInstance().getApi().GetAllTransactionHistory(token);
        }else{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar currentCal = Calendar.getInstance();
            String endDate1 = dateFormat.format(currentCal.getTime());
            currentCal.add(Calendar.DATE, -180);
            String   startDate1 = dateFormat.format(currentCal.getTime());
            String startDate = MyPreferences.getInstance(Transaction_history.this).getString(PrefConf.START_DATE,startDate1);
            String endDate = MyPreferences.getInstance(Transaction_history.this).getString(PrefConf.END_DATE,endDate1);
            String lowAmount = MyPreferences.getInstance(Transaction_history.this).getString(PrefConf.LOW_AMOUNT,"1");
            String highAmount = MyPreferences.getInstance(Transaction_history.this).getString(PrefConf.HIGH_AMOUNT,"NULL");
            String Short = MyPreferences.getInstance(Transaction_history.this).getString(PrefConf.SHORT,"1");


            FilterBody.Filters  filters = new FilterBody.Filters(startDate,endDate,lowAmount,highAmount);
            FilterBody.Sort  sort = new FilterBody.Sort(Short);
            FilterBody filterBody = new FilterBody(filters,sort);

            call  = RetrofitClient.getInstance().getApi().GetAllTransactionHistory1(token,filterBody);

        }


        call.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call, Response<TransactionHistoryResponse> response) {
                hidepDialog();
                String s= null;
                if (response.isSuccessful()){
                    TransactionHistoryResponse transactionHistoryResponse = response.body();
                    data = new ArrayList<TransactionHistoryResponse.Result>(Arrays.asList(transactionHistoryResponse.getResults()));

                    if (data!=null && data.size()>0){
                        transaaction_history_adapter = new Transaaction_history_adapter(data,getApplicationContext(),Transaction_history.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(transaaction_history_adapter);
                    }else{

                        history_Empty.setVisibility(View.VISIBLE);

                    }

                    search_input.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            transaaction_history_adapter.getFilter().filter(s);
                            search = s;
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });



                }else{
                    try{
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Transaction_history.this)
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

            }

            @Override
            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {
                hidepDialog();

                Snacky.builder()
                        .setActivity(Transaction_history.this)
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
          MyPreferences.getInstance(getApplicationContext()).deletePreference(PrefConf.SHORT);
          MyPreferences.getInstance(getApplicationContext()).deletePreference(PrefConf.LOW_AMOUNT);
          MyPreferences.getInstance(getApplicationContext()).deletePreference(PrefConf.HIGH_AMOUNT);
          MyPreferences.getInstance(getApplicationContext()).deletePreference(PrefConf.START_DATE);
          MyPreferences.getInstance(getApplicationContext()).deletePreference(PrefConf.END_DATE);

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


        Intent intent = new Intent(Transaction_history.this,Full_Transaction_History.class);
        startActivity(intent);

    }

    public void filter(View view) {
        FilterBottomSheet filterBottomSheet = new FilterBottomSheet();
        filterBottomSheet.show(getSupportFragmentManager(),filterBottomSheet.getTag());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyPreferences.getInstance(getApplicationContext()).deletePreference(PrefConf.SHORT);
        MyPreferences.getInstance(getApplicationContext()).deletePreference(PrefConf.LOW_AMOUNT);
        MyPreferences.getInstance(getApplicationContext()).deletePreference(PrefConf.HIGH_AMOUNT);
        MyPreferences.getInstance(getApplicationContext()).deletePreference(PrefConf.START_DATE);
        MyPreferences.getInstance(getApplicationContext()).deletePreference(PrefConf.END_DATE);

    }
}