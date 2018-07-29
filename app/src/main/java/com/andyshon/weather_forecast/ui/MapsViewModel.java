package com.andyshon.weather_forecast.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.db.RestClient;
import com.andyshon.weather_forecast.db.entity.WeatherToday;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by andyshon on 29.07.18.
 */

public class MapsViewModel extends ViewModel {
    private MutableLiveData<WeatherToday> weatherDayByCoordLiveData;
    private MutableLiveData<WeatherToday> weatherDayByNameLiveData;


    public LiveData<WeatherToday> getTodayDataByCoordinates(double lat, double lon) {
        weatherDayByCoordLiveData = new MutableLiveData<>();
        loadTodayDataByCoordinates(lat, lon);
        return weatherDayByCoordLiveData;
    }


    public LiveData<WeatherToday> getTodayDataByName(String name) {
        weatherDayByNameLiveData = new MutableLiveData<>();
        loadTodayDataByCityName(name);
        return weatherDayByNameLiveData;
    }


    private void loadTodayDataByCoordinates(double lat, double lon) {
        RestClient.getService().getTodayByCoordinates(lat, lon, GlobalConstants.ApiConstants.UNITS, GlobalConstants.ApiConstants.KEY)
                .enqueue(new Callback<WeatherToday>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherToday> call, @NonNull Response<WeatherToday> response) {
                        weatherDayByCoordLiveData.postValue(response.body());
                        System.out.println("hasActiveObservers:" + weatherDayByCoordLiveData.hasActiveObservers());
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherToday> call, @NonNull Throwable t) {
                        System.out.println("onFailure!");
                    }
                });
    }


    private void loadTodayDataByCityName(String name) {
        RestClient.getService().getTodayByCityName(name, GlobalConstants.ApiConstants.UNITS, GlobalConstants.ApiConstants.KEY)
                .enqueue(new Callback<WeatherToday>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherToday> call, @NonNull Response<WeatherToday> response) {
                        weatherDayByNameLiveData.postValue(response.body());
                        System.out.println("hasActiveObservers:" + weatherDayByNameLiveData.hasActiveObservers());
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherToday> call, @NonNull Throwable t) {
                        System.out.println("onFailure!");
                    }
                });
    }
}
