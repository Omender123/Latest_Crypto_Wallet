package com.crypto.croytowallet.Rewards;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chaos.view.PinView;
import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.ImtSmart.SwapEnterPin;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedBankDetails;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.TopUp.EnterTop_Up;
import com.crypto.croytowallet.TopUp.MyBottomSheetDialogFragment;
import com.crypto.croytowallet.database.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionPinBottomSheet  extends BottomSheetDialogFragment {
    String mString,transactionPin,Amount,token;
    PinView pinView;
    Button show_dailog;
    KProgressHUD progressDialog;
    UserData userData;

    public static TransactionPinBottomSheet newInstance(String string) {

        TransactionPinBottomSheet f = new TransactionPinBottomSheet();
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
        View v = inflater.inflate(R.layout.transactionpincustomsheet, container, false);

        pinView = v.findViewById(R.id.enterNew_pin);
        show_dailog= v.findViewById(R.id.confirm_redeem);

        userData = SharedPrefManager.getInstance(getContext()).getUser();
        token = userData.getToken();
        Amount = MyPreferences.getInstance(getContext()).getString(PrefConf.REDEEM_AMOUNT,"0");

        show_dailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactionPin = pinView.getText().toString();
                AppUtils.hideKeyboard(v,getContext());
                if (transactionPin.isEmpty()){
                       Toast.makeText(getContext(), "Please enter Transaction Pin !!!!", Toast.LENGTH_SHORT).show();
                }else{
                   Done(transactionPin,Amount);
                }
            }
        });

        return v;
    }

    private void Done(String transactionPin, String amount) {
        progressDialog = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().SwapRewards(token,amount,transactionPin);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                hidepDialog();
                if (response.isSuccessful() && response.code() == 200) {

                    AppUtils.showMessageOKCancel("Successfully Swap iMX Point in iMX Utility ", getActivity(), false, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);

                        }
                    });




            }else  if(response.code()==401||response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();

                Toast.makeText(getContext(), ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
