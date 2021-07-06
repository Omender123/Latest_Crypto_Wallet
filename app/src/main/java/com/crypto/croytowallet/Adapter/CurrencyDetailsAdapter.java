package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crypto.croytowallet.Extra_Class.ApiResponse.CurrencyDetailsModelResponse;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.VolleyDatabase.URLs;

public class CurrencyDetailsAdapter  extends RecyclerView.Adapter<CurrencyDetailsAdapter.CurrencyDeatilsViewHolder> {
    CurrencyDetailsModelResponse currencyDetailsModelResponse;
    String type;
    Context context;
    private  OnCurrencyDetails onCurrencyDetails;

    public CurrencyDetailsAdapter(CurrencyDetailsModelResponse currencyDetailsModelResponse, String type, Context context, OnCurrencyDetails onCurrencyDetails) {
        this.currencyDetailsModelResponse = currencyDetailsModelResponse;
        this.type = type;
        this.context = context;
        this.onCurrencyDetails = onCurrencyDetails;
    }

    public CurrencyDetailsAdapter() {
    }

    @NonNull
    @Override
    public CurrencyDeatilsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_details_layout,parent,false);

        return new CurrencyDeatilsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyDeatilsViewHolder holder, int position) {
        if (type.equalsIgnoreCase("video")){
            holder.play_button.setVisibility(View.VISIBLE);
            holder.text_title.setVisibility(View.GONE);
            Glide.with(context).load(URLs.URL_Image+currencyDetailsModelResponse.getErc().getVideo().get(position).getImage()).into(holder.details_image);
            holder.text_description.setText(currencyDetailsModelResponse.getErc().getVideo().get(position).getDescription());

        }else if (type.equalsIgnoreCase("blogs")){
            holder.play_button.setVisibility(View.GONE);
            holder.text_title.setVisibility(View.VISIBLE);
            Glide.with(context).load(URLs.URL_Image+currencyDetailsModelResponse.getErc().getBlog().get(position).getImage()).into(holder.details_image);
            holder.text_description.setText(currencyDetailsModelResponse.getErc().getBlog().get(position).getDescription());
            holder.text_title.setText(currencyDetailsModelResponse.getErc().getBlog().get(position).getTitle());

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equalsIgnoreCase("video")){
                    onCurrencyDetails.OnCurrencyDetailsClickListener(currencyDetailsModelResponse.getErc().getVideo().get(position).getVideoId(),
                            currencyDetailsModelResponse.getErc().getBlog().get(position).getLink(),type);

                }else if (type.equalsIgnoreCase("blogs")){
                    onCurrencyDetails.OnCurrencyDetailsClickListener(currencyDetailsModelResponse.getErc().getVideo().get(position).getVideoId(),
                            currencyDetailsModelResponse.getErc().getBlog().get(position).getLink(),type);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (type.equalsIgnoreCase("video")){
            size=currencyDetailsModelResponse.getErc().getVideo().size();
        }else if (type.equalsIgnoreCase("blogs")){
            size=currencyDetailsModelResponse.getErc().getBlog().size();
        }
        return size;
    }

    public class CurrencyDeatilsViewHolder extends RecyclerView.ViewHolder{

        ImageView details_image,play_button;
        TextView text_title,text_description;
        public CurrencyDeatilsViewHolder(@NonNull View itemView) {
            super(itemView);
            details_image = itemView.findViewById(R.id.details_image);
            play_button = itemView.findViewById(R.id.play_button);
            text_title = itemView.findViewById(R.id.text_title);
            text_description = itemView.findViewById(R.id.text_description);


        }
    }

    public interface OnCurrencyDetails{
        void OnCurrencyDetailsClickListener(String VideoId,String link, String type);
    }
}
