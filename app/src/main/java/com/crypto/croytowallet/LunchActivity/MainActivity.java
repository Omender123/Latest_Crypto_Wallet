package com.crypto.croytowallet.LunchActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.crypto.croytowallet.Activity.StoryView;
import com.crypto.croytowallet.Extra_Class.AppUpdateChecker;
import com.crypto.croytowallet.Extra_Class.CheckInternetConnection;
import com.crypto.croytowallet.ImtSmart.ImtSmartCoinScan;
import com.crypto.croytowallet.ImtSmart.ImtSmartRecevied;
import com.crypto.croytowallet.Extra_Class.InitApplication;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.TransactionHistorySharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;

import com.crypto.croytowallet.database.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    NavController navController;
    DrawerLayout drawer;
    NavigationView navigationView;
    AppBarConfiguration appBarConfiguration;
    BottomNavigationView bottomNavigationView;
    private View navHeader, navDrawer;
    TextView username, usergmail;
    Toolbar toolbar;
    KProgressHUD progressDialog;
    SharedPreferences sharedPreferences;
    ImageView status_img;
    Switch drawerSwitch;
    SharedPreferences sharedPreferences1, sharedPreferences2;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();

        if (InitApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        status_img = findViewById(R.id.status_user);


        changeStatusBarColor();
        init();
        moreOptions();

        navHeader = navigationView.getHeaderView(0);
        username = (TextView) navHeader.findViewById(R.id.nav_username);
        usergmail = (TextView) navHeader.findViewById(R.id.nav_usergmail);


        sharedPreferences1 = getApplicationContext().getSharedPreferences("currency", 0);
        sharedPreferences2 = getApplicationContext().getSharedPreferences("PROJECT_NAME", 0);

        //getting the current user
        UserData user = SharedPrefManager.getInstance(this).getUser();

        token = user.getToken();
        //setting the values to the textviews
        username.setText(user.getUsername());
        usergmail.setText(user.getEmail());

        //  toolbar.setNavigationIcon(R.drawable.your_drawable_name);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        //  NavigationView();

        Menu menu = navigationView.getMenu();
        MenuItem share = menu.findItem(R.id.share);

        share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String code = user.getReferral_code();

                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    String sAux = "Hey,\n \n" + "Its amazing install iMX which offer 0% transaction fees on crypto Assets \n Referral code : " + code + "\n Download " + getResources().getString(R.string.app_name) + "\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        status_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StoryView.class));
            }
        });

        // checking Update

        try {
            AppUpdateChecker appUpdateChecker = new AppUpdateChecker(this);  //pass the activity in constructure
            appUpdateChecker.checkForUpdate(false);
        } catch (Exception e) {
        }

        androidAppLauncherShortcut();
        PutDeviceToken(token);

    }

    private void androidAppLauncherShortcut() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                ShortcutManager mshortcutManager = getSystemService(ShortcutManager.class);

                Intent imt_scan = new Intent(this, ImtSmartCoinScan.class);
                imt_scan.setAction(Intent.ACTION_VIEW);

                Intent imt_received = new Intent(this, ImtSmartRecevied.class);
                imt_received.setAction(Intent.ACTION_VIEW);


                ShortcutInfo shortcutScan = new ShortcutInfo.Builder(this, "scan")
                        .setShortLabel("ImSmart Scan")
                        .setLongLabel("ImSmart Scan")
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_scan))
                        .setIntent(imt_scan)
                        .build();

                ShortcutInfo shortcutBarcode = new ShortcutInfo.Builder(this, "barcode")
                        .setShortLabel("ImSmart Received")
                        .setLongLabel("ImSmart Received")
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_pay))
                        .setIntent(imt_received)
                        .build();


                mshortcutManager.setDynamicShortcuts(Arrays.asList(shortcutScan, shortcutBarcode));

            } else {
                Toast.makeText(this, "Your Android Version is low", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Your Android Version is low", Toast.LENGTH_SHORT).show();
        }


    }

    private void init() {
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_support, getApplicationContext().getTheme());

        navController = Navigation.findNavController(this, R.id.main);
        appBarConfiguration = new AppBarConfiguration.Builder(new int[]{R.id.deshboard, R.id.myWallet, R.id.exchange, R.id.profile, R.id.security, R.id.support, R.id.setting, R.id.pay_history, R.id.coin_history, R.id.our_Offer,R.id.our_Rewards})
                .setDrawerLayout(drawer)
                .build();


    }

    public void change_menu_icon() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_support);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.exit_text)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();


        }


    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    public void moreOptions() {

        Menu menu = navigationView.getMenu();
        MenuItem logout = menu.findItem(R.id.logout);


        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialogBox();

                return true;
            }
        });

        MenuItem theme = menu.findItem(R.id.dark_mode);
        drawerSwitch = (Switch) theme.getActionView().findViewById(R.id.drawer_switch);


        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            drawerSwitch.setChecked(true);

        drawerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    InitApplication.getInstance().setIsNightModeEnabled(true);
                    //  Intent intent = getIntent();
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                    startActivity(intent);

                } else {
                    InitApplication.getInstance().setIsNightModeEnabled(false);
                    //  Intent intent = getIntent();
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                    startActivity(intent);
                }


            }
        });


        MenuItem language = menu.findItem(R.id.langauge);

        language.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showChangeLanguageDialod();
                return true;
            }
        });
    }

    private void showChangeLanguageDialod() {
        final String[] listItem = {"English", "Hindi", "Japanese", "ThaiLand", "Chinese", "Philippines"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Language.......");

        builder.setSingleChoiceItems(listItem, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {
                    setLocale("en");
                    recreate();
                } else if (i == 1) {
                    setLocale("hi");
                    recreate();
                } else if (i == 2) {
                    setLocale("ja");
                    recreate();
                } else if (i == 3) {
                    setLocale("th");
                    recreate();
                } else if (i == 4) {
                    setLocale("zh");
                    recreate();
                } else if (i == 5) {
                    setLocale("phi");
                    recreate();
                }

                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();

    }
    // load lanage saved in sharedPreference

    public void loadLocale() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String Language = sharedPreferences.getString("My_Lang", "");
        setLocale(Language);
    }

    public void AlertDialogBox() {

        //Logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        // set title
        alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));

        // set dialog message
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher_round);
        alertDialogBuilder
                .setMessage(R.string.log_text)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void logout() {
        progressDialog = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        UserData user = SharedPrefManager.getInstance(this).getUser();
        String username = user.getUsername();
        String token = user.getToken();

        showpDialog();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().LogOut(token, username, token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                hidepDialog();
                if (response.code() == 200) {
                    SharedPrefManager.getInstance(getApplicationContext()).logout();
                    TransactionHistorySharedPrefManager.getInstance(getApplicationContext()).clearPearData();
                    sharedPreferences1.edit().clear().commit();
                    sharedPreferences2.edit().clear().commit();
                    //  deleteCache(MainActivity.this);
                    clearApplicationData();
                    Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                    finish();

                } else if (response.code() == 400) {

                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(MainActivity.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();
                        // Toast.makeText(SignUp.this, jsonObject1.getString("error")+"", Toast.LENGTH_SHORT).show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 401) {
                    Snacky.builder()
                            .setActivity(MainActivity.this)
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
                        .setActivity(MainActivity.this)
                        .setText(t.getMessage())
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
    protected void onResume() {
        super.onResume();
        new CheckInternetConnection(this).checkConnection();

    }

    @SuppressLint("LongLogTag")
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("EEEEEERRRRRRROOOOOOORRRR", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    private void PutDeviceToken(String token) {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {

                            String Devicetoken = task.getResult().getToken();
                            new Handler().postDelayed(new Runnable() {


                                @Override
                                public void run() {
                                    // This method will be executed once the timer is over
                                    SendDeviceToken(token, Devicetoken);

                                    //       Toast.makeText(getApplicationContext(), token+" & "+Devicetoken, Toast.LENGTH_SHORT).show();
                                }
                            }, 2000);


                        } else {
                            Toast.makeText(getApplicationContext(), "Devicetoken is not generated", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    private void SendDeviceToken(String token, String devicetoken) {

        JsonObject bodyParameters = new JsonObject();
        bodyParameters.addProperty("jwt", token);
        bodyParameters.addProperty("fcmToken", devicetoken);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().PutDevice(token, bodyParameters);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String s = null;

                if (response.code() == 200) {
                    try {
                        s = response.body().string();

                        //  Toast.makeText(getApplicationContext(), ""+s, Toast.LENGTH_SHORT).show();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 400) {
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");
                        Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



}