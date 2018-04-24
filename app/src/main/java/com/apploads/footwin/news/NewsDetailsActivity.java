package com.apploads.footwin.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.model.Article;
import com.apploads.footwin.model.News;
import com.squareup.picasso.Picasso;

public class NewsDetailsActivity extends BaseActivity {

    Article article;
    TextView txtDesc, txtTitle, txtDate;
    Button btnClose;
    ImageView imgNews;

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
        imgNews = _findViewById(R.id.imgNews);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            article = (Article) b.getSerializable("news");

            txtDate.setText(article.getPublishedAt());
            txtTitle.setText(article.getTitle());
            txtDesc.setText(article.getDescription());

            Picasso.with(this)
                    .load(article.getUrlToImage())
                    .into(imgNews);
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
