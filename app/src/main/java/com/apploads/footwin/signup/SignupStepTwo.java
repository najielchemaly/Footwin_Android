package com.apploads.footwin.signup;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.utils.StringUtils;
import com.apploads.footwin.login.LoginActivity;
import com.apploads.footwin.model.UserResponse;
import com.apploads.footwin.services.ApiManager;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupStepTwo extends BaseActivity {
    Button btnCancel, btnContinue;
    TextView txtBack;
    TextView txtCountry;
    TextView txtGender;
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
        btnContinue = _findViewById(R.id.btnContinue);
        txtBack = _findViewById(R.id.txtBack);
        txtCountry = _findViewById(R.id.txtCountry);
        txtGender = _findViewById(R.id.txtGender);
        txtMobile = _findViewById(R.id.txtMobile);

        txtUsername = _findViewById(R.id.txtUsername);
        txtFullname = _findViewById(R.id.txtFullname);
        txtEmail = _findViewById(R.id.txtEmail);
        txtPassword = _findViewById(R.id.txtPassword);
    }

    private void initListeners(){
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
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

        txtGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
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
                        txtMobile.setText(dialCode);
                        picker.dismiss();
                    }
                });
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });

    }

    private void registerUser(){
        ApiManager.getService().registerUser(txtFullname.getText().toString() ,txtUsername.getText().toString()
                ,txtEmail.getText().toString(),txtPassword.getText().toString(),"+961"
                ,txtMobile.getText().toString(),"male",txtCountry.getText().toString(),"1").enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                Intent intent = new Intent(getApplicationContext(), SignupStepThree.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(SignupStepTwo.this, "test", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
