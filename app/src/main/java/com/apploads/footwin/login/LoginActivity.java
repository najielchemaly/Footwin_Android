package com.apploads.footwin.login;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.BaseActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.signup.SignupStepOne;
import com.apploads.footwin.utils.AppUtils;
import com.apploads.footwin.utils.StringUtils;

public class LoginActivity extends BaseActivity {
    TextView txtRecover, txtCreateAccount;
    EditText txtEmail, txtPassword;
    ImageView imgFB, imgGoogle;
    Button btnLogin;

    @Override
    public int getContentViewId() {
        return R.layout.login_activity;
    }

    @Override
    public void doOnCreate(){
        initView();
        initListeners();
    }

    /**
     * initialize view
     */
    private void initView(){
        txtCreateAccount = _findViewById(R.id.txtCreateAccount);
        txtPassword = _findViewById(R.id.txtPassword);
        txtRecover = _findViewById(R.id.txtRecover);
        imgGoogle = _findViewById(R.id.imgGoogle);
        btnLogin = _findViewById(R.id.btnLogin);
        txtEmail = _findViewById(R.id.txtEmail);
        imgFB = _findViewById(R.id.imgFB);
    }

    /**
     * initialize listeners
     */
    private void initListeners(){

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()){
                    Toast.makeText(LoginActivity.this, "Login", Toast.LENGTH_SHORT).show();
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
    private boolean validateFields(){
        if(!StringUtils.isValid(txtEmail.getText()) || !StringUtils.isValid(txtPassword.getText())){
            AppUtils.showAlert(LoginActivity.this,"Error", "Fill both fields to continue");
            return false;
        }else {
            if(!AppUtils.isEmailValid(txtEmail.getText().toString())){
                AppUtils.showAlert(LoginActivity.this,"Error", "Enter a valid email address");
                return false;
            }else {
                return true;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
