package com.apploads.footwin.loading;

import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.helpers.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CountdownActivity extends BaseActivity {

    TextView txtTimer, txtSubscribe;
    private Date endDate;
    private long startTime, milliseconds, diff;
    private CountDownTimer mCountDownTimer;
    RelativeLayout viewCountdown;

    @Override
    public int getContentViewId() {
        return R.layout.countdown_activity;
    }

    @Override
    public void doOnCreate() throws Exception {
        viewCountdown = _findViewById(R.id.viewCountdown);
        txtTimer = _findViewById(R.id.txtTimer);
        txtSubscribe = _findViewById(R.id.txtSubscribe);

        txtSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Constants.FOOTWIN_WEBSITE));
                startActivity(i);
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formatter.setLenient(false);

        String endTime = "2018-06-11 12:00";

        try {
            endDate = formatter.parse(endTime);
            milliseconds = endDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        startTime = System.currentTimeMillis();

        diff = milliseconds - startTime;

        mCountDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                startTime=startTime-1;
                Long serverUptimeSeconds = (millisUntilFinished - startTime) / 1000;

                String daysLeft = String.format("%d", serverUptimeSeconds / 86400);
                String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);
                String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);

                txtTimer.setText(daysLeft+ " Days " + hoursLeft+ " Hours " + minutesLeft + " Mins " + secondsLeft+" Sec");
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
}
