package com.apploads.footwin.signup;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.StaticData;

public class SignupStepOne extends BaseActivity {
    GridView gridTeams;
    Button btnCancel;
    RelativeLayout viewContinue;
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
        viewContinue = _findViewById(R.id.viewContinue);
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

        viewContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StaticData.favTeam == null){
                    Toast.makeText(SignupStepOne.this, "please select a team before proceeding", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), SignupStepTwo.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
