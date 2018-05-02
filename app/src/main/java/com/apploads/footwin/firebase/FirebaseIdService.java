package com.apploads.footwin.firebase;

import android.util.Log;
import android.widget.Toast;

import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.helpers.utils.StringUtils;
import com.apploads.footwin.services.ApiManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseIdService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        if(StringUtils.isValid(StaticData.user.getId())){
            sendRegistrationToServer(refreshedToken);
        }
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        ApiManager.getService().updateFirebaseToken(token).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Toast.makeText(FirebaseIdService.this, "asdasd", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(FirebaseIdService.this, "asdasd", Toast.LENGTH_SHORT).show();
            }
        });
    }
}