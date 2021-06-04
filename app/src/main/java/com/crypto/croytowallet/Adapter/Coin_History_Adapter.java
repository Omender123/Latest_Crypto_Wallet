package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.Model.CoinModal;
import com.crypto.croytowallet.R;

import java.util.ArrayList;

public class Coin_History_Adapter extends RecyclerView.Adapter<Coin_History_Adapter.myViewHolder> {
    ArrayList<CoinModal> coinModals;
    Context context;
    private HistoryClickLister historyClickLister;
    public Coin_History_Adapter(ArrayList<CoinModal> coinModals, Context context,HistoryClickLister historyClickLister) {
        this.coinModals = coinModals;
        this.context = context;
        this.historyClickLister=historyClickLister;
    }

    public Coin_History_Adapter() {
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_transactio_history,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.transaction_status.setText(coinModals.get(position).getType());
        holder.transaction_amount.setText(coinModals.get(position).getAmount());
        holder.transaction_username.setText(coinModals.get(position).getUsername());

        String dateAndTime = coinModals.get(position).getTime();
        String[] s= dateAndTime.split("T");
        String time = s[1];

        holder.transaction_date.setText(AppUtils.getDate(dateAndTime));
        holder.transaction_time.setText(time);
    }

    @Override
    public int getItemCount() {
        return coinModals.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView transaction_status,transaction_amount,transaction_username,transaction_time,transaction_date;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            transaction_status=itemView.findViewById(R.id.transaction_status);
            transaction_amount=itemView.findViewById(R.id.transaction_amount);
            transaction_username=itemView.findViewById(R.id.transaction_username);
            transaction_date=itemView.findViewById(R.id.transaction_date);
            transaction_time = itemView.findViewById(R.id.transaction_Time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyClickLister.onHistoryItemClickListener(getAdapterPosition());
                }
            });


        }
    }

}
