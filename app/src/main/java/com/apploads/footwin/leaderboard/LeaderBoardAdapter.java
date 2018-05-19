package com.apploads.footwin.leaderboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.model.Leaderboard;
import com.apploads.footwin.model.News;
import com.apploads.footwin.news.NewsDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderBoardAdapter extends BaseAdapter {

    private List<Leaderboard> root;
    private Context context;
    private LayoutInflater mInflater;

    public LeaderBoardAdapter(List<Leaderboard> root, Context context){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Leaderboard leaderboard = (Leaderboard) getItem(position);
        convertView = mInflater.inflate(R.layout.leaderboard_row_item, null);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtRank = convertView.findViewById(R.id.txtRank);
        TextView txtCoins = convertView.findViewById(R.id.txtCoins);
        CircleImageView imgProfile = convertView.findViewById(R.id.imgProfile);

        txtName.setText(leaderboard.getFullname());
        txtRank.setText(String.valueOf(position+2));
        txtCoins.setText(String.valueOf(leaderboard.getCoins()));

        if(leaderboard.getAvatar() != null && !leaderboard.getAvatar().isEmpty()) {
            Picasso.with(context)
                    .load(StaticData.config.getMediaUrl() + leaderboard.getAvatar())
                    .into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.avatar_male);
            if(StaticData.user.getGender() == "female") {
                imgProfile.setImageResource(R.drawable.avatar_female);
            }
        }

        return convertView;
    }

}