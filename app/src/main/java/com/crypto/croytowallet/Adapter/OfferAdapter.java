package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.Model.OfferModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {
ArrayList<OfferModel>offerModels;
Context context;
    private HistoryClickLister historyClickLister;

    public OfferAdapter(ArrayList<OfferModel> offerModels, Context context, HistoryClickLister historyClickLister) {
        this.offerModels = offerModels;
        this.context = context;
        this.historyClickLister = historyClickLister;
    }

    public OfferAdapter() {

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_offer_layout,parent,false);

        return new OfferAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.offerDate.setText("Date : "+AppUtils.getDate(offerModels.get(position).getDate()));
        holder.offerName.setText(offerModels.get(position).getOffer_name());
        holder.offerShortDes.setText(offerModels.get(position).getOffer_tittle());
        if(offerModels.get(position).getImageUrl().equals("https://imsmart.s3.ap-south-1.amazonaws.com/logo.png")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_artboard___5));
        }else {
            Picasso.get().load(URLs.URL_Image +offerModels.get(position).getImageUrl()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return offerModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView image;
    TextView offerName,offerDate,offerShortDes;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image_view);
            offerName=itemView.findViewById(R.id.offer_name);
            offerDate=itemView.findViewById(R.id.offer_date);
            offerShortDes=itemView.findViewById(R.id.offer_shortDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyClickLister.onHistoryItemClickListener(getAdapterPosition());
                }
            });

        }
    }
}
