package com.example.abbieturner.restaurantsfinder.FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.abbieturner.restaurantsfinder.Activities.HomeActivity;
import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.Utils.SharedPref;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;

public class MessageService extends FirebaseMessagingService {


    private SharedPref sharedPref;
    private DatabaseReference mRoofRef;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPref = new SharedPref(this);

        mRoofRef = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(getClass().getSimpleName(), new Gson().toJson(remoteMessage.getNotification()));
        Log.e(getClass().getSimpleName(), new Gson().toJson(remoteMessage.getData()));

        if (remoteMessage.getData() != null) {
            buildNotification(remoteMessage.getData());
        }
    }

    private void buildNotification(Map<String, String> messageData) {

        Log.e(getClass().getSimpleName(), new Gson().toJson(messageData));
        String msg = messageData.get("msg");
        String title = messageData.get("title");


        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "NOTIFICATION_CHANNEL_NAME";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "NOTIFICATION_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.DKGRAY);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        notificationManager.notify(0, notificationBuilder.build());
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        sharedPref.setToken(s);
    }
}