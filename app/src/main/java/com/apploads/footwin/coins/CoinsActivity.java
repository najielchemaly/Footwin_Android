package com.apploads.footwin.coins;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.apploads.footwin.MainPageActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import static com.apploads.footwin.helpers.Constants.*;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.model.BasicResponse;
import com.apploads.footwin.model.Package;
import com.apploads.footwin.model.PackageResponse;
import com.apploads.footwin.model.Reward;
import com.apploads.footwin.services.ApiManager;
import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinsActivity extends BaseActivity implements BillingProcessor.IBillingHandler{
    CircularProgressBar circularProgressBar;
    Button btnClose, btnGetCoins, btnClosePackages, btnWatchVideo;
    TextView txtCoinsTotal, txtWinningCoinsTotal, txtNextRoundCoins, txtCollect;
    RelativeLayout viewBlackOpacity;
    RecyclerView listPackages;
    BillingProcessor billingProcessor;
    RewardedVideoAd mRewardedVideoAd;
    RelativeLayout viewLoading;
    ProgressBar progressBar;
    String pack;
    Package mPackage;

    @Override
    public int getContentViewId() {
        return R.layout.coins_activity;
    }

    @Override
    public void doOnCreate() {
        initView();
        initCarousel();
        initListeners();
    }

    private void initView(){
        billingProcessor = new BillingProcessor(this, GOOGLE_LICENSE_KEY, this);

        circularProgressBar = _findViewById(R.id.progress_bar);
        btnGetCoins = _findViewById(R.id.btnGetCoins);
        btnClose = _findViewById(R.id.btnClose);
        txtCoinsTotal = _findViewById(R.id.txtCoinsTotal);
        txtWinningCoinsTotal = _findViewById(R.id.txtWinningCoinsTotal);
        txtNextRoundCoins = _findViewById(R.id.txtNextRoundCoins);
        listPackages = _findViewById(R.id.listPackages);
        viewBlackOpacity = _findViewById(R.id.viewBlackOpacity);
        btnClosePackages = _findViewById(R.id.btnClosePackages);
        btnWatchVideo = _findViewById(R.id.btnWatchVideo);
        viewLoading = _findViewById(R.id.viewLoading);
        progressBar = _findViewById(R.id.spin_kit);
        txtCollect = _findViewById(R.id.txtCollect);

        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        viewLoading.setVisibility(View.GONE);

        viewBlackOpacity.setVisibility(View.GONE);
        viewBlackOpacity.setAlpha(0f);
        listPackages.setVisibility(View.GONE);

        txtCoinsTotal.setText(StaticData.user.getCoins());
        txtWinningCoinsTotal.setText(StaticData.user.getWinningCoins());
//        txtNextRoundCoins.setText(StaticData.config.getActiveRound().getMinimumAmount());
        txtNextRoundCoins.setText(StaticData.config.getWinningUser().getWinningCoins());
        AppUtils.startCountAnimation(txtWinningCoinsTotal,0, Integer.parseInt(StaticData.user.getWinningCoins()),1500);

        circularProgressBar.setAnimateProgress(true);
        circularProgressBar.setProgressAnimationDuration(1500);
        circularProgressBar.setMaximum(Float.parseFloat(StaticData.config.getWinningUser().getWinningCoins()));
        circularProgressBar.setProgress(Float.parseFloat(StaticData.user.getWinningCoins()));
        circularProgressBar.animate();

        if(txtNextRoundCoins.getText().toString() == null ||
                txtNextRoundCoins.getText().toString().isEmpty() ||
                txtNextRoundCoins.getText().toString().equals("0")) {
            txtCollect.setText("KEEP WINNING COINS");
            txtNextRoundCoins.setVisibility(View.GONE);
        }

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                if(mRewardedVideoAd.isLoaded()){
                    mRewardedVideoAd.show();
                    viewLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                viewLoading.setVisibility(View.VISIBLE);
                ApiManager.getService().getReward(StaticData.config.getActiveReward().getId(),
                        StaticData.config.getActiveReward().getAmount()).enqueue(new Callback<Reward>() {
                    @Override
                    public void onResponse(Call<Reward> call, Response<Reward> response) {
                        Reward reward = response.body();
                        if(reward.getStatus() == 1){
                            StaticData.user.setCoins(reward.getCoins());
                            AppUtils.saveUser(CoinsActivity.this, StaticData.user);
                        }
                        viewLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<Reward> call, Throwable t) {
                        viewLoading.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(CoinsActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });

        if(!StaticData.config.getIsIAPReady()) {
            btnWatchVideo.setVisibility(View.GONE);
        }
    }

    private void loadRewardedVideoAd() {
        if(mRewardedVideoAd != null) {
            mRewardedVideoAd.loadAd("ca-app-pub-8532510371470349/8896247436",
                    new AdRequest.Builder().build());
        }
    }

    private void initCarousel() {
        listPackages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getPackages();
    }

    public void purchaseProduct(Package package_sent){
        switch (package_sent.getTitle().toLowerCase()){
            case STARTER_PACK:
                billingProcessor.purchase(this, "com.footwin.starterpack", StaticData.user.getAccess_token());
                pack = "com.footwin.starterpack";
                break;
            case HALF_TIME_PACK:
                billingProcessor.purchase(this, "com.footwin.halftimepack", StaticData.user.getAccess_token());
                pack = "com.footwin.halftimepack";
                break;
            case HAT_TRICK_PACK:
                billingProcessor.purchase(this, "com.footwin.hattrickpack", StaticData.user.getAccess_token());
                pack = "com.footwin.hattrickpack";
                break;
            case SUPER_HAT_TRICK_PACK:
                billingProcessor.purchase(this, "com.footwin.superhattrickpack", StaticData.user.getAccess_token());
                pack = "com.footwin.superhattrickpack";
                break;
            case FOOTWIN_SPECIAL_PACK:
                billingProcessor.purchase(this, "com.footwin.footwinspecialpack", StaticData.user.getAccess_token());
                pack = "com.footwin.footwinspecialpack";
                break;
            case JOKER_PACK:
                billingProcessor.purchase(this, "com.footwin.jokerpack", StaticData.user.getAccess_token());
                pack = "com.footwin.jokerpack";
                break;
            default:
                break;
        }
        mPackage = package_sent;
    }

    private void initListeners(){
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnGetCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPackages(true);
            }
        });

        btnClosePackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPackages(false);
            }
        });

        btnWatchVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLoading.setVisibility(View.VISIBLE);
                loadRewardedVideoAd();
            }
        });
    }

    private void getPackages(){
        ApiManager.getService().getPackages().enqueue(new Callback<PackageResponse>() {
            @Override
            public void onResponse(Call<PackageResponse> call, Response<PackageResponse> response) {
                try {
                    PackageResponse packageResponse = response.body();
                    listPackages.setAdapter(new CoinsAdapter(packageResponse.getPackages(), CoinsActivity.this, CoinsActivity.this));
                } catch (Exception ex) {

                }
            }

            @Override
            public void onFailure(Call<PackageResponse> call, Throwable t) {
                Toast.makeText(CoinsActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPackages(boolean toShow){
        if(toShow){
            listPackages.setVisibility(View.VISIBLE);
            viewBlackOpacity.setVisibility(View.VISIBLE);
            viewBlackOpacity.animate().alpha(1f).setDuration(1000);

            btnClose.setVisibility(View.GONE);
            btnGetCoins.setVisibility(View.GONE);
        }else {
            listPackages.setVisibility(View.GONE);
            viewBlackOpacity.setVisibility(View.GONE);
            viewBlackOpacity.animate().alpha(0f).setDuration(1000);

            btnClose.setVisibility(View.VISIBLE);
            btnGetCoins.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        if (billingProcessor != null) {
            billingProcessor.release();
        }
        if(mRewardedVideoAd != null) {
            mRewardedVideoAd.destroy(this);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if(mRewardedVideoAd != null) {
            mRewardedVideoAd.resume(this);
        }
        super.onResume();
        txtCoinsTotal.setText(StaticData.user.getCoins());
        StaticData.context = CoinsActivity.this;
    }

    @Override
    public void onPause() {
        if(mRewardedVideoAd != null) {
            mRewardedVideoAd.pause(this);
        }
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(this, "success: " + productId, Toast.LENGTH_SHORT).show();
        ApiManager.getService().purchaseCoins(mPackage.getId(),mPackage.getPrice()).enqueue(new Callback<Reward>() {
            @Override
            public void onResponse(Call<Reward> call, Response<Reward> response) {
                Reward reward = response.body();
                if(reward.getStatus() == 1){
                    billingProcessor.consumePurchase(pack);
                    StaticData.user.setCoins(reward.getCoins());
                    AppUtils.saveUser(CoinsActivity.this, StaticData.user);
                }
            }

            @Override
            public void onFailure(Call<Reward> call, Throwable t) {

            }
        });

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        if(errorCode == 1){
            // Closed the dialogue without urchasing
        }else {
            Toast.makeText(this, "error: " + errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBillingInitialized() {

    }
}
