package com.andyshon.weather_forecast.db.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andyshon on 26.07.18.
 */

public class WeatherForecast {
    @SerializedName("list")
    private List<WeatherFiveDaysForecast> items;

    public WeatherForecast(List<WeatherFiveDaysForecast> items) {
        this.items = items;
    }

    public List<WeatherFiveDaysForecast> getItems() {
        return items;
    }
}
