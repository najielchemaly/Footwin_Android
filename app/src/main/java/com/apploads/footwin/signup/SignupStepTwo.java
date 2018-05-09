package com.apploads.footwin.signup;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.CustomDialogClass;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.helpers.utils.StringUtils;
import com.apploads.footwin.login.LoginActivity;
import com.apploads.footwin.login.RetrievePasswordActivity;
import com.apploads.footwin.model.User;
import com.apploads.footwin.model.UserResponse;
import com.apploads.footwin.services.ApiManager;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupStepTwo extends BaseActivity {
    Button btnCancel;
    TextView txtBack;
    TextView txtCountry;
    TextView txtPhoneCode;
    RelativeLayout viewContinue;
    MaterialSpinner spinnerGender;
    EditText txtFullname, txtUsername, txtEmail, txtPassword, txtMobile;

    @Override
    public int getContentViewId() {
        return R.layout.signup_step_two;
    }

    @Override
    public void doOnCreate() {
        initView();
        initListeners();
    }

    private void initView(){
        btnCancel = _findViewById(R.id.btnCancel);
        viewContinue = _findViewById(R.id.viewContinue);
        txtBack = _findViewById(R.id.txtBack);
        txtCountry = _findViewById(R.id.txtCountry);
        spinnerGender = _findViewById(R.id.spinnerGender);
        txtMobile = _findViewById(R.id.txtMobile);
        txtPhoneCode = _findViewById(R.id.txtPhoneCode);

        txtUsername = _findViewById(R.id.txtUsername);
        txtFullname = _findViewById(R.id.txtFullname);
        txtEmail = _findViewById(R.id.txtEmail);
        txtPassword = _findViewById(R.id.txtPassword);

        spinnerGender.setItems("MALE", "FEMALE");
    }

    private void initListeners(){
        viewContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateView()){
                    registerUser();
                }else {
                    CustomDialogClass dialogClass = new CustomDialogClass(SignupStepTwo.this, new CustomDialogClass.AbstractCustomDialogListener() {
                        @Override
                        public void onConfirm(CustomDialogClass.DialogResponse response) {
                            response.getDialog().dismiss();
                        }

                        @Override
                        public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                        }
                    }, true);

                    dialogClass.setTitle("Oops");
                    dialogClass.setMessage("Make sure you fill all the fields before proceeding");
                    dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialogClass.show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            }
        });

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        txtCountry.setText(name);
                        txtPhoneCode.setText(dialCode);
                        picker.dismiss();
                    }
                });
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });
    }

    private boolean validateView(){
        if(StringUtils.isValid(txtFullname.getText()) && StringUtils.isValid(txtUsername.getText())
                && StringUtils.isValid(txtEmail.getText()) && StringUtils.isValid(txtPassword.getText())
                && StringUtils.isValid(txtCountry.getText()) && StringUtils.isValid(txtMobile.getText())
                && StringUtils.isValid(spinnerGender.getText())){

            return true;
        }else{
            return false;
        }
    }

    private void registerUser(){

        User user = new User();
        user.setFullname(txtFullname.getText().toString());
        user.setUsername(txtUsername.getText().toString());
        user.setEmail(txtEmail.getText().toString());
        user.setPhoneCode(txtPhoneCode.getText().toString());
        user.setPhone(txtMobile.getText().toString());
        user.setCountry(txtCountry.getText().toString());
        user.setFavoriteTeam(StaticData.favTeam.getId());
        user.setGender(spinnerGender.getText().toString().toLowerCase());

        Intent intent = new Intent(getApplicationContext(), SignupStepThree.class);
        intent.putExtra("password", txtPassword.getText().toString());
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
