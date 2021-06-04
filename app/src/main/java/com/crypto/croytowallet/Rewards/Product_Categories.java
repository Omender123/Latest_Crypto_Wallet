package com.crypto.croytowallet.Rewards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.crypto.croytowallet.Adapter.Categories_Adapter;
import com.crypto.croytowallet.Extra_Class.DrawableTextView;
import com.crypto.croytowallet.R;

public class Product_Categories extends AppCompatActivity implements DrawableTextView.DrawableClickListenerr {
    DrawableTextView toolbar;
    RecyclerView cate_recyclerView;
    String [] price ={"50+","50+","50+","50+","50+","50+"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__categories);
        toolbar = findViewById(R.id.toolbar_title);
        cate_recyclerView = findViewById(R.id.Categories_recyclerView);

        toolbar.setDrawableClickListenerr(this);
        getAllCategories();

    }


    private void getAllCategories() {
        Categories_Adapter categories_adapter = new Categories_Adapter(price,Product_Categories.this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(Product_Categories.this, LinearLayoutManager.VERTICAL, false);
        cate_recyclerView.setLayoutManager(mLayoutManager1);
        cate_recyclerView.setItemAnimator(new DefaultItemAnimator());
        cate_recyclerView.setAdapter(categories_adapter);


}
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
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
