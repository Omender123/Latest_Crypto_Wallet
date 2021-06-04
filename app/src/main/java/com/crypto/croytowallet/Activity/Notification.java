package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Adapter.NotificationAdapter;
import com.crypto.croytowallet.Adapter.OfferAdapter;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.Model.Model_Class_Add_Currency;
import com.crypto.croytowallet.Model.NoticationModel;
import com.crypto.croytowallet.Offer.Full_Offer;
import com.crypto.croytowallet.Offer.Offer;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.NotificationSharedPreference;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.TransactionHistorySharedPrefManager;
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

public class Notification extends AppCompatActivity implements HistoryClickLister {
    RecyclerView recyclerView;
    ArrayList<NoticationModel> noticationModels;
    KProgressHUD progressDialog;
    NotificationAdapter notificationAdapter;
    TextView empty_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView = findViewById(R.id.recyclerNotification);
         empty_txt = findViewById(R.id.txt_list_is_empty);

         noticationModels = new ArrayList<NoticationModel>();

         getNotification();
    }

    private void getNotification() {
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token = user.getToken();

        progressDialog = KProgressHUD.create(Notification.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getNotification(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();
                String s=null;
                if (response.code()==200){

                    try {
                        s=response.body().string();

                        JSONArray jsonArray = new JSONArray(s);
                        for (int i=0; i<=jsonArray.length();i++){
                            NoticationModel noticationModel = new NoticationModel();
                            JSONObject object = jsonArray.getJSONObject(i);

                            String details = object.getString("details");
                            String landingImage= object.getString("landingImage");
                            JSONArray jsonArray1 = new JSONArray(landingImage);
                            String landingImg =jsonArray1.getString(0);

                            String Images= object.getString("images");
                            JSONArray jsonArray2 = new JSONArray(Images);
                            String Img =jsonArray2.getString(0);

                            noticationModel.setDetails(details);
                            noticationModel.setLandingImages(landingImg);
                            noticationModel.setImages(Img);

                            noticationModels.add(noticationModel);
                           }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    if(noticationModels!=null && noticationModels.size()>0){
                        notificationAdapter = new NotificationAdapter(noticationModels,getApplicationContext(), Notification.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(notificationAdapter);
                    }else{

                        empty_txt.setVisibility(View.VISIBLE);

                    }


                } else if(response.code()==401){
                    Snacky.builder()
                            .setActivity(Notification.this)
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
                        .setActivity(Notification.this)
                        .setText("Please Check Your Internet Connection")
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

        String landindImages = noticationModels.get(position).getLandingImages();
        String Images = noticationModels.get(position).getImages();
        String Details = noticationModels.get(position).getDetails();

        NoticationModel noticationModel = new NoticationModel(landindImages,Images,Details);
        NotificationSharedPreference.getInstance(getApplicationContext()).AddNotificationData(noticationModel);

        startActivity(new Intent(getApplicationContext(), Full_Notification.class));


    }
}