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
import com.apploads.footwin.model.News;
import com.apploads.footwin.model.Notification;

import java.util.List;

public class NewsAdapter extends BaseAdapter {

    private List<News> root;
    private Context context;
    private LayoutInflater mInflater;

    public NewsAdapter(List<News> root, Context context){
        this.root        = root;
        this.context     = context;
        mInflater = LayoutInflater.from(context);
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
        final News news = (News) getItem(position);
        convertView = mInflater.inflate(R.layout.news_row_item, null);

        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtDate = convertView.findViewById(R.id.txtDate);
        final ImageView imgNews = convertView.findViewById(R.id.imgNews);

        txtTitle.setText(news.getTitle());
        txtDate.setText(news.getDate());

        imgNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("news", news);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

}