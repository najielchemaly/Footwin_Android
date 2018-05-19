package com.apploads.footwin.notifications;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apploads.footwin.NoDataView;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.helpers.CustomDialogClass;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.login.LoginActivity;
import com.apploads.footwin.model.Notification;
import com.apploads.footwin.model.NotificationResponse;
import com.apploads.footwin.profile.MatchesResultAdapter;
import com.apploads.footwin.profile.MyPredictionsActivity;
import com.apploads.footwin.services.ApiManager;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends BaseActivity {
    ListView listNotifications;
    Button btnClose;
    ProgressBar progressBar;
    RelativeLayout viewNoData;
    NotificationsAdapter notificationsAdapter;
    SwipeRefreshLayout pullToRefresh;
    NoDataView noDataView;

    @Override
    public int getContentViewId() {
        return R.layout.notifications_activity;
    }

    @Override
    public void doOnCreate(){
        initView();
        initListeners();
    }

    private void initView(){
        listNotifications = _findViewById(R.id.listNotifications);
        btnClose = _findViewById(R.id.btnClose);

        progressBar = _findViewById(R.id.spin_kit);
        viewNoData = _findViewById(R.id.viewNoData);
        pullToRefresh = _findViewById(R.id.pullToRefresh);

        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        getNotifications(false);
    }

    private void initListeners() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                refreshContent();
            }
        });
    }

    private void refreshContent(){
        getNotifications(true);
    }

    private void getNotifications(final Boolean isRefreshing) {
        progressBar.setVisibility(View.VISIBLE);
        ApiManager.getService().getNotifications().enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    NotificationResponse notificationResponse = response.body();
                    if(notificationResponse.getNotifications()!= null && notificationResponse.getNotifications().size() > 0){
                        notificationsAdapter = new NotificationsAdapter(notificationResponse.getNotifications(), NotificationsActivity.this);
                        listNotifications.setAdapter(notificationsAdapter);
                        viewNoData.setVisibility(View.GONE);
                    }else {
                        if(noDataView == null) {
                            noDataView = new NoDataView(NotificationsActivity.this, "YOU DO NOT HAVE NOTIFICATIONS YET!!");
                            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            viewNoData.addView(noDataView, lp);
                        }

                        viewNoData.setVisibility(View.VISIBLE);
                    }

                    progressBar.setVisibility(View.GONE);

                    AppUtils.updateBadge(getApplicationContext(), 0);
                }

                if (isRefreshing) {
                    pullToRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void refreshList(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getNotifications(false);
            }
        });
    }
}
