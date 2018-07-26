package com.andyshon.weather_forecast.db.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.List;

/**
 * Created by andyshon on 26.07.18.
 */

public class WeatherDay {
    public class WeatherTemp {
        Double temp;
        Double temp_min;
        Double temp_max;
        Double pressure;
        Double humidity;
    }

    public class WeatherWind {
        Double speed;
        Double deg;
    }

    public class WeatherDescription {
        String icon;
        int id;
        String main;
        String description;

        public String getDescription() {
            return description;
        }

        public String getMain() {
            return main;
        }

        public String getIcon() {
            return icon;
        }

        public int getId() {
            return id;
        }
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

    public String getDt_txt() {
        return dt_txt;
    }

    public WeatherDay(WeatherWind wind, WeatherTemp temp, List<WeatherDescription> desctiption) {
        this.wind = wind;
        this.temp = temp;
        this.desctiption = desctiption;
    }

    public Calendar getDate() {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timestamp * 1000);
        return date;
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

    public String getTemp() { return String.valueOf(temp.temp); }

    public String getTempMin() { return String.valueOf(temp.temp_min); }

    public String getTempMax() { return String.valueOf(temp.temp_max); }

    public String getPressure() {
        return String.valueOf(temp.pressure);
    }

    public String getTempInteger() { return String.valueOf(temp.temp.intValue()); }

    public String getTempWithDegree() { return String.valueOf(temp.temp.intValue()) + "\u00B0"; }

    public String getCity() { return city; }

    public String getIcon() { return desctiption.get(0).icon; }

    public String getIconUrl() {
        return "http://openweathermap.org/img/w/" + desctiption.get(0).icon + ".png";
    }
}
