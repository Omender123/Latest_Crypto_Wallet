package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Extra_Class.ApiResponse.GetNewCoinRespinse;
import com.crypto.croytowallet.Interface.CryptoClickListner;
import com.crypto.croytowallet.Model.CrptoInfoModel;
import com.crypto.croytowallet.Model.CurrencyModel;
import com.crypto.croytowallet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewCoinAdapter  extends RecyclerView.Adapter<NewCoinAdapter.MyHolder> {

    List<GetNewCoinRespinse> crptoInfoModels;
    Context context;
    private OnOrderItemListener listener;
    private List<GetNewCoinRespinse> exampleListFull;
    public NewCoinAdapter(List<GetNewCoinRespinse> crptoInfoModels, Context context,OnOrderItemListener listener) {
        this.crptoInfoModels = crptoInfoModels;
        this.context = context;
        this.listener = listener;
        exampleListFull = new ArrayList<>(crptoInfoModels);

    }



    @NonNull
    @Override
    public NewCoinAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_new_coin_layout,parent,false);

        return new NewCoinAdapter.MyHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull NewCoinAdapter.MyHolder holder, int position) {
        String coin_images=crptoInfoModels.get(position).getImage();
        String coin_names= crptoInfoModels.get(position).getName();
        String coin_Symbols=crptoInfoModels.get(position).getSymbol();

        holder.coin_name.setText(coin_names);
        holder.coin_Symbol.setText(coin_Symbols);
        Picasso.get().load(coin_images).into(holder.coinImage);

        holder.add_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onOrderItemClick(crptoInfoModels,position);

            }
        });
        }

    @Override
    public int getItemCount() {
        return crptoInfoModels.size();
    }



    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView coinImage,add_coin;
        TextView coin_name,coin_Symbol;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            coin_name=itemView.findViewById(R.id.coin_name);
            coin_Symbol=itemView.findViewById(R.id.coin_symbols);
            coinImage=itemView.findViewById(R.id.Image_cuurency);
            add_coin=itemView.findViewById(R.id.add_coin);


        }
    }
    public interface OnOrderItemListener {
        void onOrderItemClick(List<GetNewCoinRespinse> data, int position);
    }
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<GetNewCoinRespinse> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (GetNewCoinRespinse item : exampleListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)|| item.getSymbol().toLowerCase().contains(filterPattern)) {
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
            crptoInfoModels.clear();
            crptoInfoModels.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
