package com.crypto.croytowallet.TopUp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Extra_Class.ApiResponse.ResponseBankDetails;
import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedBankDetails;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.WriterException;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import de.mateware.snacky.Snacky;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowTop_UP extends AppCompatActivity {
    ImageView qrImage;
    TextView barcodeAddress;
    KProgressHUD progressDialog;
    Button transfer;
    TextView text_send,bank_name,bank_acc_no;

    Double totalAmount;
    BottomSheetDialogFragment myBottomSheet;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_top__u_p);
        barcodeAddress = findViewById(R.id.barCodeAddress);
        qrImage = findViewById(R.id.qrPlaceHolder);
        transfer = findViewById(R.id.show_dailog);
        text_send = findViewById(R.id.txt_send_amount);
        bank_name = findViewById(R.id.bank_name);
        bank_acc_no = findViewById(R.id.bank_account_no);

        myBottomSheet = MyBottomSheetDialogFragment.newInstance("Modal Bottom Sheet");


        try{
            Bundle bundle = getIntent().getExtras();
             totalAmount = bundle.getDouble("totalAmount");

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);

           String total =df.format(totalAmount);
            text_send.setText("You will receive "+total+" IMT-Utility in the wallet");
        }catch (Exception e){}



        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EnterTop_Up.class).putExtra("totalAmount",totalAmount));
            }
        });

        Show_Details();


    }

    private void Show_Details() {
        progressDialog = KProgressHUD.create(ShowTop_UP.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        UserData userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        Call<ResponseBankDetails> bankDetailsCall = RetrofitClient.getInstance().getApi().getBankDetails(userData.getToken());
        bankDetailsCall.enqueue(new Callback<ResponseBankDetails>() {
            @Override
            public void onResponse(Call<ResponseBankDetails> call, Response<ResponseBankDetails> response) {
                hidepDialog();

                String  s = null;
                if (response.isSuccessful()){

                   ResponseBankDetails   responseBankDetails = response.body();

                   barcodeAddress.setText(responseBankDetails.getUpi());
                    bank_acc_no.setText(responseBankDetails.getAccountNo());
                    bank_name.setText(responseBankDetails.getBankName());

                    barcodeAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            cm.setText(responseBankDetails.getUpi());
                            Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();

                        }
                    });

                    QRGEncoder qrgEncoder = new QRGEncoder(responseBankDetails.getUpi(),null, QRGContents.Type.TEXT,500);
                    try {
                        Bitmap qrBits = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(qrBits);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                   ResponseBankDetails  responseBankDetails1 =new ResponseBankDetails(responseBankDetails.getAccountName(),responseBankDetails.getAccountNo(),responseBankDetails.getBankName(),responseBankDetails.getIFSCcode());

                    SharedBankDetails.getInstance(getApplicationContext()).SetBankDetails(responseBankDetails1);


                }else{

                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(ShowTop_UP.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
                 }

            @Override
            public void onFailure(Call<ResponseBankDetails> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(ShowTop_UP.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

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

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    public void show(View view) {

        MyPreferences.getInstance(getApplicationContext()).putString(PrefConf.RECEIVED_AMOUNT, String.valueOf(totalAmount));
        MyPreferences.getInstance(getApplicationContext()).putString(PrefConf.TOP_UP_TYPE1,"QrCode");
        myBottomSheet.show(getSupportFragmentManager(), myBottomSheet.getTag());


    }
}