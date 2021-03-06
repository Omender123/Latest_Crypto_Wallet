package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crypto.croytowallet.Extra_Class.ApiResponse.TrueEcResponse;
import com.crypto.croytowallet.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CoinSpinnerAdapter extends BaseAdapter {
    Context context;
    List<TrueEcResponse>trueEcResponses;
    LayoutInflater inflter;

    public CoinSpinnerAdapter(Context context, List<TrueEcResponse>trueEcResponses) {
        this.context = context;
        this.trueEcResponses=trueEcResponses;
        inflter = (LayoutInflater.from(context));
    }


    @Override
    public int getCount() {
        return trueEcResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.coin_spinner_layout, null);
        ImageView coinImages = (ImageView) view.findViewById(R.id.imageView);
        TextView coinNames = (TextView) view.findViewById(R.id.coinName);

        Picasso.get().load(trueEcResponses.get(position).getImage()).into(coinImages);
        coinNames.setText(trueEcResponses.get(position).getName());


        return view;
    }
}

