package com.andyshon.weather_forecast.data.entity.weather_today_hour_forecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andyshon on 28.07.18.
 */

public class WeatherTodayHourForecast {
    @SerializedName("list")
    private List<WeatherTodayHourForecast_list> items;

    public WeatherTodayHourForecast(List<WeatherTodayHourForecast_list> items) {
        this.items = items;
    }

    public List<WeatherTodayHourForecast_list> getItems() {
        return items;
    }
}
