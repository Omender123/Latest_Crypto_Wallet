package com.crypto.croytowallet.TopUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Extra_Class.ApiResponse.RewardHistoryResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TopUp_HistoryResponse;
import com.crypto.croytowallet.R;

public class Full_TopUp_History extends AppCompatActivity {
TextView text_utility,trans_id,text_date,paid_amount,text_status,text_point;
ImageView image_status;
TopUp_HistoryResponse topUp_historyResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full__top_up__history);

        topUp_historyResponse =(TopUp_HistoryResponse) getIntent().getSerializableExtra("topUp_historyResponse");

        text_utility = findViewById(R.id.text_utility);
        trans_id = findViewById(R.id.trans_id);
        text_date = findViewById(R.id.text_date);
        paid_amount = findViewById(R.id.paid_amount);
        text_status = findViewById(R.id.text_status);
        text_point = findViewById(R.id.text_point);
        image_status = findViewById(R.id.image_status);

        text_utility.setText(topUp_historyResponse.getUtility()+" UT");
        trans_id.setText(topUp_historyResponse.getTransactionId());
        text_date.setText(topUp_historyResponse.getCreatedAt());
        paid_amount.setText(topUp_historyResponse.getAmount());
        text_status.setText(topUp_historyResponse.getStatus());
        text_point.setText(topUp_historyResponse.getReward());

        if (topUp_historyResponse.getStatus().equalsIgnoreCase("confirmed")){
            image_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_circle_24));
        }else{
            image_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_panding_24));
        }


    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    public void btn_copy(View view) {
        ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(trans_id.getText().toString());
        Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();
    }
}