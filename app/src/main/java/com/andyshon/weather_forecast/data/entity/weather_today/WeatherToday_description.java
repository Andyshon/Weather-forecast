package com.andyshon.weather_forecast.data.entity.weather_today;

/**
 * Created by andyshon on 30.07.18.
 */

public class WeatherToday_description {
    private String description;
    private String main;
    private String icon;

    public WeatherToday_description() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
