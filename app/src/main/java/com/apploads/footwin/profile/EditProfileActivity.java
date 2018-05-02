package com.apploads.footwin.profile;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.helpers.CustomDialogClass;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.helpers.utils.StringUtils;
import com.apploads.footwin.login.LoginActivity;
import com.apploads.footwin.model.UserResponse;
import com.apploads.footwin.services.ApiManager;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BaseActivity {
    Button btnSave;
    EditText txtFullname, txtEmail, txtMobile;
    MaterialSpinner spinnerGender;
    TextView txtCountry, txtPhoneCode;

    @Override
    public int getContentViewId() {
        return R.layout.edit_profile_activity;
    }

    @Override
    public void doOnCreate() {
        initView();
        initListeners();
    }

    private void initView(){
        btnSave = _findViewById(R.id.btnSave);
        txtFullname = _findViewById(R.id.txtFullname);
        txtEmail = _findViewById(R.id.txtEmail);
        txtMobile = _findViewById(R.id.txtMobile);
        spinnerGender = _findViewById(R.id.spinnerGender);
        txtCountry = _findViewById(R.id.txtCountry);
        txtPhoneCode = _findViewById(R.id.txtPhoneCode);
        spinnerGender.setItems("Male", "FEMALE");

        txtFullname.setText(StaticData.user.getFullname());
        txtCountry.setText(StaticData.user.getCountry());
        txtEmail.setText(StaticData.user.getEmail());
        txtMobile.setText(StaticData.user.getPhone());
        txtPhoneCode.setText(StaticData.user.getPhoneCode());
    }

    private void initListeners(){
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateView()){
                    editUserService();
                }
            }
        });
    }

    private boolean validateView(){
        if(StringUtils.isValid(txtFullname.getText())
                && StringUtils.isValid(txtEmail.getText()) && StringUtils.isValid(txtCountry.getText())
                && StringUtils.isValid(txtMobile.getText()) && StringUtils.isValid(spinnerGender.getText())){

            return true;
        }else{
            return false;
        }
    }

    private void editUserService(){
        ApiManager.getService().editUser(txtFullname.getText().toString(),
                txtEmail.getText().toString(), txtCountry.getText().toString(),
                txtPhoneCode.getText().toString(),txtMobile.getText().toString(),
                spinnerGender.getText().toString()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                StaticData.user = userResponse.getUser();
                AppUtils.saveUser(EditProfileActivity.this, userResponse.getUser());
                CustomDialogClass dialogClass = new CustomDialogClass(EditProfileActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                    @Override
                    public void onConfirm(CustomDialogClass.DialogResponse response) {
                        response.getDialog().dismiss();
                    }

                    @Override
                    public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                    }
                }, true);

                dialogClass.setTitle("Success");
                dialogClass.setMessage("Your user was successfully updated");
                dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialogClass.show();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }
}
