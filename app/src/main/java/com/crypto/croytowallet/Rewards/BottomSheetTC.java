package com.crypto.croytowallet.Rewards;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.crypto.croytowallet.Extra_Class.ApiResponse.TCResponse;
import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetTC extends BottomSheetDialogFragment {
    BottomSheetBehavior bottomSheetBehavior;
    View extraSpace ;
    AppBarLayout appBarLayout;
    CardView profileLayout;
    ImageView cancelBtn;
    TextView text_tc;
    UserData userData;
    KProgressHUD progressDialog;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        //inflating layout
        View view = View.inflate(getContext(), R.layout.terms_and_condition_layout, null);

        //setting layout with bottom sheet
        bottomSheet.setContentView(view);

        userData = SharedPrefManager.getInstance(getContext()).getUser();
        extraSpace = view.findViewById(R.id.extraSpace);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        profileLayout = view.findViewById(R.id.cardView);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        text_tc= view.findViewById(R.id.text_tc);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));


        //setting Peek at the 16:9 ratio keyline of its parent.
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);


        //setting max height of bottom sheet
        extraSpace.setMinimumHeight((Resources.getSystem().getDisplayMetrics().heightPixels) / 2);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (BottomSheetBehavior.STATE_EXPANDED == i) {
                    showView(appBarLayout, getActionBarSize());
                    hideAppBar(profileLayout);

                }
                if (BottomSheetBehavior.STATE_COLLAPSED == i) {
                    hideAppBar(appBarLayout);
                    showView(profileLayout, getActionBarSize());
                }

                if (BottomSheetBehavior.STATE_HIDDEN == i) {
                    dismiss();
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        //aap bar cancel button clicked
       cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });

        //hiding app bar at the start
        hideAppBar(appBarLayout);

        getTcs(userData.getToken());

        return bottomSheet;
    }

   

    @Override
    public void onStart() {
        super.onStart();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideAppBar(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);

    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray array = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) array.getDimension(0, 0);
        return size;
    }
    private void getTcs(String token) {

        progressDialog = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<TCResponse> call = RetrofitClient.getInstance().getApi().getTCs(token);
        call.enqueue(new Callback<TCResponse>() {
            @Override
            public void onResponse(Call<TCResponse> call, Response<TCResponse> response) {
                String s = null;
                hidepDialog();
                if (response.isSuccessful() && response.code() == 200) {

                    TCResponse tcResponse = response.body();
                    text_tc.setText(tcResponse.getTCs().getBody());



                }else  if(response.code()==401||response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<TCResponse> call, Throwable t) {
                hidepDialog();

                Toast.makeText(getContext(), ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

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
