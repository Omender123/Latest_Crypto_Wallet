package com.crypto.croytowallet.fragement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.crypto.croytowallet.Adapter.ExpandableListAdapter;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class About_Pager extends Fragment {
    KProgressHUD progressDialog;

    private HashMap<String, String> listDataChild;
    private List<String> questions =null;
    private List<String>answers = null;
    private ExpandableListAdapter listAdapter;

    private ExpandableListView expandableListView;
    private int lastExpandedPosition = -1;

   public About_Pager() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_about__pager, container, false);;


        expandableListView = view.findViewById(R.id.expandableListView);

        questions = new ArrayList<>();
        listDataChild = new HashMap<>();
        answers = new ArrayList<>();

        getFAQ();

        return view;
    }
    private void getFAQ() {

       /* progressDialog = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();
*/


        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().GetFAQ("About");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              //  hidepDialog();
                String s=null;

                if (response.code()==200){

                    try {
                        s=response.body().string();

                        JSONArray jsonArray = new JSONArray(s);
                        for (int i=0;i<=jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String question = object.getString("question");
                            String answer = object.getString("answer");

                            questions.add(question);
                            answers.add(answer);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    if(questions.size()>0){
                        for(int i =0;i<questions.size();i++){
                            listDataChild.put(questions.get(i),answers.get(i));
                        }
                    }

                    listAdapter = new ExpandableListAdapter(getContext(), questions, listDataChild);
                    expandableListView.setAdapter(listAdapter);

                    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                            return false;
                        }
                    });

                    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int i) {
                            if (lastExpandedPosition != -1
                                    && i != lastExpandedPosition) {
                                expandableListView.collapseGroup(lastExpandedPosition);
                            }
                            lastExpandedPosition = i;
                        }
                    });

                }else if(response.code()==400) {
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(getActivity())
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
              //  hidepDialog();

                Snacky.builder()
                        .setActivity(getActivity())
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

}