package com.crypto.croytowallet.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.login.Login;

import de.mateware.snacky.Snacky;

public class Referral_code extends AppCompatActivity {
    Button referral_ready;
    ConstraintLayout relativeLayout;
    Animation fade_in;
    EditText enterReferral_code;
    String getreferralCode;
    ImageView scan;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_code);
        referral_ready=findViewById(R.id.referral_ready);
        relativeLayout=findViewById(R.id.referral_layout);
        enterReferral_code=findViewById(R.id.referral_code);
        scan = findViewById(R.id.scan);
        // animation

        fade_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
       // relativeLayout.startAnimation(fade_in);



        sharedPreferences=getSharedPreferences("referral_scan", Context.MODE_PRIVATE);

        String code = sharedPreferences.getString("scan_code","");
        enterReferral_code.setText(code);

        referral_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 getreferralCode = enterReferral_code.getText().toString().trim();

                 if (getreferralCode.isEmpty()){
                     Snacky.builder()
                             .setActivity(Referral_code.this)
                             .setText("Please enter referral code")
                             .setDuration(Snacky.LENGTH_SHORT)
                             .setActionText(android.R.string.ok)
                             .warning()
                             .show();

                 }else{
                   Intent intent = new Intent(getApplicationContext(), SignUp.class);
                    intent.putExtra("referral_code", getreferralCode);
                    startActivity(intent);

                sharedPreferences.edit().clear().commit();
                 }
            }

        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Referral_code.this,Referral_code_scan.class));
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       onSaveInstanceState(new Bundle());
    }

    public void referral_login(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void dont_Referral(View view) {
        Intent intent = new Intent(Referral_code.this, SignUp.class);
        intent.putExtra("referral_code", "");
        startActivity(intent);
    }
}