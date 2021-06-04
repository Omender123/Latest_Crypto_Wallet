package com.crypto.croytowallet.TopUp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.R;

import de.mateware.snacky.Snacky;

public class Top_up_Money extends AppCompatActivity implements View.OnClickListener {
CardView card_paypal,card_bank,card_qrCode,card_History;


      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up__money);
        card_paypal = findViewById(R.id.card_paypal);
          card_bank = findViewById(R.id.card_bank);
          card_qrCode = findViewById(R.id.card_QrCode);
          card_History = findViewById(R.id.card_History);
          card_paypal.setOnClickListener(this);
          card_bank.setOnClickListener(this);
          card_qrCode.setOnClickListener(this);
          card_History.setOnClickListener(this);


      }
    public void back(View view) {
          onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    @Override
    public void onClick(View v) {

          switch (v.getId()){

              case R.id.card_bank:
                  startActivity(new Intent(Top_up_Money.this,Enter_TopUp_Amount.class)/*.putExtra("options","bank")*/);
                  MyPreferences.getInstance(getApplicationContext()).putString(PrefConf.TOP_UP_TYPE,"bank");
                  break;

              case R.id.card_QrCode:
                  startActivity(new Intent(Top_up_Money.this,Enter_TopUp_Amount.class)/*.putExtra("options","qrCode")*/);
                  MyPreferences.getInstance(getApplicationContext()).putString(PrefConf.TOP_UP_TYPE,"qrCode");
                  break;

              case R.id.card_paypal:

                  Snacky.builder()
                          .setActivity(Top_up_Money.this)
                          .setText("Coming Soon")
                          .setTextColor(getResources().getColor(R.color.white))
                          .setDuration(Snacky.LENGTH_SHORT)
                          .setActionText(android.R.string.ok)
                          .success()
                          .show();

                  break;
              case R.id.card_History:
                  startActivity(new Intent(Top_up_Money.this,TopUp_History.class));
                  break;
          }


    }


}