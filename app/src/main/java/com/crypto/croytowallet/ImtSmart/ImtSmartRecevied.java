package com.crypto.croytowallet.ImtSmart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ImtSmartRecevied extends AppCompatActivity {
    TextView barcodeAddress,toolbar_title;
    ImageView qrImage,imageView;
    CardView barCodeshare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imt_smart_recevied);
        imageView =findViewById(R.id.back);
        barcodeAddress=findViewById(R.id.barCodeAddress);
        qrImage = findViewById(R.id.qrPlaceHolder);
        barCodeshare=findViewById(R.id.barCodeshare);
        toolbar_title=findViewById(R.id.toolbar_title);
        back();

        UserData userData= SharedPrefManager.getInstance(this).getUser();

        String id0=userData.getETH();
        barcodeAddress.setText(id0);
        toolbar_title.setText("Receive ImSmart");
        QRGEncoder qrgEncoder0 = new QRGEncoder(id0,null, QRGContents.Type.TEXT,500);
        try {
            Bitmap qrBits = qrgEncoder0.encodeAsBitmap();
            qrImage.setImageBitmap(qrBits);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        barcodeAddress.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(id0);
                Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();

            }
        });

        barCodeshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, id0);
                startActivity(Intent.createChooser(i, "Share With"));
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(getApplicationContext(), Graph_layout.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
       */ onSaveInstanceState(new Bundle());


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