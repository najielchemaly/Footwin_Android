package com.apploads.footwin.profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.model.Article;

public class TermsAndConditionsActivity extends BaseActivity {

    Button btnClose;
    WebView webViewTerms;
    String type;
    TextView lblTerms;

    @Override
    public int getContentViewId() {
        return R.layout.terms_conditions_activity;
    }

    @Override
    public void doOnCreate() throws Exception {
        initView();
        initListeners();
    }

    private void initView(){

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            type = b.getString("type");
        }

        btnClose = _findViewById(R.id.btnClose);
        webViewTerms = _findViewById(R.id.webViewTerms);
        lblTerms = _findViewById(R.id.lblTerms);
        webViewTerms.getSettings().setJavaScriptEnabled(true);
        webViewTerms.setWebViewClient(new WebViewClient());
        if(type.equals("terms")){
            lblTerms.setText("TERMS AND CONDITIONS");
            webViewTerms.loadUrl("http://foot-win.com/terms");
        }else {
            lblTerms.setText("PRIVACY POLICY");
            webViewTerms.loadUrl("http://foot-win.com/privacy");
        }

        webViewTerms.setBackgroundColor(Color.TRANSPARENT);
    }

    private void initListeners(){
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
