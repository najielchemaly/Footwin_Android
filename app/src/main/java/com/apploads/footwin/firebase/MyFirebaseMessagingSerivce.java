package com.apploads.footwin.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.apploads.footwin.MainPageActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.login.LoginActivity;
import com.apploads.footwin.notifications.NotificationsActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

public class MyFirebaseMessagingSerivce extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
//        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if(remoteMessage.getNotification() != null){
            createNotification(remoteMessage);
        }
    }

    private void createNotification(RemoteMessage remoteMessage){
        Intent intent = new Intent(this, NotificationsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // default sound of the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.logo_white)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(remoteMessage.getNotification().getBody()))
                            .setContentText(remoteMessage.getNotification().getBody())
                            .setColor(getResources().getColor(R.color.colorPrimary));

            mBuilder.setSound(defaultSoundUri);
            mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            mBuilder.setAutoCancel(true);
            mBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(Calendar.getInstance().get(Calendar.MILLISECOND), mBuilder.build());
        } else {
            // do something for phones running an SDK before lollipop
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.logo_white)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(remoteMessage.getNotification().getBody()))
                            .setContentText(remoteMessage.getNotification().getBody());

            mBuilder.setSound(defaultSoundUri);
            mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            mBuilder.setAutoCancel(true);
            mBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(Calendar.getInstance().get(Calendar.MILLISECOND), mBuilder.build());
        }
    }
}