package com.apploads.footwin.notifications;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.login.LoginActivity;
import com.apploads.footwin.model.Notification;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends BaseActivity {
    ListView listNotifications;
    Button btnClose;
    ProgressBar progressBar;
    NotificationsAdapter notificationsAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.notifications_activity;
    }

    @Override
    public void doOnCreate(){
        initView();
    }

    private void initView(){
        listNotifications = _findViewById(R.id.listNotifications);
        btnClose = _findViewById(R.id.btnClose);

        progressBar = _findViewById(R.id.spin_kit);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationsAdapter = new NotificationsAdapter(getNotifications(), NotificationsActivity.this);
                listNotifications.setAdapter(notificationsAdapter);
                progressBar.setVisibility(View.GONE);
            }
        }, 2000);


    }

    private List<Notification> getNotifications() {
        List<Notification> notificationList = new ArrayList<>();

        Notification notification = new Notification();
        notification.setDate("1 hr ago");
        notification.setDescription("Naji you are almost out of coins! Let's go refill your account and start making predictions again");
        notification.setType(1);
        notificationList.add(notification);

        notification = new Notification();
        notification.setDate("1 hr ago");
        notification.setDescription("WELL DONE! \n You have made the correct predictions! 200 coins have been added to your account.");
        notification.setType(2);
        notificationList.add(notification);

        notification = new Notification();
        notification.setDate("1 hr ago");
        notification.setDescription("Naji you are almost out of coins! Let's go refill your account and start making predictions again");
        notification.setType(1);
        notificationList.add(notification);

        notification = new Notification();
        notification.setDate("1 hr ago");
        notification.setDescription("WELL DONE! \n You have made the correct predictions! 200 coins have been added to your account.");
        notification.setType(2);
        notificationList.add(notification);

        return notificationList;

    }

}
