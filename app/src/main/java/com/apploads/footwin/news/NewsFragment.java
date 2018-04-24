package com.apploads.footwin.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.apploads.footwin.R;
import com.apploads.footwin.model.News;
import com.apploads.footwin.services.ApiManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {

    ListView listNews;
    NewsAdapter newsAdapter;
    private View parentView;

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.news_fragment, container, false);

        initView();

        return parentView;
    }

    private void initView(){
        listNews = parentView.findViewById(R.id.listNews);
//

        ApiManager.getService().getNews("http://newsapi.org//v2//everything?sources=bbc-sport&apiKey=bf84323b4c7244aca799c4ff1dda7e1e&q=world%20cup%20").enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                News news = response.body();
                if(news != null && response.isSuccessful()){
                    newsAdapter = new NewsAdapter(news.getArticles(), getContext());
                    listNews.setAdapter(newsAdapter);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
            }
        });

    }
}