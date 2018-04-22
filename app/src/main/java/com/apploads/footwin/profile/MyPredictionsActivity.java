package com.apploads.footwin.profile;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.model.Match;

import java.util.ArrayList;
import java.util.List;

public class MyPredictionsActivity extends BaseActivity {
    ListView listPredictions;
    Button btnClose;
    MatchesResultAdapter matchesResultAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.my_predictions_activity;
    }

    @Override
    public void doOnCreate(){
        listPredictions = _findViewById(R.id.listPredictions);
        btnClose = _findViewById(R.id.btnClose);
        matchesResultAdapter = new MatchesResultAdapter(getPredictions(), MyPredictionsActivity.this);
        listPredictions.setAdapter(matchesResultAdapter);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private List<Match> getPredictions(){
        List<Match> matchList = new ArrayList<>();

        Match match = new Match();
        match.setHomeScore("1");
        match.setAwayScore("2");
        match.setAwayName("Germany");
        match.setHomeName("Brazil");
        matchList.add(match);

        match = new Match();
        match.setHomeScore("2");
        match.setAwayScore("1");
        match.setAwayName("Germany");
        match.setHomeName("Brazil");
        matchList.add(match);

        match = new Match();
        match.setHomeScore("1");
        match.setAwayScore("1");
        match.setAwayName("Germany");
        match.setHomeName("Brazil");
        matchList.add(match);

        match = new Match();
        match.setHomeScore("1");
        match.setAwayScore("0");
        match.setAwayName("Germany");
        match.setHomeName("Brazil");
        matchList.add(match);

        return matchList;
    }

}
