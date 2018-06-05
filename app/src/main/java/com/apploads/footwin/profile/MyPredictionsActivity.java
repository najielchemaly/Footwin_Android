package com.apploads.footwin.profile;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apploads.footwin.NoDataView;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.model.Match;
import com.apploads.footwin.model.PredictionResponse;
import com.apploads.footwin.services.ApiManager;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPredictionsActivity extends BaseActivity {
    ListView listPredictions;
    Button btnClose;
    MatchesResultAdapter matchesResultAdapter;
    ProgressBar progressBar;
    RelativeLayout viewNoData;
    NoDataView noDataView;

    @Override
    public int getContentViewId() {
        return R.layout.my_predictions_activity;
    }

    @Override
    public void doOnCreate(){
        listPredictions = _findViewById(R.id.listPredictions);
        btnClose = _findViewById(R.id.btnClose);
        progressBar = _findViewById(R.id.spin_kit);
        viewNoData = _findViewById(R.id.viewNoData);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        ApiManager.getService().getPredictions().enqueue(new Callback<PredictionResponse>() {
            @Override
            public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {

                if(response.isSuccessful() && response.body() != null){
                    PredictionResponse predictionResponse = response.body();
                    if(predictionResponse.getPredictions().size() > 0){
                        matchesResultAdapter = new MatchesResultAdapter(predictionResponse.getPredictions(), MyPredictionsActivity.this);
                        listPredictions.setAdapter(matchesResultAdapter);
                        viewNoData.setVisibility(View.GONE);
                    }else {
                        if(noDataView == null) {
                            noDataView = new NoDataView(MyPredictionsActivity.this, "You do not have any predictions yet!");
                            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            viewNoData.addView(noDataView, lp);
                        }

                        viewNoData.setVisibility(View.VISIBLE);
                    }
                }else {
                    //TODO SHOW NO PREDICTION SCREEN
                    viewNoData.setVisibility(View.GONE);
                    Toast.makeText(MyPredictionsActivity.this, "Check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PredictionResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
