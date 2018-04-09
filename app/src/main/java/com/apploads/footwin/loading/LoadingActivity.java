package com.apploads.footwin.loading;

import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.apploads.footwin.BaseActivity;
import com.apploads.footwin.login.LoginActivity;
import com.apploads.footwin.R;

public class LoadingActivity extends BaseActivity {

    ImageView imgBall;

    @Override
    public int getContentViewId() {
        return R.layout.loading_activity;
    }

    @Override
    public void doOnCreate(){
        initView();
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    private void initView(){
        imgBall = _findViewById(R.id.imgBall);
        rotateBall();
    }

    /**
     * This function is for rotating the ball at an infinite count
     */
    private void rotateBall(){
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        imgBall.startAnimation(rotation);
    }
}
