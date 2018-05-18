package com.apploads.footwin.coins;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.helpers.Constants;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.model.Package;
import com.apploads.footwin.model.PackageResponse;
import com.apploads.footwin.services.ApiManager;
import com.budiyev.android.circularprogressbar.CircularProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinsActivity extends BaseActivity implements BillingProcessor.IBillingHandler{
    CircularProgressBar circularProgressBar;
    Button btnClose, btnGetCoins, btnClosePackages;
    TextView txtCoinsTotal, txtWinningCoinsTotal, txtNextRoundCoins;
    RelativeLayout viewBlackOpacity;
    RecyclerView listPackages;
    BillingProcessor bp;

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
        bp = new BillingProcessor(this, Constants.GOOGLE_LICENSE_KEY, this);

        circularProgressBar = _findViewById(R.id.progress_bar);
        btnGetCoins = _findViewById(R.id.btnGetCoins);
        btnClose = _findViewById(R.id.btnClose);
        txtCoinsTotal = _findViewById(R.id.txtCoinsTotal);
        txtWinningCoinsTotal = _findViewById(R.id.txtWinningCoinsTotal);
        txtNextRoundCoins = _findViewById(R.id.txtNextRoundCoins);
        listPackages = _findViewById(R.id.listPackages);
        viewBlackOpacity = _findViewById(R.id.viewBlackOpacity);
        btnClosePackages = _findViewById(R.id.btnClosePackages);

        viewBlackOpacity.setVisibility(View.GONE);
        viewBlackOpacity.setAlpha(0f);
        listPackages.setVisibility(View.GONE);

        txtCoinsTotal.setText(StaticData.user.getCoins());
        txtWinningCoinsTotal.setText(StaticData.user.getWinningCoins());
        txtNextRoundCoins.setText(StaticData.config.getActiveRound().getMinimumAmount());
        AppUtils.startCountAnimation(txtWinningCoinsTotal,0, Integer.parseInt(StaticData.user.getWinningCoins()),1500);

        circularProgressBar.setAnimateProgress(true);
        circularProgressBar.setProgressAnimationDuration(1500);
        circularProgressBar.setMaximum(Float.parseFloat(StaticData.config.getActiveRound().getMinimumAmount()));
        circularProgressBar.setProgress(Float.parseFloat(StaticData.user.getWinningCoins()));
        circularProgressBar.animate();
    }

    private void initCarousel() {
        // vertical and cycle layout
        listPackages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getPackages();
    }

    public void purchaseProduct(Package p){
        bp.purchase(this, "android.test.purchased", StaticData.user.getAccess_token());
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
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(this, "success: " + productId, Toast.LENGTH_SHORT).show();
        bp.consumePurchase("android.test.purchased");
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
