package com.apploads.footwin.predict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.model.Match;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PredictFragment extends Fragment {

    ListView listMatches;
    Button btnRules;
    TextView txtRound, txtWinningCoinsTotal, txtCoinsTotal, txtNotificationTag;
    CircleImageView imgProfile;
    private View parentView;
    MatchesAdapter matchesAdapter;


    public static PredictFragment newInstance() {
        PredictFragment fragment = new PredictFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.predict_fragment, container, false);

        listMatches = parentView.findViewById(R.id.listMatches);
        btnRules = parentView.findViewById(R.id.btnRules);
        txtRound = parentView.findViewById(R.id.txtRound);
        txtWinningCoinsTotal = parentView.findViewById(R.id.txtWinningCoinsTotal);
        txtCoinsTotal = parentView.findViewById(R.id.txtCoinsTotal);
        txtNotificationTag = parentView.findViewById(R.id.txtNotificationTag);
        imgProfile = parentView.findViewById(R.id.imgProfile);

        txtRound.setText(StaticData.config.getActiveRound().getTitle());
        txtWinningCoinsTotal.setText(StaticData.config.getActiveRound().getWinningCoins());
        txtCoinsTotal.setText(StaticData.config.getActiveRound().getAllInCoins());

        MatchesAdapter matchesAdapter = new MatchesAdapter(getMatches(), getContext());
        listMatches.setAdapter(matchesAdapter);

        return parentView;
    }

    private List<Match> getMatches(){
        List<Match> matchesList = new ArrayList<>();

        Match match = new Match();
        match.setHomeTeam("Brazil");
        match.setAwayTeam("Germany");
        matchesList.add(match);

        match = new Match();
        match.setHomeTeam("Lebanon");
        match.setAwayTeam("Portugual");
        matchesList.add(match);

        match = new Match();
        match.setHomeTeam("England");
        match.setAwayTeam("Spain");
        matchesList.add(match);

        match = new Match();
        match.setHomeTeam("Argentina");
        match.setAwayTeam("Holland");
        matchesList.add(match);

        match = new Match();
        match.setHomeTeam("Italy");
        match.setAwayTeam("France");
        matchesList.add(match);

        return matchesList;
    }
}