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
import com.apploads.footwin.login.RetrievePasswordActivity;
import com.apploads.footwin.model.Config;
import com.apploads.footwin.model.User;
import com.apploads.footwin.model.UserResponse;
import com.apploads.footwin.services.ApiManager;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.apploads.footwin.helpers.StaticData.config;
import static com.apploads.footwin.helpers.Constants.ADDMOB_APP_ID;

public class LoadingActivity extends BaseActivity {
    ImageView imgBall;

    @Override
    public int getContentViewId() {
        return R.layout.loading_activity;
    }

    @Override
    public void doOnCreate(){
        initView();
        initAds();
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
     * http://config.foot-win.com
     */
    private void callConfigService(){
        ApiManager.getService(true).getConfig("http://config.foot-win.com/").enqueue(new Callback<Config>() {
            @Override
            public void onResponse(Call<Config> call, Response<Config> response) {
                config = response.body();
                if(config.getIsAppActive()){
                    ApiManager.setApiUrl(config.getBaseUrl());
                    IntentToLoginWithDuration(2000);
                }else {
                    IntentToTimerWithDuration(2000);
                }
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
                    callLoginService();
                }else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, delay);
    }

    /**
     * This function is for navigating to the next page with a delay
     * @param delay time before navigating to login page
     */
    private void IntentToTimerWithDuration(long delay){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), CountdownActivity.class);
                startActivity(intent);
                finish();
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

    private void initAds(){
        MobileAds.initialize(this, ADDMOB_APP_ID);
    }

    private void callLoginService(){
        ApiManager.getService(true).loginToken(getUser(LoadingActivity.this).getAccess_token()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    UserResponse userResponse = response.body();
                    AppUtils.saveUser(LoadingActivity.this, userResponse.getUser());
                    StaticData.user = userResponse.getUser();
                    Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    CustomDialogClass dialogClass = new CustomDialogClass(LoadingActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                        @Override
                        public void onConfirm(CustomDialogClass.DialogResponse response) {
                            response.getDialog().dismiss();
                        }

                        @Override
                        public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                        }
                    }, true);

                    dialogClass.setTitle("Oops");
                    dialogClass.setMessage("Something went wrong please try again later");
                    dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialogClass.show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
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
}
