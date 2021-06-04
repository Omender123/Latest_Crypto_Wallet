package com.crypto.croytowallet.Offer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.Adapter.OfferAdapter;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.Model.OfferModel;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.OfferSharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Offer extends AppCompatActivity implements HistoryClickLister {
    RecyclerView recyclerView;
    ArrayList<OfferModel> offerModels;
    OfferAdapter offerAdapter;
    KProgressHUD progressDialog;
    SharedPreferences sharedPreferences;
    TextView history_Empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        recyclerView=findViewById(R.id.offer_RecyclerView);
        history_Empty =findViewById(R.id.txt_list_is_empty);
        offerModels =new ArrayList<OfferModel>();


        getOffer();

    }

    public void getOffer() {
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        progressDialog = KProgressHUD.create(Offer.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, URLs.URL_GET_OFFER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();

                try {
                    JSONObject object = new JSONObject(response);
                    String result = object.getString("result");
                    JSONArray jsonArray = new JSONArray(result);
                    for(int i=0;i<=jsonArray.length();i++){
                        String data =jsonArray.getString(i);
                       JSONObject object1 = new JSONObject(data);

                       OfferModel offerModel = new OfferModel();
                       String image=object1.getString("offerImage");
                        String shortDes=object1.getString("sortDescription");
                        String LongDes=object1.getString("longDescription");
                        String title=object1.getString("title");
                        String date=object1.getString("offerDate");
                        String offername=object1.getString("offerName");


                       JSONArray jsonArray1=new JSONArray(image);
                       for (int j=0;j<=0;j++){
                           String images=jsonArray1.getString(j);
                          offerModel.setImageUrl(images);
                       }
                       offerModel.setShortDescription(shortDes);
                       offerModel.setLongDescription(LongDes);
                       offerModel.setOffer_tittle(title);
                       offerModel.setDate(date);
                       offerModel.setOffer_name(offername);

                       offerModels.add(offerModel);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(offerModels!=null && offerModels.size()>0){
                    offerAdapter = new OfferAdapter(offerModels,getApplicationContext(),Offer.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(offerAdapter);
                }else{

                    history_Empty.setVisibility(View.VISIBLE);

                }

                // Toast.makeText(Offer.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();

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

        String offername = offerModels.get(position).getOffer_name();
        String Short = offerModels.get(position).getShortDescription();
        String Long = offerModels.get(position).getLongDescription();
        String Title = offerModels.get(position).getOffer_tittle();
        String Image = offerModels.get(position).getImageUrl();
        String Date = offerModels.get(position).getDate();

        OfferModel offerModel = new OfferModel(offername,Short,Long,Date,Image,Title);
        OfferSharedPrefManager.getInstance(getApplicationContext()).AddOfferData(offerModel);

        startActivity(new Intent(getApplicationContext(),Full_Offer.class));
    }
}