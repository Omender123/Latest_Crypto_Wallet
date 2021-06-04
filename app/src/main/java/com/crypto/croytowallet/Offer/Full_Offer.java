package com.crypto.croytowallet.Offer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Model.OfferModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.OfferSharedPrefManager;
import com.squareup.picasso.Picasso;

public class Full_Offer extends AppCompatActivity {
OfferModel offerModel;
ImageView imageView;
TextView offername,shortDes,long_Des,title;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full__offer);

        imageView = findViewById(R.id.image_view);
        offername = findViewById(R.id.offer_name);
        shortDes = findViewById(R.id.shortDes);
        long_Des = findViewById(R.id.long_des);
        title = findViewById(R.id.offer_title);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        offerModel= OfferSharedPrefManager.getInstance(getApplicationContext()).GetOfferData();

        offername.setText(offerModel.getOffer_name());
        title.setText(offerModel.getOffer_tittle());
        shortDes.setText(offerModel.getShortDescription());
        long_Des.setText(offerModel.getLongDescription());

         if(offerModel.getImageUrl().equals("https://imsmart.s3.ap-south-1.amazonaws.com/logo.png")){
            imageView.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_artboard___5));
        }else {
            Picasso.get().load(offerModel.getImageUrl()).into(imageView);
        }

       // hideSystemUI();
    }
    public void hideSystemUI(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}