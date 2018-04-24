package com.apploads.footwin.login;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.MainPageActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.CustomDialogClass;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.model.UserResponse;
import com.apploads.footwin.services.ApiManager;
import com.apploads.footwin.signup.SignupStepOne;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.helpers.utils.StringUtils;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    TextView txtRecover, txtCreateAccount;
    EditText txtEmail, txtPassword;
    ImageView imgFB, imgGoogle;
    Button btnLogin;
    ProgressBar progressBar;

    @Override
    public int getContentViewId() {
        return R.layout.login_activity;
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
        txtCreateAccount = _findViewById(R.id.txtCreateAccount);
        txtPassword = _findViewById(R.id.txtPassword);
        txtRecover = _findViewById(R.id.txtRecover);
        progressBar = _findViewById(R.id.spin_kit);
        imgGoogle = _findViewById(R.id.imgGoogle);
        btnLogin = _findViewById(R.id.btnLogin);
        txtEmail = _findViewById(R.id.txtEmail);
        imgFB = _findViewById(R.id.imgFB);

        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * initialize listeners
     */
    private void initListeners() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    callLoginService();
                }
            }
        });

        txtRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RetrievePasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupStepOne.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
    }

    /**
     * this function is for validating the email and password fields before proceeding
     */
    private boolean validateFields() {
        if (!StringUtils.isValid(txtEmail.getText()) || !StringUtils.isValid(txtPassword.getText())) {
            CustomDialogClass dialogClass = new CustomDialogClass(LoginActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                @Override
                public void onConfirm(CustomDialogClass.DialogResponse response) {
                    response.getDialog().dismiss();
                    txtEmail.setText("");
                    txtPassword.setText("");
                }

                @Override
                public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                }
            }, true);

            dialogClass.setTitle("Oops");
            dialogClass.setMessage("Make sure you fill both fields to continue");
            dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialogClass.show();
            return false;
        } else {
            if (!AppUtils.isEmailValid(txtEmail.getText().toString())) {
                CustomDialogClass dialogClass = new CustomDialogClass(LoginActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                    @Override
                    public void onConfirm(CustomDialogClass.DialogResponse response) {
                        response.getDialog().dismiss();
                        txtEmail.setText("");
                        txtPassword.setText("");
                    }

                    @Override
                    public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                    }
                }, true);

                dialogClass.setTitle("Oops");
                dialogClass.setMessage("Make sure you enter a valid email address");
                dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialogClass.show();
                return false;
            } else {
                return true;
            }
        }
    }

    private void callLoginService() {
        progressBar.setVisibility(View.VISIBLE);
        ApiManager.getService(true).login(txtEmail.getText().toString(), txtPassword.getText().toString()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 1){
                        UserResponse userResponse = response.body();
                        StaticData.user = userResponse.getUser();
                        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                        startActivity(intent);
                        finish();
                        progressBar.setVisibility(View.GONE);
                    }else if(response.body().getStatus() == 0){
                        progressBar.setVisibility(View.GONE);
                        CustomDialogClass dialogClass = new CustomDialogClass(LoginActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                            @Override
                            public void onConfirm(CustomDialogClass.DialogResponse response) {
                                response.getDialog().dismiss();
                                txtEmail.setText("");
                                txtPassword.setText("");
                            }

                            @Override
                            public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                            }
                        }, true);

                        dialogClass.setTitle("Oops");
                        dialogClass.setMessage("Incorrect Username or password");
                        dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialogClass.show();
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
