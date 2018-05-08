package com.apploads.footwin.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.model.Match;
import com.apploads.footwin.model.Notification;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsAdapter extends BaseAdapter {

    private List<Notification> root;
    private Context context;
    private LayoutInflater mInflater;

    public NotificationsAdapter(List<Notification> root, Context context){
        this.root        = root;
        this.context     = context;
        mInflater = LayoutInflater.from(context);
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
        final Notification notification = (Notification) getItem(position);
        convertView = mInflater.inflate(R.layout.notifications_row_item, null);

        TextView txtNotificationDesc = convertView.findViewById(R.id.txtNotificationDesc);
        TextView txtNotificationTitle = convertView.findViewById(R.id.txtNotificationTitle);
        TextView txtDate = convertView.findViewById(R.id.txtDate);
        CircleImageView imgHomeTeam = convertView.findViewById(R.id.imgHomeTeam);
        CircleImageView imgAwayTeam = convertView.findViewById(R.id.imgAwayTeam);
        TextView txtHomeTeam = convertView.findViewById(R.id.txtHomeTeam);
        TextView txtAwayTeam = convertView.findViewById(R.id.txtAwayTeam);
        TextView txtHomeScore = convertView.findViewById(R.id.txtHomeScore);
        TextView txtAwayScore = convertView.findViewById(R.id.txtAwayScore);
        Button btnCoins = convertView.findViewById(R.id.btnCoins);
        RelativeLayout viewResults = convertView.findViewById(R.id.viewResults);

        txtNotificationTitle.setText(notification.getTitle());
        txtNotificationDesc.setText(notification.getDescription());
        txtDate.setText(notification.getDate());

        txtHomeTeam.setText(notification.getHomeName());
        txtAwayTeam.setText(notification.getAwayName());
        txtHomeScore.setText(notification.getHomeScore());
        txtAwayScore.setText(notification.getAwayScore());

        Picasso.with(context)
                .load(StaticData.config.getMediaUrl()+notification.getHomeFlag())
                .into(imgHomeTeam);

        Picasso.with(context)
                .load(StaticData.config.getMediaUrl()+notification.getAwayFlag())
                .into(imgAwayTeam);

        if(notification.getType().equals("message")){
            btnCoins.setVisibility(View.GONE);
            viewResults.setVisibility(View.GONE);
        } else if (notification.getType().equals("get_coins")){
            btnCoins.setVisibility(View.VISIBLE);
            viewResults.setVisibility(View.GONE);
        }else {
            btnCoins.setVisibility(View.GONE);
            viewResults.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}