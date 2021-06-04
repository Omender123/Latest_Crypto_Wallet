package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.Rewards.All_Product;

public class Categories_Adapter extends RecyclerView.Adapter<Categories_Adapter.MyViewHolder> {
    String[] name;
    Context context;

    public Categories_Adapter(String[] name, Context context) {
        this.name = name;
        this.context = context;

    }

    public Categories_Adapter() {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_categories_layout, parent, false);
        return new Categories_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //  holder.product_price.setText(name[position]);

        holder.cate_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity( new Intent(context, All_Product.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cate_Name, cate__total,cate_more;
        ImageView cate_Image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cate_Name = itemView.findViewById(R.id.cate_name);
            cate__total = itemView.findViewById(R.id.cate_total);
            cate_more = itemView.findViewById(R.id.cate_more);
            cate_Image = itemView.findViewById(R.id.cate_image);



        }
    }
}
