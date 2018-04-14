package com.apploads.footwin.signup;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;

import com.apploads.footwin.BaseActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.StaticData;
import com.apploads.footwin.model.Config;

public class SignupStepOne extends BaseActivity {
    GridView gridTeams;
    Button btnCancel, btnConfirm;
    TeamsAdapter teamsAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.signup_step_one;
    }

    @Override
    public void doOnCreate() {
        initView();
        initListeners();
    }

    /**
     * initialize view
     */
    private void initView() {
        btnConfirm = _findViewById(R.id.btnConfirm);
        gridTeams = _findViewById(R.id.gridTeams);
        btnCancel = _findViewById(R.id.btnCancel);
        teamsAdapter = new TeamsAdapter(SignupStepOne.this, StaticData.config.getTeams());
        gridTeams.setAdapter(teamsAdapter);
    }

    /**
     * initialize listeners
     */
    private void initListeners() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupStepTwo.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
