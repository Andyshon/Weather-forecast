package com.andyshon.weather_forecast.data.entity.weather_today_forecast;

import android.arch.persistence.room.TypeConverters;

import com.andyshon.weather_forecast.data.entity.Converters.TypeConverters_forecastToday_city;
import com.google.gson.annotations.SerializedName;

/**
 * Created by andyshon on 01.08.18.
 */

public class WeatherTodayForecast_city {

    public WeatherTodayForecast_city(Coord coord) {
        this.coord = coord;
    }

    @SerializedName("population")
    private double population;

    @TypeConverters(TypeConverters_forecastToday_city.class)
    @SerializedName("coord")
    private Coord coord;

    @SerializedName("name")
    private String cityName;

    @SerializedName("country")
    private String country;


    public class Coord {
        double lon;
        double lat;

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }


    public double getPopulation() {
        return population;
    }

    public void setPopulation(double population) {
        this.population = population;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
