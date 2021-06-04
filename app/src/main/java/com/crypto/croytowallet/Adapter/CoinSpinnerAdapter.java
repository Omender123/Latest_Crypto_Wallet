package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crypto.croytowallet.R;
import com.squareup.picasso.Picasso;

public class CoinSpinnerAdapter extends BaseAdapter {
    Context context;
    String [] imageUrl;
    String[] coinName;
    String[] coinSymbols;
    LayoutInflater inflter;

    public CoinSpinnerAdapter(Context context, String[] imageUrl, String[] coinName, String[] coinSymbols) {
        this.context = context;
        this.imageUrl = imageUrl;
        this.coinName = coinName;
        this.coinSymbols = coinSymbols;
        inflter = (LayoutInflater.from(context));
    }


    @Override
    public int getCount() {
        return coinName.length;
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
        //TextView coinSymbol = (TextView) view.findViewById(R.id.coinsymbols);

       // coinImages.setImageResource(coinImage[i]);


        Picasso.get().load(imageUrl[position]).into(coinImages);
        coinNames.setText(coinName[position]);
       // coinSymbol.setText(coinSymbols[position]);


        return view;
    }
    }

