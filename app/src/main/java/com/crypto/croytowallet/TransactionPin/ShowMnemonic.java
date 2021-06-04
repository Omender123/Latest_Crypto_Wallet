package com.crypto.croytowallet.TransactionPin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SignUpData;
import com.crypto.croytowallet.SharedPrefernce.SignUpRefernace;

import de.mateware.snacky.Snacky;

public class ShowMnemonic extends AppCompatActivity {

Button copy,next;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_mnemonic);
        //textView=findViewById(R.id.mnemonic);
        copy=findViewById(R.id.copy);
        next =findViewById(R.id.referral_ready4);
        final TextView txtcopypaste = findViewById(R.id.mnemonic);
        // my textview

        SignUpData user = SignUpRefernace.getInstance(this).getUser();
        String text= user.getMnemonic();
        txtcopypaste.setText(text);

        copy.setOnClickListener(new View.OnClickListener() { // set onclick listener to my textview
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(txtcopypaste.getText().toString());
                //Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();
                Snacky.builder()
                        .setActivity(ShowMnemonic.this)
                        .setText("Copied")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setDuration(Snacky.LENGTH_SHORT)
                        .success()
                        .show();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             Intent intent=new Intent(ShowMnemonic.this,EnterConfirmMnemonic.class);
             startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

}