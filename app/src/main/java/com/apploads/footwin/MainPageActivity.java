package com.apploads.footwin;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.leaderboard.LeaderboardFragment;
import com.apploads.footwin.news.NewsFragment;
import com.apploads.footwin.predict.PredictFragment;
import com.apploads.footwin.profile.ProfileFragment;

public class MainPageActivity extends BaseActivity {

    Fragment selectedFragment;
    boolean doubleBackToExitPressedOnce;
    public String selectedFragmentstr = "predict";

    @Override
    public int getContentViewId() {
        return R.layout.mainpage_activity;
    }

    @Override
    public void doOnCreate() {
        BottomNavigationView bottomNavigationView = _findViewById(R.id.bottom_navigation);

        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, PredictFragment.newInstance());
        transaction.commit();

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
