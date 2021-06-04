package com.crypto.croytowallet.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.TopUp.Top_up_Money;

public class Pay_money extends AppCompatActivity {
    ImageView imageView;
    CardView pay,cardView;
    EditText pay_enter_amount;
    TextView go_top_up,payUsername,payname,payEmail,paytransactionId;
    SharedPreferences preferences,sharedPreferences1,sharedPreferences;
    TextView textView;
    String imtPrice,currency2,CurrencySymbols;
    Double imtP;
    Animation slide_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_money);
        imageView =findViewById(R.id.back);
        pay_enter_amount=findViewById(R.id.pay_enter_amount);
        go_top_up=findViewById(R.id.go_top_up);
        pay=findViewById(R.id.pay);
        paytransactionId =findViewById(R.id.paytransactionId);
        textView  = findViewById(R.id.balance);
        payUsername=findViewById(R.id.payUsername);
        payname=findViewById(R.id.name);
        payEmail=findViewById(R.id.email);
        cardView=findViewById(R.id.cardView);

        slide_right = AnimationUtils.loadAnimation(Pay_money.this,R.anim.slide_in_right);
        payUsername.setAnimation(slide_right);
        paytransactionId.setAnimation(slide_right);
        pay.setAnimation(slide_right);
        cardView.setAnimation(slide_right);


        sharedPreferences1 = getApplicationContext().getSharedPreferences("imtInfo", Context.MODE_PRIVATE);
        preferences=getApplicationContext().getSharedPreferences("walletScan", Context.MODE_PRIVATE);
        sharedPreferences = getApplicationContext().getSharedPreferences("currency", 0);
        currency2 = sharedPreferences.getString("currency1", "usd");
        CurrencySymbols = sharedPreferences.getString("Currency_Symbols", "$");

        String username = preferences.getString("username","");
        String name = preferences.getString("name","");
        String email = preferences.getString("email","");


        imtPrice = sharedPreferences1.getString("imtPrices", "0.09");

        payUsername.setText(username);
        payname.setText(name);
        payEmail.setText(email);




        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enter_amount=pay_enter_amount.getText().toString().trim();

                if (enter_amount.isEmpty()){
                    pay_enter_amount.setError("Please enter Amount to Pay");
                    pay_enter_amount.requestFocus();
                }else{

                   Intent intent =new Intent(getApplicationContext(),Enter_transaction_pin.class);
                 //  Double amount = Double.valueOf(enter_amount);
                   intent.putExtra("amount12",enter_amount);
                   startActivity(intent);
                }

            }
        });
//add money
       pay_enter_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amount = s.toString();

                if (amount.isEmpty()){
                    textView.setText(CurrencySymbols+" 0 ");
                }else{
                  try {

                      Double a = Double.valueOf(s.toString());
                      imtP = Double.parseDouble(imtPrice);
                      double result = a*imtP;

                      textView.setText(CurrencySymbols +result );

                  }catch (Exception e){

                  }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        go_top_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Top_up_Money.class));

            }
        });

        back();
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