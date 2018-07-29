package com.andyshon.weather_forecast.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.db.RestClient;
import com.andyshon.weather_forecast.db.entity.WeatherDayHourForecastList;
import com.andyshon.weather_forecast.db.entity.WeatherForecast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by andyshon on 27.07.18.
 */

public class WeatherViewModel extends ViewModel {

    private MutableLiveData<WeatherForecast> weatherForecastLiveData;
    private MutableLiveData<WeatherDayHourForecastList> weatherDayHourForecastLiveData;


    public LiveData<WeatherForecast> getForecastData() {
        if (GlobalConstants.CURRENT_CITY_EN == null)
            weatherForecastLiveData = new MutableLiveData<>();
        else
            loadForecastDataByCityName();
        return weatherForecastLiveData;
    }

    public LiveData<WeatherDayHourForecastList> getHourForecastData() {
        if (GlobalConstants.CURRENT_CITY_EN == null)
            weatherDayHourForecastLiveData = new MutableLiveData<>();
        else
            loadHourForecastDataByCityName();
        return weatherDayHourForecastLiveData;
    }

    private void loadHourForecastDataByCityName() {
        RestClient.getService().getHourForecastByCityName(GlobalConstants.CURRENT_CITY_EN, 9, GlobalConstants.ApiConstants.UNITS, GlobalConstants.ApiConstants.KEY)
                .enqueue(new Callback<WeatherDayHourForecastList>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherDayHourForecastList> call, @NonNull Response<WeatherDayHourForecastList> response) {
                        weatherDayHourForecastLiveData.postValue(response.body());
                        System.out.println("hasActiveObservers hour forecast:" + weatherDayHourForecastLiveData.hasActiveObservers());
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherDayHourForecastList> call, @NonNull Throwable t) {
                        System.out.println("onFailure hour forecast");
                    }
                });
    }

    private void loadForecastDataByCityName() {
        RestClient.getService().getForecastByCityName(GlobalConstants.CURRENT_CITY_EN, 5, GlobalConstants.ApiConstants.UNITS, GlobalConstants.ApiConstants.KEY)
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
}
