package com.andyshon.weather_forecast.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast;

import java.util.List;


/**
 * Created by andyshon on 30.07.18.
 */

@Dao
public interface FavouritesDao {

    @Query("SELECT * FROM favourites_weather_forecast_today")
    LiveData<List<WeatherTodayForecast>> loadAllFavourites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavourite(WeatherTodayForecast weatherToday_forecast);

    @Delete
    void deleteFavourite(WeatherTodayForecast weatherToday);
}
