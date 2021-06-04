package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.crypto.croytowallet.Adapter.Add_Currency_Adapter;

import com.crypto.croytowallet.Interface.EnabledClickedListner;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.Model.Model_Class_Add_Currency;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
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

public class
Add_Currency extends AppCompatActivity implements EnabledClickedListner {
    RecyclerView recyclerView;
    ArrayList<Model_Class_Add_Currency> item_data;
    EditText search;
    ImageView imageView;
    CharSequence search1 = "";
    KProgressHUD progressDialog;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__currency);
        search = findViewById(R.id.search_currency);
        imageView = findViewById(R.id.back);
        item_data = new ArrayList<Model_Class_Add_Currency>();
        recyclerView = findViewById(R.id.recyclerView_add_currenecy);

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        getAllCoins();

        back();

    }


    public void back() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_Currency.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

             //   onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
      //  onSaveInstanceState(new Bundle());
        Intent intent = new Intent(Add_Currency.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }

    public void getAllCoins() {
        progressDialog = KProgressHUD.create(Add_Currency.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();


        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getAllCoinDataBase(userData.getToken());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hidepDialog();
                String s = null;
                item_data.clear();
                if (response.isSuccessful() && response.code() == 200 && response.body() != null) {

                    try {
                        s =response.body().string();

                        JSONObject object = new JSONObject(s);

                        String account = object.getString("Account");

                        JSONArray jsonArray = new JSONArray(account);


                        for (int i=0 ; i<=jsonArray.length();i++ ){
                            Model_Class_Add_Currency currency_model = new Model_Class_Add_Currency();

                            JSONObject object1 =jsonArray.getJSONObject(i);
                            String symbol = object1.getString("symbol");
                            String name = object1.getString("name");
                            String image = object1.getString("image");
                            Boolean enabled = object1.getBoolean("enabled");

                            currency_model.setCurrency_Title(name);
                            currency_model.setTitle_Des(symbol);
                            currency_model.setCoinId(name);
                            currency_model.setImage(image);
                            currency_model.setChecked(enabled);

                            item_data.add(currency_model);
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                    Add_Currency_Adapter adapter = new Add_Currency_Adapter(item_data, getApplicationContext(),Add_Currency.this);
                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(Add_Currency.this, LinearLayoutManager.VERTICAL, false);
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

                }


            else if(response.code()==401||response.code()==400)

            {

                try {
                    s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    String error = object.getString("error");

                    Snacky.builder()
                            .setActivity(Add_Currency.this)
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
        public void onFailure (Call < ResponseBody> call, Throwable t){
            hidepDialog();

            Snacky.builder()
                    .setActivity(Add_Currency.this)
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
    public void onEnabledItemClickListener(int position, Boolean aBoolean) {
        String  Symbols = item_data.get(position).getTitle_Des();
        String value = String.valueOf(aBoolean);
        CoinEdit(Symbols,value);

    }

    private void CoinEdit(String symbols, String aBoolean) {
        Call<ResponseBody>call = RetrofitClient.getInstance().getApi().EditCoin(userData.getToken(),symbols,aBoolean);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String  s = null;
                if (response.isSuccessful()){

                }
                else if(response.code()==401||response.code()==400) {

                    try {
                        s = response.errorBody().string();
                        JSONObject object = new JSONObject(s);
                        String error = object.getString("error");

                        Snacky.builder()
                                .setActivity(Add_Currency.this)
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
                        .setActivity(Add_Currency.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }

    public void newCoin(View view) {
        startActivity(new Intent(Add_Currency.this,New_Currency.class));
        finish();

    }
}
