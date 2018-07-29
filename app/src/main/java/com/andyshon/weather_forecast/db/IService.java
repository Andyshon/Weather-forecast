package com.andyshon.weather_forecast.db;

import com.andyshon.weather_forecast.db.entity.WeatherDayHourForecastList;
import com.andyshon.weather_forecast.db.entity.WeatherToday;
import com.andyshon.weather_forecast.db.entity.WeatherForecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by andyshon on 26.07.18.
 */

public interface IService {

    @GET("forecast/daily")
    Call<WeatherForecast> getForecastByCityName(
            @Query("q") String cityName,
            @Query("cnt") int cnt,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("forecast")
    Call<WeatherDayHourForecastList> getHourForecastByCityName(
            @Query("q") String cityName,
            @Query("cnt") int cnt,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("weather")
    Call<WeatherToday> getTodayByCityName(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("weather")
    Call<WeatherToday> getTodayByCoordinates(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("units") String units,
            @Query("appid") String appid
    );

}
