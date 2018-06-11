package com.apploads.footwin.services;

import com.apploads.footwin.helpers.StaticData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiManager {

    private static String API_URL = "http://api.foot-win.com/";

    private static Retrofit retrofit = null;
    private static boolean disbaleHeader;

    public static Retrofit getClient() {

        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = null;
                if (disbaleHeader) {
                    requestBuilder = original.newBuilder().header("ID", "1");
                } else {
                    //LoadingActivity.appConfig.getTOKEN()
                    requestBuilder = original.newBuilder().header("User-Id",StaticData.user.getId());
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient
                .readTimeout(300000, TimeUnit.MILLISECONDS)
                .writeTimeout(300000, TimeUnit.MILLISECONDS)
                .connectTimeout(300000, TimeUnit.MILLISECONDS)

                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

    private static final ServiceInterface serviceInterface = getClient().create(ServiceInterface.class);

    public static ServiceInterface getService() {
        return getService(false);
    }

    public static ServiceInterface getService(boolean disbaleHeader) {
        ApiManager.disbaleHeader = disbaleHeader;
        return serviceInterface;
    }

    public static String getApiUrl() {
        return API_URL;
    }

    public static void setApiUrl(String url) {
        API_URL = url;
    }

}