package com.crypto.croytowallet.Rewards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.crypto.croytowallet.Adapter.Product_adapter;
import com.crypto.croytowallet.Extra_Class.DrawableTextView;
import com.crypto.croytowallet.R;

public class All_Product extends AppCompatActivity implements DrawableTextView.DrawableClickListenerr  {
    RecyclerView recyclerView;
    Product_adapter product_adapter;
    DrawableTextView toolbar;
    String [] price = {"50$","50$","50$","50$","50$","50$"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__product);
        recyclerView = findViewById(R.id.Product_recyclerView);
        toolbar = findViewById(R.id.toolbar_title);

        toolbar.setDrawableClickListenerr(this);

        getAllprouct();
    }
    private void getAllprouct() {
        product_adapter = new Product_adapter(price,All_Product.this);
        RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(All_Product.this,2,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(product_adapter);
    }
    @Override
    public void onClick(DrawableTextView.DrawableClickListenerr.DrawablePosition target) {
        switch (target) {
            case LEFT:
                onBackPressed();
                break;

            case RIGHT:
                Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}