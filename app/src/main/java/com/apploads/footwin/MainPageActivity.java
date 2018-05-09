package com.apploads.footwin;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.firebase.FirebaseIdService;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.leaderboard.LeaderboardFragment;
import com.apploads.footwin.news.NewsFragment;
import com.apploads.footwin.predict.PredictFragment;
import com.apploads.footwin.profile.ProfileFragment;
import com.apploads.footwin.services.ApiManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPageActivity extends BaseActivity {

    Fragment selectedFragment;
    boolean doubleBackToExitPressedOnce;
    TextView txtTutorialText, txtTutorialInTitle, txtTutorialInDesc, txtHomeTeam;
    Button btnStart,btnNextStep;
    CircleImageView imgProfile, imgHomeTeam;
    LinearLayout viewNotification, viewCoins, viewRules, viewTeam, viewExactScore;
    RelativeLayout viewTutorial, viewSteps, viewWelcome;
    public String selectedFragmentstr = "predict";
    int selectedView = 0;

    public BottomNavigationView bottomNavigationView;

    @Override
    public int getContentViewId() {
        return R.layout.mainpage_activity;
    }

    @Override
    public void doOnCreate() {
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/footwinnews");
        bottomNavigationView = _findViewById(R.id.bottom_navigation);
        btnStart = _findViewById(R.id.btnStart);
        viewTutorial = _findViewById(R.id.viewTutorial);
        btnNextStep = _findViewById(R.id.btnNextStep);

        viewWelcome = _findViewById(R.id.viewWelcome);
        viewSteps = _findViewById(R.id.viewSteps);
        txtTutorialText = _findViewById(R.id.txtTutorialText);
        viewNotification = _findViewById(R.id.viewNotification);
        viewCoins = _findViewById(R.id.viewCoins);
        viewRules = _findViewById(R.id.viewRules);
        viewTeam = _findViewById(R.id.viewTeam);
        viewExactScore = _findViewById(R.id.viewExactScore);
        txtTutorialInTitle = _findViewById(R.id.txtTutorialInTitle);
        txtTutorialInDesc = _findViewById(R.id.txtTutorialInDesc);
        txtHomeTeam = _findViewById(R.id.txtHomeTeam);
        imgProfile = _findViewById(R.id.imgProfile);
        imgHomeTeam = _findViewById(R.id.imgHomeTeam);

        Picasso.with(MainPageActivity.this)
                .load(StaticData.config.getMediaUrl()+StaticData.user.getAvatar())
                .into(imgProfile);

        viewNotification.setAlpha(0f);
        viewCoins.setAlpha(0f);
        viewRules.setAlpha(0f);
        viewTeam.setAlpha(0f);
        viewExactScore.setAlpha(0f);
        viewWelcome.setVisibility(View.VISIBLE);
        viewTutorial.setVisibility(View.GONE);
        viewSteps.setVisibility(View.GONE);

        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, PredictFragment.newInstance());
        transaction.commit();

        String tutorialText = "Hello ".toUpperCase() +
                StaticData.user.getUsername() + ", " +
                StaticData.config.getTutorialText().toUpperCase();
        txtTutorialText.setText(tutorialText);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewWelcome.setVisibility(View.GONE);
                viewSteps.setVisibility(View.VISIBLE);
                viewNotification.animate().alpha(1f).setDuration(500);
                // TODO
//                AppUtils.setFirstLaunch(MainPageActivity.this);
            }
        });

        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedView == 0){
                    viewNotification.animate().alpha(0f).setDuration(500);
                    viewCoins.animate().alpha(1f).setDuration(500);

                    txtTutorialInTitle.setText("coin stash");
                    txtTutorialInDesc.setText("tap on the yellow coin to check your balance, and get more whenever you are out of coins");
                    selectedView = 1;
                }else if(selectedView == 1){
                    viewCoins.animate().alpha(0f).setDuration(500);
                    viewRules.animate().alpha(1f).setDuration(500);

                    txtTutorialInTitle.setText("view rules");
                    txtTutorialInDesc.setText("tap on the 'view rules' button to check the round rules");
                    selectedView = 2;
                }else if(selectedView == 2){
                    viewRules.animate().alpha(0f).setDuration(500);
                    viewTeam.animate().alpha(1f).setDuration(500);

                    txtTutorialInTitle.setText("Prediction");
                    txtTutorialInDesc.setText("tap on the team icon to predict the winning team");
                    selectedView = 3;
                }else if(selectedView == 3) {
                    viewTeam.animate().alpha(0f).setDuration(500);
                    viewExactScore.animate().alpha(1f).setDuration(500);

                    txtTutorialInTitle.setText("exact score");
                    txtTutorialInDesc.setText("tap here to predict the exact score and increase your winning coins");
                    btnNextStep.setText("get started");
                    selectedView = 4;
                }else {
                    viewTutorial.setVisibility(View.GONE);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });

        sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                if (!selectedFragmentstr.equals("predict")) {
                                    selectedFragmentstr = "predict";
                                    selectedFragment = PredictFragment.newInstance();
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frame_layout, selectedFragment);
                                    transaction.commit();
                                }
                                break;

                            case R.id.action_news:
                                if (!selectedFragmentstr.equals("news")) {
                                    selectedFragmentstr = "news";
                                    selectedFragment = NewsFragment.newInstance();
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frame_layout, selectedFragment);
                                    transaction.commit();
                                }
                                break;

                            case R.id.action_leaderboard:
                                if (!selectedFragmentstr.equals("leaderboard")) {
                                    selectedFragmentstr = "leaderboard";
                                    selectedFragment = LeaderboardFragment.newInstance();
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frame_layout, selectedFragment);
                                    transaction.commit();
                                }
                                break;

                            case R.id.action_profile:
                                if (!selectedFragmentstr.equals("profile")) {
                                    selectedFragmentstr = "profile";
                                    selectedFragment = ProfileFragment.newInstance();
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frame_layout, selectedFragment);
                                    transaction.commit();
                                }
                                break;

                        }

                        return true;
                    }
                });
    }

    private void sendRegistrationToServer(final String token) {
        ApiManager.getService().updateFirebaseToken(token).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("TOKEN UPDATED: ", token);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("TOKEN ERROR: ", t.getMessage());
            }
        });
    }

    public void checkTutorial(String teamName, String teamFlag) {
        if(AppUtils.isFirstLaunch(MainPageActivity.this)){
            viewTutorial.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
        }else {
            viewTutorial.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.GONE);

            txtHomeTeam.setText(teamName);

            Picasso.with(this)
                    .load(StaticData.config.getMediaUrl()+teamFlag)
                    .into(imgHomeTeam);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {

            // handle back button
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
            }

            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

            return true;
        }

        return false;
    }
}
