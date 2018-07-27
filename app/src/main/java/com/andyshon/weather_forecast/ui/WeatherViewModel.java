package com.andyshon.weather_forecast.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.db.RestClient;
import com.andyshon.weather_forecast.db.entity.WeatherDay;
import com.andyshon.weather_forecast.db.entity.WeatherForecast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by andyshon on 27.07.18.
 */

public class WeatherViewModel extends ViewModel {

    private MutableLiveData<WeatherDay> weatherDayLiveData;
    private MutableLiveData<WeatherForecast> weatherForecastLiveData;

    public LiveData<WeatherDay> getTodayData() {
        if (GlobalConstants.CURRENT_CITY_EN == null)
            weatherDayLiveData = new MutableLiveData<>();
        else
            loadTodayData();
        return weatherDayLiveData;
    }

    public LiveData<WeatherForecast> getForecastData() {
        if (GlobalConstants.CURRENT_CITY_EN == null)
            weatherForecastLiveData = new MutableLiveData<>();
        else
            loadForecastData();
        return weatherForecastLiveData;
    }

    private void loadForecastData() {
        RestClient.getService().getForecast(GlobalConstants.CURRENT_CITY_EN, GlobalConstants.UNITS, GlobalConstants.KEY)
                .enqueue(new Callback<WeatherForecast>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherForecast> call, @NonNull Response<WeatherForecast> response) {
                        weatherForecastLiveData.postValue(response.body());
                        System.out.println("hasActiveObservers forecast:" + weatherForecastLiveData.hasActiveObservers());
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherForecast> call, @NonNull Throwable t) {
                        System.out.println("onFailure forecast");
                    }
                });
    }

    private void loadTodayData() {
        System.out.println("111:" + GlobalConstants.CURRENT_CITY_EN);
        RestClient.getService().getToday(GlobalConstants.CURRENT_CITY_EN, GlobalConstants.UNITS, GlobalConstants.KEY)
                .enqueue(new Callback<WeatherDay>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherDay> call, @NonNull Response<WeatherDay> response) {
                        weatherDayLiveData.postValue(response.body());
                        System.out.println("hasActiveObservers:" + weatherDayLiveData.hasActiveObservers());
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherDay> call, @NonNull Throwable t) {
                        System.out.println("onFailure!");
                    }
                });
    }
}
