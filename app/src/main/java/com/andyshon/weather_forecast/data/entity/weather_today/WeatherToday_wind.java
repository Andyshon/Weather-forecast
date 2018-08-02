package com.andyshon.weather_forecast.data.entity.weather_today;

/**
 * Created by andyshon on 30.07.18.
 */

public class WeatherToday_wind {
    private Double speed;
    private Double deg;

    public WeatherToday_wind() {}

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getDeg() {
        return deg;
    }

    public void setDeg(Double deg) {
        this.deg = deg;
    }
}
