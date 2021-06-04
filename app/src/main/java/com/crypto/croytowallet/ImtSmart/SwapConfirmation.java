package com.crypto.croytowallet.ImtSmart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.CoinTransfer.Payout_verification;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.Model.SwapModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SwapSharedPrefernce;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwapConfirmation extends AppCompatActivity {
    Button confirm;
    SwapModel swapModel;
    UserData userData;
    String sendData,receivedData,coinPrice,currencyType,currencySymbols,enterAmount,coinAmount,Token,ethAddress,type,coinTypes;
    int value;
    TextView coinValue,showCoinAmount,showEnteredAmount;
    String balance,total;
    Double userBalance,TotalAmount;

    TextView textView;
    RelativeLayout relat;
    Animation slide_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_confirmation);
        confirm = findViewById(R.id.confirm_btn);
        coinValue = findViewById(R.id.price_show);
        showCoinAmount = findViewById(R.id.cryto_amount);
        showEnteredAmount = findViewById(R.id.amount);

        textView=findViewById(R.id.textView);
        relat=findViewById(R.id.relat);
        //Animation
        slide_right = AnimationUtils.loadAnimation(SwapConfirmation.this,R.anim.slide_in_right);
        confirm.startAnimation(slide_right);
        relat.startAnimation(slide_right);
        textView.startAnimation(slide_right);

        swapModel = SwapSharedPrefernce.getInstance(getApplicationContext()).getSwapData();
        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        Token = userData.getToken();
        ethAddress = userData.getETH();

        sendData = swapModel.getSendData();
        receivedData = swapModel.getReceivedData();
        coinPrice = swapModel.getCoinPrice();
        currencyType = swapModel.getCurrencyType();
        currencySymbols = swapModel.getCurrencySymbol();
        enterAmount = swapModel.getEnterAmount();
        coinAmount = swapModel.getCoinAmount();
        value = swapModel.getValue();
        balance = swapModel.getUserBalance();
        total = swapModel.getEnterAmount();
        type = swapModel.getType();
        coinTypes = swapModel.getCoinType();



        if(sendData.equals("airdrop")){
            showCoinAmount.setText(coinAmount+currencySymbols);
            coinValue.setText("1 IMT-U = "+coinPrice+" "+currencyType.toUpperCase());
            showEnteredAmount.setText(enterAmount+"IMT-U");
        }else{
            showCoinAmount.setText(coinAmount+" "+currencySymbols);
            coinValue.setText("1 "+sendData.toUpperCase()+" = "+coinPrice+" "+currencyType.toUpperCase());
            showEnteredAmount.setText(enterAmount+" "+sendData.toUpperCase());
        }


        try {
            userBalance = Double.parseDouble(balance);
            TotalAmount = Double.parseDouble(total);


        }catch (Exception e){


        }


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           try{
               if(TotalAmount==null || balance==null){
                   Snacky.builder()
                           .setActivity(SwapConfirmation.this)
                           .setText(" User Balance Not Found ")
                           .setDuration(Snacky.LENGTH_SHORT)
                           .setActionText(android.R.string.ok)
                           .error()
                           .show();
               }else if(TotalAmount>=userBalance){
                   Snacky.builder()
                           .setActivity(SwapConfirmation.this)
                           .setText(" Inefficient balance")
                           .setDuration(Snacky.LENGTH_SHORT)
                           .setActionText(android.R.string.ok)
                           .error()
                           .show();
               }else if (type.equalsIgnoreCase("Swap")){
                  startActivity(new Intent(getApplicationContext(),SwapEnterPin.class));
               //  Toast.makeText(SwapConfirmation.this, ""+coinTypes, Toast.LENGTH_SHORT).show();
               }else {
                   startActivity(new Intent(getApplicationContext(), Payout_verification.class));
                   // Toast.makeText(SwapConfirmation.this, ""+type, Toast.LENGTH_SHORT).show();
               }
           }catch (Exception e){
               Snacky.builder()
                       .setActivity(SwapConfirmation.this)
                       .setText(" User Balance Not Found ")
                       .setDuration(Snacky.LENGTH_SHORT)
                       .setActionText(android.R.string.ok)
                       .error()
                       .show();
           }



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




}