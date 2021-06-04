package com.crypto.croytowallet.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Adapter.OldChatAdapter;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.Model.OldChatModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.TransactionHistory.Transaction_history;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OldChat extends AppCompatActivity implements HistoryClickLister {
RecyclerView recyclerView;
TextView empty_text;
ArrayList<OldChatModel>oldChatModels;
OldChatAdapter oldChatAdapter;
    KProgressHUD progressDialog;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_chat);
        recyclerView = findViewById(R.id.recycler_Old_chat);
        empty_text = findViewById(R.id.txt_list_is_empty);

        oldChatModels = new ArrayList<OldChatModel>();
        getChatHistory();
        sharedPreferences=getSharedPreferences("chathistory", Context.MODE_PRIVATE);

    }

    public void getChatHistory() {
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        progressDialog = KProgressHUD.create(OldChat.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getChatHistory(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();
                String s = null;
                if (response.code()==200){
                    try {
                        s=response.body().string();

                        JSONObject object = new JSONObject(s);
                        String result=object.getString("result");
                        JSONArray jsonArray = new JSONArray(result);

                        for (int i=0;i<=jsonArray.length();i++){
                            JSONObject object1 = jsonArray.getJSONObject(i);

                            OldChatModel chatModel = new OldChatModel();

                            String chatid = object1.getString("_id");
                            String userid = object1.getString("userId");
                            String username = object1.getString("username");
                            String date = object1.getString("createdAt");

                            chatModel.setChatId(chatid);
                            chatModel.setUserId(userid);
                            chatModel.setUsername(username);
                            chatModel.setDate(date);

                            oldChatModels.add(chatModel);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    if (oldChatModels!=null && oldChatModels.size()>0){
                        oldChatAdapter = new OldChatAdapter(oldChatModels,getApplicationContext(),OldChat.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(oldChatAdapter);
                    }else {

                        empty_text.setVisibility(View.VISIBLE);
                    }

                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");
                        Snacky.builder()
                                .setActivity(OldChat.this)
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
                            .setActivity(OldChat.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(OldChat.this)
                        .setText("Internet Problem ")
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

    @Override
    public void onHistoryItemClickListener(int position) {

        String chatId = oldChatModels.get(position).getChatId();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("chatId",chatId);
        editor.commit();


        startActivity(new Intent(OldChat.this,OldChatHistory.class));
    }
}