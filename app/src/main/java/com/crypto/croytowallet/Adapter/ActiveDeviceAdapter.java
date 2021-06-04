package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Extra_Class.ApiResponse.ActiveDeviceResponse;
import com.crypto.croytowallet.Interface.HistoryClickLister;

import com.crypto.croytowallet.R;

import java.util.ArrayList;

public class ActiveDeviceAdapter extends RecyclerView.Adapter<ActiveDeviceAdapter.myViewHolder> {
ArrayList<ActiveDeviceResponse.Result> modelArrayList;
Context context;
    private HistoryClickLister historyClickLister;
    public ActiveDeviceAdapter(ArrayList<ActiveDeviceResponse.Result> modelArrayList, Context context ,HistoryClickLister historyClickLister) {
        this.modelArrayList = modelArrayList;
        this.context = context;
        this.historyClickLister=historyClickLister;
    }

    public ActiveDeviceAdapter() {
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_active_device_layout,parent,false);

        return new ActiveDeviceAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.Os_Name.setText("OS Name : "+modelArrayList.get(position).getOsName());
        holder.IP_Address.setText("IP Address : "+modelArrayList.get(position).getIpV4());
        holder.Location.setText("Location : "+modelArrayList.get(position).getLocation());


    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView Os_Name,IP_Address,Location;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            Os_Name=itemView.findViewById(R.id.osname);
            IP_Address=itemView.findViewById(R.id.ip_address);
            Location=itemView.findViewById(R.id.location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyClickLister.onHistoryItemClickListener(getAdapterPosition());
                }
            });

        }
    }
}
