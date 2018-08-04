package com.andyshon.weather_forecast.data;

import android.app.Application;

import com.andyshon.weather_forecast.data.local.DataRepository;

/**
 * Created by andyshon on 30.07.18.
 */

public class BasicApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
