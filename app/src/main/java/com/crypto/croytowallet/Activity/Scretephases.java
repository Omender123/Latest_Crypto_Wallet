package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;

import de.mateware.snacky.Snacky;

public class Scretephases extends AppCompatActivity {
    ImageView imageView;
    TextView text1,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,text12;
    String txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10,txt11,txt12;
    Button Copy;
    UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scretephases);

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        imageView = findViewById(R.id.back);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);
        text7 = findViewById(R.id.text7);
        text8 = findViewById(R.id.text8);
        text9 = findViewById(R.id.text9);
        text10 = findViewById(R.id.text10);
        text11 = findViewById(R.id.text11);
        text12 = findViewById(R.id.text12);



        String mnemonic = userData.getMnemonic();

        String[] s= mnemonic.split(" ");

        txt1 =s[0];
        txt2 =s[1];
        txt3 =s[2];
        txt4 =s[3];
        txt5 =s[4];
        txt6 =s[5];
        txt7 =s[6];
        txt8 =s[7];
        txt9 =s[8];
        txt10 =s[9];
        txt11 =s[10];
        txt12 =s[11];


        text1.setText("1. "+txt1);
        text2.setText("2. "+txt2);
        text3.setText("3. "+txt3);
        text4.setText("4. "+txt4);
        text5.setText("5. "+txt5);
        text6.setText("6. "+txt6);
        text7.setText("7. "+txt7);
        text8.setText("8. "+txt8);
        text9.setText("9. "+txt9);
        text10.setText("10. "+txt10);
        text11.setText("11. "+txt11);
        text12.setText("12. "+txt12);

        Copy = findViewById(R.id.button_secrete);


        Copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(mnemonic);
                Snacky.builder()
                        .setActivity(Scretephases.this)
                        .setText("Copied")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setDuration(Snacky.LENGTH_SHORT)
                        .success()
                        .show();
            }
        });
        back();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Scretephases.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void back() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Scretephases.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}