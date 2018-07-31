package com.andyshon.weather_forecast.db.entity;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by andyshon on 30.07.18.
 */

public class ListTypeConverters {
    static Gson gson = new Gson();

    @TypeConverter
    public static List<WeatherDescription> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<WeatherDescription>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<WeatherDescription> someObjects) {
        return gson.toJson(someObjects);
    }
}
