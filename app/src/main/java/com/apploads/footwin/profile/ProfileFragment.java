package com.apploads.footwin.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apploads.footwin.R;

public class ProfileFragment extends Fragment {

    private View parentView;
    TextView txtMyPredictions, txtEditMyProfile, txtChangePassword, txtTerms;
    LinearLayout viewLogout;
    ImageButton btnCamera;


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.profile_fragment, container, false);


        return parentView;
    }
}