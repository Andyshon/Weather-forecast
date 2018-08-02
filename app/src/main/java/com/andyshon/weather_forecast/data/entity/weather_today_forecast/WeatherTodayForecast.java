package com.andyshon.weather_forecast.data.entity.weather_today_forecast;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.andyshon.weather_forecast.data.entity.Converters.ListTypeConverters_forecastToday_list;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andyshon on 01.08.18.
 */

@Entity(tableName = "favourites_weather_forecast_today")
public class WeatherTodayForecast {

    public WeatherTodayForecast(List<WeatherTodayForecast_list> items, WeatherTodayForecast_city items_city) {
        this.items = items;
        this.items_city = items_city;
    }


    @PrimaryKey(autoGenerate = true)
    private int id;

    @TypeConverters(ListTypeConverters_forecastToday_list.class)
    @SerializedName("list")
    private List<WeatherTodayForecast_list> items;

    @Embedded
    @SerializedName("city")
    private WeatherTodayForecast_city items_city;


    public List<WeatherTodayForecast_list> getItems() {
        return items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WeatherTodayForecast_city getItems_city() {
        return items_city;
    }
}
