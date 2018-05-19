package com.apploads.footwin.predict;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.MainPageActivity;
import com.apploads.footwin.ViewRulesActivity;
import com.apploads.footwin.coins.CoinsActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.CustomDialogClass;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.helpers.utils.StringUtils;
import com.apploads.footwin.login.LoginActivity;
import com.apploads.footwin.model.BasicResponse;
import com.apploads.footwin.model.Match;
import com.apploads.footwin.model.Profile;
import com.apploads.footwin.notifications.NotificationsActivity;
import com.apploads.footwin.services.ApiManager;
import com.apploads.footwin.signup.SignupStepThree;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PredictFragment extends Fragment {

    ListView listMatches;
    Button btnRules, btnConfirm, btnCancel;
    EditText txtAwayScore, txtHomeScore;
    TextView txtRound, txtWinningCoinsTotal, txtCoinsTotal, txtNotificationTag, txtHomeTeam, txtAwayTeam;
    CircleImageView imgProfile, imgHomeTeam, imgAwayTeam;
    private View parentView;
    RelativeLayout viewExactScore, viewExactScoreParent, viewNotificationTag;
    ImageView imgCoins;
    ProgressBar progressBar;
    Animation bottom_to_top, top_to_bottom;
    MatchesAdapter matchesAdapter;
    SwipeRefreshLayout pullToRefresh;
    int badge;

    private String homeScore = "-1";
    private String awayScore = "-1";
    Match selectedMatch;

    MainPageActivity mainPageActivity;

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

    @Override
    public void onResume() {
        super.onResume();
        updateBadge();
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
        viewExactScoreParent = parentView.findViewById(R.id.viewExactScoreParent);
        viewNotificationTag = parentView.findViewById(R.id.viewNotificationTag);
        btnCancel = parentView.findViewById(R.id.btnCancel);
        btnConfirm = parentView.findViewById(R.id.btnConfirm);
        txtAwayScore = parentView.findViewById(R.id.txtAwayScore);
        txtHomeScore = parentView.findViewById(R.id.txtHomeScore);
        txtHomeTeam = parentView.findViewById(R.id.txtHomeTeam);
        txtAwayTeam = parentView.findViewById(R.id.txtAwayTeam);
        imgHomeTeam = parentView.findViewById(R.id.imgHomeTeam);
        imgAwayTeam = parentView.findViewById(R.id.imgAwayTeam);
        pullToRefresh = parentView.findViewById(R.id.pullToRefresh);

        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        viewExactScoreParent.setVisibility(View.GONE);

        if(StaticData.user.getAvatar() != null && !StaticData.user.getAvatar().isEmpty()) {
            Picasso.with(getActivity())
                    .load(StaticData.config.getMediaUrl() + StaticData.user.getAvatar())
                    .into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.avatar_male);
            if(StaticData.user.getGender() == "female") {
                imgProfile.setImageResource(R.drawable.avatar_female);
            }
        }

        txtRound.setText(StaticData.config.getActiveRound().getTitle());

        AppUtils.startCountAnimation(txtCoinsTotal,0, Integer.parseInt(StaticData.user.getCoins()),1500);
        AppUtils.startCountAnimation(txtWinningCoinsTotal,0, Integer.parseInt(StaticData.user.getWinningCoins()),1500);

        bottom_to_top = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_to_top_wt);
        top_to_bottom = AnimationUtils.loadAnimation(getContext(), R.anim.top_to_bottom_wt);

        if(txtNotificationTag.getText().toString().isEmpty()) {
            viewNotificationTag.setVisibility(View.INVISIBLE);
        }

        if(getActivity().getClass().equals(MainPageActivity.class)) {
            mainPageActivity = (MainPageActivity)getActivity();
        }
        callMatchesService();
    }

    public void updateBadge(){
        badge = AppUtils.getBadge(getActivity());

        if(badge == 0){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewNotificationTag.setVisibility(View.GONE);
                    txtNotificationTag.setText("");
                }
            });
        }else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewNotificationTag.setVisibility(View.VISIBLE);
                    txtNotificationTag.setText(String.valueOf(badge));
                }
            });
        }
    }

    public void updateWinningCoins(){
        txtWinningCoinsTotal.setText(StaticData.user.getWinningCoins());
    }

    public void showExactScore(Match match) {
        viewExactScoreParent.setVisibility(View.VISIBLE);
        btnRules.setAlpha(0.5f);
        btnRules.setClickable(false);
        viewExactScore.startAnimation(bottom_to_top);
        selectedMatch = match;

        txtHomeTeam.setText(match.getHomeName());
        txtAwayTeam.setText(match.getAwayName());

        Picasso.with(getActivity())
                .load(StaticData.config.getMediaUrl()+match.getHomeFlag())
                .into(imgHomeTeam);

        Picasso.with(getActivity())
                .load(StaticData.config.getMediaUrl()+match.getAwayFlag())
                .into(imgAwayTeam);

        if(mainPageActivity != null) {
            mainPageActivity.bottomNavigationView.setVisibility(View.GONE);
        }
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
                top_to_bottom.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        viewExactScoreParent.setVisibility(View.GONE);
                        if(mainPageActivity != null) {
                            mainPageActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                btnRules.setAlpha(1f);
                btnRules.setClickable(true);

                int homeScoreInt = -1;
                int awayScoreInt = -1;
                if(!txtHomeScore.getText().toString().equals("") && !txtAwayScore.getText().toString().equals("")){
                     homeScoreInt = Integer.parseInt(txtHomeScore.getText().toString());
                     awayScoreInt = Integer.parseInt(txtAwayScore.getText().toString());
                }

                if(homeScoreInt > awayScoreInt && selectedMatch.isAwayToWin()){
                    Toast.makeText(getActivity(), "You entered a score that is conficting with your prediction, set home to win", Toast.LENGTH_SHORT).show();
                }

                if(homeScoreInt < awayScoreInt && selectedMatch.isHomeToWin()){
                    Toast.makeText(getActivity(), "You entered a score that is conficting with your prediction, set away to win", Toast.LENGTH_SHORT).show();
                }

                if(StringUtils.isValid(txtAwayScore.getText()) || StringUtils.isValid(txtHomeScore.getText())){
                    homeScore = txtHomeScore.getText().toString();
                    awayScore = txtAwayScore.getText().toString();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewExactScore.startAnimation(top_to_bottom);
                top_to_bottom.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        viewExactScoreParent.setVisibility(View.GONE);
                        if(mainPageActivity != null) {
                            mainPageActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                btnRules.setAlpha(1f);
                btnRules.setClickable(true);

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

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                refreshContent();
            }
        });
    }

    private void refreshContent(){
        callMatchesService(true);
    }

    public void showAlert(final Match match, final String winningTeamID, final String winningTeamName){
        CustomDialogClass dialogClass = new CustomDialogClass(getActivity(), new CustomDialogClass.AbstractCustomDialogListener() {
            @Override
            public void onConfirm(CustomDialogClass.DialogResponse response) {
                response.getDialog().dismiss();

                ApiManager.getService().sendPredictions(StaticData.user.getId(), match.getId(), winningTeamID, homeScore
                        , awayScore ,"1", winningTeamName,match.getDate()).enqueue(new Callback<BasicResponse>() {
                    @Override
                    public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                        BasicResponse basicResponse = response.body();

                        if(basicResponse.getStatus() == 1){
                            txtCoinsTotal.setText(basicResponse.getCoins());
                            match.setIsConfirmed("1");
                            StaticData.user.setCoins(basicResponse.getCoins());
                            AppUtils.startCountAnimation(txtCoinsTotal,0,Integer.parseInt(basicResponse.getCoins()),1500);
                            listMatches.setAdapter(matchesAdapter);
                        }else if(basicResponse.getStatus() == 0){
                            CustomDialogClass dialogClass = new CustomDialogClass(getActivity(), new CustomDialogClass.AbstractCustomDialogListener() {
                                @Override
                                public void onConfirm(CustomDialogClass.DialogResponse response) {
                                    response.getDialog().dismiss();
                                }

                                @Override
                                public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                                }
                            }, true);

                            dialogClass.setTitle("FOOTWIN");
                            dialogClass.setMessage(basicResponse.getMessage());
                            dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            dialogClass.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BasicResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                dialogResponse.getDialog().dismiss();
            }
        }, false, "CONFIRM", "EDIT");

        dialogClass.setTitle("FOOTWIN");
        if(!StringUtils.isValid(winningTeamName)){
            dialogClass.setMessage("ARE YOU SURE YOU WANT TO CONFIRM YOUR DRAW PREDICTION? ONCE CONFIRMED YOU CANNOT EDIT IT!!");
        }else {
            dialogClass.setMessage("ARE YOU SURE YOU WANT TO PREDICT " + winningTeamName + " AS THE WINNING TEAM? ONCE CONFIRMED YOU CANNOT EDIT IT!!");
        }
        dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogClass.show();
    }

    private void callMatchesService() {
        callMatchesService(false);
    }

    private void callMatchesService(final Boolean isRefreshing) {
        ApiManager.getService().getMatches().enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, final Response<Profile> response) {
                if(response.isSuccessful()  && response.body() != null){
                    final Profile profile = response.body();
                    if(profile.getStatus() == 1) {
                        if (profile.getMatches() != null && profile.getMatches().size() > 0) {
                            if (mainPageActivity != null) {
                                Match currentMatch = profile.getMatches().get(0);
                                mainPageActivity.checkTutorial(currentMatch.getHomeName(), currentMatch.getHomeFlag());
                            }

                            if (isRefreshing) {
                                pullToRefresh.setRefreshing(false);
                            }

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    matchesAdapter = new MatchesAdapter(profile.getMatches(), getContext(), PredictFragment.this);
                                    listMatches.setAdapter(matchesAdapter);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }, 2000);
                        }
                    } else if(profile.getMessage() != null) {
                        AppUtils.showAlert(getContext(), profile.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
            }
        });
    }
}