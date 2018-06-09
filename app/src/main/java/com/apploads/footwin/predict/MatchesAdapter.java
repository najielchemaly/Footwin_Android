package com.apploads.footwin.predict;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.model.Match;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MatchesAdapter extends BaseAdapter {
    private List<Match> root;
    private Context context;
    private LayoutInflater mInflater;
    private PredictFragment predictFragment;
    private Date endDate;
    private long startTime, milliseconds, diff;
    private CountDownTimer mCountDownTimer;
    private String currentDate;

    public MatchesAdapter(List<Match> root, Context context, PredictFragment predictFragment, String currentDate){
        this.root = root;
        this.context = context;
        this.currentDate = currentDate;
        this.predictFragment = predictFragment;

        if(context != null){
            mInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return root.size();
    }

    @Override
    public Object getItem(int position) {
        return root.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) Math.random();
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Match match = (Match) getItem(position);
        final Holder holder = new Holder();

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.match_row_item, null);
            convertView.setTag(holder);

            initializeViews(convertView, holder, match);

            updateIsConfirmedView(holder, match);

//            final Animation scale_up = AnimationUtils.loadAnimation(context, R.anim.scale_up);
//            final Animation scale_up_init = AnimationUtils.loadAnimation(context, R.anim.scale_up_init);
//            final Animation scale_up_normal = AnimationUtils.loadAnimation(context, R.anim.scale_up_normal);
//            final Animation scale_down = AnimationUtils.loadAnimation(context, R.anim.scale_down);
//            final Animation scale_down_init = AnimationUtils.loadAnimation(context, R.anim.scale_down_init);
//            final Animation scale_down_normal = AnimationUtils.loadAnimation(context, R.anim.scale_down_normal);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setLenient(false);

            String endTime = match.getDate();

            try {
                endDate = formatter.parse(endTime+":00");
                milliseconds = endDate.getTime();
                startTime = formatter.parse(currentDate).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            diff = milliseconds - startTime;

            mCountDownTimer = new CountDownTimer(milliseconds, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    startTime=startTime-1;
                    Long serverUptimeSeconds = (millisUntilFinished - startTime) / 1000;

                    if(serverUptimeSeconds >= 0){
                        String daysLeft = String.format("%d", serverUptimeSeconds / 86400);
                        String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                        String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);
                        String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                        holder.txtDate.setText(daysLeft+ " Days " + hoursLeft+ " Hours " + minutesLeft + " Mins " + secondsLeft+" Sec");

//                        if(daysLeft.equals("0")){
//                            holder.txtDate.setText(hoursLeft+ " Hours " + minutesLeft + " Mins " + secondsLeft+" Sec");
//                        }
//                        if(hoursLeft.equals("0")){
//                            holder.txtDate.setText( minutesLeft + " Mins " + secondsLeft+" Sec");
//                        }
//                        if(minutesLeft.equals("0")){
//                            holder.txtDate.setText(secondsLeft+" Sec");
//                        }
                        holder.txtDate.setTextColor(context.getResources().getColor(R.color.white));
                    }else {
                        holder.txtDate.setText("LIVE NOW!");
                        holder.txtDate.setTextColor(context.getResources().getColor(R.color.green));
                    }
                }

                @Override
                public void onFinish() {
                }
            }.start();

            holder.viewHomeTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    match.setHomeScore("");
                    match.setAwayScore("");

                    updateIsHomeToWin(holder, match);
                }
            });

            holder.viewAwayTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    match.setHomeScore("");
                    match.setAwayScore("");

                    updateIsAwayToWin(holder, match);
                }
            });

            holder.btnDraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    match.setHomeScore("");
                    match.setAwayScore("");

                    updateIsDraw(holder, match);
                }
            });

            holder.viewConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.getAlpha() > 0) {
                        String winningTeamName = match.isHomeToWin() ? match.getHomeName() : match.getAwayName();
                        String winningTeamID = match.isHomeToWin() ? match.getHomeId() : match.getAwayId();
                        String homeScore = match.getHomeScore();
                        String awayScore = match.getAwayScore();
                        if (!match.isAwayToWin() && !match.isHomeToWin()) {
                            predictFragment.showAlert(match, "0", "",
                                    homeScore.isEmpty() ? "-1" : homeScore, awayScore.isEmpty() ? "-1" : awayScore, position);
                        } else {
                            predictFragment.showAlert(match, winningTeamID, winningTeamName,
                                    homeScore.isEmpty() ? "-1" : homeScore, awayScore.isEmpty() ? "-1" : awayScore, position);
                        }
                    }
                }
            });

            holder.viewExactScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    predictFragment.showExactScore(match);
                }
            });
        }else {
            if(convertView.getTag() != null) {
                Holder convertViewHolder = (Holder)convertView.getTag();
                updateReusableConvertView(convertViewHolder, match);
            }
        }

        return convertView;
    }

    public void initializeViews(View convertView, Holder holder, Match match) {
        holder.txtHomeTeam = convertView.findViewById(R.id.txtHomeTeam);
        holder.txtAwayTeam = convertView.findViewById(R.id.txtAwayTeam);
        holder.viewHomeTeam = convertView.findViewById(R.id.viewHomeTeam);
        holder.viewAwayTeam = convertView.findViewById(R.id.viewAwayTeam);
        holder.imgHomeTeam = convertView.findViewById(R.id.imgHomeTeam);
        holder.imgAwayTeam = convertView.findViewById(R.id.imgAwayTeam);
        holder.viewConfirm = convertView.findViewById(R.id.viewConfirm);
        holder.viewExactScore = convertView.findViewById(R.id.viewExactScore);
        holder.imgCheck = convertView.findViewById(R.id.imgCheck);
        holder.txtDate = convertView.findViewById(R.id.txtDate);
        holder.btnDraw = convertView.findViewById(R.id.btnDraw);
        holder.txtConfirm = convertView.findViewById(R.id.txtConfirm);

        holder.viewConfirm.setClickable(false);

        holder.txtHomeTeam.setText(match.getHomeName());
        holder.txtAwayTeam.setText(match.getAwayName());

        Picasso.with(context)
                .load(StaticData.config.getMediaUrl()+match.getHomeFlag())
                .into(holder.imgHomeTeam);

        Picasso.with(context)
                .load(StaticData.config.getMediaUrl()+match.getAwayFlag())
                .into(holder.imgAwayTeam);
    }

    public void updateIsConfirmedView(Holder holder, Match match) {
        if(holder.imgHomeTeam == null || holder.imgAwayTeam == null) { return; }
        if("1".equals(match.getIsConfirmed())){
            holder.viewAwayTeam.setClickable(false);
            holder.viewHomeTeam.setClickable(false);
            holder.viewAwayTeam.setEnabled(false);
            holder.viewHomeTeam.setEnabled(false);
            holder.viewExactScore.setEnabled(false);
            holder.btnDraw.setClickable(false);
            holder.btnDraw.setEnabled(false);
            holder.viewConfirm.setClickable(false);
            holder.viewConfirm.setEnabled(false);
            holder.viewExactScore.setAlpha(0.5f);
            holder.viewConfirm.setAlpha(1f);
            holder.txtConfirm.setText("Confirmed");
            holder.txtConfirm.setTextColor(context.getResources().getColor(R.color.white));
            holder.imgCheck.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

            if(match.getPredictionWinningTeam().equals(match.getHomeId())){
                match.setHomeToWin(true);
                match.setAwayToWin(false);
            }

            if(match.getPredictionWinningTeam().equals(match.getAwayId())){
                match.setAwayToWin(true);
                match.setHomeToWin(false);
            }

            if(match.isHomeToWin()){
                holder.imgHomeTeam.setBackgroundResource(R.drawable.selected_team_background);
                holder.imgHomeTeam.setAlpha(1f);
                holder.txtHomeTeam.setAlpha(1f);
                holder.imgAwayTeam.setBackgroundResource(0);
                holder.imgAwayTeam.setAlpha(0.5f);
                holder.txtAwayTeam.setAlpha(0.5f);
                holder.btnDraw.setAlpha(0.5f);

                holder.imgHomeTeam.setScaleX(1.2f);
                holder.imgHomeTeam.setScaleY(1.2f);
                holder.imgAwayTeam.setScaleX(0.8f);
                holder.imgAwayTeam.setScaleY(0.8f);

                holder.txtConfirm.setTextColor(context.getResources().getColor(R.color.white));
                holder.imgCheck.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                holder.viewConfirm.setBackgroundResource(R.drawable.circle_shape_green);
            }else if(match.isAwayToWin()) {
                holder.imgAwayTeam.setBackgroundResource(R.drawable.selected_team_background);
                holder.imgAwayTeam.setAlpha(1f);
                holder.txtAwayTeam.setAlpha(1f);
                holder.imgHomeTeam.setBackgroundResource(0);
                holder.imgHomeTeam.setAlpha(0.5f);
                holder.txtHomeTeam.setAlpha(0.5f);

                holder.imgAwayTeam.setScaleX(1.2f);
                holder.imgAwayTeam.setScaleY(1.2f);
                holder.imgHomeTeam.setScaleX(0.8f);
                holder.imgHomeTeam.setScaleY(0.8f);

                holder.btnDraw.setAlpha(0.5f);
                holder.txtConfirm.setTextColor(context.getResources().getColor(R.color.white));
                holder.imgCheck.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                holder.viewConfirm.setBackgroundResource(R.drawable.circle_shape_green);
            }else {
                holder.imgAwayTeam.setBackgroundResource(0);
                holder.imgAwayTeam.setAlpha(0.5f);
                holder.txtAwayTeam.setAlpha(0.5f);
                holder.imgHomeTeam.setBackgroundResource(0);
                holder.imgHomeTeam.setAlpha(0.5f);
                holder.txtHomeTeam.setAlpha(0.5f);

                holder.imgAwayTeam.setScaleX(0.8f);
                holder.imgAwayTeam.setScaleY(0.8f);
                holder.imgHomeTeam.setScaleX(0.8f);
                holder.imgHomeTeam.setScaleY(0.8f);

                holder.viewConfirm.animate().alpha(1f).setDuration(300);
                holder.viewConfirm.setClickable(true);
                holder.viewConfirm.setEnabled(true);
                holder.txtConfirm.setTextColor(context.getResources().getColor(R.color.white));
                holder.btnDraw.setBackgroundResource(R.color.appBlue);
                holder.viewConfirm.setBackgroundResource(R.drawable.circle_shape_green);
            }
        }
    }

    public void updateReusableConvertView(Holder holder, Match match) {
        if(holder.imgHomeTeam == null || holder.imgAwayTeam == null) { return; }
        updateIsConfirmedView(holder, match);
        if(match.isHomeToWin()){
            holder.imgHomeTeam.setBackgroundResource(R.drawable.selected_team_background);
            if(match.isDraw()) {
                scaleImage(holder.imgHomeTeam, 1.2f);
            } else if(match.isAwayToWin()) {
                scaleImage(holder.imgHomeTeam, 1.2f);
                scaleImage(holder.imgAwayTeam, 0.8f);
            } else {
                scaleImage(holder.imgHomeTeam, 1.2f);
                scaleImage(holder.imgAwayTeam, 0.8f);
            }
            holder.imgHomeTeam.animate().alpha(1f).setDuration(300);
            holder.txtHomeTeam.animate().alpha(1f).setDuration(300);
            holder.imgAwayTeam.setBackgroundResource(0);
            holder.imgAwayTeam.animate().alpha(0.5f).setDuration(300);
            holder.txtAwayTeam.animate().alpha(0.5f).setDuration(300);
            holder.viewConfirm.animate().alpha(1f).setDuration(300);
            holder.viewConfirm.setClickable(true);
            holder.viewConfirm.setEnabled(true);
            holder.btnDraw.setBackgroundResource(R.drawable.retangle_white_border);
            match.setHomeToWin(true);
            match.setAwayToWin(false);
            match.setDraw(false);
        } else if(match.isAwayToWin()){
            holder.imgAwayTeam.setBackgroundResource(R.drawable.selected_team_background);
            if(match.isDraw()) {
                scaleImage(holder.imgAwayTeam, 1.2f);
            } else if(match.isHomeToWin()) {
                scaleImage(holder.imgAwayTeam, 1.2f);
                scaleImage(holder.imgHomeTeam, 0.8f);
            } else {
                scaleImage(holder.imgAwayTeam, 1.2f);
                scaleImage(holder.imgHomeTeam, 0.8f);
            }
            holder.imgAwayTeam.animate().alpha(1f).setDuration(300);
            holder.txtAwayTeam.animate().alpha(1f).setDuration(300);
            holder.imgHomeTeam.setBackgroundResource(0);
            holder.imgHomeTeam.animate().alpha(0.5f).setDuration(300);
            holder.txtHomeTeam.animate().alpha(0.5f).setDuration(300);
            holder.viewConfirm.animate().alpha(1f).setDuration(300);
            holder.viewConfirm.setClickable(true);
            holder.viewConfirm.setEnabled(true);
            holder.btnDraw.setBackgroundResource(R.drawable.retangle_white_border);

            match.setAwayToWin(true);
            match.setHomeToWin(false);
            match.setDraw(false);
        } else if(match.isDraw()) {
            holder.imgHomeTeam.setBackgroundResource(0);
            holder.imgHomeTeam.animate().alpha(0.5f).setDuration(300);
            holder.txtHomeTeam.animate().alpha(0.5f).setDuration(300);
            holder.imgAwayTeam.setBackgroundResource(0);
            holder.imgAwayTeam.animate().alpha(0.5f).setDuration(300);
            holder.txtAwayTeam.animate().alpha(0.5f).setDuration(300);
            holder.viewConfirm.animate().alpha(1f).setDuration(300);
            if(match.isHomeToWin()) {
                scaleImage(holder.imgHomeTeam, 0.8f);
            } else if(match.isAwayToWin()) {;
                scaleImage(holder.imgAwayTeam, 0.8f);
            } else {
                scaleImage(holder.imgHomeTeam, 0.8f);
                scaleImage(holder.imgAwayTeam, 0.8f);
            }
            holder.viewConfirm.setClickable(true);
            holder.viewConfirm.setEnabled(true);
            holder.btnDraw.setBackgroundResource(R.color.appBlue);
            match.setAwayToWin(false);
            match.setHomeToWin(false);
            match.setDraw(true);
        }
    }

    public void updateIsHomeToWin(Holder holder, Match match) {
        if(holder.imgHomeTeam == null || holder.imgAwayTeam == null) { return; }
        if(match.isHomeToWin()){
            holder.imgHomeTeam.setBackgroundResource(0);
            scaleImage(holder.imgHomeTeam, 1f);
            holder.imgAwayTeam.animate().alpha(1f).setDuration(300);
            holder.txtAwayTeam.animate().alpha(1f).setDuration(300);
            holder.viewConfirm.animate().alpha(0f).setDuration(300);
            holder.viewConfirm.setClickable(false);
            holder.viewConfirm.setEnabled(false);
            scaleImage(holder.imgAwayTeam, 1f);

            match.setHomeToWin(false);
            match.setAwayToWin(false);
        }else {
            holder.imgHomeTeam.setBackgroundResource(R.drawable.selected_team_background);
            if(match.isDraw()) {
                scaleImage(holder.imgHomeTeam, 1.2f);
            } else if(match.isAwayToWin()) {
                scaleImage(holder.imgHomeTeam, 1.2f);
                scaleImage(holder.imgAwayTeam, 0.8f);
            } else {
                scaleImage(holder.imgHomeTeam, 1.2f);
                scaleImage(holder.imgAwayTeam, 0.8f);
            }
            holder.imgHomeTeam.animate().alpha(1f).setDuration(300);
            holder.txtHomeTeam.animate().alpha(1f).setDuration(300);
            holder.imgAwayTeam.setBackgroundResource(0);
            holder.imgAwayTeam.animate().alpha(0.5f).setDuration(300);
            holder.txtAwayTeam.animate().alpha(0.5f).setDuration(300);
            holder.viewConfirm.animate().alpha(1f).setDuration(300);
            holder.viewConfirm.setClickable(true);
            holder.viewConfirm.setEnabled(true);
            holder.btnDraw.setBackgroundResource(R.drawable.retangle_white_border);
            match.setHomeToWin(true);
            match.setAwayToWin(false);
            match.setDraw(false);
        }
    }

    public void updateIsAwayToWin(Holder holder, Match match) {
        if(holder.imgHomeTeam == null || holder.imgAwayTeam == null) { return; }
        if(match.isAwayToWin()){
            holder.imgAwayTeam.setBackgroundResource(0);
            scaleImage(holder.imgAwayTeam, 1f);
            holder.imgHomeTeam.animate().alpha(1f).setDuration(300);
            holder.txtHomeTeam.animate().alpha(1f).setDuration(300);
            scaleImage(holder.imgHomeTeam, 1f);
            holder.viewConfirm.animate().alpha(0f).setDuration(300);
            holder.viewConfirm.setClickable(false);
            holder.viewConfirm.setEnabled(false);
            holder.btnDraw.setBackgroundResource(R.drawable.retangle_white_border);

            match.setAwayToWin(false);
            match.setHomeToWin(false);
        }else {
            holder.imgAwayTeam.setBackgroundResource(R.drawable.selected_team_background);
            if(match.isDraw()) {
                scaleImage(holder.imgAwayTeam, 1.2f);
            } else if(match.isHomeToWin()) {
                scaleImage(holder.imgAwayTeam, 1.2f);
                scaleImage(holder.imgHomeTeam, 0.8f);
            } else {
                scaleImage(holder.imgAwayTeam, 1.2f);
                scaleImage(holder.imgHomeTeam, 0.8f);
            }
            holder.imgAwayTeam.animate().alpha(1f).setDuration(300);
            holder.txtAwayTeam.animate().alpha(1f).setDuration(300);
            holder.imgHomeTeam.setBackgroundResource(0);
            holder.imgHomeTeam.animate().alpha(0.5f).setDuration(300);
            holder.txtHomeTeam.animate().alpha(0.5f).setDuration(300);
            holder.viewConfirm.animate().alpha(1f).setDuration(300);
            holder.viewConfirm.setClickable(true);
            holder.viewConfirm.setEnabled(true);
            holder.btnDraw.setBackgroundResource(R.drawable.retangle_white_border);

            match.setAwayToWin(true);
            match.setHomeToWin(false);
            match.setDraw(false);
        }
    }

    public void updateIsDraw(Holder holder, Match match) {
        if(holder.imgHomeTeam == null || holder.imgAwayTeam == null) { return; }
        if(match.isDraw()) {
            holder.imgHomeTeam.setBackgroundResource(0);
            holder.imgHomeTeam.animate().alpha(1f).setDuration(300);
            holder.imgHomeTeam.animate().alpha(1f).setDuration(300);
            scaleImage(holder.imgHomeTeam, 1f);
            holder.imgAwayTeam.setBackgroundResource(0);
            holder.imgAwayTeam.animate().alpha(1f).setDuration(300);
            holder.txtAwayTeam.animate().alpha(1f).setDuration(300);
            scaleImage(holder.imgAwayTeam, 1f);
            holder.viewConfirm.animate().alpha(0f).setDuration(300);
            holder.viewConfirm.setClickable(false);
            holder.viewConfirm.setEnabled(false);
            holder.btnDraw.setBackgroundResource(R.drawable.retangle_white_border);
            match.setHomeToWin(false);
            match.setAwayToWin(false);
            match.setDraw(false);
        } else {
            holder.imgHomeTeam.setBackgroundResource(0);
            holder.imgHomeTeam.animate().alpha(0.5f).setDuration(300);
            holder.txtHomeTeam.animate().alpha(0.5f).setDuration(300);
            holder.imgAwayTeam.setBackgroundResource(0);
            holder.imgAwayTeam.animate().alpha(0.5f).setDuration(300);
            holder.txtAwayTeam.animate().alpha(0.5f).setDuration(300);
            holder.viewConfirm.animate().alpha(1f).setDuration(300);
            if(match.isHomeToWin()) {
                scaleImage(holder.imgHomeTeam, 0.8f);
            } else if(match.isAwayToWin()) {;
                scaleImage(holder.imgAwayTeam, 0.8f);
            } else {
                scaleImage(holder.imgHomeTeam, 0.8f);
                scaleImage(holder.imgAwayTeam, 0.8f);
            }
            holder.viewConfirm.setClickable(true);
            holder.viewConfirm.setEnabled(true);
            holder.btnDraw.setBackgroundResource(R.color.appBlue);
            match.setAwayToWin(false);
            match.setHomeToWin(false);
            match.setDraw(true);
        }
    }

    public void scaleImage(ImageView imageView, float newSize){
        imageView.animate().
                scaleX(newSize).
                scaleY(newSize).
                setDuration(300).start();
    }

    public class Holder {
        TextView txtHomeTeam, txtAwayTeam, txtDate, txtConfirm;
        LinearLayout viewHomeTeam, viewAwayTeam, viewConfirm, viewExactScore;
        ImageView imgAwayTeam, imgHomeTeam, imgCheck;
        Button btnDraw;
    }

    public void setRoot(List<Match> root) {
        this.root = root;
    }
}