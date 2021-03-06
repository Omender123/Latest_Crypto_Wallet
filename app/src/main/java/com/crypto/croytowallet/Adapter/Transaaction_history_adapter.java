package com.crypto.croytowallet.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Extra_Class.ApiResponse.TransactionHistoryResponse;
import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;

import java.util.ArrayList;
import java.util.List;

public class Transaaction_history_adapter extends RecyclerView.Adapter<Transaaction_history_adapter.myViewHolder> implements Filterable {
    ArrayList<TransactionHistoryResponse.Result> transactionHistoryModels;
    Context context;
    Activity activity;
    private HistoryClickLister historyClickLister;
    SharedPreferences sharedPreferences;
    private List<TransactionHistoryResponse.Result> exampleListFull;
    String userId;

    public Transaaction_history_adapter(ArrayList<TransactionHistoryResponse.Result> transactionHistoryModels, Context context, HistoryClickLister historyClickLister,String userId) {
        this.transactionHistoryModels = transactionHistoryModels;
        this.context = context;
        this.historyClickLister = historyClickLister;
        this.userId=userId;
        exampleListFull = new ArrayList<>(transactionHistoryModels);
    }

    public Transaaction_history_adapter() {

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_transactio_history, parent, false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {


        holder.transaction_status.setText("Receiver " + transactionHistoryModels.get(position).getReceiverName());
        holder.transaction_amount.setText(transactionHistoryModels.get(position).getAmount());
        holder.transaction_username.setText("Sender " + transactionHistoryModels.get(position).getSenderName());

        String receiverId =transactionHistoryModels.get(position).getReceiverId();
         Log.d("egals",receiverId+"="+userId);


        if (userId.equals(receiverId)){
            holder.transaction_amount.setText("+"+transactionHistoryModels.get(position).getAmount());
            holder.transaction_amount.setTextColor(context.getResources().getColor(R.color.green));
        }else{
            holder.transaction_amount.setText("-"+transactionHistoryModels.get(position).getAmount());
            holder.transaction_amount.setTextColor(context.getResources().getColor(R.color.red));
        }

        String dateAndTime = transactionHistoryModels.get(position).getCreatedAt();
        String[] s = dateAndTime.split("T");
        String time = s[1];

        holder.transaction_date.setText(AppUtils.getDate(dateAndTime));
        holder.transaction_time.setText(time);

        if (transactionHistoryModels.get(position).getType().equalsIgnoreCase("Reward") || transactionHistoryModels.get(position).getType().equalsIgnoreCase("airDropIMT")
        ||transactionHistoryModels.get(position).getType().equalsIgnoreCase("purchase by airdrop")){
           // holder.cardView.setBackgroundColor();
         //   holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.card_background));
          // holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.card_background));


        }else{
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.orange1));
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.orange1));

        }


        setAnimation(holder.itemView);



    }

    @Override
    public int getItemCount() {
        return transactionHistoryModels.size();
    }

    private void setAnimation(View view) {

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        view.setAnimation(animation);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView transaction_status, transaction_amount, transaction_username, transaction_time, transaction_date;
        CardView cardView;
        RelativeLayout relativeLayout;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            transaction_status = itemView.findViewById(R.id.transaction_status);
            transaction_amount = itemView.findViewById(R.id.transaction_amount);
            transaction_username = itemView.findViewById(R.id.transaction_username);
            transaction_date = itemView.findViewById(R.id.transaction_date);
            transaction_time = itemView.findViewById(R.id.transaction_Time);
            cardView = itemView.findViewById(R.id.card);
            relativeLayout = itemView.findViewById(R.id.relative);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyClickLister.onHistoryItemClickListener(getAdapterPosition());
                }
            });

        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TransactionHistoryResponse.Result> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TransactionHistoryResponse.Result item : exampleListFull) {
                    if (item.getReceiverName().toLowerCase().contains(filterPattern) || item.getId().toLowerCase().contains(filterPattern) || item.getSenderName().toLowerCase().contains(filterPattern) || item.getCreatedAt().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            transactionHistoryModels.clear();
            transactionHistoryModels.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
