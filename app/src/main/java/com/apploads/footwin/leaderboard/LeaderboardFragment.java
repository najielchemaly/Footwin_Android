package com.apploads.footwin.leaderboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.model.Leaderboard;
import com.apploads.footwin.model.LeaderboardResponse;
import com.apploads.footwin.services.ApiManager;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardFragment extends Fragment {
    ListView listLeaderboard;
    private View parentView;
    CircleImageView imgRank1;
    TextView txtUserNameRank1;
    TextView txtCoinsRank1;
    ImageButton btnUp, btnDown, btnMe;
    LeaderBoardAdapter leaderBoardAdapter;
    ProgressBar progressBar;
    Leaderboard firstPlaceUser;
    List<Leaderboard> leaderboards = new ArrayList<>();

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

    private void initView() {
        listLeaderboard = parentView.findViewById(R.id.listLeaderboard);
        btnUp = parentView.findViewById(R.id.btnUp);
        btnDown = parentView.findViewById(R.id.btnDown);
        btnMe = parentView.findViewById(R.id.btnMe);
        progressBar = parentView.findViewById(R.id.spin_kit);
        imgRank1 = parentView.findViewById(R.id.imgRank1);
        txtUserNameRank1 = parentView.findViewById(R.id.txtUserNameRank1);
        txtCoinsRank1 = parentView.findViewById(R.id.txtCoinsRank1);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        getRanks();
    }


    private void initListeners() {
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listLeaderboard.smoothScrollToPosition(0);
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listLeaderboard.smoothScrollToPosition(leaderboards.size() - 1);
            }
        });
    }

    private void getRanks() {
        ApiManager.getService().getLeaderBoard().enqueue(new Callback<LeaderboardResponse>() {
            @Override
            public void onResponse(Call<LeaderboardResponse> call, Response<LeaderboardResponse> response) {
                if(response.isSuccessful() && response.body().getStatus() == 1){
                    LeaderboardResponse leaderboardResponse = response.body();
                    if(leaderboardResponse.getLeaderboard() != null){
                        firstPlaceUser = leaderboardResponse.getLeaderboard().get(0);
                        leaderboards = leaderboardResponse.getLeaderboard();

                        leaderboardResponse.getLeaderboard().remove(0);
                        leaderBoardAdapter = new LeaderBoardAdapter(leaderboardResponse.getLeaderboard(), getContext());
                        listLeaderboard.setAdapter(leaderBoardAdapter);


                        progressBar.setVisibility(View.GONE);
                        txtUserNameRank1.setText(firstPlaceUser.getFullname());
                        txtCoinsRank1.setText(firstPlaceUser.getCoins());

                        if(firstPlaceUser.getAvatar() != null && !firstPlaceUser.getAvatar().isEmpty()) {
                            Picasso.with(getActivity())
                                    .load(StaticData.config.getMediaUrl() + firstPlaceUser.getAvatar())
                                    .into(imgRank1);
                        } else {
                            imgRank1.setImageResource(R.drawable.avatar_male);
                            if(StaticData.user.getGender() == "female") {
                                imgRank1.setImageResource(R.drawable.avatar_female);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LeaderboardResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error fetching leaderboard", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}