package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.Model.NoticationModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.myViewHolder>{
    ArrayList<NoticationModel> noticationModels;
    Context context;
    private HistoryClickLister historyClickLister;

    public NotificationAdapter(ArrayList<NoticationModel> noticationModels, Context context, HistoryClickLister historyClickLister) {
        this.noticationModels = noticationModels;
        this.context = context;
        this.historyClickLister = historyClickLister;
    }

    public NotificationAdapter() {
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_notifications,parent,false);

        return new NotificationAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Picasso.get().load(URLs.URL_Image+noticationModels.get(position).getLandingImages()).into(holder.ImageView);

        setAnimation(holder.itemView);

    }

    @Override
    public int getItemCount() {
        return noticationModels.size();
    }

    private void setAnimation(View view){

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        view.setAnimation(animation);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView ImageView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            ImageView=itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyClickLister.onHistoryItemClickListener(getAdapterPosition());
                }
            });

        }
    }
}
