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
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.model.Match;
import com.apploads.footwin.model.Prediction;
import com.apploads.footwin.predict.PredictFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MatchesResultAdapter extends BaseAdapter {

    private List<Prediction> root;
    private Context context;
    LayoutInflater mInflater;

    public MatchesResultAdapter(List<Prediction> root, Context context){
        this.root        = root;
        this.context     = context;
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
        final Prediction prediction = (Prediction) getItem(position);
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

            holder.viewWinning.setVisibility(View.GONE);

            holder.txtHomeTeam.setText(prediction.getHomeName());
            holder.txtAwayTeam.setText(prediction.getAwayName());
            holder.txtHeaderTite.setText(prediction.getTitle());
            holder.txtHeaderDesc.setText(prediction.getDescription());

            if(prediction.getHomeScore().equals("-1") && prediction.getAwayScore().equals("-1")){
                holder.txtScoreHome.setText("");
                holder.txtScoreAway.setText("");
            }else {
                holder.txtScoreHome.setText(prediction.getHomeScore());
                holder.txtScoreAway.setText(prediction.getAwayScore());
            }

            Picasso.with(context)
                    .load(StaticData.config.getMediaUrl()+prediction.getHomeFlag())
                    .into(holder.imgHomeTeam);

            Picasso.with(context)
                    .load(StaticData.config.getMediaUrl()+prediction.getAwayFlag())
                    .into(holder.imgAwayTeam);

            final Animation scale_up = AnimationUtils.loadAnimation(context, R.anim.scale_up);
            final Animation scale_down = AnimationUtils.loadAnimation(context, R.anim.scale_down);

            if(prediction.getHomeId().equals(prediction.getWinningTeam())){
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
            }else if(prediction.getAwayId().equals(prediction.getWinningTeam())){
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
                holder.btnDraw.setBackgroundResource(R.color.appBlue);
                holder.viewConfirm.setClickable(true);
            }

        }else {
//            holder = (Holder) convertView.getTag();
        }

        return convertView;
    }

    public class Holder {
        TextView txtHomeTeam, txtAwayTeam, txtScoreHome, txtScoreAway, txtScoreSep, txtHeaderTite, txtHeaderDesc;
        LinearLayout viewHomeTeam, viewAwayTeam, viewConfirm, viewScore, viewWinning;
        ImageView imgAwayTeam, imgHomeTeam;
        Button btnDraw;
    }

}