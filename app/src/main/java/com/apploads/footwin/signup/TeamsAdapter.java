package com.apploads.footwin.signup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.R;
import com.apploads.footwin.model.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamsAdapter extends BaseAdapter {

    Context context;
    List<Team> teams;
    private static LayoutInflater inflater = null;
    private ImageView selectedLayout;

    public TeamsAdapter(Context context, List<Team> teams) {

        this.teams = teams;
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return teams.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.team_row_item, null);
        holder.txtTeam = rowView.findViewById(R.id.txtTeam);
        holder.imgTeam = rowView.findViewById(R.id.imgTeam);
        holder.imgCheck = rowView.findViewById(R.id.imgCheck);

        holder.txtTeam.setText(teams.get(position).getName());
        holder.imgCheck.setVisibility(View.GONE);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLayout != null) {
                    selectedLayout.setBackgroundResource(0);
                }
                selectedLayout = holder.imgTeam;
                selectedLayout = selectView(holder);
            }
        });

        return rowView;
    }

    public class Holder {
        TextView txtTeam;
        ImageView imgTeam, imgCheck;
    }

    private ImageView selectView(Holder holder) {

        holder.imgTeam.setImageResource(R.drawable.selected_team_background);
        return holder.imgTeam;
    }

}