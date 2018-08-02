package com.andyshon.weather_forecast.data.entity.weather_today_forecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andyshon on 26.07.18.
 */

public class WeatherTodayForecast_list {

    public class WeatherDescription {
        public int id;
        public String description;
        public String main;
        public String icon;
    }

    class WeatherTemp {
        Double day;
        Double min;
        Double max;
        Double night;
        Double eve;
        Double morn;
    }

    public WeatherTodayForecast_list(List<WeatherDescription> description, WeatherTemp temp) {
        this.description = description;
        this.temp = temp;
    }

    @SerializedName("temp")
    private WeatherTemp temp;

    @SerializedName("weather")
    private List<WeatherDescription> description;

    @SerializedName("humidity")
    private Double humidity;

    @SerializedName("pressure")
    private Double pressure;

    @SerializedName("speed")
    private Double speed;

    @SerializedName("deg")
    private Double deg;

    @SerializedName("dt")
    private long dt;


    public long getDt() {
        return dt;
    }

    public String getDeg() {
        return String.valueOf(deg.intValue());
    }

    public String getSpeed() {
        return String.valueOf(speed.intValue());
    }

    public String getHumidity() {
        return String.valueOf(humidity.intValue());
    }

    public String getPressure() {
        return String.valueOf(pressure.intValue());
    }

    public String getTempDay() {
        return String.valueOf(temp.day.intValue());
    }

    public String getTempNight() {
        return String.valueOf(temp.night.intValue());
    }

    public String getTempEve() {
        return String.valueOf(temp.eve.intValue());
    }

    public String getTempMorn() {
        return String.valueOf(temp.morn.intValue());
    }

    public String getTempMax() {
        return String.valueOf(temp.max.intValue());
    }

    public String getTempMin() {
        return String.valueOf(temp.min.intValue());
    }

    public WeatherDescription getMain() {
        return description.get(0);
    }

    public WeatherDescription getDescription() {
        return description.get(0);
    }

    public String getIcon() {
        return description.get(0).icon;
    }

    public String getIconUrl() {
        return "http://openweathermap.org/img/w/" + description.get(0).icon + ".png";
    }
}
