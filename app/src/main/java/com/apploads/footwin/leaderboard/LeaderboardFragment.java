package com.apploads.footwin.leaderboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.apploads.footwin.R;
import com.apploads.footwin.model.Leaderboard;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {
    ListView listLeaderboard;
    private View parentView;
    ImageButton btnUp, btnDown, btnMe;
    LeaderBoardAdapter leaderBoardAdapter;

    public static LeaderboardFragment newInstance() {
        LeaderboardFragment fragment = new LeaderboardFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.leaderboard_fragment, container, false);

        initView();
        initListeners();

        return parentView;
    }

    private void initView(){
        listLeaderboard = parentView.findViewById(R.id.listLeaderboard);
        btnUp = parentView.findViewById(R.id.btnUp);
        btnDown = parentView.findViewById(R.id.btnDown);
        btnMe = parentView.findViewById(R.id.btnMe);

        leaderBoardAdapter = new LeaderBoardAdapter(getRanks(), getContext());
        listLeaderboard.setAdapter(leaderBoardAdapter);
    }


    private void initListeners(){
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listLeaderboard.smoothScrollToPosition(0);
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listLeaderboard.smoothScrollToPosition(getRanks().size() - 1);
            }
        });
    }
    private List<Leaderboard> getRanks(){
        List<Leaderboard> leaderboardsList = new ArrayList<>();

        for(int i = 0; i<50; i++){
            Leaderboard leaderboard = new Leaderboard();
            leaderboard.setAmount(1000);
            leaderboard.setUsername("MEssi");
            leaderboard.setRank(i);
            leaderboardsList.add(leaderboard);
        }

        return leaderboardsList;
    }
}