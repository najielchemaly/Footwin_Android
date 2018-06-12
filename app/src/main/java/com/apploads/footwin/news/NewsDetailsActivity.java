package com.apploads.footwin.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.model.Article;
import com.apploads.footwin.model.News;
import com.bumptech.glide.Glide;
//import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class NewsDetailsActivity extends BaseActivity {

    Article article;
    TextView txtDesc, txtTitle, txtDate, txtUrl;
    Button btnClose;
    ImageView imgNews;
    String newURL;

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
        txtUrl = _findViewById(R.id.txtUrl);
        imgNews = _findViewById(R.id.imgNews);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            article = (Article) b.getSerializable("news");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
            txtDate.setText(simpleDateFormat.format(article.getPublishedAt()));
            txtTitle.setText(article.getTitle());
            txtDesc.setText(article.getDescription());
            newURL = article.getUrl();

            Glide
                    .with(this)
                    .load(Uri.parse(article.getUrlToImage()))
//                .apply(new RequestOptions()
//                        .placeholder(R.mipmap.ic_launcher)
//                        .fitCenter())
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

        txtUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(newURL));
                startActivity(i);
            }
        });
    }
}
