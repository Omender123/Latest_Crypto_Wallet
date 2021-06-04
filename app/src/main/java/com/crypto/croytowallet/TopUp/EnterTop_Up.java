package com.crypto.croytowallet.TopUp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Extra_Class.ApiResponse.SendAddAmountRequestResponse;
import com.crypto.croytowallet.Extra_Class.ImagePath;
import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SharedRequestResponse;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.Extra_Class.Utility;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.mateware.snacky.Snacky;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterTop_Up extends AppCompatActivity {
    EditText ed_bank_name, ed_acc_no, ed_holder_name, ed_transactionId, ed_upi_id, ed_Amount;
    Spinner sp_currency;
    ArrayList<String> Currency;
    Button done;
    String BankName, Acc_no, Holder_name, trans_id, Upi_Id, Amount, options, currencyType;
    UserData userData;
    KProgressHUD progressDialog;
    TextView chooseFile,text_currency;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView ivImage;
    private String userChoosenTask;
    File file;
    String total;
    Double totalAmount;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_top__up);
        ed_bank_name = findViewById(R.id.bank_name);
        ed_acc_no = findViewById(R.id.acc_no);
        ed_holder_name = findViewById(R.id.holder_name);
        ed_transactionId = findViewById(R.id.trans_id);
        ed_upi_id = findViewById(R.id.upi_id);
        ed_Amount = findViewById(R.id.enter_amount);
        sp_currency = findViewById(R.id.select_currency);
        text_currency = findViewById(R.id.txt_currency);
        done = findViewById(R.id.show_dailog);
        chooseFile = findViewById(R.id.chooseFile);
        ivImage = findViewById(R.id.setImageView);


        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        Currency = new ArrayList<String>();



        options= MyPreferences.getInstance(getApplicationContext()).getString(PrefConf.TOP_UP_TYPE,"");

        if (options.equalsIgnoreCase("bank")){
            sp_currency.setVisibility(View.VISIBLE);
            getCurrency();
            text_currency.setVisibility(View.GONE);

        }else if (options.equalsIgnoreCase("qrCode")){
            currencyType="THB";

        }

        try{
            Bundle bundle = getIntent().getExtras();
            Double totalAmount = bundle.getDouble("totalAmount");

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(8);

            total =df.format(totalAmount);

        }catch (Exception e){}



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankName = ed_bank_name.getText().toString().trim();
                Acc_no = ed_acc_no.getText().toString().trim();
                Holder_name = ed_holder_name.getText().toString().trim();
                trans_id = ed_transactionId.getText().toString().trim();
                Upi_Id = ed_upi_id.getText().toString().trim();
                Amount = ed_Amount.getText().toString().trim();

                if (BankName.isEmpty() || Acc_no.isEmpty() || Holder_name.isEmpty() || trans_id.isEmpty() || Amount.isEmpty()) {
                    Snacky.builder()
                            .setActivity(EnterTop_Up.this)
                            .setText("Please filled all required details")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .warning()
                            .show();

                } else {
                    PaymentDone(BankName, Acc_no, Holder_name, trans_id, Amount, currencyType,total,file);
                   // Toast.makeText(EnterTop_Up.this, ""+currencyType, Toast.LENGTH_SHORT).show();
                }


            }

        });
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    public void PaymentDone(String bankName, String acc_no, String holder_name, String trans_id, String amount, String currencyType,String  utility,File file ) {

        String token = userData.getToken();

        progressDialog = KProgressHUD.create(EnterTop_Up.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();


        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part image =
                MultipartBody.Part.createFormData("attachement", file.getName(), requestFile);

        RequestBody bank_Name =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), bankName);
        RequestBody Acc_no =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),acc_no);

        RequestBody Holder_name1 =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), holder_name);
        RequestBody Trans_id =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),trans_id);
        RequestBody Amount1 =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), amount);
        RequestBody Currency_Type =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),currencyType);
        RequestBody utility1 =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),utility);

        Call<SendAddAmountRequestResponse> call = RetrofitClient.getInstance().getApi().SendAddAmountRequest(token,bank_Name,Acc_no,Holder_name1,Trans_id,Amount1,Currency_Type,utility1,image);

        call.enqueue(new Callback<SendAddAmountRequestResponse>() {
            @Override
            public void onResponse(Call<SendAddAmountRequestResponse> call, Response<SendAddAmountRequestResponse> response) {
                hidepDialog();
                String s = null;
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        SendAddAmountRequestResponse response1 = response.body();

                        SharedRequestResponse.getInstans(getApplicationContext()).SetData(
                                response1.getTransactionId(),response1.getId(),response1.getAmount(), String.valueOf(response1.getUtility()),response1.getStatus(),response1.getCurrency(),"");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(), TopUp_Acknowlegement.class));
                            }
                        },500);


                    }
                  /*  try {
                        s = response.body().string();
                        JSONObject  object = new JSONObject(s);
                        String trans_id =object.getString("transactionId");
                        String request_id =object.getString("_id");
                        String EnterAmount =object.getString("amount");
                        String Status =object.getString("status");
                        String currency =object.getString("currency");

                        SharedRequestResponse.getInstans(getApplicationContext()).SetData(trans_id,request_id,EnterAmount,total,Status,currency);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(), TopUp_Acknowlegement.class));
                            }
                        },500);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }*/



                } else {
                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(EnterTop_Up.this)
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
            public void onFailure(Call<SendAddAmountRequestResponse> call, Throwable t) {
                hidepDialog();

                Snacky.builder()
                        .setActivity(EnterTop_Up.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    public void getCurrency() {
        String token = userData.getToken();

        progressDialog = KProgressHUD.create(EnterTop_Up.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getAllCurrency(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();
                String s = null;
                Currency.clear();
                if (response.isSuccessful()) {
                    try {
                        s = response.body().string();

                        JSONObject object = new JSONObject(s);
                        String result = object.getString("currency");
                        JSONArray jsonArray = new JSONArray(result);

                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            String currency = object1.getString("currency");

                            Currency.add(currency);

                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter<String> adp = new ArrayAdapter<String>(EnterTop_Up.this, android.R.layout.simple_spinner_dropdown_item, Currency);
                    sp_currency.setAdapter(adp);

                    sp_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            currencyType = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } else {
                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(EnterTop_Up.this)
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();

                Snacky.builder()
                        .setActivity(EnterTop_Up.this)
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(EnterTop_Up.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(EnterTop_Up.this);
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
        Uri uri = data.getData();
        System.out.println("urii  "+uri +" "+uri.getPath());
        String path  = ImagePath.getPath(EnterTop_Up.this,uri);
        System.out.println("urii path "+path );
        if(path!=null && !path.equals("")) {
            file = new File(path);

        }

    }


}