package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.Model.TicketModel;
import com.crypto.croytowallet.R;

import java.util.ArrayList;

public class Ticket_Adapter extends RecyclerView.Adapter<Ticket_Adapter.myVeiwholder> {

    ArrayList<TicketModel>ticketModels;
    Context context;

    public Ticket_Adapter(ArrayList<TicketModel> ticketModels, Context context) {
        this.ticketModels = ticketModels;
        this.context = context;
    }

    public Ticket_Adapter() {
    }

    @NonNull
    @Override
    public myVeiwholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.customticketlayout,parent,false);

        return new myVeiwholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myVeiwholder holder, int position) {

        String status =ticketModels.get(position).getStatus();
        holder.ticket_status.setText(status);
        if(status.equals("open")){
            holder.ticket_status.setTextColor(Color.parseColor("#FFD600"));
        }else  if(status.equals("closed")){
            holder.ticket_status.setTextColor(Color.parseColor("#D50000"));
        }else  if(status.equals("answered")){
            holder.ticket_status.setTextColor(Color.parseColor("#00C853"));
        }

        String dateAndTime = ticketModels.get(position).getDate();
        holder.ticket_Date.setText(AppUtils.getDate(dateAndTime));
        holder.ticket_Userid.setText(ticketModels.get(position).getUserId());
        holder.tickect_subject.setText(ticketModels.get(position).getSubject());

    }

    @Override
    public int getItemCount() {
        return ticketModels.size();
    }

    public class myVeiwholder extends RecyclerView.ViewHolder {

        TextView ticket_status,ticket_Date,ticket_Userid,tickect_subject;

        public myVeiwholder(@NonNull View itemView) {
            super(itemView);

            tickect_subject  = itemView.findViewById(R.id.transaction_status);
            ticket_Date = itemView.findViewById(R.id.transaction_amount);
            ticket_Userid   = itemView.findViewById(R.id.transaction_username);
            ticket_status  = itemView.findViewById(R.id.transaction_date);


        }
    }
}
