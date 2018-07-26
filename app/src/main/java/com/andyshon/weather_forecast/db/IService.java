package com.andyshon.weather_forecast.db;

import com.andyshon.weather_forecast.db.entity.WeatherDay;
import com.andyshon.weather_forecast.db.entity.WeatherForecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by andyshon on 26.07.18.
 */

public interface IService {
    @GET("forecast")
    Call<WeatherForecast> getForecast(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("weather")
    Call<WeatherDay> getToday(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("appid") String appid
    );
}
