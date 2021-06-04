package com.crypto.croytowallet.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.crypto.croytowallet.Extra_Class.ApiResponse.PearToPearResponse;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SharedRequestResponse;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Enter_transaction_pin extends AppCompatActivity {
PinView pinView;
CardView pay_money;
    ImageView imageView;
    EditText enter_token;
    KProgressHUD progressDialog;
    String url="http://13.233.136.56:8080/api/transaction/peerToPeer";
    UserData userData;
    SharedPreferences preferences;
    TextView textView;
    Animation slide_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_transaction_pin);
        imageView =findViewById(R.id.back);
        pinView = findViewById(R.id.enter_pin);
        pay_money = findViewById(R.id.pay_money);
        textView = findViewById(R.id. textview);

        slide_right = AnimationUtils.loadAnimation(Enter_transaction_pin.this,R.anim.slide_in_right);

        textView.setAnimation(slide_right);
        pinView.setAnimation(slide_right);
        pay_money.setAnimation(slide_right);


        userData= SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String trans =userData.getTransaction_Pin();

     //   Toast.makeText(this, ""+trans, Toast.LENGTH_SHORT).show();
        preferences=getApplicationContext().getSharedPreferences("walletScan", Context.MODE_PRIVATE);



        pay_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enterPin=pinView.getText().toString();

                if (enterPin.isEmpty()){
                    pinView.setError("Please enter transaction pin");
                    pinView.requestFocus();
                }else if (enterPin.equals(trans)){
                    pinView.setLineColor(getResources().getColor(R.color.green));
                    done();
                }else{
                     new Handler().postDelayed(new Runnable() {
                         @Override
                    public void run() {
                            // This method will be executed once the timer is over
                             pinView.setLineColor(getResources().getColor(R.color.light_gray));
                                         }
                                      }, 200);
                    pinView.setLineColor(getResources().getColor(R.color.red));
                }
               // Toast.makeText(Enter_transaction_pin.this, ""+enterPin, Toast.LENGTH_SHORT).show();


            }
        });


    //    Toast.makeText(this, ""+Amount, Toast.LENGTH_SHORT).show();

        back();
    }


    public void done(){

        progressDialog = KProgressHUD.create(Enter_transaction_pin.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        String userAddressID=userData.getId();
        Bundle bundle = getIntent().getExtras();
        String  Amount = bundle.getString("amount12");

        String enterPin=pinView.getText().toString();
        String username = preferences.getString("username","");
        String token=userData.getToken();





        Call<PearToPearResponse> call = RetrofitClient.getInstance().getApi().P2P(token,Amount,enterPin,userAddressID,username);

        call.enqueue(new Callback<PearToPearResponse>() {
            @Override
            public void onResponse(Call<PearToPearResponse> call, Response<PearToPearResponse> response) {
                String s =null;
                hidepDialog();
                if (response.code()==200){
                    pinView.setLineColor(getResources().getColor(R.color.light_gray));

                    PearToPearResponse pearToPearResponse = response.body();

                    SharedRequestResponse.getInstans(getApplicationContext()).SetData(
                            pearToPearResponse.getResult().getId(),
                            pearToPearResponse.getResult().getReceiverName(),
                            pearToPearResponse.getResult().getSenderName(),
                            pearToPearResponse.getResult().getAmount(),
                            pearToPearResponse.getResult().getStatus(),
                            "",
                            pearToPearResponse.getResult().getEarnedReward());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getApplicationContext(), Complate_payment.class));
                        }
                    },500);

                 }else if (response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Enter_transaction_pin.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();
                        pinView.setLineColor(getResources().getColor(R.color.light_gray));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code()==401){
                    Snacky.builder()
                            .setActivity(Enter_transaction_pin.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                    pinView.setLineColor(getResources().getColor(R.color.light_gray));
                }


            }

            @Override
            public void onFailure(Call<PearToPearResponse> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(Enter_transaction_pin.this)
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



    }
