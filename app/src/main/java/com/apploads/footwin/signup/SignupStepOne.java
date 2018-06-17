package com.apploads.footwin.signup;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.CustomDialogClass;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.login.RetrievePasswordActivity;
import com.apploads.footwin.model.User;
import com.apploads.footwin.model.UserResponse;
import com.apploads.footwin.services.ApiManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @Override
    public void onBackPressed() {
        if(StaticData.user.getFacebookId() != null &&
                !StaticData.user.getFacebookId().toString().isEmpty() &&
                (StaticData.user.getFavoriteTeam() == null ||
                        StaticData.user.getFavoriteTeam().toString().isEmpty())) {
            Log.e("", "Restrict to add favorite team.");
        } else {
            finish();
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        }
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

        if(StaticData.user.getFacebookId() != null &&
                !StaticData.user.getFacebookId().toString().isEmpty() &&
                (StaticData.user.getFavoriteTeam() == null ||
                        StaticData.user.getFavoriteTeam().toString().isEmpty())) {
            btnCancel.setVisibility(View.INVISIBLE);
        }
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
                    showToastyMessage(SignupStepOne.this, "Please select a team before proceeding");
                } else {
                    if(StaticData.user.getFacebookId() != null &&
                            !StaticData.user.getFacebookId().toString().isEmpty() &&
                            (StaticData.user.getFavoriteTeam() == null ||
                            StaticData.user.getFavoriteTeam().toString().isEmpty())) {
                        ApiManager.getService().updateFavoriteTeam(StaticData.favTeam.getId()).enqueue(new Callback<UserResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                if(response.isSuccessful() && response.body().getStatus() == 1){
                                    UserResponse userResponse = response.body();
                                    CustomDialogClass dialogClass = new CustomDialogClass(SignupStepOne.this, new CustomDialogClass.AbstractCustomDialogListener() {
                                        @Override
                                        public void onConfirm(CustomDialogClass.DialogResponse response) {
                                            StaticData.user.setFavoriteTeam(StaticData.favTeam.getName());
                                            AppUtils.saveUser(SignupStepOne.this, StaticData.user);
                                            response.getDialog().dismiss();
                                            finish();
                                        }

                                        @Override
                                        public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                                        }
                                    }, true);

                                    dialogClass.setTitle("FOOTWIN");
                                    dialogClass.setMessage("Your favorite team was updated successfully");
                                    dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                    dialogClass.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {

                            }
                        });
                    } else {
                        Intent intent = new Intent(getApplicationContext(), SignupStepTwo.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
