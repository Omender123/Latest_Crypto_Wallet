package com.crypto.croytowallet.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Extra_Class.ApiResponse.GetNewCoinRespinse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TopUp_HistoryResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TransactionHistoryResponse;
import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.TopUp.Enter_TopUp_Amount;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class TopUp_HistoryAdapter extends RecyclerView.Adapter<TopUp_HistoryAdapter.myViewHolder>  implements Filterable {
    List<TopUp_HistoryResponse> transactionHistoryModels;
    Context context;
    Activity activity;
    private OnOrderItemListener listener;
    private List<TopUp_HistoryResponse> exampleListFull;

    public TopUp_HistoryAdapter(List<TopUp_HistoryResponse> transactionHistoryModels, Context context, OnOrderItemListener historyClickLister) {
        this.transactionHistoryModels = transactionHistoryModels;
        this.context = context;
        this.listener = historyClickLister;
        exampleListFull = new ArrayList<>(transactionHistoryModels);
    }

    public TopUp_HistoryAdapter() {

    }

    @NonNull
    @Override
    public TopUp_HistoryAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_transactio_history, parent, false);

        return new TopUp_HistoryAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

    SharedPreferences    sharedPreferences1 =context.getSharedPreferences("imtInfo", Context.MODE_PRIVATE);

      String price =  sharedPreferences1.getString("price","0.0912");
        holder.transaction_status.setText(transactionHistoryModels.get(position).getTransactionId());



            holder.transaction_amount.setText(transactionHistoryModels.get(position).getUtility()+"UT");



        String dateAndTime = transactionHistoryModels.get(position).getCreatedAt();
        String[] s = dateAndTime.split("T");
        String time = s[0];
         holder.transaction_username.setText(time);


        holder.transaction_date.setText(transactionHistoryModels.get(position).getAmount()+" "+transactionHistoryModels.get(position).getCurrency());

       // setAnimation(holder.itemView);

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

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            transaction_status = itemView.findViewById(R.id.transaction_status);
            transaction_amount = itemView.findViewById(R.id.transaction_amount);
            transaction_username = itemView.findViewById(R.id.transaction_username);
            transaction_date = itemView.findViewById(R.id.transaction_date);
            transaction_time = itemView.findViewById(R.id.transaction_Time);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onOrderItemClick(transactionHistoryModels,getAdapterPosition());
                }
            });

        }
    }


    public interface OnOrderItemListener {
        void onOrderItemClick(List<TopUp_HistoryResponse> data, int position);
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TopUp_HistoryResponse> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TopUp_HistoryResponse item : exampleListFull) {
                    if (item.getAccountName().toLowerCase().contains(filterPattern) || item.getId().toLowerCase().contains(filterPattern) || item.getAccountNo().toLowerCase().contains(filterPattern) || item.getTransactionId().toLowerCase().contains(filterPattern)) {
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
