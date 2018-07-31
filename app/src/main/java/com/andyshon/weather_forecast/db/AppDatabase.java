package com.andyshon.weather_forecast.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.andyshon.weather_forecast.db.dao.FavouritesDao;
import com.andyshon.weather_forecast.db.entity.FavouriteEntity;
import com.andyshon.weather_forecast.db.entity.WeatherToday;

/**
 * Created by andyshon on 30.07.18.
 */

@Database(entities = {WeatherToday.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    @VisibleForTesting
    public static final String DATABASE_NAME = "weather-test-db";

    public abstract FavouritesDao favouritesDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    System.out.println("INSTANCE == null -> build database");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
