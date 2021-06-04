package com.crypto.croytowallet.Extra_Class;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.crypto.croytowallet.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
public class MyMarkerView extends MarkerView {

    private final TextView tvContent;
     SharedPreferences sharedPreferences;
     String CurrencySymbols;
    private final DecimalFormat format;
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tvContent);
        sharedPreferences = context.getSharedPreferences("currency",0);
        CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");
        format = new DecimalFormat("###.#####");

    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

      /*  if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText(CurrencySymbols+Utils.formatNumber(ce.getHigh(), 0, true));
        } else {

            tvContent.setText(CurrencySymbols+Utils.formatNumber(e.getY(), 0, true));
        }*/

 tvContent.setText( CurrencySymbols+format.format(e.getY()));
        super.refreshContent(e, highlight);

    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}