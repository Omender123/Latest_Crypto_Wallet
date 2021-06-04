package com.crypto.croytowallet.Chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.Adapter.TickectChatAdapter;
import com.crypto.croytowallet.Interface.MessageClickListner;
import com.crypto.croytowallet.Model.TicketChatModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.database.RetrofitClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketChat extends AppCompatActivity implements MessageClickListner {
CardView send;
EditText text_message;
TextView textView_Send,first_textUsername,fullName;
String message,sendername,messageId;
    KProgressHUD progressDialog;
    UserData userData;
    RecyclerView chatRecyclerView;
    TickectChatAdapter tickectChatAdapter;
    ArrayList<TicketChatModel>ticketChatModels;

    ImageView deleteMessage;

    SharedPreferences sharedPreferences = null;

    RelativeLayout bottom_layout;

    private Socket mSocket;
    {
        try {
          mSocket = IO.socket("https://api.imx.global");

          // mSocket = IO.socket("http://13.233.136.56:8080");

        } catch (URISyntaxException e) {}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_chat);
        send= findViewById(R.id.btn_send);
        text_message = findViewById(R.id.text_send);
        textView_Send = findViewById(R.id.textView_send);
        first_textUsername = findViewById(R.id.userFirsttext);
        fullName = findViewById(R.id.username);
        deleteMessage = findViewById(R.id.message_delete);
        chatRecyclerView = findViewById(R.id.recycler_view);
        bottom_layout = findViewById(R.id.bottom);

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        sharedPreferences = getSharedPreferences("season",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("status","false");
        editor.commit();
        editor.apply();

        ticketChatModels = new ArrayList<TicketChatModel>();

        send.setAlpha(0.4f);
        textView_Send.setAlpha(0.4f);
        text_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String msg = s.toString();
                if (msg.isEmpty()){
                    send.setAlpha(0.4f);
                    textView_Send.setAlpha(0.4f);


                }else{
                    send.setAlpha(0.9f);
                    textView_Send.setAlpha(0.9f);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = text_message.getText().toString().trim();

                if(message.isEmpty()){

                }else {
                    sendMessage();
                }

            }
        });

        deleteMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // deleteMessageOne();
            }
        });

        mSocket.connect();
     //  mSocket.on("hello", onNewMessage);
       mSocket.on("messagesToAdmin",getmessage);
     //  getChat();

    }


    private Emitter.Listener getmessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = (JSONArray) args[0];
                    ticketChatModels.clear();
                    for (int i=0;i<=data.length();i++){
                        try {
                            JSONObject object = data.getJSONObject(i);
                            String array = object.getString("array");
                            String username = object.getString("username");
                            String username1= userData.getUsername();
                            Log.d("usernmae",username+"\n"+username1);
                            JSONArray jsonArray = new JSONArray(array);
                            if (username.equals(userData.getUsername())){
                                for (int j=0; j<=jsonArray.length();i++){
                                    JSONObject object1 = jsonArray.getJSONObject(i);

                                    TicketChatModel ticketChatModel1 = new TicketChatModel();
                                    String role= object1.getString("role");
                                    String time= object1.getString("createdAt");

                                    ticketChatModel1.setRoleId(role);
                                    ticketChatModel1.setTime(time);

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


                                }
                            }else{

                            //   getChat();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        tickectChatAdapter = new TickectChatAdapter(getApplicationContext(),ticketChatModels,TicketChat.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                        linearLayoutManager.setStackFromEnd(true);
                        tickectChatAdapter.notifyDataSetChanged();
                        tickectChatAdapter.update(ticketChatModels);
                        chatRecyclerView.setLayoutManager(linearLayoutManager);
                        chatRecyclerView.setHasFixedSize(true);
                        chatRecyclerView.setAdapter(tickectChatAdapter);
                    }

                  // Log.d("hello",data.toString());
               //  Toast.makeText(TicketChat.this, ""+data.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    };


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                   Log.d("hello111",data.toString());


              Toast.makeText(TicketChat.this, ""+data.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    };




    private void sendMessage() {

        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();
        message = text_message.getText().toString();




        JsonObject bodyParameters = new JsonObject();
        JsonArray details= new JsonArray();
        JsonObject orderData= new JsonObject();
        orderData.addProperty("outgoingMessage", message);
        details.add(orderData);
        bodyParameters.add("outgoingMessages", details);

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().SendMessageApi(token,bodyParameters);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              //  hidepDialog();
                String s = null;
                if (response.code()==200){
                    try {
                        s=response.body().string();

                        text_message.setText(" ");
                  //  Toast.makeText(TicketChat.this, ""+s, Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // This method will be executed once the timer is over
                           //  getChat();
                            }
                        }, 500);
                    } catch (IOException  e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(TicketChat.this)
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
                            .setActivity(TicketChat.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }

            }




            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //hidepDialog();
                Snacky.builder()
                        .setActivity(TicketChat.this)
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

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void EndChat_AlertDialogBox(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TicketChat.this);

        alertDialogBuilder.setTitle(R.string.close_chat);

        alertDialogBuilder.setIcon(R.drawable.ic_disagreement);
        alertDialogBuilder
                .setMessage(R.string.close_chat_text)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChatUnActive();
                       // send.setVisibility(View.GONE);
                       // textView_Send.setVisibility(View.GONE);
                        bottom_layout.setVisibility(View.GONE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status","true");
                        editor.commit();
                        editor.apply();
                    }
                })
                .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        Snacky.builder()
                                .setActivity(TicketChat.this)
                                .setText("Continue chat")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .success()
                                .show();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void ChatUnActive(){
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().Chat_Un_Active(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                String s = null;
                if (response.code()==200){
                    try {
                        s=response.body().string();


                              } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(TicketChat.this)
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
                            .setActivity(TicketChat.this)
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
                        .setActivity(TicketChat.this)
                        .setText("Internet Problem ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }

    public void allDelete(View view) {
      EndChat_AlertDialogBox();
    }

    public void allRemoveMessage(){
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        Call<ResponseBody>call =RetrofitClient.getInstance().getApi().deleteAllMessage(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                if (response.code()==200){
                    try {
                        s=response.body().string();

                       // getChat();
                        //  Log.d("support",s);
                     //   Toast.makeText(TicketChat.this, ""+s, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(TicketChat.this)
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
                            .setActivity(TicketChat.this)
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
                        .setActivity(TicketChat.this)
                        .setText("Internet Problem ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }

   /* @Override
    public void onItemClick(int position) {
        Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void onLongItemClick(int position) {
           messageId = ticketChatModels.get(position).getMessageId();
        //deleteMessage.setVisibility(View.VISIBLE);
    }

    public void deleteMessageOne(){
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        String Urls= URLs.URL_DELETE_MESSAGE+messageId;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, Urls, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                deleteMessage.setVisibility(View.GONE);
             //   getChat();
             //   Toast.makeText(TicketChat.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", token);

                return headers;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }



}