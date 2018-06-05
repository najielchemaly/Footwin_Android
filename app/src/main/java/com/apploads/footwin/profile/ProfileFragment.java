package com.apploads.footwin.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apploads.footwin.R;
import com.apploads.footwin.helpers.CustomDialogClass;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.login.LoginActivity;
import com.apploads.footwin.model.BasicResponse;
import com.apploads.footwin.services.ApiManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private View parentView;
    TextView txtMyPredictions, txtEditMyProfile, txtChangePassword, txtTerms, txtName, txtPrivacyPolicy;
    LinearLayout viewLogout;
    ImageButton btnCamera;
    CircleImageView imgProfile;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.profile_fragment, container, false);

        txtMyPredictions = parentView.findViewById(R.id.txtMyPredictions);
        txtChangePassword = parentView.findViewById(R.id.txtChangePassword);
        txtEditMyProfile = parentView.findViewById(R.id.txtEditMyProfile);
        txtPrivacyPolicy = parentView.findViewById(R.id.txtPrivacyPolicy);
        txtName = parentView.findViewById(R.id.txtName);
        txtTerms = parentView.findViewById(R.id.txtTerms);
        imgProfile = parentView.findViewById(R.id.imgProfile);
        viewLogout = parentView.findViewById(R.id.viewLogout);

        txtName.setText(StaticData.user.getFullname());

        if(StaticData.user.getAvatar() != null && !StaticData.user.getAvatar().isEmpty()) {
            Picasso.with(getActivity())
                    .load(StaticData.config.getMediaUrl() + StaticData.user.getAvatar())
                    .into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.avatar_male);
            if(StaticData.user.getGender() == "female") {
                imgProfile.setImageResource(R.drawable.avatar_female);
            }
        }

        txtMyPredictions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyPredictionsActivity.class);
                startActivity(intent);
            }
        });

        txtEditMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TermsAndConditionsActivity.class);
                intent.putExtra("type", "terms");
                startActivity(intent);
            }
        });

        txtPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TermsAndConditionsActivity.class);
                intent.putExtra("type", "privacy");
                startActivity(intent);
            }
        });

        viewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogClass customDialogClass = new CustomDialogClass(getActivity(), new CustomDialogClass.AbstractCustomDialogListener() {
                    @Override
                    public void onConfirm(CustomDialogClass.DialogResponse response) {
                        callLogoutService();
                        response.getDialog().dismiss();
                    }

                    @Override
                    public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                        dialogResponse.getDialog().dismiss();
                    }
                }, false);

                customDialogClass.setTitle("FOOTWIN");
                customDialogClass.setMessage("Are you sure you want to logout?");
                customDialogClass.show();
            }
        });
        return parentView;
    }

    private void callLogoutService(){
        ApiManager.getService().logout().enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                BasicResponse basicResponse = response.body();

                FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/footwinnews");
                AppUtils.saveUser(getContext(), null);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });
    }
}