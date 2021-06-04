package com.crypto.croytowallet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.crypto.croytowallet.Chat.ChatOptions;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;

public class Support extends AppCompatActivity {
ImageView imageView;
ActionBar actionBar;
    CardView Mobilesupport, knowlegeBase,Addticket,contactdetails;
    RelativeLayout knowledge_base,Contact_detail,add_ticket,Mobile_support;
    LinearLayout support;
    Animation slide_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        imageView =findViewById(R.id.back);
        support=findViewById(R.id.support2);
        knowledge_base=findViewById(R.id.knowlege_Base);
        Contact_detail=findViewById(R.id.contact_details);
        add_ticket=findViewById(R.id.Add_ticket_layout);
        Mobile_support=findViewById(R.id.Mobile_support);


        //cardview id
        Mobilesupport=findViewById(R.id.Mobilesupport);
        knowlegeBase=findViewById(R.id.knowlegeBase);
        Addticket=findViewById(R.id.Addticket);
        contactdetails=findViewById(R.id.contactdetails);


        slide_up = AnimationUtils.loadAnimation(Support.this, R.anim.silde_up);
        knowlegeBase.setAnimation(slide_up);
        contactdetails.setAnimation(slide_up);
        Addticket.setAnimation(slide_up);
        Mobilesupport.setAnimation(slide_up);




        Mobile_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Support.this, ChatOptions.class);
                startActivity(i);
            }
        });


        add_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Support.this,Ticket.class);
                startActivity(i);
            }
        });
        Contact_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Support.this,Contact_details.class);
                startActivity(i);
            }
        });

        knowledge_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Support.this,Knowlege_Base.class));
            }
        });

        back();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(Support.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void actionBarSetup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

            //   actionBar.setTitle("Price Action Strategy ");

        }
    }
    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(Support.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
                // onSaveInstanceState(new Bundle());

                onBackPressed();
            }
        });

    }


    public void privacy_policy(View view) {
        String url = "https://www.imx.global/terms-conditions";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }
}