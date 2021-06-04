package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.crypto.croytowallet.R;

public class Restore_wallet extends AppCompatActivity {
Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_wallet);
        btn_next=findViewById(R.id.next_btn);

    }
}