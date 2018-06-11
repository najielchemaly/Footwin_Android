package com.apploads.footwin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.helpers.StaticData;

public class ViewRulesActivity extends Activity {

    TextView txtPredict, txtWin, txtExactScore;
    Button btnClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_rules_activity);

        initView();
        initListeners();
    }

    private void initView(){
        btnClose = findViewById(R.id.btnClose);
        txtPredict = findViewById(R.id.txtPredict);
        txtWin = findViewById(R.id.txtWin);
        txtExactScore = findViewById(R.id.txtExactScore);

        txtPredict.setText(StaticData.config.getActiveRound().getPredictionCoins());
        txtWin.setText(StaticData.config.getActiveRound().getWinningCoins());
        txtExactScore.setText(StaticData.config.getActiveRound().getExactScoreCoins());
    }

    private void initListeners(){
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
