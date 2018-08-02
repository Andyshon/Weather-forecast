package com.andyshon.weather_forecast.data.entity.weather_today;

/**
 * Created by andyshon on 30.07.18.
 */

public class WeatherToday_main {
    private Double temp;
    private Double temp_min;
    private Double temp_max;
    private Double pressure;
    private Double humidity;

    public WeatherToday_main() {}

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(Double temp_min) {
        this.temp_min = temp_min;
    }

    public Double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(Double temp_max) {
        this.temp_max = temp_max;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }
}
