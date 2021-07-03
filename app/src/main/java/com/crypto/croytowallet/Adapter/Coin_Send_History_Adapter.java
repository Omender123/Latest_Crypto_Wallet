package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Extra_Class.ApiResponse.GetNewCoinRespinse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.SendCoinHistoryResponse;
import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.Model.CoinModal;
import com.crypto.croytowallet.R;

import java.util.ArrayList;
import java.util.List;

public class Coin_Send_History_Adapter extends RecyclerView.Adapter<Coin_Send_History_Adapter.myViewHolder> {
     SendCoinHistoryResponse sendCoinHistoryResponse;
    Context context;
    private OnSendCoinItemListener historyClickLister;
    public Coin_Send_History_Adapter(SendCoinHistoryResponse sendCoinHistoryResponse, Context context, OnSendCoinItemListener historyClickLister) {
        this.sendCoinHistoryResponse = sendCoinHistoryResponse;
        this.context = context;
        this.historyClickLister=historyClickLister;
    }

    public Coin_Send_History_Adapter() {
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_transactio_history,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.transaction_status.setText(sendCoinHistoryResponse.getResult().get(position).getCryptoCurrency().toUpperCase());
        holder.transaction_amount.setText(sendCoinHistoryResponse.getResult().get(position).getAmtOfCrypto());

        holder.transaction_username.setText(sendCoinHistoryResponse.getResult().get(position).getUserId().getUsername());

        String dateAndTime = sendCoinHistoryResponse.getResult().get(position).getCreatedAt();
        String[] s= dateAndTime.split("T");
        String time = s[1];

        holder.transaction_date.setText(AppUtils.getDate(dateAndTime));
        holder.transaction_time.setText(time);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyClickLister.onSendCoinItemClickListener(sendCoinHistoryResponse,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sendCoinHistoryResponse.getResult().size();
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




        }
    }

    public interface OnSendCoinItemListener {
        void onSendCoinItemClickListener(SendCoinHistoryResponse sendCoinHistoryResponse, int position);
    }
}
