package com.crypto.croytowallet.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.Rewards.Rewards;
import com.crypto.croytowallet.SharedPrefernce.SharedRequestResponse;
import com.crypto.croytowallet.TopUp.TopUp_Acknowlegement;

import de.mateware.snacky.Snacky;

public class Complate_payment extends AppCompatActivity implements View.OnClickListener {
    TextView back;
   Button okay;
    TextView showEnteredAmount,amount_in_crypto,amount_in_Currency,trans_hash,trans_status,btncopy,reciverName,txt_rewards;
    CardView Rewards_cards;
    String Erans_Rewards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complate_payment);
        back =findViewById(R.id.toolbar_title);
        showEnteredAmount = findViewById(R.id.amount);
        amount_in_crypto = findViewById(R.id.amount_in_crypto);
        amount_in_Currency = findViewById(R.id.amount_in_currency);
        trans_hash = findViewById(R.id.trans_hash_id);
        trans_status = findViewById(R.id.status);
        reciverName = findViewById(R.id.recriver_address);
        btncopy  =findViewById(R.id.btn_copy);
        Rewards_cards = findViewById(R.id.card_rewards);
        txt_rewards = findViewById(R.id.text_point);
        okay = findViewById(R.id.okay_btn);


        showEnteredAmount.setText( SharedRequestResponse.getInstans(getApplicationContext()).getIMT_U_Amount()+" Utility");
        amount_in_crypto.setText( SharedRequestResponse.getInstans(getApplicationContext()).getIMT_U_Amount()+" Utility");
        amount_in_Currency.setText( SharedRequestResponse.getInstans(getApplicationContext()).getEnter_Amount());
        trans_hash.setText( SharedRequestResponse.getInstans(getApplicationContext()).getTransferId());
        reciverName.setText( SharedRequestResponse.getInstans(getApplicationContext()).getRequest_ID());

        Erans_Rewards =  SharedRequestResponse.getInstans(getApplicationContext()).getRewards();

        if (!Erans_Rewards.equals("null") || !Erans_Rewards.isEmpty()){
            Reward_box();
        }


        back.setOnClickListener(this);
        btncopy.setOnClickListener(this);
        okay.setOnClickListener(this);
        Rewards_cards.setOnClickListener(this);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Complate_payment.this, MainActivity.class));
        SharedRequestResponse.getInstans(getApplicationContext()).RemoveData();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.toolbar_title:
                startActivity(new Intent(Complate_payment.this, MainActivity.class));
                SharedRequestResponse.getInstans(getApplicationContext()).RemoveData();
            break;

            case  R.id.btn_copy:
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(trans_hash.getText().toString());
                Snacky.builder()
                        .setActivity(Complate_payment.this)
                        .setText("Copied")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .success()
                        .show();
                break;

            case  R.id.okay_btn:
                startActivity(new Intent(Complate_payment.this, MainActivity.class));
                SharedRequestResponse.getInstans(getApplicationContext()).RemoveData();
                break;

            case  R.id.card_rewards:
                startActivity(new Intent(Complate_payment.this, Rewards.class));
                break;

        }
    }

    private void Reward_box() {
        TextView text_point;
        ScratchView scratchView;

        final Dialog dialog = new Dialog(Complate_payment.this);
        dialog.setContentView(R.layout.reward_dialog_box);
        text_point=dialog.findViewById(R.id.text_point1);
        scratchView=dialog.findViewById(R.id.scratchView);
        text_point.setText("You've Earns IMX Points \n "+Erans_Rewards+" Pt");

        scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                //  Toast.makeText(getContext(), "Revealed!", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        txt_rewards.setText(Erans_Rewards);
                        Rewards_cards.setVisibility(View.VISIBLE);
                    }
                },2000);


            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                if (percent>=0.5) {
                    Log.d("Reveal Percentage", "onRevealPercentChangedListener: " + String.valueOf(percent));
                    scratchView.clear();
                }
            }
        });

        dialog.show();
        dialog.setCancelable(false);





    }
}
