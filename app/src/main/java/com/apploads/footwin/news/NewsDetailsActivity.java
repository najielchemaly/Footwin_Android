package com.apploads.footwin.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.model.News;

public class NewsDetailsActivity extends BaseActivity {

    News news;
    TextView txtDesc, txtTitle, txtDate;
    Button btnClose;

    @Override
    public int getContentViewId() {
        return R.layout.news_details_activity;
    }

    @Override
    public void doOnCreate()  {
        initView();
        initListeners();
    }

    private void initView(){
        txtDesc = _findViewById(R.id.txtDesc);
        txtDate = _findViewById(R.id.txtDate);
        txtTitle = _findViewById(R.id.txtTitle);
        btnClose = _findViewById(R.id.btnClose);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            news = (News) b.getSerializable("news");

            txtDate.setText(news.getDate());
            txtTitle.setText(news.getTitle());
            txtDesc.setText(news.getDesc());
        }
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
