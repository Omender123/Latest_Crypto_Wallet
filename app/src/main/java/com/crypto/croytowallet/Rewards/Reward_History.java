package com.crypto.croytowallet.Rewards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Extra_Class.ApiResponse.RewardHistoryResponse;
import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.Transaction_HistoryModel;

public class Reward_History extends AppCompatActivity {
    TextView date,time,amount,download,share,trans_id,btncopy,receiverName,txt_send;
    Button showBtn,hideBtn;
    String date1,time1,Rwards,id,Airdrop_Amount;
    RewardHistoryResponse.Result rewardHistoryResponse;
    CardView card2,card3,card_rewards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward__history);

        rewardHistoryResponse =(RewardHistoryResponse.Result) getIntent().getSerializableExtra("result");
        date  =findViewById(R.id.transaction_date);
        time  =findViewById(R.id.transaction_Time);
        amount  =findViewById(R.id.amount);
        download  =findViewById(R.id.download);
        share  =findViewById(R.id.history_share);
        trans_id  =findViewById(R.id.trans_id);
        btncopy  =findViewById(R.id.btn_copy);
        receiverName  =findViewById(R.id.receiver_name);
        txt_send =findViewById(R.id.paidto);
        showBtn  =findViewById(R.id.show_btn);
        hideBtn  =findViewById(R.id.hide_btn);
        card2  = findViewById(R.id.card2);
        card3  = findViewById(R.id.card3);

        trans_id.setText(rewardHistoryResponse.getId());

        date.setText(AppUtils.getDate(rewardHistoryResponse.getCreatedAt()));
        String[] s= rewardHistoryResponse.getCreatedAt().split("T");
        time1 = s[1];
        time.setText(time1);


        if(rewardHistoryResponse.getEarnedReward().contains("-")){
            amount.setText(rewardHistoryResponse.getEarnedReward());
            txt_send.setText("Received IMT-Utility ");
        }else{
            amount.setText("+"+rewardHistoryResponse.getEarnedReward());
            txt_send.setText("Send IMT-Utility ");
        }
        receiverName.setText(rewardHistoryResponse.getAmount());
       // amount.setText();




        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    card2.setVisibility(View.VISIBLE);
                    card3.setVisibility(View.VISIBLE);
                    showBtn.setVisibility(View.GONE);
                    hideBtn.setVisibility(View.VISIBLE);



            }
        });

        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    card2.setVisibility(View.GONE);
                    card3.setVisibility(View.GONE);
                    showBtn.setVisibility(View.VISIBLE);
                    hideBtn.setVisibility(View.GONE);

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


    }

    public void onBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onSaveInstanceState(new Bundle());
    }
}