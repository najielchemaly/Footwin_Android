package com.apploads.footwin.services;

import com.apploads.footwin.model.Config;
import com.apploads.footwin.model.LeaderboardResponse;
import com.apploads.footwin.model.Match;
import com.apploads.footwin.model.News;
import com.apploads.footwin.model.Profile;
import com.apploads.footwin.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ServiceInterface {

    @POST
    Call<Config> getConfig(@Url String url);

    @GET
    Call<News> getNews(@Url String url);

    @Headers({
            "Content-Type: application/json",
            "User-id: 1"
    })
    @POST("getMatches/")
    Call<Profile> getMatches();

    @Headers({
            "Content-Type: application/json",
            "User-id: 1"
    })
    @POST("getMatches/")
    Call<LeaderboardResponse> getLeaderBoard();

    @FormUrlEncoded
    @POST("registerUser/")
    Call<UserResponse> registerUser(@Field("fullname") String fullname, @Field("username") String username, @Field("email") String email
            , @Field("password") String password, @Field("phone_code") String phone_code, @Field("phone") String phone
            , @Field("gender") String gender, @Field("country") String country,  @Field("favorite_team") String favorite_team);

}
