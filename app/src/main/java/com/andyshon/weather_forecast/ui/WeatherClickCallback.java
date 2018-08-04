package com.andyshon.weather_forecast.ui;


import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast_list;

public interface WeatherClickCallback {
    void onClick(WeatherTodayForecast_list fiveDaysForecast, boolean isToday);
}
