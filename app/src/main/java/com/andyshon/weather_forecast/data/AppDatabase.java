package com.andyshon.weather_forecast.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.andyshon.weather_forecast.data.dao.FavouritesDao;
import com.andyshon.weather_forecast.data.entity.weather_today.WeatherToday;
import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast;

/**
 * Created by andyshon on 30.07.18.
 */

@Database(entities = {WeatherTodayForecast.class, WeatherToday.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    @VisibleForTesting
    public static final String DATABASE_NAME = "weather-db";

    public abstract FavouritesDao favouritesDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
