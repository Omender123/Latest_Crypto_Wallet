package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.Model.CurrencyModel;
import com.crypto.croytowallet.R;

import java.util.ArrayList;
import java.util.List;

public class SelectCurrencyAdapter extends RecyclerView.Adapter<SelectCurrencyAdapter.Myviewholder> implements Filterable {
    ArrayList<CurrencyModel> currencyModels;
    Context context;
    private HistoryClickLister historyClickLister;
    private List<CurrencyModel> exampleListFull;

    public SelectCurrencyAdapter(ArrayList<CurrencyModel> currencyModels, Context contex, HistoryClickLister historyClickLister) {
        this.currencyModels = currencyModels;
        this.context = context;
        this.historyClickLister = historyClickLister;
        exampleListFull = new ArrayList<>(currencyModels);

    }

    public SelectCurrencyAdapter() {

    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customcurrencylayout, parent, false);

        return new SelectCurrencyAdapter.Myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

        holder.currency.setText(currencyModels.get(position).getCurrency());
        holder.CountryName.setText(currencyModels.get(position).getCountryName());


    }


    @Override
    public int getItemCount() {
        return currencyModels.size();
    }



    public class Myviewholder extends RecyclerView.ViewHolder {
        TextView currency, CountryName;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            currency = itemView.findViewById(R.id.currency);
            CountryName = itemView.findViewById(R.id.CountryName);
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
            List<CurrencyModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CurrencyModel item : exampleListFull) {
                    if (item.getCurrency().toLowerCase().contains(filterPattern)|| item.getCountryName().toLowerCase().contains(filterPattern)) {
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
            currencyModels.clear();
            currencyModels.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}



