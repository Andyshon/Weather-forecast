package com.andyshon.weather_forecast.data.entity.Converters;

import android.arch.persistence.room.TypeConverter;

import com.andyshon.weather_forecast.data.entity.weather_today.WeatherToday_description;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by andyshon on 30.07.18.
 */

public class ListTypeConverters_weatherDescription {
    static Gson gson = new Gson();

    @TypeConverter
    public static List<WeatherToday_description> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<WeatherToday_description>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<WeatherToday_description> someObjects) {
        return gson.toJson(someObjects);
    }
}
