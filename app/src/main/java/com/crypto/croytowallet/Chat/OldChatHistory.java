package com.crypto.croytowallet.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Adapter.TickectChatAdapter;
import com.crypto.croytowallet.Interface.MessageClickListner;
import com.crypto.croytowallet.Model.TicketChatModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
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

public class OldChatHistory extends AppCompatActivity implements MessageClickListner {
    TextView first_textUsername,fullName;
    String message,sendername,messageId;
    KProgressHUD progressDialog;
    UserData userData;
    RecyclerView chatRecyclerView;
    TickectChatAdapter tickectChatAdapter;
    ArrayList<TicketChatModel> ticketChatModels;
    SharedPreferences  sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_chat_history);
         first_textUsername = findViewById(R.id.userFirsttext);
        fullName = findViewById(R.id.username);
        chatRecyclerView = findViewById(R.id.recycler_view);
        sharedPreferences=getSharedPreferences("chathistory", Context.MODE_PRIVATE);

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        ticketChatModels = new ArrayList<TicketChatModel>();

        getAllChat();
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    public void getAllChat(){

        String token = userData.getToken();
        String id = sharedPreferences.getString("chatId","");

        progressDialog = KProgressHUD.create(OldChatHistory.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getALLChat(token,id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();
                String s = null;
                if (response.code()==200){
                    try {
                        s=response.body().string();

                        JSONObject object = new JSONObject(s);


                        String array = object.getString("result");
                        JSONArray jsonArray = new JSONArray(array);
                        ticketChatModels.clear();
                        for (int i=0;i<=jsonArray.length();i++){
                            JSONObject  object1 =jsonArray.getJSONObject(i);
                            TicketChatModel ticketChatModel1 = new TicketChatModel();
                            String role= object1.getString("role");
                            String id = object1.getString("_id");
                            String time= object1.getString("createdAt");

                            ticketChatModel1.setRoleId(role);
                            ticketChatModel1.setTime(time);
                            ticketChatModel1.setMessageId(id);

                            if (role.equals("incomingMessages")){
                                String in_message= object1.getString("incomingMessage");
                                sendername = object1.getString("senderName");
                                first_textUsername.setText(sendername);
                                fullName.setText(sendername);
                                ticketChatModel1.setMessage(in_message);
                            }else {
                                String out_message= object1.getString("outgoingMessage");
                                ticketChatModel1.setMessage(out_message);
                            }

                            ticketChatModels.add(ticketChatModel1);
                            //  Log.d("s1",role);

                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    tickectChatAdapter = new TickectChatAdapter(getApplicationContext(),ticketChatModels,OldChatHistory.this);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setStackFromEnd(true);
                    tickectChatAdapter.notifyDataSetChanged();
                    tickectChatAdapter.update(ticketChatModels);
                    chatRecyclerView.setLayoutManager(linearLayoutManager);
                    chatRecyclerView.setHasFixedSize(true);
                    chatRecyclerView.setAdapter(tickectChatAdapter);


                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(OldChatHistory.this)
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
                            .setActivity(OldChatHistory.this)
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
                        .setActivity(OldChatHistory.this)
                        .setText("Internet Problem ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

            }
        });
    }

    @Override
    public void onLongItemClick(int position) {

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