package com.apploads.footwin.predict;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.view.inputmethod.InputMethodManager;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    private InterstitialAd mInterstitialAd;;
    int badge;
    CustomDialogClass dialogClass;

    List<Match> matches = new ArrayList<>();
    int selectedMatchIndex;

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

        try {
            if (StaticData.user.getCoins() != null) {
                AppUtils.startCountAnimation(txtCoinsTotal, 0, Integer.parseInt(StaticData.user.getCoins()), 1500);
            }
            if (StaticData.user.getWinningCoins() != null) {
                AppUtils.startCountAnimation(txtWinningCoinsTotal, 0, Integer.parseInt(StaticData.user.getWinningCoins()), 1500);
            }

            updateBadge();
        } catch (Exception ex) {
            Log.e("", ex.getLocalizedMessage());
        }
    }

    private void initView() {
        try {
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

            mInterstitialAd = new InterstitialAd(getActivity());
            mInterstitialAd.setAdUnitId("ca-app-pub-8532510371470349/4251669653");
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Toasty.error(mainPageActivity,"error : " + errorCode,1000,true).show();
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when when the interstitial ad is closed.
                }
            });

            DoubleBounce doubleBounce = new DoubleBounce();
            progressBar.setIndeterminateDrawable(doubleBounce);

            viewExactScoreParent.setVisibility(View.GONE);

            if (StaticData.user.getAvatar() != null && !StaticData.user.getAvatar().isEmpty()) {
                Glide.with(getActivity())
                        .load(Uri.parse(StaticData.config.getMediaUrl() + StaticData.user.getAvatar()))
                        .apply(new RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.avatar_male)
                                .diskCacheStrategy(DiskCacheStrategy.ALL))

                        .into(imgProfile);

            } else {
                imgProfile.setImageResource(R.drawable.avatar_male);
                if(StaticData.user.getGender() != null &&
                        StaticData.user.getGender().toLowerCase() == "female") {
                    imgProfile.setImageResource(R.drawable.avatar_female);
                }
            }

