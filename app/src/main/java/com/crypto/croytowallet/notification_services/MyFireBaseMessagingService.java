package com.crypto.croytowallet.notification_services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFireBaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    //private MyPreferences preferenceManager;
    public static String notificationType ;
    private static final String PUSH_NOTIFICATION = "fcmpushnotification";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

          }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e(TAG, "From: " +remoteMessage.getFrom ());
        // preferenceManager = new PreferenceManager(mainActivity);
        if (remoteMessage == null)
            return;
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getTitle ());
            handleNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
        }

        //notification in data payload
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                //  JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Map<String, String> data = remoteMessage.getData();
                handleDataMessage(data);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message,String title) {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class)
                    .putExtra("status","10");

            showNotification(getApplicationContext(), title, message, resultIntent);
        }else{
            // if (notification_type.equals("0")){
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Intent pushNotification = new Intent(getApplicationContext(), NotificationIntentService.class);
                pushNotification.putExtra("status","10");
                startService(pushNotification);
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(message);

                // notification icon
                final int icon = R.mipmap.app_logo;
                NotificationCompat.Builder notificationBuilder = new
                        NotificationCompat.Builder(this);
                setSmallIcon(notificationBuilder);
                //notificationBuilder .setSmallIcon(R.drawable.ic_cancel_small);
                notificationBuilder .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), icon));
                notificationBuilder  .setContentTitle(title);
                notificationBuilder   .setContentText(message);
                notificationBuilder    .setAutoCancel(true);
                // .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager)
                                getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(1410, notificationBuilder.build());

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                // showNotificationMessage(getApplicationContext(), title, message, "2500", pushNotification);
            } else {
                //app is in background, show the notification in notification tray

                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class)
                        .putExtra("status","10");
                //resultIntent.putExtra("notification_type",notification_type);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                        resultIntent, PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(message);

                // notification icon
                final int icon =R.mipmap.app_logo ;
                NotificationCompat.Builder notificationBuilder = new
                        NotificationCompat.Builder(this);
                setSmallIcon(notificationBuilder);
                //notificationBuilder.setSmallIcon(R.drawable.ic_cancel_small)
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), icon));
                notificationBuilder.setContentTitle(title);
                notificationBuilder.setContentText(message);
                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager)
                                getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(1410, notificationBuilder.build());
                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                showNotificationMessage(getApplicationContext(), title, message, "2500", resultIntent);
            }
        }
    }


    private void handdddd(Map<String,String>json) {
        Log.e(TAG, "push json: " + json.toString());
        // preferenceManager = new MyPreferences (getApplicationContext ());
        try {
            String message = json.get("body");
            String title = json.get("title");
            String type = json.get("type");

            String data = json.get("data");
            JSONObject jsonObject = new JSONObject(data);
            String status = jsonObject.getString("status");


            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class)
                    .putExtra("status",status);

            showNotification(getApplicationContext(), title, message, resultIntent);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleDataMessage(Map<String, String> json) {
        Log.e(TAG, "push json: " + json.toString());
        // preferenceManager = new MyPreferences (getApplicationContext ());
        try {
            String message = json.get("body");
            String title = json.get ("title");
            String  type = json.get("type");
            String data = json.get("data");
            JSONObject jsonObject = new JSONObject(data);
            String status = jsonObject.getString("status");
            if (status.equals("11")){
                float balance = (float) jsonObject.getDouble("balance");
//                Pref.setValue(getApplicationContext(),AppConstants.WALLET_AMMOUNT,balance);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                handdddd(json);
            }else{
                // if (notification_type.equals("0")){
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    Intent pushNotification = new Intent(getApplicationContext(), NotificationIntentService.class);
                    pushNotification.putExtra("status",status);
                    startService(pushNotification);
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                    inboxStyle.addLine(message);

                    // notification icon
                    final int icon = R.mipmap.app_logo;


                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

                    setSmallIcon(notificationBuilder);
                    //.setSmallIcon(R.drawable.ic_cancel_small)
                    notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), icon));
                    notificationBuilder.setContentTitle(title);
                    notificationBuilder.setContentText(message);
                    notificationBuilder.setAutoCancel(true);
                    // .setContentIntent(pendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager)
                                    getSystemService(Context.NOTIFICATION_SERVICE);
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(getPackageName (),
                                "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(channel);
                    }*/

                    notificationManager.notify(1410, notificationBuilder.build());

                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                    // showNotificationMessage(getApplicationContext(), title, message, "2500", pushNotification);


                }
                else {
                    //app is in background, show the notification in notification tray

                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class)
                            .putExtra("status",status);

                    //resultIntent.putExtra("notification_type",notification_type);


                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                            resultIntent, PendingIntent.FLAG_ONE_SHOT);


                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                    inboxStyle.addLine(message);

                    // notification icon
                    final int icon = R.mipmap.app_logo;


                    NotificationCompat.Builder notificationBuilder = new
                            NotificationCompat.Builder(this);
                    setSmallIcon(notificationBuilder);
                    //.setSmallIcon(R.drawable.ic_cancel_small)
                    notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), icon));
                    notificationBuilder.setContentTitle(title);
                    notificationBuilder .setContentText(message);
                    notificationBuilder.setAutoCancel(true);
                    notificationBuilder.setContentIntent(pendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager)
                                    getSystemService(Context.NOTIFICATION_SERVICE);
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel (getPackageName (),
                                "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel (channel);
                    }*/
                    notificationManager.notify(1410, notificationBuilder.build());


                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                    showNotificationMessage(getApplicationContext(), title, message, "2500", resultIntent);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
        // play notification sound
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();
    }


    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        // notification icon
        final int icon = R.mipmap.app_logo;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId);
        setSmallIcon(mBuilder);
        //.setSmallIcon(R.drawable.ic_cancel_small)
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), icon));
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(body);
        mBuilder.setAutoCancel(true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notificationId, mBuilder.build());
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }



    public static String getYoutubeVideoId(String youtubeUrl) {
        String video_id="";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http"))
        {
            String expression = "^.*((youtu.be"+ "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches())
            {
                String groupIndex1 = matcher.group(7);
                if(groupIndex1!=null && groupIndex1.length()==11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }

    private void setSmallIcon ( NotificationCompat.Builder notification)  {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.setSmallIcon(R.mipmap.app_logo);
            notification.setColor(getResources().getColor(R.color.background));
        } else {
            notification.setSmallIcon(R.mipmap.app_logo);
        }
    }

}
