package com.apploads.footwin.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.CustomDialogClass;
import com.apploads.footwin.helpers.utils.StringUtils;
import com.apploads.footwin.model.BasicResponse;
import com.apploads.footwin.services.ApiManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrievePasswordActivity extends BaseActivity {
    Button btnCancel, btnRecover;
    EditText txtEmail;

    @Override
    public int getContentViewId() {
        return R.layout.retrieve_password_activity;
    }

    @Override
    public void doOnCreate() {
        initView();
        initListeners();
    }

    /**
     * initialize view
     */
    private void initView(){
        btnRecover = _findViewById(R.id.btnRecover);
        btnCancel = _findViewById(R.id.btnCancel);
        txtEmail = _findViewById(R.id.txtEmail);
    }

    /**
     * initialize listeners
     */
    private void initListeners(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            }
        });

        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtils.isValid(txtEmail.getText())){
                    callForgotPasswordService();
                }else {
                    CustomDialogClass dialogClass = new CustomDialogClass(RetrievePasswordActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                        @Override
                        public void onConfirm(CustomDialogClass.DialogResponse response) {
                            response.getDialog().dismiss();
                            txtEmail.setText("");
                        }

                        @Override
                        public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                        }
                    }, true);

                    dialogClass.setTitle("Oops");
                    dialogClass.setMessage("Make sure you fill your email before proceeding");
                    dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialogClass.show();
                }
            }
        });
    }

    private void callForgotPasswordService(){
        ApiManager.getService(true).forgotPassword(txtEmail.getText().toString()).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                BasicResponse basicResponse = response.body();
                if(basicResponse.getStatus() == 1){
                    Toast.makeText(RetrievePasswordActivity.this, basicResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(RetrievePasswordActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Toast.makeText(RetrievePasswordActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
