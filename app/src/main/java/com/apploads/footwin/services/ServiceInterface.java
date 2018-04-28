package com.apploads.footwin.services;

import com.apploads.footwin.model.BasicResponse;
import com.apploads.footwin.model.Config;
import com.apploads.footwin.model.LeaderboardResponse;
import com.apploads.footwin.model.Match;
import com.apploads.footwin.model.News;
import com.apploads.footwin.model.Notification;
import com.apploads.footwin.model.PackageResponse;
import com.apploads.footwin.model.Profile;
import com.apploads.footwin.model.UserResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ServiceInterface {

    @POST
    Call<Config> getConfig(@Url String url);

    @Headers({
            "Content-Type: application/json"
    })
    @POST("logout/")
    Call<BasicResponse> logout();

    @GET
    Call<News> getNews(@Url String url);

    @Headers({
            "Content-Type: application/json"
    })
    @POST("getMatches/")
    Call<Profile> getMatches();

    @Headers({
            "Content-Type: application/json"
    })
    @POST("getNotifications/")
    Call<Object> getNotifications(); // TODO change the response object

    @Headers({
            "Content-Type: application/json"
    })
    @POST("getLeaderboard/")
    Call<LeaderboardResponse> getLeaderBoard();

    @FormUrlEncoded
    @POST("registerUser/")
    Call<UserResponse> registerUser(@Field("fullname") String fullname, @Field("username") String username, @Field("email") String email
            , @Field("password") String password, @Field("phone_code") String phone_code, @Field("phone") String phone
            , @Field("gender") String gender, @Field("country") String country,  @Field("favorite_team") String favorite_team);

    @FormUrlEncoded
    @POST("login/")
    Call<UserResponse> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("facebookLogin/")
    Call<UserResponse> facebookLogin(@Field("facebook_id") String facebook_id, @Field("facebook_token") String facebook_token
            , @Field("fullname") String fullname, @Field("email") String email
            , @Field("gender") String gender);

    @Headers({
            "Content-Type: application/json"
    })
    @FormUrlEncoded
    @POST("editUser/")
    Call<UserResponse> editUser(@Field("fullname") String fullname, @Field("email") String email
            , @Field("country") String country, @Field("phone_code") String phone_code, @Field("phone") String phone
            , @Field("gender") String gender);

    @Headers({
            "Content-Type: application/json"
    })
    @FormUrlEncoded
    @POST("forgotPassword/")
    Call<BasicResponse> forgotPassword(@Field("email") String email);

    @Headers({
            "Content-Type: application/json"
    })
    @FormUrlEncoded
    @POST("changePassword/")
    Call<BasicResponse> changePassowrd(@Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

    @Headers({
            "Content-Type: application/json"
    })
    @POST("getPackages/")
    Call<PackageResponse> getPackages();

    @Headers({
            "Content-Type: application/json"
    })
    @POST("getPredictions/")
    Call<Object> getPredictions(); // TODO change the response object

    @Multipart
    @POST("updateAvatar/")
    Call<Object> updateAvatar(@Part MultipartBody.Part file);

//    @FormUrlEncoded
//    @POST("sendPredictions/")
//    Call<BasicResponse> sendPredictions(@Field("user_id") String user_id, @Field("match_id") String match_id,
//                                 @Field("winning_team") String winning_team, @Field("home_score") String home_score,
//                                 @Field("away_score") String away_score, @Field("status") String status,
//                                 @Field("selected_team") String selected_team, @Field("date") String date);

    @FormUrlEncoded
    @POST("sendPredictions/")
    Call<BasicResponse> sendPredictions(@Field("user_id") String user_id, @Field("match_id") String match_id, @Field("winning_team") String winning_team
            , @Field("home_score") String home_score, @Field("away_score") String away_score, @Field("status") String status
            , @Field("selected_team") String selected_team, @Field("date") String date);


    @FormUrlEncoded
    @POST("updateFirebaseToken/")
    Call<Object> updateFirebaseToken(@Field("firebase_token") String firebase_token); // TODO change the response object

    @Headers({
            "Content-Type: application/json"
    })
    @POST("updateNotification/")
    Call<Object> updateNotification(@Field("id") String id); // TODO change the response object

}
