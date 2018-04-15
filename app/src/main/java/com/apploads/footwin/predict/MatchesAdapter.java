package com.apploads.footwin.predict;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apploads.footwin.R;
import com.apploads.footwin.model.Match;

import java.util.List;

public class MatchesAdapter extends BaseAdapter {

    private List<Match> root;
    private Context context;
    private LayoutInflater mInflater;

    public MatchesAdapter(List<Match> root, Context context){
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
        final Match match = (Match) getItem(position);
        convertView = mInflater.inflate(R.layout.match_row_item, null);

        TextView txtHomeTeam = convertView.findViewById(R.id.txtHomeTeam);
        TextView txtAwayTeam = convertView.findViewById(R.id.txtAwayTeam);

        txtHomeTeam.setText(match.getHomeTeam());
        txtAwayTeam.setText(match.getAwayTeam());

        return convertView;
    }

}