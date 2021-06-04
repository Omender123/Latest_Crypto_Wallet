package com.crypto.croytowallet.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Adapter.NewCoinAdapter;
import com.crypto.croytowallet.Extra_Class.ApiResponse.GetNewCoinRespinse;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.database.RetrofitGraph;
import com.kaopiz.kprogresshud.KProgressHUD;

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

public class New_Currency extends AppCompatActivity implements NewCoinAdapter.OnOrderItemListener {
    RecyclerView recyclerView;
    KProgressHUD progressDialog;
    UserData userData;
    String token;
    ArrayList<String> coinName;
    TextView empty;
    EditText search;
    CharSequence search1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__currency);
        recyclerView = findViewById(R.id.recyclerView_add_NewCoin);
        empty = findViewById(R.id.txt_list_is_empty);
        search = findViewById(R.id.search_currency);
        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        token = userData.getToken();
        coinName = new ArrayList<String>();


        getNewCoin(token);
        // getNewcoinss("coinName", "usd");

    }

    private void getNewCoin(String token) {

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getNewCoin(token);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                coinName.clear();
                String s = null;
                if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
                    try {
                        s = response.body().string();
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            coinName.add(jsonArray.getString(i));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    String coinId = String.join(",", coinName);
                    if (!coinId.isEmpty()){
                        getNewcoinss(coinId, "usd");
                    }else {
                         empty.setVisibility(View.VISIBLE);
                    }

                } else if (response.code() == 401 || response.code() == 400) {

                    try {
                        s = response.errorBody().string();
                        JSONObject object = new JSONObject(s);
                        String error = object.getString("error");

                        Snacky.builder()
                                .setActivity(New_Currency.this)
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
                        .setActivity(New_Currency.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }

    private void getNewcoinss(String coinId, String usd) {

        progressDialog = KProgressHUD.create(New_Currency.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<List<GetNewCoinRespinse>> call = RetrofitGraph.getInstance().getApi().getNewCoin(coinId, usd);

        call.enqueue(new Callback<List<GetNewCoinRespinse>>() {
            @Override
            public void onResponse(Call<List<GetNewCoinRespinse>> call, Response<List<GetNewCoinRespinse>> response) {
                hidepDialog();

                if (response.code() == 200 || response.isSuccessful()) {
                    if (response.body() != null || response.body().size() > 0) {

                        NewCoinAdapter adapter = new NewCoinAdapter(response.body(), getApplicationContext(), New_Currency.this);
                        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(New_Currency.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager1);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);

                        search.addTextChangedListener(new TextWatcher() {
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

                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetNewCoinRespinse>> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(New_Currency.this)
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
        startActivity(new Intent(New_Currency.this, Add_Currency.class));
        finish();
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        startActivity(new Intent(New_Currency.this, Add_Currency.class));
        finish();
    }


    @Override
    public void onOrderItemClick(List<GetNewCoinRespinse> data, int position) {

        String symbols = data.get(position).getSymbol();
        String name = data.get(position).getId();
        String image = data.get(position).getImage();
        Boolean aBoolean = true;

        AddCoinInUserProfile(token, name, aBoolean, symbols, image);


    }

    private void AddCoinInUserProfile(String token, String name, Boolean aBoolean, String symbols, String image) {
        progressDialog = KProgressHUD.create(New_Currency.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().AddCoinInProfile(token, name, aBoolean, symbols, image);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();
                String s =null;
                if (response.isSuccessful()||response.code()==200){
                    getNewCoin(token);
                    Snacky.builder()
                            .setActivity(New_Currency.this)
                            .setText("Successfully Active the "+name.toUpperCase())
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .success()
                            .show();

                }else if (response.code()==400||response.code()==401){
                    try {
                        s = response.errorBody().string();
                        JSONObject object = new JSONObject(s);
                        String error = object.getString("error");

                        Snacky.builder()
                                .setActivity(New_Currency.this)
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
                        .setActivity(New_Currency.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

    }
}