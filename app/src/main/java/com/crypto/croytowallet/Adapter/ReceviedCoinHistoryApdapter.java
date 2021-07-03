package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Extra_Class.ApiResponse.ReceviedCoinHistoryResponse;
import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.R;

import java.util.List;

public class ReceviedCoinHistoryApdapter extends RecyclerView.Adapter<ReceviedCoinHistoryApdapter.myViewHolder> {
    List<ReceviedCoinHistoryResponse>receviedCoinHistoryResponses;
    Context context;
    private OnReceviedCoinItemListener historyClickLister;
    public ReceviedCoinHistoryApdapter(List<ReceviedCoinHistoryResponse> receviedCoinHistoryResponses, Context context, OnReceviedCoinItemListener historyClickLister) {
        this.receviedCoinHistoryResponses = receviedCoinHistoryResponses;
        this.context = context;
        this.historyClickLister=historyClickLister;
    }

    public ReceviedCoinHistoryApdapter() {
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_transactio_history,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.transaction_status.setText("Received");
        holder.transaction_amount.setText(receviedCoinHistoryResponses.get(position).getValue());

        holder.transaction_username.setText(receviedCoinHistoryResponses.get(position).getFrom());

        String dateAndTime = receviedCoinHistoryResponses.get(position).getTimeStamp();
        String[] s= dateAndTime.split("T");
        String time = s[1];

        holder.transaction_date.setText(AppUtils.getDate(dateAndTime));
        holder.transaction_time.setText(time);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyClickLister.onReceviedCoinItemClickListener(receviedCoinHistoryResponses,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return receviedCoinHistoryResponses.size();
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

    public interface OnReceviedCoinItemListener {
        void onReceviedCoinItemClickListener(List<ReceviedCoinHistoryResponse> receviedCoinHistoryResponses, int position);
    }
}
