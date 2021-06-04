package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Adapter.CountryNameSpinnerAddapter;
import com.crypto.croytowallet.Extra_Class.ImagePath;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.Model.CountryModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.Extra_Class.Utility;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.database.RetrofitCountryName;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import de.mateware.snacky.Snacky;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kyc extends AppCompatActivity implements View.OnClickListener {
    Spinner countryNameSpinner,stateNameSpinner,selectDocumentSpinner;
    TextView chooseFile;
    String countryName,stateName,documentName,token,house_no,street_no,zipCode,document;
    KProgressHUD progressDialog;
    ArrayList<CountryModel>countryModels ;
    ArrayList<String>stateList;
    String[] documentType = { "Select Document","Driving License", " Voter ID card", "Passport", " Birth Certificate"};
    Button submit;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView ivImage;
    private String userChoosenTask;
    File file;
    UserData user;
    EditText house,street,zipPin,document_no;
    LinearLayout linearLayout,linearsst,lineardoc;
    RelativeLayout relativeLayout;
    Animation enterright,slide_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);
        countryNameSpinner = findViewById(R.id.CountryNameSpinner);
        stateNameSpinner = findViewById(R.id.stateNameSpinner);
        selectDocumentSpinner = findViewById(R.id.select_documet_Spinner);
        chooseFile = findViewById(R.id.chooseFile);
        house = findViewById(R.id.house_no);
        street = findViewById(R.id.street_address);
        zipPin = findViewById(R.id.zippin);
        document_no = findViewById(R.id.document_no);
       // takeImge = findViewById(R.id.camra);
        ivImage = findViewById(R.id.setImageView);
        submit = findViewById(R.id.submit);
        linearLayout = findViewById(R.id.spin_cou);
        linearsst = findViewById(R.id.state_spin);
        lineardoc = findViewById(R.id.seldoc);
        relativeLayout = findViewById(R.id.chosfil);

        slide_right = AnimationUtils.loadAnimation(Kyc.this,R.anim.slide_in_right);

        house.startAnimation(slide_right);
        street.startAnimation(slide_right);
        linearLayout.startAnimation(slide_right);
        linearsst.startAnimation(slide_right);
        zipPin.startAnimation(slide_right);
        lineardoc.startAnimation(slide_right);
        document_no.startAnimation(slide_right);
        relativeLayout.startAnimation(slide_right);
        submit.startAnimation(slide_right);

        chooseFile.setOnClickListener(this);
        //takeImge.setOnClickListener(this);

        countryModels = new ArrayList<CountryModel>();
        stateList  = new ArrayList<String>();

         user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
         token=user.getToken();

        getCountryName();

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,documentType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDocumentSpinner.setAdapter(aa);

        selectDocumentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                documentName = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               house_no = house.getText().toString().trim();
               street_no = street.getText().toString().trim();
                document = document_no.getText().toString().trim();
                zipCode = zipPin.getText().toString().trim();

                if (house_no.isEmpty() || street_no.isEmpty() || document.isEmpty() || zipCode.isEmpty()){

                    Snacky.builder()
                            .setActivity(Kyc.this)
                            .setText("Please filled all required details")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .warning()
                            .show();

                }else{

                    setAddress(token,house_no,street_no,countryName,stateName,zipCode);
                    KycSet(token,file,documentName,document);
                }


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

    public void getCountryName() {

        progressDialog = KProgressHUD.create(Kyc.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody>call = RetrofitCountryName.getInstance().getApiCountryName().getCountryname();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s=null;
                hidepDialog();

                if (response.isSuccessful()){
                    try {
                        s = response.body().string();

                        JSONArray jsonArray = new JSONArray(s);
                        for (int i =0; i<=jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            CountryModel countryModel = new CountryModel();
                            String name = object.getString("name");
                            String alpha2Code = object.getString("alpha2Code");
                            String flags = object.getString("flag");
                            countryModel.setCountryName(name);
                            countryModel.setImage(flags);
                            countryModel.setCountryCode(alpha2Code);

                            countryModels.add(countryModel);

                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    CountryNameSpinnerAddapter  adp =  new CountryNameSpinnerAddapter(Kyc.this,countryModels);
                    countryNameSpinner.setAdapter(adp);

                    countryNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            countryName = countryModels.get(position).getCountryName();
                            String countryCode = countryModels.get(position).getCountryCode();
                            String Code = countryCode.toLowerCase();

                            getState(Code);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }else {
                    Snacky.builder()
                            .setActivity(Kyc.this)
                            .setText("Api not is working")
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
                        .setActivity(Kyc.this)
                        .setText("Internet problem")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

    }


    public void getState(String code){

        Call<ResponseBody>call = RetrofitClient.getInstance().getApi().GET_State(code);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s =null;

                if (response.isSuccessful()){
                    stateList.clear();
                    try {
                        s = response.body().string();
                        JSONArray jsonArray  = new JSONArray(s);

                        for (int i=0; i<=jsonArray.length();i++){
                            String state = jsonArray.getString(i);
                            stateList.add(state);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter<String> adp = new ArrayAdapter<String>(Kyc.this,android.R.layout.simple_spinner_dropdown_item,stateList);

                    stateNameSpinner.setAdapter(adp);

                    stateNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            stateName  = parent.getItemAtPosition(position).toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }else{
                    Snacky.builder()
                            .setActivity(Kyc.this)
                            .setText("State not found")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snacky.builder()
                        .setActivity(Kyc.this)
                        .setText("Internet problem")
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.chooseFile:
                selectImage();
                 break;

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Kyc.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(Kyc.this);
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
        Uri uri = data.getData();
        System.out.println("urii  "+uri +" "+uri.getPath());
        String path  = ImagePath.getPath(Kyc.this,uri);
        System.out.println("urii path "+path );
        if(path!=null && !path.equals("")) {
           file = new File(path);

        }

    }

    public void setAddress(String token,String house,String street, String country,String state, String zip_code ){


        progressDialog = KProgressHUD.create(Kyc.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();



        JsonObject bodyParameters = new JsonObject();

        JsonObject data = new JsonObject();
        data.addProperty("street",street);
        data.addProperty("house",house);
        data.addProperty("zipCode",zip_code);
        data.addProperty("state",state);
        data.addProperty("country",country);
        bodyParameters.add("address",data);

        Call<ResponseBody>call = RetrofitClient.getInstance().getApi().SetAddress(token,bodyParameters);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               hidepDialog();
               String  s=null;
               if (response.code()==200){

                        }else if (response.code()==400){

                   try {
                       s=response.errorBody().string();
                       JSONObject jsonObject1=new JSONObject(s);
                       String error =jsonObject1.getString("error");


                       Snacky.builder()
                               .setActivity(Kyc.this)
                               .setText(error)
                               .setDuration(Snacky.LENGTH_SHORT)
                               .setActionText(android.R.string.ok)
                               .error()
                               .show();


                   } catch (IOException | JSONException e) {
                       e.printStackTrace();
                   }

                } else if (response.code()==401){
                   Snacky.builder()
                           .setActivity(Kyc.this)
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
                        .setActivity(Kyc.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }

    public void KycSet(String Token,File file,String documentType,String document_no){
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part image =
                MultipartBody.Part.createFormData("kyc", file.getName(), requestFile);

        RequestBody doc_no =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), document_no);
        RequestBody doc_type =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),documentType);


        Call<ResponseBody>call = RetrofitClient.getInstance().getApi().Kyc(Token,image,doc_no,doc_type);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String  s=null;
                if (response.code()==200){
                    try {
                        s = response.body().string();


                        Toast.makeText(Kyc.this, "Successfully Submit", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else if (response.code()==400){

                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Kyc.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code()==401){
                    Snacky.builder()
                            .setActivity(Kyc.this)
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
                        .setActivity(Kyc.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }

}