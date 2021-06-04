package com.crypto.croytowallet.TransactionPin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.signup.GmailCorrection;
import com.crypto.croytowallet.signup.SignUp;


public class TransactionPin extends AppCompatActivity {
    private EditText e1, e2, e3, e4, e5, e6, e11, e22, e33, e44, e55, e66;
   // Pinview enterPin,confirmPin;
   SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_pin);
       init();

      /*  enterPin = findViewById(R.id.pinview);
        confirmPin = findViewById(R.id.pinview1);
*/
        sharedPreferences=getSharedPreferences("MyData", Context.MODE_PRIVATE);
    }

    public void Trnasaction_Pin(View view) {

        com();

    }

    public void init()
  {

        e1=findViewById(R.id.otp_edit_box1);
        e2=findViewById(R.id.otp_edit_box2);
        e3=findViewById(R.id.otp_edit_box3);
        e4=findViewById(R.id.otp_edit_box4);
        e5=findViewById(R.id.otp_edit_box5);
        e6=findViewById(R.id.otp_edit_box6);
        e11=findViewById(R.id.otp_edit_box11);
        e22=findViewById(R.id.otp_edit_box22);
        e33=findViewById(R.id.otp_edit_box33);
        e44=findViewById(R.id.otp_edit_box44);
        e55=findViewById(R.id.otp_edit_box55);
        e66=findViewById(R.id.otp_edit_box66);



        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e2.requestFocus();

                }
            }
        });
        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e3.requestFocus();
                }
            }
        });
        e3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e4.requestFocus();
                }
            }
        });
        e4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e5.requestFocus();
                }
            }
        });
        e5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e6.requestFocus();
                }
            }
        });
        e6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e11.requestFocus();
                }
            }
        });
        e11.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e22.requestFocus();
                }
            }
        });
        e22.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e33.requestFocus();
                }
            }
        });
        e33.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e44.requestFocus();
                }
            }
        });
        e44.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e55.requestFocus();
                }
            }
        });
        e55.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e66.requestFocus();
                }
            }
        });
        e66.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    e66.requestFocus();
                }
            }
        });



    }


 public void com() {

        String a1=e1.getText().toString();
        String a2=e2.getText().toString();
        String a3=e3.getText().toString();
        String a4=e4.getText().toString();
        String a5=e5.getText().toString();
        String a6=e6.getText().toString();
        String a11=e11.getText().toString();
        String a22=e22.getText().toString();
        String a33=e33.getText().toString();
        String a44=e44.getText().toString();
        String a55=e55.getText().toString();
        String a66=e66.getText().toString();

        String first=a1+a2+a3+a4+a5+a6;
        String second=a11+a22+a33+a44+a55+a66;

     if (first.isEmpty() || second.isEmpty()){
         Toast.makeText(this, "Please enter Transaction pin", Toast.LENGTH_SHORT).show();
      }
     else if (first.equals(second))
        {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("transaction",first);
            editor.commit();

            Intent intent =new Intent(TransactionPin.this,ShowMnemonic.class);
            startActivity(intent);


        }else {

            Toast.makeText(this, "Transaction Pin isD Don't Match", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       onSaveInstanceState(new Bundle());
    }
}


