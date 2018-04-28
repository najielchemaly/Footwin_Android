package com.apploads.footwin.loading;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.apploads.footwin.MainPageActivity;
import com.apploads.footwin.firebase.FirebaseIdService;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.helpers.CustomDialogClass;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.login.LoginActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.model.Config;
import com.apploads.footwin.model.User;
import com.apploads.footwin.model.UserResponse;
import com.apploads.footwin.services.ApiManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.apploads.footwin.helpers.StaticData.config;

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
        ApiManager.getService(true).getConfig("http://config.foot-win.com").enqueue(new Callback<Config>() {
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
                if(isAutoLogin()){
                    StaticData.user = getUser(LoadingActivity.this);
                    Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, delay);
    }

    /**
     * checks if the user is already logged in from the User object stored in the shared preferences
     * @return
     */
    private boolean isAutoLogin(){
        if(getUser(LoadingActivity.this) == null){
            return false;
        }else {
            return true;
        }
    }

    public User getUser(Context context) {
        SharedPreferences settings;
        String json;
        settings = context.getSharedPreferences(StaticData.PREFS_NAME, Context.MODE_PRIVATE); //1

        Gson gson = new Gson();
        json = settings.getString(StaticData.PREFS_USER, ""); //2
        User user = gson.fromJson(json, User.class);
        return user;
    }

    private void loginService(){ //TODO put real password
        ApiManager.getService(true).login(getUser(LoadingActivity.this).getEmail(), "password").enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 1){
                        UserResponse userResponse = response.body();
                        StaticData.user = userResponse.getUser();
                        AppUtils.saveUser(LoadingActivity.this, userResponse.getUser());
                        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(response.body().getStatus() == 0){
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Toast.makeText(LoadingActivity.this, "Check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoadingActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
