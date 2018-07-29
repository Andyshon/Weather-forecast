package com.andyshon.weather_forecast.db.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andyshon on 28.07.18.
 */

public class WeatherDayHourForecastList {
    @SerializedName("list")
    private List<WeatherDayHourForecast> items;

    public WeatherDayHourForecastList(List<WeatherDayHourForecast> items) {
        this.items = items;
    }

    public List<WeatherDayHourForecast> getItems() {
        return items;
    }
}
