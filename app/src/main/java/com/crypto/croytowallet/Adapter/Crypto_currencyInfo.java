package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.crypto.croytowallet.Interface.CryptoClickListner;
import com.crypto.croytowallet.Model.CrptoInfoModel;
import com.crypto.croytowallet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Crypto_currencyInfo extends RecyclerView.Adapter<Crypto_currencyInfo.MyHolder>{
ArrayList<CrptoInfoModel>crptoInfoModels;
Context context;
private CryptoClickListner cryptoClickListner;
    SharedPreferences sharedPreferences;
    public Crypto_currencyInfo(ArrayList<CrptoInfoModel> crptoInfoModels, Context context, CryptoClickListner cryptoClickListner) {
        this.crptoInfoModels = crptoInfoModels;
        this.context = context;
        this.cryptoClickListner= cryptoClickListner;
    }

    public Crypto_currencyInfo() {
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.customdashboard,parent,false);

        return new MyHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String image=crptoInfoModels.get(position).getImage();
        String iconname= crptoInfoModels.get(position).getName();
       String  CurrentPrice= crptoInfoModels.get(position).getCurrentPrice();
        String currencyRate= crptoInfoModels.get(position).getCurrencyRate();
        String highRate= crptoInfoModels.get(position).getCurrencyRate();
        String lowRate= crptoInfoModels.get(position).getCurrencyRate();
        String id=crptoInfoModels.get(position).getId();
        sharedPreferences =context.getSharedPreferences("currency",0);

      String   CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");


        Picasso.get().load(image).into(holder.imageView);
        holder.name.setText(iconname);
        holder.currencyPrice.setText(CurrencySymbols+CurrentPrice);
        holder.increaseRate.setText(currencyRate);

        holder.increaseRate.setTextColor(crptoInfoModels.get(position).getCurrencyRate().contains("-")?
                context.getResources().getColor(R.color.red): context.getResources().getColor(R.color.green)  );
        holder.percentage.setTextColor(crptoInfoModels.get(position).getCurrencyRate().contains("-")?
                context.getResources().getColor(R.color.red): context.getResources().getColor(R.color.green) );

       if(crptoInfoModels.get(position).getCurrencyRate().contains("-")){
           holder.increaseRate.setText(currencyRate);
           holder.lineChartView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down_blue));
           holder.lineChartView.setScaleType(ImageView.ScaleType.CENTER_CROP);
       }else{
           holder.increaseRate.setText("+"+currencyRate);
           holder.lineChartView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_up_blue));
       }

       // Picasso.get().load(crptoInfoModels.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return crptoInfoModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView,lineChartView;
        TextView increaseRate,name,currencyPrice,percentage;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.coinImage);
            increaseRate=itemView.findViewById(R.id.increaseRate);
            name=itemView.findViewById(R.id.coinname);
            currencyPrice=itemView.findViewById(R.id.coinrate);
            percentage=itemView.findViewById(R.id.null1);
            lineChartView =itemView.findViewById(R.id.chart);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cryptoClickListner.onCryptoItemClickListener(getAdapterPosition());
                }
            });

        }
    }
}
