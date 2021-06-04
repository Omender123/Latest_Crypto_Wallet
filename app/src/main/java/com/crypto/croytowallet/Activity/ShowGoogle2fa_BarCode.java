package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.WriterException;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowGoogle2fa_BarCode extends AppCompatActivity {
    Button google_auth,copy,done;
    KProgressHUD progressDialog;
    EditText editText;
    TextView googlekeySet;
    String key_url,key;
    ImageView qrImage;
    UserData userData;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_google2fa__bar_code);
        qrImage = findViewById(R.id.qrPlaceHolder);
        google_auth=findViewById(R.id.skip_btn);
        done=findViewById(R.id.Done_btn);
        copy=findViewById(R.id.copy);
        editText=findViewById(R.id.enter_GoogleAuth);
        googlekeySet=findViewById(R.id.setgooglekey);

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        sharedPreferences = getSharedPreferences("GoogleKey", MODE_PRIVATE);


        key_url  = sharedPreferences.getString("oauthKey", "");
        key  = sharedPreferences.getString("key", "");


        googlekeySet.setText(key);
        copy.setOnClickListener(new View.OnClickListener() { // set onclick listener to my textview
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(googlekeySet.getText().toString());
                Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();
            }
        });

        QRGEncoder qrgEncoder = new QRGEncoder(key_url,null, QRGContents.Type.TEXT,500);
        try {
            Bitmap qrBits = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(qrBits);
        } catch (WriterException e) {
            e.printStackTrace();
        }



        google_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String token = editText.getText().toString().trim();

                if (token.isEmpty()){
                    editText.requestFocus();

                    Snackbar warningSnackBar = Snacky.builder()
                            .setActivity(ShowGoogle2fa_BarCode.this)
                            .setText("Please enter Google Authentication Token")
                            .setTextColor(getResources().getColor(R.color.black))
                            .setDuration(Snacky.LENGTH_SHORT)
                            .error();
                    warningSnackBar.show();
                }else{
                    GoogleAuthVerify(view);
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowGoogle2fa_BarCode.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
    public void GoogleAuthVerify(View view) {
        String token = editText.getText().toString().trim();
        String username = userData.getUsername();
        progressDialog = KProgressHUD.create(ShowGoogle2fa_BarCode.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().GoogleAuthVerify(username,token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();

                String s = null;
                if (response.code() == 200) {
                    hideKeyboard(view);
                    showRightCustomDialog();
                    google_auth.setVisibility(View.GONE);
                    done.setVisibility(View.VISIBLE);

                } else if (response.code() == 400) {
                    hideKeyboard(view);
                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");
                        showWrongCustomDialog();
                        Snacky.builder()
                                .setView(view)
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
                hideKeyboard(view);
                Snacky.builder()
                        .setView(view)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void showRightCustomDialog() {
        ImageView imageView,close;
        TextView textView,text_dialog;
        final Dialog dialog = new Dialog(ShowGoogle2fa_BarCode.this);
        dialog.setContentView(R.layout.custom_dailog);

        imageView = (ImageView) dialog.findViewById(R.id.imageView4);
        text_dialog =(TextView) dialog.findViewById(R.id.text_dialog);
        close =  (ImageView) dialog.findViewById(R.id.close);

        imageView.setImageResource(R.drawable.ic_green_google);
        //Now we need an AlertDialog.Builder object
        text_dialog.setText("your Google Authentication Key has been  verified");
        //setting the view of the builder to our custom view that we already inflated


        //finally creating the alert dialog and displaying it
        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }

    private void showWrongCustomDialog() {
        ImageView imageView,close;
        TextView textView,text_dialog;

        final Dialog dialog = new Dialog(ShowGoogle2fa_BarCode.this);
        dialog.setContentView(R.layout.custom_dailog);

        imageView =dialog.findViewById(R.id.imageView4);
        close =dialog.findViewById(R.id.close);
        textView=dialog.findViewById(R.id.textView5);
        imageView.setImageResource(R.drawable.ic_red_google);
        text_dialog =dialog.findViewById(R.id.text_dialog);

        textView.setText("Sorry");
        text_dialog.setText("your Google Authentication Key has been not verified");


        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    public void back(View view) {
        onBackPressed();
    }
}