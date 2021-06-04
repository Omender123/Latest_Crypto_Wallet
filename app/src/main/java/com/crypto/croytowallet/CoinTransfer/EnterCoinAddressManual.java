package com.crypto.croytowallet.CoinTransfer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.crypto.croytowallet.Activity.ManualEnterUserName;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

import de.mateware.snacky.Snacky;

public class EnterCoinAddressManual extends AppCompatActivity {
    ImageView imageView;
    EditText ed_address;
    String address;
    CardView comfirm;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_coin_address_manual);
        imageView =findViewById(R.id.back);
        ed_address = findViewById(R.id.ed_username1);

        comfirm  = findViewById(R.id.confirm);

        sharedPreferences=getSharedPreferences("coinScan", Context.MODE_PRIVATE);

        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address =ed_address.getText().toString().trim();
                //     userId =ed_userid.getText().toString().trim();

                if (address.isEmpty()){
                    ed_address.requestFocus();
                    Snackbar warningSnackBar = Snacky.builder()
                            .setActivity(EnterCoinAddressManual.this)
                            .setText(" Please Enter Coin Address ")
                            .setTextColor(getResources().getColor(R.color.white))
                            .setDuration(Snacky.LENGTH_SHORT)
                            .warning();
                    warningSnackBar.show();
                }else{
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("address",address);
                    editor.commit();
                    Intent intent =  new Intent(EnterCoinAddressManual.this, Pay_Coin.class);
                    //  intent.putExtra("position",position);
                    //intent.putExtra("result",s);
                    startActivity(intent);


                }
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