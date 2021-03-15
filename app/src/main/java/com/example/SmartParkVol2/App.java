package com.example.SmartParkVol2;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App  extends Application {
   public static final String  FCM_CHANNEL_MAIN = "fcmChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NotificationChannel fcmChannelcreate =new NotificationChannel(FCM_CHANNEL_MAIN,"FCM_Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(fcmChannelcreate);
        }
    }
}
