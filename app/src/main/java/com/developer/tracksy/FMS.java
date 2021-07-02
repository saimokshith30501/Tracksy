package com.developer.tracksy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FMS extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title;
        String header;
        String sub_title;
        Log.d("TRACKSY_LOGS", "From: " + remoteMessage.getFrom());
        Log.d("TRACKSY_LOGS", "From: " + remoteMessage);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("TRACKSY_LOGS", "Message data payload: " + remoteMessage.getData());
            title=remoteMessage.getData().get("title");
            header=remoteMessage.getData().get("header");
            sub_title=remoteMessage.getData().get("sub_title");
            buildNotification(title,header,sub_title);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("TRACKSY_LOGS", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void buildNotification(String title, String header, String sub_title) {
        Intent intent = new Intent(this, HomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis() / 100, intent, PendingIntent.FLAG_ONE_SHOT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vaccination);
        String GROUP_KEY_WORK_EMAIL = "group_notifications";
        int SUMMARY_ID = 0;
        NotificationCompat.Builder summaryNotification = new NotificationCompat.Builder(this,createNotificationChannel())
                .setSmallIcon(R.drawable.vaccination)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .setAutoCancel(true)
                .setGroupSummary(true);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, createNotificationChannel())
                .setSmallIcon(R.drawable.vaccination)
                .setLargeIcon(bitmap)
                .setContentTitle(title)
                .setContentText(sub_title)
                .setSubText(header)
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int) System.currentTimeMillis() / 100, builder.build());
        notificationManager.notify(SUMMARY_ID, summaryNotification.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
      AppTracksy.deviceID=s;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    private String createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        String CHANNEL_ID = "TRACKSY_NOTIFICATIONS";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TRACKSY Notifications";
            String description = "Vaccine Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        return CHANNEL_ID;
    }


}
