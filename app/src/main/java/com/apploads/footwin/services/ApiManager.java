package com.apploads.footwin.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiManager {

    private static final String API_URL = "";

    private static Retrofit retrofit = null;
    private static final ServiceInterface serviceInterface = getClient().create(ServiceInterface.class);

    public static Retrofit getClient() {

        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }

    public static ServiceInterface getService() {
        return serviceInterface;
    }

    public static String getApiUrl() {
        return API_URL;
    }
}