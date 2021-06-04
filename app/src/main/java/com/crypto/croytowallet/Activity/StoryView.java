package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.mateware.snacky.Snacky;
import jp.shts.android.storiesprogressview.StoriesProgressView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryView extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    private StoriesProgressView storiesProgressView;
    private ImageView image;

    private int counter = 0;
    long pressTime = 0L;
    long limit = 500L;

    KProgressHUD progressDialog;
    List<String> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_story_view);

        storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);
        image = (ImageView) findViewById(R.id.image);

        getStory();
        // bind reverse view
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);


    }
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };
    @Override
    public void onNext() {
        Picasso.get().load(images.get(++counter)).into(image);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
        Picasso.get().load(images.get(--counter)).into(image);

    }

    @Override
    public void onComplete() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        storiesProgressView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        storiesProgressView.resume();
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        // Very important !
        storiesProgressView.destroy();
        super.onDestroy();
    }

    public void getStory(){
        images = new ArrayList<>();
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();
        progressDialog = KProgressHUD.create(StoryView.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().getStory(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            hidepDialog();
                String s = null;
                if (response.code()==200){
                    try {
                        s=response.body().string();
                        images.clear();

                        JSONArray  jsonArray = new JSONArray(s);

                        for (int i=0;i<=jsonArray.length();i++){
                            JSONObject  object = jsonArray.getJSONObject(i);
                            String imagess = object.getString("images");

                            JSONArray jsonArray1 = new JSONArray(imagess);
                            for ( int j=0;j<=0;j++){
                                JSONObject object1 = jsonArray1.getJSONObject(j);

                                String imageUrl = object1.getString("url");

                                String fullImage = URLs.URL_Image+imageUrl;

                                Log.d("imagesss",imageUrl);

                                images.add(fullImage);
                            }

                        }





                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    storiesProgressView.setStoriesCount(images.size()); // <- set stories
                    storiesProgressView.setStoryDuration(5000L); // <- set a story duration
                    storiesProgressView.setStoriesListener(StoryView.this); // <- set listener
                    storiesProgressView.startStories(counter); // <- start progress

                    Glide.with(StoryView.this).load(images.get(counter)).into(image);

                   // Picasso.get().load(images.get(counter)).into(image);


                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(StoryView.this)
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
                            .setActivity(StoryView.this)
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
                        .setActivity(StoryView.this)
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
}
