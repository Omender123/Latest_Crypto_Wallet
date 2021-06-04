package com.crypto.croytowallet.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.crypto.croytowallet.Model.NoticationModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.NotificationSharedPreference;
import com.crypto.croytowallet.SharedPrefernce.OfferSharedPrefManager;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.squareup.picasso.Picasso;

public class Full_Notification extends AppCompatActivity {
ImageView imageView;
TextView details;
NoticationModel noticationModel;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_full__notification);
        imageView = findViewById(R.id.image_view);
        details = findViewById(R.id.details);

        noticationModel= NotificationSharedPreference.getInstance(getApplicationContext()).GetNotificationData();

        Picasso.get().load(URLs.URL_Image+noticationModel.getImages()).into(imageView);
        details.setText(noticationModel.getDetails());


    }
}