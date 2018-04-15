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
import com.apploads.footwin.model.Match;
import com.apploads.footwin.model.Notification;

import java.util.List;

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

        TextView txtNotification = convertView.findViewById(R.id.txtNotification);
        TextView txtDate = convertView.findViewById(R.id.txtDate);
        Button btnCoins = convertView.findViewById(R.id.btnCoins);
        RelativeLayout viewResults = convertView.findViewById(R.id.viewResults);

        txtNotification.setText(notification.getDescription());
        txtDate.setText(notification.getDate());

        if(notification.getType() == 1){
            btnCoins.setVisibility(View.VISIBLE);
            viewResults.setVisibility(View.GONE);
        }else {
            btnCoins.setVisibility(View.GONE);
            viewResults.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

}