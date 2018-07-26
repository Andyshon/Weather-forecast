package com.andyshon.weather_forecast.db.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andyshon on 26.07.18.
 */

public class WeatherForecast {
    @SerializedName("list")
    private List<WeatherDay> items;

    public WeatherForecast(List<WeatherDay> items) {
        this.items = items;
    }

    public List<WeatherDay> getItems() {
        return items;
    }
}
