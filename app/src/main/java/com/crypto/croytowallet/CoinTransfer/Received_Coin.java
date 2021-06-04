package com.crypto.croytowallet.CoinTransfer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Extra_Class.ApiResponse.PublicKeyResponse;
import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.Updated_data;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
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

public class
Received_Coin extends AppCompatActivity implements View.OnClickListener {
    TextView barcodeAddress,toolbar_title,address,address1;
    ImageView qrImage,imageView;
    CardView barCodeshare;
    String coinId,publicKey,coinId1;
    UserData userData;
    KProgressHUD progressDialog;
    PublicKeyResponse publicKeyResponses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received__coin);
        imageView =findViewById(R.id.back);
        barcodeAddress=findViewById(R.id.barCodeAddress);
        qrImage = findViewById(R.id.qrPlaceHolder);
        barCodeshare=findViewById(R.id.barCodeshare);
        toolbar_title=findViewById(R.id.toolbar_title);
        address=findViewById(R.id.address);
        address1=findViewById(R.id.address1);

        userData= SharedPrefManager.getInstance(this).getUser();

       coinId1 = Updated_data.getInstans(getApplicationContext()).getmobile();

        coinId = MyPreferences.getInstance(getApplicationContext()).getString(PrefConf.TOKEN_TYPE,"");

        back();



         address.setText("Your "+coinId1.toUpperCase()+" Address");
         address1.setText("Tab "+coinId1.toUpperCase()+" Address to Copy");
        toolbar_title.setText("Receive "+coinId1.toUpperCase());

        barcodeAddress.setOnClickListener(this);
        barCodeshare.setOnClickListener(this);



       getPublicKey(userData.getToken(),coinId);

        }

    public void getPublicKey(String token, String CoinId) {

        progressDialog = KProgressHUD.create(Received_Coin.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<PublicKeyResponse> call = RetrofitClient.getInstance().getApi().GetPublicKey(token,CoinId);

       call.enqueue(new Callback<PublicKeyResponse>() {
           @Override
           public void onResponse(Call<PublicKeyResponse> call, Response<PublicKeyResponse> response) {
              hidepDialog();
              String s = null;
               if (response.isSuccessful()){
                   publicKeyResponses=response.body();

                   if (publicKeyResponses!=null){
                       publicKey = publicKeyResponses.getPublicKey();
                      QRGEncoder qrgEncoder = new QRGEncoder(publicKey, null, QRGContents.Type.TEXT, 500);
                       try {
                           Bitmap qrBits = qrgEncoder.encodeAsBitmap();
                           qrImage.setImageBitmap(qrBits);
                       } catch (WriterException e) {
                           e.printStackTrace();
                       }
                       barcodeAddress.setText(publicKey);


                   }else{
                       publicKey="";
                       Snacky.builder()
                               .setActivity(Received_Coin.this)
                               .setText("Public Key Not Generate to that coin ")
                               .setDuration(Snacky.LENGTH_SHORT)
                               .setActionText(android.R.string.ok)
                               .error()
                               .show();
                   }
               }else {

                   try {
                       s=response.errorBody().string();
                       JSONObject jsonObject1=new JSONObject(s);
                       String error =jsonObject1.getString("error");

                       Snacky.builder()
                               .setActivity(Received_Coin.this)
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
           public void onFailure(Call<PublicKeyResponse> call, Throwable t) {
               hidepDialog();
               Snacky.builder()
                       .setActivity(Received_Coin.this)
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
    public void onBackPressed() {
        super.onBackPressed();
    onSaveInstanceState(new Bundle());


    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.barCodeAddress:
                if (!publicKey.equals("")){
                    ClipboardManager cm = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(publicKey);
                    Snacky.builder()
                            .setActivity(Received_Coin.this)
                            .setText("Copied")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .success()
                            .show();

                }else{
                    Snacky.builder()
                            .setActivity(Received_Coin.this)
                            .setText("Not Public key Found" )
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }
                break;

            case  R.id.barCodeshare:
                if (!publicKey.equals("")){
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_TEXT, publicKey);
                    startActivity(Intent.createChooser(i, "Share With"));

                }else{
                    Snacky.builder()
                            .setActivity(Received_Coin.this)
                            .setText("Yet be Not Public key share" )
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }
                break;
        }
    }
}