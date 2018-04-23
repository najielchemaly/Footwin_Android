package com.apploads.footwin.news;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apploads.footwin.R;
import com.apploads.footwin.model.Article;
import com.apploads.footwin.model.News;
import com.apploads.footwin.model.Notification;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends BaseAdapter {

    private List<Article> root;
    private Context context;
    private LayoutInflater mInflater;

    public NewsAdapter(List<Article> root, Context context){
        this.root        = root;
        this.context     = context;
        if(context != null){
            mInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return root.size();
    }

    @Override
    public Object getItem(int position) {
        return root.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) Math.random();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Article article = (Article) getItem(position);
        convertView = mInflater.inflate(R.layout.news_row_item, null);

        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtDate = convertView.findViewById(R.id.txtDate);
        final ImageView imgNews = convertView.findViewById(R.id.imgNews);

        txtTitle.setText(article.getTitle());
        txtDate.setText(article.getPublishedAt());

        Picasso.with(context)
                .load(article.getUrlToImage())
                .into(imgNews);

        imgNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("news", article);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

}