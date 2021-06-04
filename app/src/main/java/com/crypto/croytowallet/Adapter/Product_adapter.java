package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.R;

public class Product_adapter extends RecyclerView.Adapter<Product_adapter.MyViewHolder> {
String [] name;
Context context;

    public Product_adapter(String[] name,Context context) {
        this.name = name;
        this.context=context;
    }

    public Product_adapter() {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

          //  holder.product_price.setText(name[position]);

    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView productName,product_point, product_price;
        ImageView productImage;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.text_product_name);
            product_point = itemView.findViewById(R.id.text_pt);
            product_price = itemView.findViewById(R.id.text_price);
            productImage = itemView.findViewById(R.id.product_image);
            checkBox = itemView.findViewById(R.id.checkbox_cart);




        }
    }
}
