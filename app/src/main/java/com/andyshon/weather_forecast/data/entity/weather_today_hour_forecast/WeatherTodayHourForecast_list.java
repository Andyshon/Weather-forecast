package com.andyshon.weather_forecast.data.entity.weather_today_hour_forecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andyshon on 26.07.18.
 */

public class WeatherTodayHourForecast_list {

    public class WeatherTemp {
        Double temp;
        Double temp_min;
        Double temp_max;
        Double pressure;
        Double humidity;
    }

    public class WeatherDescription {
        String description;

        public String getDescription() {
            return description;
        }
    }

    public class WeatherWind {
        Double speed;
        Double deg;
    }

    @SerializedName("dt_txt")
    private String dt_txt;

    @SerializedName("main")
    private WeatherTemp temp;

    @SerializedName("wind")
    private WeatherWind wind;

    @SerializedName("weather")
    private List<WeatherDescription> desctiption;

    @SerializedName("name")
    private String city;

    @SerializedName("dt")
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public WeatherTodayHourForecast_list(WeatherWind wind, WeatherTemp temp, List<WeatherDescription> desctiption) {
        this.wind = wind;
        this.temp = temp;
        this.desctiption = desctiption;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public String getSpeed() {
        return String.valueOf(wind.speed);
    }

    public String getDeg() {
        return String.valueOf(wind.deg);
    }

    public String getHumidity() {
        return String.valueOf(temp.humidity);
    }

    public WeatherDescription getMain() {
        return desctiption.get(0);
    }

    public WeatherDescription getDescription() {
        return desctiption.get(0);
    }

    public String getTemp() {
        return String.valueOf(temp.temp);
    }

    public String getTempMin() {
        return String.valueOf(temp.temp_min);
    }

    public String getTempMax() {
        return String.valueOf(temp.temp_max);
    }

    public String getPressure() {
        return String.valueOf(temp.pressure);
    }

    public String getTempInteger() {
        return String.valueOf(temp.temp.intValue());
    }

    public String getCity() {
        return city;
    }
}
