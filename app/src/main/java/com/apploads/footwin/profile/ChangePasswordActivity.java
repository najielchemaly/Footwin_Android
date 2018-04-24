package com.apploads.footwin.profile;

import android.view.View;
import android.widget.Button;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;

public class ChangePasswordActivity extends BaseActivity {
    Button btnCancel;

    @Override
    public int getContentViewId() {
        return R.layout.change_password_activity;
    }

    @Override
    public void doOnCreate() {

        btnCancel = _findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