//        AppUtils.startCountAnimation(txtCoinsTotal,0, Integer.parseInt(StaticData.user.getCoins()),1500);
//        AppUtils.startCountAnimation(txtWinningCoinsTotal,0, Integer.parseInt(StaticData.user.getWinningCoins()),1500);

            bottom_to_top = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_to_top_wt);
            top_to_bottom = AnimationUtils.loadAnimation(getContext(), R.anim.top_to_bottom_wt);

            if (txtNotificationTag.getText().toString().isEmpty()) {
                viewNotificationTag.setVisibility(View.INVISIBLE);
            }

            if (getActivity().getClass().equals(MainPageActivity.class)) {
                mainPageActivity = (MainPageActivity) getActivity();
            }

            callMatchesService();
        } catch (Exception ex) {
            Log.e("", ex.getLocalizedMessage());
        }
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

    public void updateWinningCoins() {
        try {
            mainPageActivity.runOnUiThread(() -> txtWinningCoinsTotal.setText(StaticData.user.getWinningCoins()));
        } catch (Exception ex) {
            Log.e("", ex.getLocalizedMessage());
        }
    }

    public void showExactScore(Match match) {
        viewExactScoreParent.setVisibility(View.VISIBLE);
        btnRules.setAlpha(0.5f);
        btnRules.setClickable(false);
        viewExactScore.startAnimation(bottom_to_top);

        selectedMatchIndex = matches.indexOf(match);

        txtHomeTeam.setText(match.getHomeName());
        txtAwayTeam.setText(match.getAwayName());

        txtHomeScore.setText(match.getHomeScore() == "-1" ? "" : match.getHomeScore());
        txtAwayScore.setText(match.getAwayScore() == "-1" ? "" : match.getAwayScore());

        Glide.with(getActivity())
                .load(Uri.parse(StaticData.config.getMediaUrl()+match.getHomeFlag()))
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imgHomeTeam);

        Glide.with(getActivity())
                .load(Uri.parse(StaticData.config.getMediaUrl()+match.getAwayFlag()))
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imgAwayTeam);

        if(mainPageActivity != null) {
            mainPageActivity.bottomNavigationView.setVisibility(View.GONE);
        }
    }

    private void initListeners() {
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getActivity(), NotificationsActivity.class);
                    startActivity(intent);
                } catch (Exception ex) {
                    Log.e("", ex.getLocalizedMessage());
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    viewExactScore.startAnimation(top_to_bottom);
                    top_to_bottom.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            viewExactScoreParent.setVisibility(View.GONE);
                            if (mainPageActivity != null) {
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
                    if (!txtHomeScore.getText().toString().isEmpty() && !txtAwayScore.getText().toString().isEmpty()) {
                        homeScoreInt = Integer.parseInt(txtHomeScore.getText().toString());
                        awayScoreInt = Integer.parseInt(txtAwayScore.getText().toString());

                        if (homeScoreInt > awayScoreInt && !matches.get(selectedMatchIndex).isHomeToWin()) {
                            matches.get(selectedMatchIndex).setHomeToWin(true);
                            matches.get(selectedMatchIndex).setAwayToWin(false);
                            matches.get(selectedMatchIndex).setDraw(false);
                        } else if (homeScoreInt < awayScoreInt && !matches.get(selectedMatchIndex).isAwayToWin()) {
                            matches.get(selectedMatchIndex).setHomeToWin(false);
                            matches.get(selectedMatchIndex).setAwayToWin(true);
                            matches.get(selectedMatchIndex).setDraw(false);
                        } else if (homeScoreInt == awayScoreInt && !matches.get(selectedMatchIndex).isDraw()) {
                            matches.get(selectedMatchIndex).setHomeToWin(false);
                            matches.get(selectedMatchIndex).setAwayToWin(false);
                            matches.get(selectedMatchIndex).setDraw(true);
                        }
                    }

                    if (StringUtils.isValid(txtAwayScore.getText().toString()) &&
                            StringUtils.isValid(txtHomeScore.getText().toString())) {
                        matches.get(selectedMatchIndex).setHomeScore(txtHomeScore.getText().toString());
                        matches.get(selectedMatchIndex).setAwayScore(txtAwayScore.getText().toString());
                    } else {
                        matches.get(selectedMatchIndex).setHomeScore("-1");
                        matches.get(selectedMatchIndex).setAwayScore("-1");
                    }

                    matchesAdapter.setRoot(matches);
                    matchesAdapter.notifyDataSetChanged();

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                } catch (Exception ex) {
                    Log.e("", ex.getLocalizedMessage());
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    viewExactScore.startAnimation(top_to_bottom);
                    top_to_bottom.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            viewExactScoreParent.setVisibility(View.GONE);
                            if (mainPageActivity != null) {
                                mainPageActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    btnRules.setAlpha(1f);
                    btnRules.setClickable(true);
                } catch (Exception ex) {
                    Log.e("", ex.getLocalizedMessage());
                }
            }
        });

        imgCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getActivity(), CoinsActivity.class);
                    startActivity(intent);
                } catch (Exception ex) {
                    Log.e("", ex.getLocalizedMessage());
                }
            }
        });

        btnRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getActivity(), ViewRulesActivity.class);
                    startActivity(intent);
                } catch (Exception ex) {
                    Log.e("", ex.getLocalizedMessage());
                }
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                try {
                    refreshContent();
                } catch (Exception ex) {
                    Log.e("", ex.getLocalizedMessage());
                }
            }
        });
    }

    private void refreshContent(){
        callMatchesService(true);
    }

    public void showAlert(final Match match, final String winningTeamID, final String winningTeamName, final String homeScore, final String awayScore, final int position){
            dialogClass = new CustomDialogClass(getActivity(), new CustomDialogClass.AbstractCustomDialogListener() {
            @Override
            public void onConfirm(CustomDialogClass.DialogResponse response) {
                response.getDialog().dismiss();
                dialogClass.yes.setEnabled(false);
                ApiManager.getService().sendPredictions(StaticData.user.getId(), match.getId(), winningTeamID, homeScore
                        , awayScore,"1", winningTeamName, match.getDate()).enqueue(new Callback<BasicResponse>() {
                    @Override
                    public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                        if(response.isSuccessful() && response.body().getStatus() == 1){
                            dialogClass.yes.setEnabled(true);
                            final BasicResponse basicResponse = response.body();
                            if(basicResponse.getStatus() == 1){
                                int predCount = AppUtils.getPredictionsCount(getActivity());

                                if(predCount%3 == 0){
                                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                                }
                                AppUtils.updatePredictionsCount(getActivity(), predCount + 1);
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
                                        if(basicResponse.getErrorCode().equals("404")){
//                                        matches.remove(position);
////                                        matchesAdapter.setRoot(matches);
////                                        matchesAdapter.notifyDataSetChanged();
                                            callMatchesService();
                                        }
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
                        dialogClass.yes.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<BasicResponse> call, Throwable t) {
                        dialogClass.yes.setEnabled(true);
                        Toasty.error(getActivity(),"An error has occured",Toast.LENGTH_LONG,true).show();
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
            dialogClass.setMessage("Are you sure you want to confirm your DRAW prerdiction?\nOnce confirmed you cannot edit!!");
        }else {
            dialogClass.setMessage("Are you sure you want to predict " + winningTeamName.toLowerCase() + " as the winning team?\nOnce confirmed you cannot edit!!");
        }
        dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogClass.show();
    }

    public void callMatchesService() {
        callMatchesService(false);
    }

    public void callMatchesService(final Boolean isRefreshing) {
        ApiManager.getService().getMatches().enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, final Response<Profile> response) {
                if(response.isSuccessful()  && response.body() != null){
                    final Profile profile = response.body();
                    if(profile.getStatus() == 1) {
                        if (profile.getMatches() != null && profile.getMatches().size() > 0) {
                            matches = profile.getMatches();
                            if (mainPageActivity != null) {
                                Match currentMatch = matches.get(0);
                                mainPageActivity.checkTutorial(currentMatch.getHomeName(), currentMatch.getHomeFlag());
                            }

                            if (isRefreshing) {
                                pullToRefresh.setRefreshing(false);
                            }

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    matchesAdapter = new MatchesAdapter(matches, getContext(), PredictFragment.this,profile.getCurrentDate());
                                    listMatches.setAdapter(matchesAdapter);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }, 2000);
                        }

                        txtRound.setText(StaticData.config.getActiveRound().getTitle());
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
