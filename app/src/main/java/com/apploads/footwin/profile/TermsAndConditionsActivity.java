package com.apploads.footwin.profile;

import android.graphics.Color;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;

public class TermsAndConditionsActivity extends BaseActivity {

    Button btnClose;
    WebView webViewTerms;

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
        btnClose = _findViewById(R.id.btnClose);
        webViewTerms = _findViewById(R.id.webViewTerms);
        webViewTerms.getSettings().setJavaScriptEnabled(true);
        webViewTerms.setWebViewClient(new WebViewClient());
        webViewTerms.loadUrl("http://foot-win.com/terms");
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
