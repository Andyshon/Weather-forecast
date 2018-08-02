package com.andyshon.weather_forecast.data.local;

import android.arch.lifecycle.LiveData;

import com.andyshon.weather_forecast.data.AppDatabase;
import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast;

import java.util.List;

/**
 * Created by andyshon on 30.07.18.
 */

public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;


    private DataRepository(final AppDatabase database) {
        mDatabase = database;
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<WeatherTodayForecast>> getFavourites() {
       return mDatabase.favouritesDao().loadAllFavourites();
    }

    public void addToFavourite(WeatherTodayForecast favourite) {
        mDatabase.favouritesDao().insertFavourite(favourite);
    }

    public void deleteFromFavourite(WeatherTodayForecast favouriteEntity) {
        mDatabase.favouritesDao().deleteFavourite(favouriteEntity);
    }
}
