package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


import com.crypto.croytowallet.R;
import com.crypto.croytowallet.login.Login;

import de.mateware.snacky.Snacky;

public class Contact_details extends AppCompatActivity implements View.OnClickListener {
ImageView back_btn;
CardView card_email,card_phone_no,card_website;
    Animation enterright;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        back_btn =findViewById(R.id.back);
        card_email= findViewById(R.id.card_email);
        card_phone_no = findViewById(R.id.card_phone_no_);
        card_website = findViewById(R.id.card_webSite);

        card_email.setOnClickListener(this);
        card_phone_no.setOnClickListener(this);
        card_website.setOnClickListener(this);

        enterright = AnimationUtils.loadAnimation(Contact_details.this, R.anim.slide_in_left);
        card_email.setAnimation(enterright);
        card_phone_no.setAnimation(enterright);
        card_website.setAnimation(enterright);

        back();
    }
    public void back(){
      back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent intent = new Intent(Contact_details.this, Support.class);
               // startActivity(intent);

                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // Intent intent = new Intent(Contact_details.this, Support.class);
       // startActivity(intent);

        onSaveInstanceState(new Bundle());
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.card_email:
                Intent intent1 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","imxchange.imx@gmail.com", null));
              //  startActivity(Intent.createChooser(intent1, "Choose an Email client :"));

                try {
                    startActivity(Intent.createChooser(intent1, "Choose an Email client :"));
                   // finish();
                    Log.i("Finished sending email...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Snacky.builder()
                            .setActivity(Contact_details.this)
                            .setText("There is no email client installed.")
                            .setTextColor(getResources().getColor(R.color.white))
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                 //   Toast.makeText(Contact_details.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.card_phone_no_:
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+66646981930"));
                startActivity(intent);
                break;

            case R.id.card_webSite:
                String url = "https://www.imx.global";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

        }
    }
}