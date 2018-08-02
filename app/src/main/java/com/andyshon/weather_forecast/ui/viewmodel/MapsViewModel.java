package com.andyshon.weather_forecast.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.andyshon.weather_forecast.data.BasicApp;
import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast;
import com.andyshon.weather_forecast.data.local.DataRepository;
import com.andyshon.weather_forecast.data.remote.RestClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.andyshon.weather_forecast.GlobalConstants.ApiConstants.*;

/**
 * Created by andyshon on 29.07.18.
 */

public class MapsViewModel extends AndroidViewModel {

    private MutableLiveData<WeatherTodayForecast> weatherDayByCoordLiveData;
    private MutableLiveData<WeatherTodayForecast> weatherDayByNameLiveData;

    private DataRepository dataRepository;

    public MapsViewModel(@NonNull Application application) {
        super(application);

        dataRepository = ((BasicApp) application).getRepository();
    }


    public LiveData<WeatherTodayForecast> getTodayDataByCoordinates(double lat, double lon) {
        weatherDayByCoordLiveData = new MutableLiveData<>();
        loadTodayDataByCoordinates(lat, lon);
        return weatherDayByCoordLiveData;
    }


    public LiveData<WeatherTodayForecast> getTodayDataByName(String name) {
        weatherDayByNameLiveData = new MutableLiveData<>();
        loadTodayDataByCityName(name);
        return weatherDayByNameLiveData;
    }


    public void addToFavourite(WeatherTodayForecast favourite) {
        dataRepository.addToFavourite(favourite);
    }


    private void loadTodayDataByCoordinates(double lat, double lon) {
       RestClient.getService().getForecastByCoordinates(lat, lon, 2, UNITS, KEY)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(weatherForecastToday_list -> weatherDayByCoordLiveData.postValue(weatherForecastToday_list),
                       throwable -> Toast.makeText(getApplication(), "Error while loading city by coordinates, try again", Toast.LENGTH_SHORT).show());
    }


    private void loadTodayDataByCityName(String name) {
        RestClient.getService().getTodayForecastByCityName(name, UNITS, KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherForecastToday_list -> weatherDayByNameLiveData.postValue(weatherForecastToday_list),
                        throwable -> Toast.makeText(getApplication(), "Cannot find city, please enter valid city name", Toast.LENGTH_SHORT).show());
    }
}
