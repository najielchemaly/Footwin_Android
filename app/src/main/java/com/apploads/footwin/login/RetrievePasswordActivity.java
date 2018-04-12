package com.apploads.footwin.login;

import android.view.View;
import android.widget.Button;

import com.apploads.footwin.BaseActivity;
import com.apploads.footwin.R;

public class RetrievePasswordActivity extends BaseActivity {
    Button btnCancel, btnRecover;

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
    }
}
