package com.andyshon.weather_forecast.db;

import android.app.Application;

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
