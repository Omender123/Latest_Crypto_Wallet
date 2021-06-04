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
import com.crypto.croytowallet.Model.OldChatModel;
import com.crypto.croytowallet.R;

import java.util.ArrayList;

public class OldChatAdapter extends RecyclerView.Adapter<OldChatAdapter.myVeiwholder>{
    ArrayList<OldChatModel> oldChatModels;
    Context context;
    private HistoryClickLister historyClickLister;

    public OldChatAdapter(ArrayList<OldChatModel> oldChatModels, Context context, HistoryClickLister historyClickLister) {
        this.oldChatModels = oldChatModels;
        this.context = context;
        this.historyClickLister = historyClickLister;
    }

    public OldChatAdapter() {
    }

    @NonNull
    @Override
    public myVeiwholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.customticketlayout,parent,false);

        return new OldChatAdapter.myVeiwholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myVeiwholder holder, int position) {

        holder.chat_username.setText(oldChatModels.get(position).getUsername());
        String dateAndTime = oldChatModels.get(position).getDate();
        holder.chat_Date.setText(AppUtils.getDate(dateAndTime));

        holder.chat_Userid.setText(oldChatModels.get(position).getUserId());
        holder.chat_Userid.setTextSize(8f);

        holder.chat_id.setText(oldChatModels.get(position).getChatId());
        holder.chat_id.setTextSize(8f);
    }

    @Override
    public int getItemCount() {
        return oldChatModels.size();
    }

    public class myVeiwholder extends RecyclerView.ViewHolder {

        TextView chat_username,chat_Date,chat_Userid,chat_id;

        public myVeiwholder(@NonNull View itemView) {
            super(itemView);

            chat_username  = itemView.findViewById(R.id.transaction_status);
            chat_Date = itemView.findViewById(R.id.transaction_amount);
            chat_Userid   = itemView.findViewById(R.id.transaction_username);
            chat_id  = itemView.findViewById(R.id.transaction_date);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyClickLister.onHistoryItemClickListener(getAdapterPosition());
                }
            });

        }
    }
}
