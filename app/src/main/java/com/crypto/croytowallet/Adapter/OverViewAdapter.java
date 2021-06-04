package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Interface.CryptoClickListner;
import com.crypto.croytowallet.Interface.OverViewClickListner;
import com.crypto.croytowallet.Model.CrptoInfoModel;
import com.crypto.croytowallet.Model.OverViewModel;
import com.crypto.croytowallet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OverViewAdapter extends RecyclerView.Adapter<OverViewAdapter.MyHolder> {
    ArrayList<OverViewModel> overViewModels;
    Context context;
    SharedPreferences sharedPreferences;
    private OverViewClickListner overViewClickListner;
    public OverViewAdapter(ArrayList<OverViewModel> overViewModels, Context context,OverViewClickListner overViewClickListner) {
        this.overViewModels = overViewModels;
        this.context = context;
        this.overViewClickListner = overViewClickListner;
    }

    public OverViewAdapter() {
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.overview_custom_layout,parent,false);

        return new OverViewAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String image=overViewModels.get(position).getImage();
        String iconname= overViewModels.get(position).getName();
        String  CurrentPrice= overViewModels.get(position).getCurrentPrice();
        String currencyRate= overViewModels.get(position).getCurrencyRate();
        String highRate= overViewModels.get(position).getCurrencyRate();
        String lowRate= overViewModels.get(position).getCurrencyRate();
        String id=overViewModels.get(position).getId();
        sharedPreferences =context.getSharedPreferences("currency",0);

        String   CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");

        Picasso.get().load(image).into(holder.imageView);
        holder.name.setText(iconname);
        holder.currencyPrice.setText(CurrencySymbols+CurrentPrice);
        holder.increaseRate.setText(currencyRate);

        holder.increaseRate.setTextColor(overViewModels.get(position).getCurrencyRate().contains("-")?
                context.getResources().getColor(R.color.red): context.getResources().getColor(R.color.green)  );
        holder.percentage.setTextColor(overViewModels.get(position).getCurrencyRate().contains("-")?
                context.getResources().getColor(R.color.red): context.getResources().getColor(R.color.green) );

        if(overViewModels.get(position).getCurrencyRate().contains("-")){
            holder.increaseRate.setText(currencyRate);
            holder.chart.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_orange_down));
        }else{
            holder.increaseRate.setText("+"+currencyRate);
            holder.chart.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_orange_up));
        }
        holder.coinSmybols.setText(overViewModels.get(position).getSymbol());
    }

    @Override
    public int getItemCount() {
        return overViewModels.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView,chart;
        TextView increaseRate,name,currencyPrice,percentage,coinSmybols;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            increaseRate=itemView.findViewById(R.id.increaseRate);
            name=itemView.findViewById(R.id.coinName);
            coinSmybols=itemView.findViewById(R.id.coinsymbols);
            currencyPrice=itemView.findViewById(R.id.price);
            percentage = itemView.findViewById(R.id.null1);
            chart= itemView.findViewById(R.id.imageView3);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    overViewClickListner.onOverViewItemClickListener(getAdapterPosition());
                }
            });

        }
    }
}
