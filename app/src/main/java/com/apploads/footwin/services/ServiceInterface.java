package com.apploads.footwin.services;

import com.apploads.footwin.model.Config;
import com.apploads.footwin.model.Match;
import com.apploads.footwin.model.Profile;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ServiceInterface {

    @POST
    Call<Config> getConfig(@Url String url);

    @Headers({
            "Content-Type: application/json",
            "User-id: 1"
    })
    @POST("getMatches/")
    Call<Profile> getMatches();
}
