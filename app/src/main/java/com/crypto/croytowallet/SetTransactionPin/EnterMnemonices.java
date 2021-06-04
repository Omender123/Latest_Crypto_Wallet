package com.crypto.croytowallet.SetTransactionPin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class EnterMnemonices extends AppCompatActivity {
EditText ed_mnemonic;
String mnemonic,getMemonics;
Button done;
UserData userData;
    KProgressHUD progressDialog;
    TextView entr,fllw,sset;
    Animation slide_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mnemonices);
        ed_mnemonic = findViewById(R.id.description);
        done = findViewById(R.id.skip_btn);

        entr =findViewById(R.id.entr_meno);
        fllw=findViewById(R.id.fllw);
        sset=findViewById(R.id.sset);

        //Animation
        slide_right = AnimationUtils.loadAnimation(EnterMnemonices.this,R.anim.slide_in_right);

        entr.startAnimation(slide_right);
        ed_mnemonic.startAnimation(slide_right);
        fllw.startAnimation(slide_right);
        sset.startAnimation(slide_right);
        done.startAnimation(slide_right);

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        getMemonics = userData.getMnemonic();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mnemonic = ed_mnemonic.getText().toString().trim();
                if (mnemonic.isEmpty()){
                    Snacky.builder()
                        .setActivity(EnterMnemonices.this)
                            .setText("Please Enter Mnemonic")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else if(!getMemonics.equalsIgnoreCase(mnemonic)){
                    Snacky.builder()
                        .setActivity(EnterMnemonices.this)
                            .setText("Please Enter Right Mnemonic")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else{
                   resendOTP();
                }
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


    public void resendOTP() {

        String usernames = userData.getUsername();


        progressDialog = KProgressHUD.create(EnterMnemonices.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().sendOtp(usernames);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hidepDialog();

                String s = null;
                if (response.code() == 200) {

                    Intent intent = new Intent(getApplicationContext(),EnterDetails.class);
                    intent.putExtra("CurrentPin","");
                    intent.putExtra("Type","forgetPin");
                    intent.putExtra("mnemonic",mnemonic);
                    startActivity(intent);

                    Toast.makeText(EnterMnemonices.this, "Otp send in your register Email", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 400) {
                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(EnterMnemonices.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                        // Toast.makeText(SignUp.this, jsonObject1.getString("error")+"", Toast.LENGTH_SHORT).show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(EnterMnemonices.this)
                        .setText(t.getMessage())
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



}