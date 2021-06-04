package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.crypto.croytowallet.R;

public class CustomSpinnerAdapter   extends BaseAdapter {
    Context context;
    int coinImage[];
    String[] coinName;
    String[] coinSymbols;
    String[] coinId;
    String[] PriceCoinId;
    LayoutInflater inflter;

    public CustomSpinnerAdapter(Context applicationContext, int[] coinImage, String[] coinName,String[] coinSymbols, String[] coinId,String[] PriceCoinId) {
        this.context = applicationContext;
        this.coinImage = coinImage;
        this.coinName = coinName;
        this.coinSymbols=coinSymbols;
        this.coinId=coinId;
        this.PriceCoinId=PriceCoinId;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return coinImage.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        ImageView coinImages = (ImageView) view.findViewById(R.id.imageView);
        TextView coinNames = (TextView) view.findViewById(R.id.coinName);
        TextView coinSymbol = (TextView) view.findViewById(R.id.coinsymbols);

        coinImages.setImageResource(coinImage[i]);
        coinNames.setText(coinName[i]);
        coinSymbol.setText(coinSymbols[i]);


        return view;
    }
}
