package com.andyshon.weather_forecast.data.entity.Converters;

import android.arch.persistence.room.TypeConverter;

import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast_city;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by andyshon on 30.07.18.
 */

public class TypeConverters_forecastToday_city {
    static Gson gson = new Gson();

    @TypeConverter
    public static WeatherTodayForecast_city.Coord stringToSomeObjectList(String data) {
        if (data == null) {
            return null;
        }

        Type type = new TypeToken<WeatherTodayForecast_city.Coord>() {}.getType();

        return gson.fromJson(data, type);
    }

    @TypeConverter
    public static String someObjectListToString(WeatherTodayForecast_city.Coord someObjects) {
        return gson.toJson(someObjects);
    }
}
