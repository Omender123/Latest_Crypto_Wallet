package com.crypto.croytowallet.TopUp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Activity.New_Currency;
import com.crypto.croytowallet.Adapter.NewCoinAdapter;
import com.crypto.croytowallet.Adapter.TopUp_HistoryAdapter;
import com.crypto.croytowallet.Adapter.Transaaction_history_adapter;
import com.crypto.croytowallet.Extra_Class.ApiResponse.FilterBody;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TopUp_HistoryResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TopUp_HistoryResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TransactionHistoryResponse;
import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.Rewards.Reward_History;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.TransactionHistory.FilterBottomSheet;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.database.RetrofitGraph;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.List;

import de.mateware.snacky.Snacky;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopUp_History extends AppCompatActivity implements TopUp_HistoryAdapter.OnOrderItemListener {
    RecyclerView recyclerView;
     KProgressHUD progressDialog;
    TextView history_Empty;
    EditText search_input;

    CharSequence search1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up__history);
        search_input = findViewById(R.id.search_currency);
        recyclerView=findViewById(R.id.recyclerTransation);
        history_Empty =findViewById(R.id.txt_list_is_empty);
        getRewardHistory();
    }

    private void getRewardHistory() {
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        progressDialog = KProgressHUD.create(TopUp_History.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar currentCal = Calendar.getInstance();
        String endDate1 = dateFormat.format(currentCal.getTime());
        currentCal.add(Calendar.DATE, -365);
        String   startDate1 = dateFormat.format(currentCal.getTime());
        String startDate = MyPreferences.getInstance(TopUp_History.this).getString(PrefConf.START_DATE,startDate1);
        String endDate = MyPreferences.getInstance(TopUp_History.this).getString(PrefConf.END_DATE,endDate1);
        String lowAmount = MyPreferences.getInstance(TopUp_History.this).getString(PrefConf.LOW_AMOUNT,"1");
        String highAmount = MyPreferences.getInstance(TopUp_History.this).getString(PrefConf.HIGH_AMOUNT,"NULL");
        String Short = MyPreferences.getInstance(TopUp_History.this).getString(PrefConf.SHORT,"1");

        FilterBody.Filters  filters = new FilterBody.Filters(startDate,endDate,lowAmount,highAmount);
        FilterBody.Sort  sort = new FilterBody.Sort(Short);
        FilterBody filterBody = new FilterBody(filters,sort);

        Call<List<TopUp_HistoryResponse>> call = RetrofitClient.getInstance().getApi().getRewardHistory(token,filterBody);

        call.enqueue(new Callback<List<TopUp_HistoryResponse>>() {
            @Override
            public void onResponse(Call<List<TopUp_HistoryResponse>> call, Response<List<TopUp_HistoryResponse>> response) {
                  hidepDialog();
                if (response.code() == 200 || response.isSuccessful()) {
                    if (response.body() != null && response.body().size() > 0) {

                        TopUp_HistoryAdapter adapter = new TopUp_HistoryAdapter(response.body(), getApplicationContext(), TopUp_History.this);
                        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(TopUp_History.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager1);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);

                        search_input.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                adapter.getFilter().filter(s);
                                search1 = s;
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                    } else {

                        history_Empty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TopUp_HistoryResponse>> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(TopUp_History.this)
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

    public void back(View view) {
        onBackPressed();
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

    @Override
    public void onOrderItemClick(List<TopUp_HistoryResponse> data, int position) {
        TopUp_HistoryResponse topUp_historyResponse = new TopUp_HistoryResponse();
        String trans_id = data.get(position).getTransactionId();
        String date = data.get(position).getCreatedAt();
        String amount = data.get(position).getAmount();
        String utility = data.get(position).getUtility();
        String status = data.get(position).getStatus();
        String rewards = data.get(position).getReward();

        topUp_historyResponse.setTransactionId(trans_id);
        topUp_historyResponse.setCreatedAt(date);
        topUp_historyResponse.setAmount(amount);
        topUp_historyResponse.setUtility(utility);
        topUp_historyResponse.setStatus(status);
        topUp_historyResponse.setReward(rewards);

        startActivity(new Intent(getApplicationContext(), Full_TopUp_History.class).putExtra("topUp_historyResponse",topUp_historyResponse));

    }

    public void filter(View view) {
        FilterBottomSheet filterBottomSheet = new FilterBottomSheet();
        filterBottomSheet.show(getSupportFragmentManager(),filterBottomSheet.getTag());
        MyPreferences.getInstance(getApplicationContext()).putString(PrefConf.CHECK_SCREEN,"TopUp_history");
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