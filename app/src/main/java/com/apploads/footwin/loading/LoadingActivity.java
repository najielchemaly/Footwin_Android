package com.apploads.footwin.loading;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.apploads.footwin.BaseActivity;
import com.apploads.footwin.login.LoginActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.model.Config;
import com.apploads.footwin.services.ApiManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.apploads.footwin.StaticData.config;

public class LoadingActivity extends BaseActivity {
    ImageView imgBall;

    @Override
    public int getContentViewId() {
        return R.layout.loading_activity;
    }

    @Override
    public void doOnCreate(){
        initView();
    }

    /**
     * initialize view
     */
    private void initView(){
        imgBall = _findViewById(R.id.imgBall);
        rotateBall();
        callConfigService();
    }

    /**
     * This function is for rotating the ball at an infinite count
     */
    private void rotateBall(){
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        imgBall.startAnimation(rotation);
    }

    /**
     * Api request for getting the config and saving them in the config Object
     */
    private void callConfigService(){
        ApiManager.getService().getConfig("http://config.foot-win.com").enqueue(new Callback<Config>() {
            @Override
            public void onResponse(Call<Config> call, Response<Config> response) {
                config = response.body();
                IntentToLoginWithDuration(2000);
            }

            @Override
            public void onFailure(Call<Config> call, Throwable t) {
                Log.e("e", t.getMessage());
            }
        });
    }

    /**
     * This function is for navigating to the next page with a delay
     * @param delay time before navigating to login page
     */
    private void IntentToLoginWithDuration(long delay){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, delay);
    }
}
