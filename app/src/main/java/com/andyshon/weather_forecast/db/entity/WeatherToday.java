package com.andyshon.weather_forecast.db.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andyshon on 30.07.18.
 */

@Entity(tableName = "favourites_weather")
public class WeatherToday {

    public WeatherToday() {}

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Embedded
    @SerializedName("main")
    private WeatherMain temp;

    @Embedded
    @SerializedName("wind")
    private WeatherWind wind;

    @SerializedName("dt")
    private long dt;

    @SerializedName("name")
    private String city;

    @TypeConverters(ListTypeConverters.class)
    @SerializedName("weather")
    private List<WeatherDescription> description;


    public void setDescription(List<WeatherDescription> description) {
        this.description = description;
    }

    public List<WeatherDescription> getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public WeatherMain getTemp() {
        return temp;
    }

    public void setTemp(WeatherMain temp) {
        this.temp = temp;
    }

    public WeatherWind getWind() {
        return wind;
    }

    public void setWind(WeatherWind wind) {
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
