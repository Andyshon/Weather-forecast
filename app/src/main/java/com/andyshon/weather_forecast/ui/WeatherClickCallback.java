package com.andyshon.weather_forecast.ui;

import com.andyshon.weather_forecast.db.entity.WeatherFiveDaysForecast;

public interface WeatherClickCallback {
    void onClick(WeatherFiveDaysForecast book);
}
