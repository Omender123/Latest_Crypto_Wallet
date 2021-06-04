package com.crypto.croytowallet.TopUp;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.crypto.croytowallet.Extra_Class.ApiResponse.ResponseBankDetails;
import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedBankDetails;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    String mString,received_Amount,show;
    TextView bank_name, bank_acc_no, bank_ifsc, bank_ho_name;
    ResponseBankDetails responseBankDetails;
    Button transfer;

    Double totalAmount;



    public static MyBottomSheetDialogFragment newInstance(String string) {
        MyBottomSheetDialogFragment f = new MyBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString("string", string);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mString = getArguments().getString("string");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_modal, container, false);
        bank_name = v.findViewById(R.id.bank_name);
        bank_acc_no = v.findViewById(R.id.bank_account_no);
        bank_ho_name = v.findViewById(R.id.bank_holder_name);
        bank_ifsc = v.findViewById(R.id.Ifsc_code);
        transfer = v.findViewById(R.id.show_dailog);

        responseBankDetails = SharedBankDetails.getInstance(getContext()).getBankDetails();
        received_Amount = MyPreferences.getInstance(getActivity()).getString(PrefConf.RECEIVED_AMOUNT,"0");
        show = MyPreferences.getInstance(getActivity()).getString(PrefConf.TOP_UP_TYPE1,"QrCode");
        if (show.equalsIgnoreCase("QrCode")){
            transfer.setVisibility(View.GONE);
        }

        bank_name.setOnClickListener(this);
        bank_acc_no.setOnClickListener(this);
        bank_ho_name.setOnClickListener(this);
        bank_ifsc.setOnClickListener(this);
        transfer.setOnClickListener(this);



        bank_name.setText(responseBankDetails.getBankName());
        bank_acc_no.setText(responseBankDetails.getAccountNo());
        bank_ho_name.setText(responseBankDetails.getAccountName());
        bank_ifsc.setText(responseBankDetails.getIFSCcode());
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bank_name:
                ClipboardManager cm1 = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm1.setText(responseBankDetails.getBankName());
                Toast.makeText(getContext(), "Copied ", Toast.LENGTH_SHORT).show();

                break;
            case R.id.bank_account_no:
                ClipboardManager cm2 = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm2.setText(responseBankDetails.getAccountNo());
                Toast.makeText(getContext(), "Copied ", Toast.LENGTH_SHORT).show();

                break;
            case R.id.bank_holder_name:
                ClipboardManager cm3 = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm3.setText(responseBankDetails.getAccountName());
                Toast.makeText(getContext(), "Copied ", Toast.LENGTH_SHORT).show();

                break;
            case R.id.Ifsc_code:
                ClipboardManager cm4 = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm4.setText(responseBankDetails.getIFSCcode());
                Toast.makeText(getContext(), "Copied ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.show_dailog:
                totalAmount = Double.valueOf(received_Amount);

                startActivity(new Intent(getContext(),EnterTop_Up.class).putExtra("totalAmount",totalAmount));
                break;
        }

    }
}

