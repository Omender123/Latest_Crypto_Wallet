package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Extra_Class.ApiResponse.RewardHistoryResponse;
import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.R;

import java.util.ArrayList;

public class Rewards_HistoryAdapter extends RecyclerView.Adapter<Rewards_HistoryAdapter.myViewHolder> {
    ArrayList<RewardHistoryResponse.Result> modelArrayList;
    Context context;
    private HistoryClickLister historyClickLister;
    public Rewards_HistoryAdapter(ArrayList<RewardHistoryResponse.Result> modelArrayList, Context context , HistoryClickLister historyClickLister) {
        this.modelArrayList = modelArrayList;
        this.context = context;
        this.historyClickLister=historyClickLister;
    }


    public Rewards_HistoryAdapter() {
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_transactio_history,parent,false);

        return new Rewards_HistoryAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.transaction_status.setText(modelArrayList.get(position).getType());
        holder.transaction_amount.setText(modelArrayList.get(position).getEarnedReward());
        holder.transaction_username.setText( modelArrayList.get(position).getSenderName());


        holder.transaction_amount.setTextColor(modelArrayList.get(position).getEarnedReward().contains("-")?
                context.getResources().getColor(R.color.red): context.getResources().getColor(R.color.green)  );

        if(modelArrayList.get(position).getEarnedReward().contains("-")){
            holder.transaction_amount.setText(modelArrayList.get(position).getEarnedReward());
            }else{
            holder.transaction_amount.setText("+"+modelArrayList.get(position).getEarnedReward());
        }

        String dateAndTime = modelArrayList.get(position).getCreatedAt();
        String[] s = dateAndTime.split("T");
        String time = s[1];

        holder.transaction_date.setText(AppUtils.getDate(dateAndTime));
        holder.transaction_time.setText(time);


    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
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
