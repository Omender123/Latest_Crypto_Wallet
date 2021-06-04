package com.crypto.croytowallet.TopUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedRequestResponse;

import de.mateware.snacky.Snacky;

public class TopUp_Acknowlegement extends AppCompatActivity {
    TextView showCoinAmount,showEnteredAmount,amount_in_crypto,amount_in_Currency,trans_hash,trans_status,btncopy,reciverName;
    String TransactionId,requestId,EnterAmount,IMT_Amout,Status,CurrencyType;
    Button okay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up__acknowlegement);
        okay = findViewById(R.id.okay_btn);
        showCoinAmount = findViewById(R.id.cryto_amount);
        showEnteredAmount = findViewById(R.id.amount);
        amount_in_crypto = findViewById(R.id.amount_in_crypto);
        amount_in_Currency = findViewById(R.id.amount_in_currency);
        trans_hash = findViewById(R.id.trans_hash_id);
        trans_status = findViewById(R.id.status);
        btncopy  =findViewById(R.id.btn_copy);
        reciverName = findViewById(R.id.recriver_address);

        TransactionId = SharedRequestResponse.getInstans(getApplicationContext()).getTransferId();
        requestId = SharedRequestResponse.getInstans(getApplicationContext()).getRequest_ID();
        EnterAmount = SharedRequestResponse.getInstans(getApplicationContext()).getEnter_Amount();
        IMT_Amout = SharedRequestResponse.getInstans(getApplicationContext()).getIMT_U_Amount();
        Status = SharedRequestResponse.getInstans(getApplicationContext()).getStatus();
        CurrencyType = SharedRequestResponse.getInstans(getApplicationContext()).getCurrency_type();

        showCoinAmount.setText(IMT_Amout+" IMT-Utility");
        showEnteredAmount.setText(EnterAmount+" "+CurrencyType);
        amount_in_crypto.setText(IMT_Amout+" IMT-Utility");
        amount_in_Currency.setText(EnterAmount+" "+CurrencyType);
        trans_hash.setText(requestId);
        reciverName.setText(TransactionId);
        trans_status.setText(Status);

        btncopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(trans_hash.getText().toString());
                 Snacky.builder()
                        .setActivity(TopUp_Acknowlegement.this)
                        .setText("Copied")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .success()
                        .show();
            }
        });

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TopUp_Acknowlegement.this, MainActivity.class));
                SharedRequestResponse.getInstans(getApplicationContext()).RemoveData();
            }
        });
    }

    public void back(View view) {
        startActivity(new Intent(TopUp_Acknowlegement.this, MainActivity.class));
        SharedRequestResponse.getInstans(getApplicationContext()).RemoveData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TopUp_Acknowlegement.this, MainActivity.class));
        SharedRequestResponse.getInstans(getApplicationContext()).RemoveData();
    }
}