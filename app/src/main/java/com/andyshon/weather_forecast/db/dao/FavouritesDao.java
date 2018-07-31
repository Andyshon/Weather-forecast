package com.andyshon.weather_forecast.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.andyshon.weather_forecast.db.entity.WeatherToday;

import java.util.List;


/**
 * Created by andyshon on 30.07.18.
 */

@Dao
public interface FavouritesDao {
    @Query("SELECT * FROM favourites_weather")
    LiveData<List<WeatherToday>> loadAllFavourites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavourite(WeatherToday weatherToday);

    @Delete
    void deleteFavourite(WeatherToday weatherToday);
}
