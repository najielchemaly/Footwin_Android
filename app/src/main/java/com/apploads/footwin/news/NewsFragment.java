package com.apploads.footwin.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.apploads.footwin.R;
import com.apploads.footwin.model.News;

import java.util.ArrayList;
import java.util.List;

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
        newsAdapter = new NewsAdapter(getNews(), getContext());
        listNews.setAdapter(newsAdapter);
    }

    private List<News> getNews(){
        List<News> newsList = new ArrayList<>();

        News news = new News();
        news.setDate("11 April 2018");
        news.setTitle("Messi is back to his old habbits of scoring 3 goals every game");
        newsList.add(news);

        news = new News();
        news.setDate("25 July 2018");
        news.setTitle("Mirana hoping his time would come");
        newsList.add(news);

        news = new News();
        news.setDate("11 April 2018");
        news.setTitle("Messi is back to his old habbits of scoring 3 goals every game");
        newsList.add(news);

        news = new News();
        news.setDate("11 April 2018");
        news.setTitle("Messi is back to his old habbits of scoring 3 goals every game");
        newsList.add(news);

        news = new News();
        news.setDate("11 April 2018");
        news.setTitle("Messi is back to his old habbits of scoring 3 goals every game");
        newsList.add(news);

        news = new News();
        news.setDate("11 April 2018");
        news.setTitle("Messi is back to his old habbits of scoring 3 goals every game");
        newsList.add(news);

        news = new News();
        news.setDate("11 April 2018");
        news.setTitle("Messi is back to his old habbits of scoring 3 goals every game");
        newsList.add(news);

        return newsList;
    }
}