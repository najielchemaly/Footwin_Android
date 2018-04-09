package com.apploads.footwin.services;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ServiceInterface {

    @POST
    Call<String> getConfig(@Url String url);
}
