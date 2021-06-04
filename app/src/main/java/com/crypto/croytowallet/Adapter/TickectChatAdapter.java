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
import com.crypto.croytowallet.Interface.MessageClickListner;
import com.crypto.croytowallet.Model.TicketChatModel;
import com.crypto.croytowallet.R;

import java.util.ArrayList;
import java.util.List;

public class TickectChatAdapter extends  RecyclerView.Adapter<TickectChatAdapter.ViewHolder>{
    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private ArrayList<TicketChatModel> mChat;
    private MessageClickListner messageClickListner;

    public TickectChatAdapter(Context mContext, ArrayList<TicketChatModel> mChat, MessageClickListner messageClickListner) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.messageClickListner=messageClickListner;
    }

    public TickectChatAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new TickectChatAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new TickectChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.show_message.setText(mChat.get(position).getMessage());

        String dateAndTime = mChat.get(position).getTime();
        String[] s= dateAndTime.split("T");
        String time1 = s[1];

        holder.date.setText(AppUtils.getDate(dateAndTime));
        holder.time.setText(time1);

    }

    @Override
    public int getItemCount() {
      return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message,time,date;
        public ImageView profile_image;
        public TextView txt_seen;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);

          /*  itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageClickListner.onItemClick(getAdapterPosition());
                }
            });*/

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    messageClickListner.onLongItemClick(getAdapterPosition());
                    return true;
                }
            });

}
    }

    @Override
    public int getItemViewType(int position) {
        if (mChat.get(position).getRoleId().equals("outgoingMessages") ){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    public void update(List<TicketChatModel> commentList){
        mChat = new ArrayList<>();
        mChat.addAll(commentList);
        notifyDataSetChanged();
    }
}
