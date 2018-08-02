package com.andyshon.weather_forecast.data.entity.weather_today;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.andyshon.weather_forecast.data.entity.Converters.ListTypeConverters_weatherDescription;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andyshon on 30.07.18.
 */

/*
* This class uses with MyWidget class to show current weather conditions
* */

@Entity(tableName = "favourites_weather_today")
public class WeatherToday {

    public WeatherToday() {}

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Embedded
    @SerializedName("main")
    private WeatherToday_main temp;

    @Embedded
    @SerializedName("wind")
    private WeatherToday_wind wind;

    @SerializedName("dt")
    private long dt;

    @SerializedName("name")
    private String city;

    @TypeConverters(ListTypeConverters_weatherDescription.class)
    @SerializedName("weather")
    private List<WeatherToday_description> description;


    public void setDescription(List<WeatherToday_description> description) {
        this.description = description;
    }

    public List<WeatherToday_description> getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public WeatherToday_main getTemp() {
        return temp;
    }

    public void setTemp(WeatherToday_main temp) {
        this.temp = temp;
    }

    public WeatherToday_wind getWind() {
        return wind;
    }

    public void setWind(WeatherToday_wind wind) {
        this.wind = wind;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
