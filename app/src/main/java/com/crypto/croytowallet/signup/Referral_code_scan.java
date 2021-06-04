package com.crypto.croytowallet.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.crypto.croytowallet.R;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Referral_code_scan extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView scannView;
    TextView back;
    SharedPreferences sharedPreferences;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_code_scan);
        scannView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this,scannView);

        back=findViewById(R.id.back1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Referral_code_scan.this, Referral_code.class));
                finish();
            }
        });


    sharedPreferences=getSharedPreferences("referral_scan",Context.MODE_PRIVATE);

        codeScanner.setDecodeCallback(new DecodeCallback() {
        @Override
        public void onDecoded(@NonNull final Result result) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    code= result.getText();

                     SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("scan_code",code);
                        editor.commit();

                    startActivity(new Intent(Referral_code_scan.this, Referral_code.class));
                    finish();
                }
            });

        }
    });


        scannView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            codeScanner.startPreview();
        }
    });

}

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();

    }

    public void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(Referral_code_scan.this, "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }

}