package com.apploads.footwin.firebase;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.apploads.footwin.MainPageActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.model.Config;
import com.apploads.footwin.notifications.NotificationsActivity;
import com.apploads.footwin.predict.PredictFragment;
import com.apploads.footwin.services.ApiManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFirebaseMessagingSerivce extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage.getNotification() != null){
            createNotification(remoteMessage);
        }

        if(remoteMessage.getData() != null) {
            String total_winning_coins = remoteMessage.getData().get("total_winning_coins");
            StaticData.user.setWinningCoins(total_winning_coins);

            if(remoteMessage.getData().get("type") != null &&
                    remoteMessage.getData().get("type").equals("update_active_matches")){
                if(StaticData.context.getClass() == MainPageActivity.class){
                    MainPageActivity myActivity = (MainPageActivity) StaticData.context;
                    FragmentManager fm = myActivity.getSupportFragmentManager();
                    Fragment fragment = fm.findFragmentById(R.id.frame_layout);
                    if (fragment instanceof PredictFragment){
                        ((PredictFragment) fragment).callMatchesService();
                    }
                    ApiManager.getService().getActiveRound().enqueue(new Callback<Config>() {
                        @Override
                        public void onResponse(Call<Config> call, Response<Config> response) {
                            Config config = response.body();
                            StaticData.config.setActiveRound(config.getActiveRound());
                            StaticData.config.setActiveReward(config.getActiveReward());
                        }

                        @Override
                        public void onFailure(Call<Config> call, Throwable t) {

                        }
                    });
                }
            }else {
                int badge = AppUtils.getBadge(getApplicationContext());
                AppUtils.updateBadge(getApplicationContext(), ++badge);

                if(StaticData.context.getClass() == MainPageActivity.class){
                    MainPageActivity myActivity = (MainPageActivity) StaticData.context;
                    FragmentManager fm = myActivity.getSupportFragmentManager();
                    Fragment fragment = fm.findFragmentById(R.id.frame_layout);
                    if (fragment instanceof PredictFragment){
                        ((PredictFragment) fragment).updateBadge();
                        ((PredictFragment) fragment).updateWinningCoins();
                    }
                }

                if(StaticData.context.getClass() == NotificationsActivity.class){
                    ((NotificationsActivity) StaticData.context).refreshList();
                }
            }
        }

    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
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