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
    LayoutInflater mInflater;
    private PredictFragment predictFragment;
    Date endDate;
    long startTime, milliseconds, diff;
    CountDownTimer mCountDownTimer;

    public MatchesAdapter(List<Match> root, Context context, PredictFragment predictFragment){
        this.root = root;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Match match = (Match) getItem(position);
        final Holder holder = new Holder();

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.match_row_item, null);
            convertView.setTag(holder);

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
                    holder.txtConfirm.setTextColor(context.getResources().getColor(R.color.white));
                    holder.btnDraw.setBackgroundResource(R.color.appBlue);
                    holder.viewConfirm.setBackgroundResource(R.drawable.circle_shape_green);
                }
            }

            final Animation scale_up = AnimationUtils.loadAnimation(context, R.anim.scale_up);
            final Animation scale_up_init = AnimationUtils.loadAnimation(context, R.anim.scale_up_init);
            final Animation scale_up_normal = AnimationUtils.loadAnimation(context, R.anim.scale_up_normal);
            final Animation scale_down = AnimationUtils.loadAnimation(context, R.anim.scale_down);
            final Animation scale_down_init = AnimationUtils.loadAnimation(context, R.anim.scale_down_init);
            final Animation scale_down_normal = AnimationUtils.loadAnimation(context, R.anim.scale_down_normal);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            formatter.setLenient(false);

            String endTime = match.getDate();

            try {
                endDate = formatter.parse(endTime);
                milliseconds = endDate.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }

            startTime = System.currentTimeMillis();

            diff = milliseconds - startTime;

            mCountDownTimer = new CountDownTimer(milliseconds, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    startTime=startTime-1;
                    Long serverUptimeSeconds = (millisUntilFinished - startTime) / 1000;

                    String daysLeft = String.format("%d", serverUptimeSeconds / 86400);
                    String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                    String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);
                    String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);

                    holder.txtDate.setText(daysLeft+ " Days " + hoursLeft+ " Hours " + minutesLeft + " Mins " + secondsLeft+" Sec");
                }

                @Override
                public void onFinish() {

                }
            }.start();

            holder.viewHomeTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(match.isHomeToWin()){
                        holder.imgHomeTeam.setBackgroundResource(0);
                        holder.imgHomeTeam.startAnimation(scale_down_normal);
                        holder.imgAwayTeam.animate().alpha(1f).setDuration(300);
                        holder.txtAwayTeam.animate().alpha(1f).setDuration(300);
                        holder.viewConfirm.animate().alpha(0f).setDuration(300);
                        holder.viewConfirm.setClickable(false);
                        holder.imgAwayTeam.startAnimation(scale_up_normal);

                        match.setHomeToWin(false);
                        match.setAwayToWin(false);
                    }else {
                        holder.imgHomeTeam.setBackgroundResource(R.drawable.selected_team_background);
                        if(match.isDraw()) {
                            holder.imgHomeTeam.startAnimation(scale_up);
                        } else if(match.isAwayToWin()) {
                            holder.imgHomeTeam.startAnimation(scale_up);
                            holder.imgAwayTeam.startAnimation(scale_down);
                        } else {
                            holder.imgHomeTeam.startAnimation(scale_up_init);
                            holder.imgAwayTeam.startAnimation(scale_down_init);
                        }
                        holder.imgHomeTeam.animate().alpha(1f).setDuration(300);
                        holder.txtHomeTeam.animate().alpha(1f).setDuration(300);
                        holder.imgAwayTeam.setBackgroundResource(0);
                        holder.imgAwayTeam.animate().alpha(0.5f).setDuration(300);
                        holder.txtAwayTeam.animate().alpha(0.5f).setDuration(300);
                        holder.viewConfirm.animate().alpha(1f).setDuration(300);
                        holder.viewConfirm.setClickable(true);

                        holder.btnDraw.setBackgroundResource(R.drawable.retangle_white_border);
                        match.setHomeToWin(true);
                        match.setAwayToWin(false);
                        match.setDraw(false);
                    }
            }
            });

            holder.viewAwayTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(match.isAwayToWin()){
                        holder.imgAwayTeam.setBackgroundResource(0);
                        holder.imgAwayTeam.startAnimation(scale_down_normal);
                        holder.imgHomeTeam.animate().alpha(1f).setDuration(300);
                        holder.txtHomeTeam.animate().alpha(1f).setDuration(300);
                        holder.imgHomeTeam.startAnimation(scale_up_normal);
                        holder.viewConfirm.animate().alpha(0f).setDuration(300);
                        holder.viewConfirm.setClickable(false);
                        holder.btnDraw.setBackgroundResource(R.drawable.retangle_white_border);

                        match.setAwayToWin(false);
                        match.setHomeToWin(false);
                    }else {
                        holder.imgAwayTeam.setBackgroundResource(R.drawable.selected_team_background);
                        if(match.isDraw()) {
                            holder.imgAwayTeam.startAnimation(scale_up);
                        } else if(match.isHomeToWin()) {
                            holder.imgAwayTeam.startAnimation(scale_up);
                            holder.imgHomeTeam.startAnimation(scale_down);
                        } else {
                            holder.imgAwayTeam.startAnimation(scale_up_init);
                            holder.imgHomeTeam.startAnimation(scale_down_init);
                        }
                        holder.imgAwayTeam.animate().alpha(1f).setDuration(300);
                        holder.txtAwayTeam.animate().alpha(1f).setDuration(300);
                        holder.imgHomeTeam.setBackgroundResource(0);
                        holder.imgHomeTeam.animate().alpha(0.5f).setDuration(300);
                        holder.txtHomeTeam.animate().alpha(0.5f).setDuration(300);
                        holder.viewConfirm.animate().alpha(1f).setDuration(300);
                        holder.viewConfirm.setClickable(true);
                        holder.btnDraw.setBackgroundResource(R.drawable.retangle_white_border);

                        match.setAwayToWin(true);
                        match.setHomeToWin(false);
                        match.setDraw(false);
                    }
                }
            });

            holder.btnDraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(match.isDraw()) {
                        holder.imgHomeTeam.setBackgroundResource(0);
                        holder.imgHomeTeam.animate().alpha(1f).setDuration(300);
                        holder.imgHomeTeam.animate().alpha(1f).setDuration(300);
                        holder.imgHomeTeam.startAnimation(scale_up_normal);
                        holder.imgAwayTeam.setBackgroundResource(0);
                        holder.imgAwayTeam.animate().alpha(1f).setDuration(300);
                        holder.txtAwayTeam.animate().alpha(1f).setDuration(300);
                        holder.imgAwayTeam.startAnimation(scale_up_normal);
                        holder.viewConfirm.animate().alpha(0f).setDuration(300);
                        holder.viewConfirm.setClickable(false);
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
                            holder.imgHomeTeam.startAnimation(scale_down);
                        } else if(match.isAwayToWin()) {
                            holder.imgAwayTeam.startAnimation(scale_down);
                        } else {
                            holder.imgAwayTeam.startAnimation(scale_down_init);
                            holder.imgHomeTeam.startAnimation(scale_down_init);
                        }
                        holder.viewConfirm.setClickable(true);
                        holder.btnDraw.setBackgroundResource(R.color.appBlue);
                        match.setAwayToWin(false);
                        match.setHomeToWin(false);
                        match.setDraw(true);
                    }
                }
            });

            holder.viewConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String winningTeamName =  match.isHomeToWin() ? match.getHomeName() : match.getAwayName();
                    String winningTeamID =  match.isHomeToWin() ? match.getHomeId() : match.getAwayId();
                    if(!match.isAwayToWin() && !match.isHomeToWin()){
                        predictFragment.showAlert(match,"0","");
                    }else {
                        predictFragment.showAlert(match, winningTeamID, winningTeamName);
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
//            holder = (Holder) convertView.getTag();
        }

        return convertView;
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

}