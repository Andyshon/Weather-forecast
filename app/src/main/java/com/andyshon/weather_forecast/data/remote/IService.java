package com.andyshon.weather_forecast.data.remote;

import com.andyshon.weather_forecast.data.entity.weather_today_hour_forecast.WeatherTodayHourForecast;
import com.andyshon.weather_forecast.data.entity.weather_today.WeatherToday;
import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by andyshon on 26.07.18.
 */

public interface IService {

    @GET("weather")
    Single<WeatherToday> getTodayByCityName(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("forecast")
    Single<WeatherTodayHourForecast> getHourForecastByCityName(
            @Query("q") String cityName,
            @Query("cnt") int cnt,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("forecast/daily")
    Single<WeatherTodayForecast> getForecastByCityName(
            @Query("q") String cityName,
            @Query("cnt") int cnt,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("forecast/daily")
    Single<WeatherTodayForecast> getTodayForecastByCityName(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("forecast/daily")
    Single<WeatherTodayForecast> getForecastByCoordinates(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("cnt") int cnt,
            @Query("units") String units,
            @Query("appid") String appid
    );
}
