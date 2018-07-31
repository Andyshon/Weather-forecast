package com.andyshon.weather_forecast.db.entity;

/**
 * Created by andyshon on 30.07.18.
 */

public class WeatherWind {
    private Double speed;
    private Double deg;

    public WeatherWind() {}

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
