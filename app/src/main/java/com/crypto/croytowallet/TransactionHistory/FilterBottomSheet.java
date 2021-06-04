package com.crypto.croytowallet.TransactionHistory;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FilterBottomSheet extends BottomSheetDialogFragment {
    TextView exit, Clear_All;
    TextView startDate, endDate;
    CheckBox L_to_H, H_to_L;
    RadioGroup radioGroup, radioGroup1;
    CardView find;
    RadioButton month_1, day7, to1, to200, to500;
    String short1 , lowAmount, highAmount, startDate1, endDate1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        //inflating layout
        View view = View.inflate(getContext(), R.layout.filter_bottomsheet_layout, null);
        exit = view.findViewById(R.id.exit);
        startDate = view.findViewById(R.id.startDate);
        endDate = view.findViewById(R.id.endDate);
        Clear_All = view.findViewById(R.id.clear_all);
        L_to_H = view.findViewById(R.id.L_to_H);
        H_to_L = view.findViewById(R.id.H_to_L);
        find = view.findViewById(R.id.find);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup1 = view.findViewById(R.id.radioGroup1);
        month_1 = view.findViewById(R.id.month);
        day7 = view.findViewById(R.id.day);
        to1 = view.findViewById(R.id.radio_1to200);
        to200 = view.findViewById(R.id.radio_201to500);
        to500 = view.findViewById(R.id.radio_500);


        //setting layout with bottom sheet
        bottomSheet.setContentView(view);


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismiss();
            }
        });
        Clear_All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDate.setText("");
                startDate.setText("");
                month_1.setChecked(false);
                day7.setChecked(false);
                to1.setChecked(false);
                to200.setChecked(false);
                to500.setChecked(false);
                L_to_H.setChecked(false);
                H_to_L.setChecked(false);


            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate1 = startDate.getText().toString();
                endDate1 = endDate.getText().toString();

                if(to1.isChecked()){
                    lowAmount = "1";
                    highAmount = "200";
                }else if(to200.isChecked()) {
                    lowAmount = "201";
                    highAmount = "500";
                }else if (to500.isChecked()){
                    lowAmount = "500";
                    highAmount = "NULL";
                }else{
                    lowAmount = "1";
                    highAmount = "NULL";
                }

                if(L_to_H.isChecked()){
                    short1 = "1";
                }else if(H_to_L.isChecked()){
                    short1 = "-1";
                }else{
                    short1 ="1";
                }

                if (startDate1.isEmpty() && endDate1.isEmpty()) {
                        Toast.makeText(getContext(), "Please enter StartDate and EndDate OR Select 1 month OR Select Last Seven Days", Toast.LENGTH_SHORT).show();
                    }

                 else {




                    MyPreferences.getInstance(getActivity()).putString(PrefConf.START_DATE, startDate1);
                    MyPreferences.getInstance(getActivity()).putString(PrefConf.END_DATE, endDate1);
                    MyPreferences.getInstance(getActivity()).putString(PrefConf.LOW_AMOUNT, lowAmount);
                    MyPreferences.getInstance(getActivity()).putString(PrefConf.HIGH_AMOUNT, highAmount);
                    MyPreferences.getInstance(getActivity()).putString(PrefConf.SHORT, short1);
                    MyPreferences.getInstance(getActivity()).putString(PrefConf.CHECK, "true");

                    startActivity(new Intent(getActivity(), Transaction_history.class));



                }


            }
        });


        L_to_H.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (L_to_H.isChecked()) {
                    H_to_L.setChecked(false);
                }

            }
        });

        H_to_L.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (H_to_L.isChecked()) {
                    L_to_H.setChecked(false);
                }
            }
        });
        month_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (month_1.isChecked()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar currentCal = Calendar.getInstance();

                    startDate1 = dateFormat.format(currentCal.getTime());
                    currentCal.add(Calendar.DATE, -30);
                    endDate1 = dateFormat.format(currentCal.getTime());
                    startDate.setText(endDate1);
                    endDate.setText(startDate1);

                }
            }
        });
        day7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (day7.isChecked()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar currentCal = Calendar.getInstance();
                    startDate1 = dateFormat.format(currentCal.getTime());
                    currentCal.add(Calendar.DATE, -7);
                    endDate1 = dateFormat.format(currentCal.getTime());
                    startDate.setText(endDate1);
                    endDate.setText(startDate1);


                }
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerDialog();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerDialog1();
            }
        });
        return bottomSheet;
    }

    private void showPickerDialog() {

        DatePickerDialog dtPickerDlg =  new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
              String  mDate = DateFormat.format("yyyy-MM-dd",c).toString();
               startDate.setText(mDate);
            }
        }, 2020, 01, 01);
        dtPickerDlg.getDatePicker().setSpinnersShown(true);
        dtPickerDlg.getDatePicker().setCalendarViewShown(false);
        dtPickerDlg.setTitle("Select your StartDate");
        dtPickerDlg.show();
    }

    private void showPickerDialog1() {

        DatePickerDialog dtPickerDlg =  new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String  mDate = DateFormat.format("yyyy-MM-dd",c).toString();
                endDate.setText(mDate);
            }
        }, 2020, 01, 01);
        dtPickerDlg.getDatePicker().setSpinnersShown(true);
        dtPickerDlg.getDatePicker().setCalendarViewShown(false);
        dtPickerDlg.setTitle("Select your endDate");
        dtPickerDlg.show();

    }
}
