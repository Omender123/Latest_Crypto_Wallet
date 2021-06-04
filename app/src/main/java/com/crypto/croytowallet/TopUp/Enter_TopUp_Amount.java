package com.crypto.croytowallet.TopUp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Extra_Class.ApiResponse.ResponseBankDetails;
import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedBankDetails;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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

public class Enter_TopUp_Amount extends AppCompatActivity {
    EditText ed_Amount;
    Spinner  sp_currency;
    ArrayList<String> Currency;
    CardView done;
    String  Amount, currencyType,imtPrices,token,options;
    UserData userData;
    KProgressHUD progressDialog;
    TextView text_send,text_currency,Show_amount;
    Double totalAmoumt;
    BottomSheetDialogFragment myBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter__top_up__amount);
        ed_Amount = findViewById(R.id.enter_amount);
        sp_currency = findViewById(R.id.select_currency);
        done = findViewById(R.id.add_money);
        text_send = findViewById(R.id.txt_send_amount);
        text_currency = findViewById(R.id.txt_currency);
        Show_amount = findViewById(R.id.Show_amount);
        Currency = new ArrayList<String>();

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        myBottomSheet = MyBottomSheetDialogFragment.newInstance("Modal Bottom Sheet");

        token = userData.getToken();



            options= MyPreferences.getInstance(getApplicationContext()).getString(PrefConf.TOP_UP_TYPE,"");

            if (options.equalsIgnoreCase("bank")){
                sp_currency.setVisibility(View.VISIBLE);
                text_currency.setVisibility(View.GONE);
                getCurrency();

            }else if (options.equalsIgnoreCase("qrCode")){
                currencyType="THB";
                 getImtDetails(currencyType);
            }





        Show_Details();


      done.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Amount = ed_Amount.getText().toString().trim();
              if (Amount.isEmpty()){
                  Snacky.builder()
                          .setActivity(Enter_TopUp_Amount.this)
                          .setText("Please Enter Amount")
                          .setDuration(Snacky.LENGTH_SHORT)
                          .setActionText(android.R.string.ok)
                          .warning()
                          .show();

              }else{
                  if (options.equalsIgnoreCase("bank")){
                     MyPreferences.getInstance(getApplicationContext()).putString(PrefConf.TOP_UP_TYPE1,"bank");
                      MyPreferences.getInstance(getApplicationContext()).putString(PrefConf.RECEIVED_AMOUNT, String.valueOf(totalAmoumt));

                      myBottomSheet.show(getSupportFragmentManager(), myBottomSheet.getTag());
                   }else {
                      startActivity(new Intent(getApplicationContext(),ShowTop_UP.class).putExtra("totalAmount",totalAmoumt));

                  }

              }
          }
      });





        ed_Amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String msg = s.toString();

                if (msg.isEmpty()){
                    text_send.setText(" ");
                }else {
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(8);

                    Double coinprices,enterAmount;
                    coinprices=Double.parseDouble(imtPrices);
                    enterAmount=Double.parseDouble(msg);


                    totalAmoumt = enterAmount/coinprices;

                    text_send.setText(msg +" "+currencyType.toUpperCase() +"="+df.format(totalAmoumt)+" IMT-Utility"  );
                  }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    private void Show_Details() {

        UserData userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        Call<ResponseBankDetails> bankDetailsCall = RetrofitClient.getInstance().getApi().getBankDetails(userData.getToken());
        bankDetailsCall.enqueue(new Callback<ResponseBankDetails>() {
            @Override
            public void onResponse(Call<ResponseBankDetails> call, Response<ResponseBankDetails> response) {

                String  s = null;
                if (response.isSuccessful()){

                    ResponseBankDetails   responseBankDetails = response.body();


                    ResponseBankDetails  responseBankDetails1 =new ResponseBankDetails(responseBankDetails.getAccountName(),responseBankDetails.getAccountNo(),responseBankDetails.getBankName(),responseBankDetails.getIFSCcode());

                    SharedBankDetails.getInstance(getApplicationContext()).SetBankDetails(responseBankDetails1);




                }else{

                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(Enter_TopUp_Amount.this)
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
            public void onFailure(Call<ResponseBankDetails> call, Throwable t) {
                Snacky.builder()
                        .setActivity(Enter_TopUp_Amount.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

            }
        });

    }

    public void getCurrency() {


        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getAllCurrency(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s=null;
                Currency.clear();
                if (response.isSuccessful()){
                    try {
                        s = response.body().string();

                        JSONObject object = new JSONObject(s);
                        String result = object.getString("currency");
                        JSONArray jsonArray = new JSONArray(result);

                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            String currency = object1.getString("currency");

                            Currency.add(currency);

                        }

                    }
                    catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter<String> adp = new ArrayAdapter<String> (Enter_TopUp_Amount.this,android.R.layout.simple_spinner_dropdown_item,Currency);
                    sp_currency.setAdapter(adp);

                    sp_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            currencyType = parent.getItemAtPosition(position).toString();
                            getImtDetails(currencyType);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });



                }else {
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Enter_TopUp_Amount.this)
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
                        .setActivity(Enter_TopUp_Amount.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }

    public  void getImtDetails(String currency){



        String Token =userData.getToken();

        progressDialog = KProgressHUD.create(Enter_TopUp_Amount.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody>call = RetrofitClient.getInstance().getApi().getIMTDetails(Token,currency);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s =null;
                hidepDialog();
                if (response.code()==200){
                    try {
                        s=response.body().string();

                        JSONArray jsonArray = new JSONArray(s);

                        for (int i=0; i<=jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            imtPrices =object.getString("price");
                            Show_amount.setText(" 1  IMT-Utility"+"="+imtPrices+" "+currencyType.toUpperCase() );


                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                        Amount = ed_Amount.getText().toString().trim();
                        if (!Amount.isEmpty()){
                            DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(8);

                            Double coinprices,enterAmount;
                            coinprices=Double.parseDouble(imtPrices);
                            enterAmount=Double.parseDouble(Amount);

                            totalAmoumt = enterAmount/coinprices;
                            text_send.setText(Amount +" "+currencyType.toUpperCase() +"="+df.format(totalAmoumt)+" IMT-Utility"  );
                        }

                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Enter_TopUp_Amount.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==401){

                    Snacky.builder()
                            .setActivity(Enter_TopUp_Amount.this)
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
                Snacky.builder()
                        .setActivity(Enter_TopUp_Amount.this)
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


}