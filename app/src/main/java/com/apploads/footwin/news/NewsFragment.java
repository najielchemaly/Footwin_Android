package com.apploads.footwin.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apploads.footwin.NoDataView;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.model.Article;
import com.apploads.footwin.model.News;
import com.apploads.footwin.services.ApiManager;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ListView listNews;
    NewsAdapter newsAdapter;
    private View parentView;
    RelativeLayout viewNoData;
    ProgressBar progressBar;
    NoDataView noDataView;
    SwipeRefreshLayout swipeRefreshLayout;

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
        progressBar = parentView.findViewById(R.id.spin_kit);
        viewNoData = parentView.findViewById(R.id.viewNoData);
        swipeRefreshLayout = (SwipeRefreshLayout) parentView.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        callService();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        callService();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void callService(){
        String serviceName = StaticData.user.getFavoriteTeam() == null ? StaticData.config.getNewsUrl() :
                StaticData.config.getNewsUrl() + StaticData.user.getFavoriteTeam();

        ApiManager.getService().getNews(serviceName).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful()){
                    if(response.body().getArticles() != null){
                        News news = response.body();
                        Collections.sort(news.getArticles(),Collections.<Article>reverseOrder());
                        newsAdapter = new NewsAdapter(news.getArticles(), getContext());
                        listNews.setAdapter(newsAdapter);
                        progressBar.setVisibility(View.GONE);
                    }else {
                        if(noDataView == null) {
                            noDataView = new NoDataView(getActivity(), "IT SEEMS THERE ARE NO NEWS YET!!");
                            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            viewNoData.addView(noDataView, lp);
                        }

                        viewNoData.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }else {
                    if(noDataView == null) {
                        noDataView = new NoDataView(getActivity(), "IT SEEMS THERE ARE NO NEWS YET!!");
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        viewNoData.addView(noDataView, lp);
                    }

                    viewNoData.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}