package com.andyshon.weather_forecast.db;

import com.andyshon.weather_forecast.GlobalConstants;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andyshon on 26.07.18.
 */

public class RestClient {
    private static IService iService;
    private static retrofit2.Retrofit restAdapter;

    private RestClient() {}

    public static IService initService() {

        if (iService == null) {

            restAdapter = new retrofit2.Retrofit.Builder()
                    .baseUrl(GlobalConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            iService = restAdapter.create(IService.class);
            return getService();
        }
        return getService();
    }

    public static IService getService() {
        return iService;
    }
}
