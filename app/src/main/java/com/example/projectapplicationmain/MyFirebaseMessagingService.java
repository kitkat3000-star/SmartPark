package com.example.projectapplicationmain;

import android.app.Notification;
import android.app.NotificationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.service.controls.ControlsProviderService.TAG;
import static com.example.projectapplicationmain.App.FCM_CHANNEL_MAIN;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG,"onmessagerecieved: called");

        Log.d(TAG,"onmessagerecieved: Message recieved from; " + remoteMessage.getFrom());

        if(remoteMessage.getNotification() != null) {
            String title =  remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Notification notification = new NotificationCompat.Builder(this,FCM_CHANNEL_MAIN)
                    .setSmallIcon(R.drawable.ic_baseline_notofications)
                    .setContentTitle(title)
                    .setContentText(body)
                    .build();
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(1002,notification);
        }
if (remoteMessage.getData()!= null) {
    Log.d(TAG, "onMessageRecieved: Data: " + remoteMessage.getData().toString());
}
        getFirebaseMessage(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();


                    }
                });
    }

    public void getFirebaseMessage(String title, String msg) {
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, "notifychannel")
                .setSmallIcon(R.drawable.ic_baseline_notofications)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true);

        NotificationManagerCompat nManager = NotificationManagerCompat.from(this);
        nManager.notify(101,notifyBuilder.build());
    }

}
