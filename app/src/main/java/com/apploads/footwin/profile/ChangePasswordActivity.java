package com.apploads.footwin.profile;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.helpers.CustomDialogClass;
import com.apploads.footwin.helpers.utils.StringUtils;
import com.apploads.footwin.model.BasicResponse;
import com.apploads.footwin.model.UserResponse;
import com.apploads.footwin.services.ApiManager;
import com.apploads.footwin.signup.SignupStepTwo;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {
    Button btnCancel, btnSave;
    EditText txtConfirmPassword;
    EditText txtOldPassword;
    EditText txtNewPassword;


    @Override
    public int getContentViewId() {
        return R.layout.change_password_activity;
    }

    @Override
    public void doOnCreate() {

        btnCancel = _findViewById(R.id.btnCancel);
        btnSave = _findViewById(R.id.btnSave);
        txtOldPassword = _findViewById(R.id.txtOldPassword);
        txtNewPassword = _findViewById(R.id.txtNewPassword);
        txtConfirmPassword = _findViewById(R.id.txtConfirmPassword);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtils.isValid(txtOldPassword) && StringUtils.isValid(txtNewPassword) && StringUtils.isValid(txtConfirmPassword)){
                    if(txtNewPassword.getText().toString().equals(txtConfirmPassword.getText().toString())){
                        callChangePasswordService();
                    }else {
                        CustomDialogClass dialogClass = new CustomDialogClass(ChangePasswordActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                            @Override
                            public void onConfirm(CustomDialogClass.DialogResponse response) {
                                response.getDialog().dismiss();
                            }

                            @Override
                            public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                            }
                        }, true);

                        dialogClass.setTitle("Oops");
                        dialogClass.setMessage("Passwords do not match");
                        dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialogClass.show();
                    }

                }else {
                    CustomDialogClass dialogClass = new CustomDialogClass(ChangePasswordActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                        @Override
                        public void onConfirm(CustomDialogClass.DialogResponse response) {
                            response.getDialog().dismiss();
                        }

                        @Override
                        public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                        }
                    }, true);

                    dialogClass.setTitle("Oops");
                    dialogClass.setMessage("Fill all the fields to continue");
                    dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialogClass.show();
                }
            }
        });
    }

    private void callChangePasswordService(){
        ApiManager.getService().changePassowrd(txtOldPassword.getText().toString(), txtNewPassword.getText().toString()).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                BasicResponse basicResponse = response.body();
                if(basicResponse.getStatus() == 1){
//
                    showToastyMessage(ChangePasswordActivity.this,basicResponse.getMessage());
                    finish();
                }else {
                    showToastyMessage(ChangePasswordActivity.this,"Something went wrong please try again");
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                showToastyMessage(ChangePasswordActivity.this,"Something went wrong please try again");
//
            }
        });
    }
}
