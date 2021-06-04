package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crypto.croytowallet.Model.CountryModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.Extra_Class.Utils;

import java.util.ArrayList;

public class CountryNameSpinnerAddapter extends BaseAdapter {
    Context context;
    ArrayList<CountryModel> countryModels;
    LayoutInflater inflter;

    public CountryNameSpinnerAddapter(Context context, ArrayList<CountryModel> countryModels) {
        this.context = context;
        this.countryModels = countryModels;
        inflter = (LayoutInflater.from(context));
    }

    public CountryNameSpinnerAddapter() {
    }

    @Override
    public int getCount() {
        return countryModels.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.coin_spinner_layout, null);
        ImageView coinImages = (ImageView) convertView.findViewById(R.id.imageView);
        TextView coinNames = (TextView) convertView.findViewById(R.id.coinName);




       // Picasso.get().load("https://restcountries.eu/data/afg.svg").into(coinImages);

        String coinname =countryModels.get(position).getCountryName();
        coinNames.setText(coinname);

        if (coinname.equals("Belize")){
            Utils.fetchSvg(context, "https://restcountries.eu/data/ben.svg", coinImages);
        }else if(coinname.equals("Guatemala")){
            Utils.fetchSvg(context, "https://restcountries.eu/data/gtm.svg", coinImages);
        }else if(coinname.equals("Saint Helena, Ascension and Tristan da Cunha")){
            Utils.fetchSvg(context, "https://restcountries.eu/data/shn.svg", coinImages);
        }else if(coinname.equals("Saint Pierre and Miquelon")){
            Utils.fetchSvg(context, "https://restcountries.eu/data/spm.svg", coinImages);
        }else if(coinname.equals("Slovenia")){
            Utils.fetchSvg(context, "https://restcountries.eu/data/svn.svg", coinImages);
        }else if(coinname.equals("Taiwan")){
            Utils.fetchSvg(context, "https://restcountries.eu/data/twn.svg", coinImages);
        }else{
            String userAvatarUrl = countryModels.get(position).getImage();
            Utils.fetchSvg(context, userAvatarUrl, coinImages);

        }

        return convertView;
    }


}
