package com.apploads.footwin.predict;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.ViewRulesActivity;
import com.apploads.footwin.coins.CoinsActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.model.Match;
import com.apploads.footwin.model.Profile;
import com.apploads.footwin.notifications.NotificationsActivity;
import com.apploads.footwin.services.ApiManager;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PredictFragment extends Fragment {

    ListView listMatches;
    Button btnRules, btnConfirm, btnCancel;
    TextView txtRound, txtWinningCoinsTotal, txtCoinsTotal, txtNotificationTag, txtHomeTeam, txtAwayTeam;
    CircleImageView imgProfile, imgHomeTeam, imgAwayTeam;
    private View parentView;
    MatchesAdapter matchesAdapter;
    RelativeLayout viewExactScore;
    ImageView imgCoins;
    ProgressBar progressBar;
    Animation bottom_to_top, top_to_bottom;


    public static PredictFragment newInstance() {
        PredictFragment fragment = new PredictFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.predict_fragment, container, false);

        initView();
        initListeners();

        return parentView;
    }

    private void initView() {
        listMatches = parentView.findViewById(R.id.listMatches);
        btnRules = parentView.findViewById(R.id.btnRules);
        imgCoins = parentView.findViewById(R.id.imgCoins);
        txtRound = parentView.findViewById(R.id.txtRound);
        txtWinningCoinsTotal = parentView.findViewById(R.id.txtWinningCoinsTotal);
        txtCoinsTotal = parentView.findViewById(R.id.txtCoinsTotal);
        txtNotificationTag = parentView.findViewById(R.id.txtNotificationTag);
        imgProfile = parentView.findViewById(R.id.imgProfile);
        progressBar = parentView.findViewById(R.id.spin_kit);
        viewExactScore = parentView.findViewById(R.id.viewExactScore);
        btnCancel = parentView.findViewById(R.id.btnCancel);
        btnConfirm = parentView.findViewById(R.id.btnConfirm);
        txtHomeTeam = parentView.findViewById(R.id.txtHomeTeam);
        txtAwayTeam = parentView.findViewById(R.id.txtAwayTeam);
        imgHomeTeam = parentView.findViewById(R.id.imgHomeTeam);
        imgAwayTeam = parentView.findViewById(R.id.imgAwayTeam);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        viewExactScore.setVisibility(View.GONE);

        txtRound.setText(StaticData.config.getActiveRound().getTitle());
        txtWinningCoinsTotal.setText(StaticData.config.getActiveRound().getWinningCoins());
        txtCoinsTotal.setText(StaticData.config.getActiveRound().getAllInCoins());

        bottom_to_top = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_to_top_wt);
        top_to_bottom = AnimationUtils.loadAnimation(getContext(), R.anim.top_to_bottom_wt);

        callMatchesService();
    }

    public void showExactScore(Match match) {
        viewExactScore.setVisibility(View.VISIBLE);
        viewExactScore.startAnimation(bottom_to_top);

        txtHomeTeam.setText(match.getHomeName());
        txtAwayTeam.setText(match.getAwayName());
    }

    private void initListeners() {
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationsActivity.class);
                startActivity(intent);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewExactScore.startAnimation(top_to_bottom);
                viewExactScore.setVisibility(View.GONE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewExactScore.startAnimation(top_to_bottom);
                viewExactScore.setVisibility(View.GONE);
            }
        });

        imgCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CoinsActivity.class);
                startActivity(intent);
            }
        });

        btnRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ViewRulesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callMatchesService() {
        ApiManager.getService().getMatches().enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                final Profile match = response.body();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MatchesAdapter matchesAdapter = new MatchesAdapter(match.getMatches(), getContext(), PredictFragment.this);
                        listMatches.setAdapter(matchesAdapter);
                        progressBar.setVisibility(View.GONE);

                    }
                }, 2000);
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
            }
        });
    }
}