package com.andyshon.weather_forecast.db.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andyshon on 26.07.18.
 */

public class WeatherToday {

    public class WeatherDescription {
        public int id;
        public String description;
        public String main;
        public String icon;
    }

    class WeatherMain {
        Double temp;
        Double temp_min;
        Double temp_max;
        Double pressure;
        Double humidity;
    }

    class WeatherWind {
        Double speed;
        Double deg;
    }

    public WeatherToday(List<WeatherDescription> description, WeatherMain temp, WeatherWind wind) {
        this.description = description;
        this.temp = temp;
        this.wind = wind;
    }

    @SerializedName("main")
    private WeatherMain temp;

    @SerializedName("weather")
    private List<WeatherDescription> description;

    @SerializedName("wind")
    private WeatherWind wind;

    @SerializedName("dt")
    private long dt;

    @SerializedName("name")
    private String city;


    public long getDt() {
        return dt;
    }

    public String getSpeed() {
        return String.valueOf(wind.speed.intValue());
    }

    public String getDeg() {
        return String.valueOf(wind.deg.intValue());
    }

    public String getTempInteger() {
        return String.valueOf(temp.temp.intValue());
    }

    public String getPressure() {
        return String.valueOf(temp.pressure.intValue());
    }

    public String getHumidity() {
        return String.valueOf(temp.humidity.intValue());
    }

    public String getTempMaxInteger() {
        return String.valueOf(temp.temp_max.intValue());
    }

    public String getTempMinInteger() {
        return String.valueOf(temp.temp_min.intValue());
    }

    public WeatherDescription getMain() {
        return description.get(0);
    }

    public WeatherDescription getDescription() {
        return description.get(0);
    }

    public String getCity() {
        return city;
    }
}
