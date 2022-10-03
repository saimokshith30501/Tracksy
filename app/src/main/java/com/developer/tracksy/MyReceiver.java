package com.developer.tracksy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class MyReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "covid";

    @Override
    public void onReceive(Context context, Intent intent) {
        Random rd = new Random();
        int id = ((int) System.currentTimeMillis())%3;
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.epidemic_prevention);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, createChannel(context))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Covid 19 Safety")
                .setContentText(getDescription(id))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bm))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(((int) System.currentTimeMillis()) / 10, builder.build());
    }


    public int getIcon(int id) {
        int resId = 0;
        switch (id) {
            case 0:
            case 1:
            case 2: {
                resId = R.drawable.hand_sanitize;
                break;
            }
        }
        return resId;
    }

    public String getDescription(int id) {
        String des = "";
        if (id==0){
            des="Please sanitize your hands regularly";
        }else if(id==1){
            des="Please wash your hands regularly";
        }else {
            des="Please wear mask in public";
        }
        return des;
    }

    public String createChannel(Context context) {
        String CHANNEL_ID = "TRACKSY_NOTIFICATIONS";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "TRACKSY Notifications";
            String description = "Covid safety Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        return CHANNEL_ID;
    }

}