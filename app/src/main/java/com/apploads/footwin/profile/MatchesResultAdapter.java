package com.apploads.footwin.profile;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.R;
import com.apploads.footwin.model.Match;
import com.apploads.footwin.predict.PredictFragment;

import java.util.List;

public class MatchesResultAdapter extends BaseAdapter {

    private List<Match> root;
    private Context context;
    LayoutInflater mInflater;
    private PredictFragment predictFragment;

    public MatchesResultAdapter(List<Match> root, Context context, PredictFragment predictFragment){
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
            convertView = mInflater.inflate(R.layout.match_result_row_item, null);
            convertView.setTag(holder);

            holder.txtHomeTeam = convertView.findViewById(R.id.txtHomeTeam);
            holder.txtAwayTeam = convertView.findViewById(R.id.txtAwayTeam);
            holder.viewHomeTeam = convertView.findViewById(R.id.viewHomeTeam);
            holder.viewAwayTeam = convertView.findViewById(R.id.viewAwayTeam);
            holder.imgHomeTeam = convertView.findViewById(R.id.imgHomeTeam);
            holder.imgAwayTeam = convertView.findViewById(R.id.imgAwayTeam);
            holder.viewConfirm = convertView.findViewById(R.id.viewConfirm);
            holder.viewScore = convertView.findViewById(R.id.viewScore);
            holder.txtScoreAway = convertView.findViewById(R.id.txtScoreAway);
            holder.txtScoreHome = convertView.findViewById(R.id.txtScoreHome);
            holder.txtScoreSep = convertView.findViewById(R.id.txtScoreSep);
            holder.viewWinning = convertView.findViewById(R.id.viewWinning);
            holder.txtHeaderTite = convertView.findViewById(R.id.txtHeaderTite);
            holder.txtHeaderDesc = convertView.findViewById(R.id.txtHeaderDesc);
            holder.btnDraw = convertView.findViewById(R.id.btnDraw);
            holder.viewConfirm.setClickable(false);

            holder.txtHomeTeam.setText(match.getHomeName());
            holder.txtAwayTeam.setText(match.getAwayName());
            holder.txtScoreHome.setText(match.getHomeScore());
            holder.txtScoreAway.setText(match.getAwayScore());

            final Animation scale_up = AnimationUtils.loadAnimation(context, R.anim.scale_up);
            final Animation scale_down = AnimationUtils.loadAnimation(context, R.anim.scale_down);

            if(Integer.parseInt(match.getHomeScore()) > Integer.parseInt(match.getAwayScore())){
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
            }else if(Integer.parseInt(match.getHomeScore()) < Integer.parseInt(match.getAwayScore())){
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
            }else {
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
            }

        }else {
//            holder = (Holder) convertView.getTag();
        }

        return convertView;
    }

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }

    public class Holder {
        TextView txtHomeTeam, txtAwayTeam, txtScoreHome, txtScoreAway, txtScoreSep, txtHeaderTite, txtHeaderDesc;
        LinearLayout viewHomeTeam, viewAwayTeam, viewConfirm, viewScore, viewWinning;
        ImageView imgAwayTeam, imgHomeTeam;
        Button btnDraw;
    }

}