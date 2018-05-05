package com.apploads.footwin.coins;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.model.Package;
import com.apploads.footwin.model.PackageResponse;
import com.apploads.footwin.services.ApiManager;
import com.budiyev.android.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinsActivity extends BaseActivity {
    CircularProgressBar circularProgressBar;
    Button btnClose, btnGetCoins, btnClosePackages;
    TextView txtCoinsTotal, txtWinningCoinsTotal, txtNextRoundCoins;
    RelativeLayout viewBlackOpacity;
    RecyclerView listPackages;

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
        circularProgressBar.setProgress(30f);
    }

    private void initCarousel() {
        // vertical and cycle layout
        listPackages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getPackages();
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
                PackageResponse packageResponse = response.body();
                listPackages.setAdapter(new CoinsAdapter(packageResponse.getPackages(), CoinsActivity.this));
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
}
