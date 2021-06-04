package com.crypto.croytowallet.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crypto.croytowallet.Activity.Support;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatOptions extends AppCompatActivity {
    RelativeLayout newChat,oldChat;
    SharedPreferences sharedPreferences;
    String status;

    CardView new_chat,old_chat;
    Animation enterright;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_options);
        newChat = findViewById(R.id.Mobile_support);
        oldChat = findViewById(R.id.Mobile_support1);
        new_chat =findViewById(R.id.new_chat);
        old_chat = findViewById(R.id.old_chat);

        enterright = AnimationUtils.loadAnimation(ChatOptions.this, R.anim.slide_in_left);
        new_chat.startAnimation(enterright);
        old_chat.startAnimation(enterright);



        sharedPreferences = getSharedPreferences("season",0);

        newChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = sharedPreferences.getString("status","false");
               // Toast.makeText(getApplicationContext(), ""+status, Toast.LENGTH_SHORT).show();
                ChatActive();
                if (status.equals("true") ){
                 newChatSeason();
                }
                Intent i = new Intent(ChatOptions.this, TicketChat.class);
                startActivity(i);
            }
        });

        oldChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Snacky.builder()
                        .setActivity(ChatOptions.this)
                        .setText("Coming Up Features")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setDuration(Snacky.LENGTH_SHORT)
                        .success()
                        .show();*/

                Intent i = new Intent(ChatOptions.this, OldChat.class);
                startActivity(i);
            }
        });
    }

    public void back(View view) {
       // startActivity(new Intent(getApplicationContext(), Support.class));
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    public void ChatActive(){
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().ChatActive(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                String s = null;
                if (response.code()==200){
                    try {
                        s=response.body().string();
                        //  Log.d("support",s);
                        //  Toast.makeText(Support.this, ""+s, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(ChatOptions.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==401){

                    Snacky.builder()
                            .setActivity(ChatOptions.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snacky.builder()
                        .setActivity(ChatOptions.this)
                        .setText("Internet Problem ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }


    public void newChatSeason(){
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().newChatSeason(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                if (response.code()==200){
                    try {
                        s=response.body().string();
                       // Log.d("support",s);
                      //   Toast.makeText(ChatOptions.this, ""+s, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(ChatOptions.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==401){

                    Snacky.builder()
                            .setActivity(ChatOptions.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snacky.builder()
                        .setActivity(ChatOptions.this)
                        .setText("Internet Problem ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }


}