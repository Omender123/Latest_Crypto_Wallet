package com.crypto.croytowallet.Rewards;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.TopUp.MyBottomSheetDialogFragment;

import de.mateware.snacky.Snacky;

public class Redeem extends AppCompatActivity implements View.OnClickListener {
TextView redeem_now;
EditText imx_point;
TransactionPinBottomSheet transactionPinBottomSheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        redeem_now = findViewById(R.id.redeem_now);
        imx_point = findViewById(R.id.imx_point);

        transactionPinBottomSheet = TransactionPinBottomSheet.newInstance("Transaction Pin BottomSheet");

        redeem_now.setOnClickListener(this);
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    public void Term_Condition(View view) {
        BottomSheetTC bottomSheet = new BottomSheetTC();
        bottomSheet.show(getSupportFragmentManager(),bottomSheet.getTag());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.redeem_now:
                String amount = imx_point.getText().toString().trim();

                if (amount.isEmpty()){
                    Snacky.builder()
                            .setActivity(Redeem.this)
                            .setText("Please Enter amount !!!")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .warning()
                            .show();
                }else {
                    MyPreferences.getInstance(getApplicationContext()).putString(PrefConf.REDEEM_AMOUNT,amount);
                    transactionPinBottomSheet.show(getSupportFragmentManager(), transactionPinBottomSheet.getTag());
                }
                break;
        }
    }
}