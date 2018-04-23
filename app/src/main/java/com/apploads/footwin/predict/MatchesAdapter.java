package com.apploads.footwin.predict;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
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
import com.apploads.footwin.model.Match;

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
        this.root        = root;
        this.context     = context;
        this.predictFragment     = predictFragment;
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
            holder.txtDate = convertView.findViewById(R.id.txtDate);
            holder.btnDraw = convertView.findViewById(R.id.btnDraw);
            holder.viewConfirm.setClickable(false);

            holder.txtHomeTeam.setText(match.getHomeName());
            holder.txtAwayTeam.setText(match.getAwayName());

            final Animation scale_up = AnimationUtils.loadAnimation(context, R.anim.scale_up);
            final Animation scale_up_normal = AnimationUtils.loadAnimation(context, R.anim.scale_up_normal);
            final Animation scale_down = AnimationUtils.loadAnimation(context, R.anim.scale_down);
            final Animation scale_down_normal = AnimationUtils.loadAnimation(context, R.anim.scale_down_normal);

            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
            formatter.setLenient(false);

            String endTime = "27.04.2018, 15:05:36";

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
                        holder.imgAwayTeam.animate().alpha(1f).setDuration(1000);
                        holder.txtAwayTeam.animate().alpha(1f).setDuration(1000);
                        holder.viewConfirm.animate().alpha(0f).setDuration(1000);
                        holder.viewConfirm.setClickable(false);
                        holder.imgAwayTeam.startAnimation(scale_up_normal);
                        match.setHomeToWin(false);
                        match.setAwayToWin(false);
                    }else {
                        holder.imgHomeTeam.setBackgroundResource(R.drawable.selected_team_background);
                        holder.imgHomeTeam.startAnimation(scale_up);
                        holder.imgHomeTeam.animate().alpha(1f).setDuration(1000);
                        holder.txtHomeTeam.animate().alpha(1f).setDuration(1000);

                        holder.imgAwayTeam.setBackgroundResource(0);
                        holder.imgAwayTeam.animate().alpha(0.5f).setDuration(1000);
                        holder.txtAwayTeam.animate().alpha(0.5f).setDuration(1000);
                        holder.imgAwayTeam.startAnimation(scale_down);
                        holder.viewConfirm.animate().alpha(1f).setDuration(1000);
                        holder.viewConfirm.setClickable(true);
                        match.setHomeToWin(true);
                        match.setAwayToWin(false);
                    }
            }
            });

            holder.viewAwayTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(match.isAwayToWin()){
                        holder.imgAwayTeam.setBackgroundResource(0);
                        holder.imgAwayTeam.startAnimation(scale_down_normal);
                        holder.imgHomeTeam.animate().alpha(1f).setDuration(1000);
                        holder.txtHomeTeam.animate().alpha(1f).setDuration(1000);
                        holder.imgHomeTeam.startAnimation(scale_up_normal);
                        holder.viewConfirm.animate().alpha(0f).setDuration(1000);
                        holder.viewConfirm.setClickable(false);
                        match.setAwayToWin(false);
                        match.setHomeToWin(false);
                    }else {
                        holder.imgAwayTeam.setBackgroundResource(R.drawable.selected_team_background);
                        holder.imgAwayTeam.startAnimation(scale_up);
                        holder.imgAwayTeam.animate().alpha(1f).setDuration(1000);
                        holder.txtAwayTeam.animate().alpha(1f).setDuration(1000);

                        holder.imgHomeTeam.setBackgroundResource(0);
                        holder.imgHomeTeam.animate().alpha(0.5f).setDuration(1000);
                        holder.txtHomeTeam.animate().alpha(0.5f).setDuration(1000);
                        holder.imgHomeTeam.startAnimation(scale_down);
                        holder.viewConfirm.animate().alpha(1f).setDuration(1000);
                        holder.viewConfirm.setClickable(true);
                        match.setAwayToWin(true);
                        match.setHomeToWin(false);
                    }
                }
            });

            holder.btnDraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.imgAwayTeam.setBackgroundResource(0);
                    holder.imgAwayTeam.animate().alpha(0.5f).setDuration(1000);
                    holder.txtAwayTeam.animate().alpha(0.5f).setDuration(1000);
                    holder.imgAwayTeam.startAnimation(scale_down);

                    holder.imgHomeTeam.setBackgroundResource(0);
                    holder.imgHomeTeam.animate().alpha(0.5f).setDuration(1000);
                    holder.txtHomeTeam.animate().alpha(0.5f).setDuration(1000);
                    holder.imgHomeTeam.startAnimation(scale_down);

                    holder.viewConfirm.animate().alpha(1f).setDuration(1000);
                    holder.viewConfirm.setClickable(true);

                    match.setAwayToWin(false);
                    match.setHomeToWin(false);
                }
            });


            holder.viewConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name =  match.isHomeToWin() ? match.getHomeName() : match.getAwayName();
                    Toast.makeText(context, "predict " + name, Toast.LENGTH_SHORT).show();
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


    public class Holder {
        TextView txtHomeTeam, txtAwayTeam, txtDate;
        LinearLayout viewHomeTeam, viewAwayTeam, viewConfirm, viewExactScore;
        ImageView imgAwayTeam, imgHomeTeam;
        Button btnDraw;
    }

}