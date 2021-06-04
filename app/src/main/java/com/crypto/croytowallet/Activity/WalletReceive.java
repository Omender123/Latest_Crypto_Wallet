package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class WalletReceive extends AppCompatActivity {
TextView barcodeAddress,yurwall,tabwall;
    ImageView qrImage,imageView;
    CardView barCodeshare,baradd,recbar;
    Animation slide_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_receive);
        imageView =findViewById(R.id.back);
        back();

        barcodeAddress=findViewById(R.id.barCodeAddress);
        qrImage = findViewById(R.id.qrPlaceHolder);
        barCodeshare=findViewById(R.id.barCodeshare);
        baradd=findViewById(R.id.baradd);
        recbar=findViewById(R.id.recbar);
        yurwall=findViewById(R.id.yurwall);
        tabwall=findViewById(R.id.tbwall);

        slide_right = AnimationUtils.loadAnimation(WalletReceive.this,R.anim.slide_in_right);
        //set Animatoin
        baradd.startAnimation(slide_right);

        recbar.startAnimation(slide_right);
        barCodeshare.startAnimation(slide_right);
        yurwall.startAnimation(slide_right);
        tabwall.startAnimation(slide_right);




        UserData userData= SharedPrefManager.getInstance(this).getUser();

      //  String barcodeText=barcodeAddress.getText().toString();

        String username=userData.getUsername();
        String id=userData.getId();
        barcodeAddress.setText(username);


        barcodeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(username);
                Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();

            }
        });

        QRGEncoder qrgEncoder = new QRGEncoder(username,null, QRGContents.Type.TEXT,500);
        try {
            Bitmap qrBits = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(qrBits);
        } catch (WriterException e) {
            e.printStackTrace();
        }


        barCodeshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, userData.getUsername());
                startActivity(Intent.createChooser(i, "Share With"));
            }
        });


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