package com.crypto.croytowallet.Extra_Class;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.crypto.croytowallet.Activity.Setting;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.TransactionHistorySharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppUpdateChecker {
    private Activity activity;
    public AppUpdateChecker(Activity activity) {
        this.activity = activity;
    }
    //current version of app installed in the device
    private String getCurrentVersion(){
        PackageManager pm = activity.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = pm.getPackageInfo(activity.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return pInfo.versionName;
    }
    private class GetLatestVersion extends AsyncTask<String, String, String> {
        private String latestVersion;
        private ProgressDialog progressDialog;
        private boolean manualCheck;
        GetLatestVersion(boolean manualCheck) {
            this.manualCheck = manualCheck;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (manualCheck)
            {
                if (progressDialog!=null)
                {
                    if (progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }
                }
            }
            String currentVersion = getCurrentVersion();
            //If the versions are not the same
            if(!currentVersion.equals(latestVersion)&&latestVersion!=null){
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("An Update is Available");
                builder.setMessage("Its better to update now");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Click button action

                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+activity.getPackageName())));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel button action
                        activity.finish();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }else {
                if (manualCheck) {
                    Snacky.builder()
                            .setActivity(activity)
                            .setText("No Update Available")
                            .setTextColor(activity.getResources().getColor(R.color.white))
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();
                   // Toast.makeText(activity, "No Update Available", Toast.LENGTH_SHORT).show();
                }
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (manualCheck) {
                progressDialog=new ProgressDialog(activity);
                progressDialog.setMessage("Checking For Update.....");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(7)
                        .ownText();
                return latestVersion;
            } catch (Exception e) {
                return latestVersion;
            }
        }
    }
    public void checkForUpdate(boolean manualCheck)
    {
        new GetLatestVersion(manualCheck).execute();
    }


}