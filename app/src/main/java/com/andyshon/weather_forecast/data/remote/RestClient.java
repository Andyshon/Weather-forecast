package com.andyshon.weather_forecast.data.remote;

import com.andyshon.weather_forecast.GlobalConstants;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andyshon on 26.07.18.
 */

public class RestClient {
    private static IService iService;
    private static retrofit2.Retrofit restAdapter;

    private RestClient() {}

    public static void initService() {

        if (iService == null) {

            restAdapter = new retrofit2.Retrofit.Builder()
                    .baseUrl(GlobalConstants.ApiConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            iService = restAdapter.create(IService.class);
        }
    }

    public static IService getService() {
        return iService;
    }
}
