package com.andyshon.weather_forecast.data;

import android.app.Application;

import com.andyshon.weather_forecast.data.local.DataRepository;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by andyshon on 30.07.18.
 */

public class BasicApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
