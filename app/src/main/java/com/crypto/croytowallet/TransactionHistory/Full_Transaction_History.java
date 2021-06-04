package com.crypto.croytowallet.TransactionHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.TransactionHistorySharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.Transaction_HistoryModel;

public class Full_Transaction_History extends AppCompatActivity {
TextView date,time,amount,download,share,trans_id,btncopy,receiverName,text_point;
Button showBtn,hideBtn;
String date1,time1,amount1,id,receiverName1,EarnsRewards,Type;
Transaction_HistoryModel transaction_historyModel;
ImageView imageView;
CardView card2,card3,card_rewards;
String back;
SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full__transaction__history);

        date  =findViewById(R.id.transaction_date);
        time  =findViewById(R.id.transaction_Time);
        amount  =findViewById(R.id.amount);
        download  =findViewById(R.id.download);
        share  =findViewById(R.id.history_share);
        trans_id  =findViewById(R.id.trans_id);
        btncopy  =findViewById(R.id.btn_copy);
        receiverName  =findViewById(R.id.receiver_name);
        showBtn  =findViewById(R.id.show_btn);
        hideBtn  =findViewById(R.id.hide_btn);
        imageView =findViewById(R.id.back);
        card2  = findViewById(R.id.card2);
        card3  = findViewById(R.id.card3);
        card_rewards = findViewById(R.id.card_rewards);
        text_point = findViewById(R.id.text_point);
        transaction_historyModel = TransactionHistorySharedPrefManager.getInstance(getApplicationContext()).getTransaction_History();


        sharedPreferences =getApplicationContext().getSharedPreferences("currency",0);

        String   CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");



        date1 = transaction_historyModel.getTime();
        amount1 = transaction_historyModel.getAmtOfCrypto();
        receiverName1 = transaction_historyModel.getReciverName();
        id = transaction_historyModel.getId();
        EarnsRewards = transaction_historyModel.getRewards();
        back = transaction_historyModel.getStatus();
        Type= transaction_historyModel.getType();

        date.setText(AppUtils.getDate(date1));
        String[] s= date1.split("T");
         time1 = s[1];
         time.setText(time1);
         amount.setText(amount1);
         trans_id.setText(id);
         receiverName.setText(receiverName1);

         showBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if (EarnsRewards!=null){
                     text_point.setText(EarnsRewards+" PT");
                     card2.setVisibility(View.VISIBLE);
                     card3.setVisibility(View.VISIBLE);
                     card_rewards.setVisibility(View.VISIBLE);
                     showBtn.setVisibility(View.GONE);
                     hideBtn.setVisibility(View.VISIBLE);
                 }else{
                     card2.setVisibility(View.VISIBLE);
                     card3.setVisibility(View.VISIBLE);
                    // card_rewards.setVisibility(View.GONE);
                     showBtn.setVisibility(View.GONE);
                     hideBtn.setVisibility(View.VISIBLE);

                 }


             }
         });

        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EarnsRewards!=null){
                    card2.setVisibility(View.GONE);
                    card3.setVisibility(View.GONE);
                    card_rewards.setVisibility(View.GONE);
                    showBtn.setVisibility(View.VISIBLE);
                    hideBtn.setVisibility(View.GONE);
                }else{
                    card2.setVisibility(View.GONE);
                    card3.setVisibility(View.GONE);
                    showBtn.setVisibility(View.VISIBLE);
                    hideBtn.setVisibility(View.GONE);

                }


            }
        });

         imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onBackPressed();

             }
         });


    btncopy.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(trans_id.getText().toString());
            Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();
        }
    });

    share.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           /* try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String sAux = "Hey,\n Its amazing install Crypto Wallet app and use this\n Referral code : "+code +"\n Download "+ getResources().getString(R.string.app_name) + "\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }*/
        }
    });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }
}