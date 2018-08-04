package com.andyshon.weather_forecast.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.data.BasicApp;
import com.andyshon.weather_forecast.data.entity.weather_today.WeatherToday;
import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast;
import com.andyshon.weather_forecast.data.local.DataRepository;
import com.andyshon.weather_forecast.data.remote.RestClient;
import com.andyshon.weather_forecast.data.entity.weather_today_hour_forecast.WeatherTodayHourForecast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.andyshon.weather_forecast.GlobalConstants.ApiConstants.*;

/**
 * Created by andyshon on 27.07.18.
 */

public class WeatherViewModel extends AndroidViewModel {

    private MutableLiveData<WeatherTodayForecast> weatherForecastLiveData;
    private MutableLiveData<WeatherTodayHourForecast> weatherDayHourForecastLiveData;
    private MutableLiveData<WeatherTodayHourForecast> weatherDayHourForecastByDate;
    private MutableLiveData<WeatherToday> weatherNowWidgetLiveData;

    private DataRepository dataRepository;

    public WeatherViewModel(@NonNull Application application) {
        super(application);

        dataRepository = ((BasicApp) application).getRepository();
    }


    public LiveData<WeatherTodayForecast> getForecastData() {
        if (weatherForecastLiveData == null) {
            weatherForecastLiveData = new MutableLiveData<>();
        }
        loadForecastDataByCityName();
        return weatherForecastLiveData;
    }


    public LiveData<WeatherTodayHourForecast> getHourForecastData() {
        if (weatherDayHourForecastLiveData == null) {
            weatherDayHourForecastLiveData = new MutableLiveData<>();
        }
        loadHourForecastDataByCityName();
        return weatherDayHourForecastLiveData;
    }


    public LiveData<WeatherTodayHourForecast> getHourForecastDataByDate(String date, boolean isToday) {
        if (weatherDayHourForecastByDate == null) {
            weatherDayHourForecastByDate = new MutableLiveData<>();
        }
        loadHourForecastDataByCityNameAndDate(date, isToday);
        return weatherDayHourForecastByDate;
    }


    public LiveData<WeatherToday> getWeatherNowWidget() {
        if (weatherNowWidgetLiveData == null) {
            weatherNowWidgetLiveData = new MutableLiveData<>();
        }
        loadWeatherTodayNowWidgetData();
        return weatherNowWidgetLiveData;
    }


    public void addCurLocToFav(String name) {
        RestClient.getService().getTodayForecastByCityName(name, UNITS, KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favourite -> {
                    Thread thread = new Thread(() -> dataRepository.addToFavourite(favourite));
                    thread.start();
                }, throwable -> Toast.makeText(getApplication(), "Error while adding to favourites", Toast.LENGTH_SHORT).show());
    }


    private void loadHourForecastDataByCityName() {
        RestClient.getService().getHourForecastByCityName(GlobalConstants.CURRENT_CITY_EN, 9, UNITS, KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> weatherDayHourForecastLiveData.postValue(weather),
                        throwable -> Toast.makeText(getApplication(), "Error while loading hour forecast by city", Toast.LENGTH_SHORT).show());
    }


    private void loadHourForecastDataByCityNameAndDate(String date, boolean isToday) {
        RestClient.getService().getHourForecastByCityNameAndDate(GlobalConstants.CURRENT_CITY_EN, UNITS, KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(weatherTodayHourForecast -> {
                    if (isToday) {
                        for (int i=8; i<weatherTodayHourForecast.getItems().size(); i++) {
                            weatherTodayHourForecast.getItems().remove(i);
                            --i;
                        }
                    }
                    else {
                        for (int i = 0; i < weatherTodayHourForecast.getItems().size(); i++) {
                            if (!weatherTodayHourForecast.getItems().get(i).getDt_txt().contains(date)) {
                                weatherTodayHourForecast.getItems().remove(i);
                                --i;
                            }
                        }
                    }
                    return true;
                })
                .subscribe(weather -> weatherDayHourForecastByDate.postValue(weather),
                        throwable -> Toast.makeText(getApplication(), "Error while loading hour forecast by city2", Toast.LENGTH_SHORT).show());
    }


    private void loadForecastDataByCityName() {
        RestClient.getService().getForecastByCityName(GlobalConstants.CURRENT_CITY_EN, 5, UNITS, KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> weatherForecastLiveData.postValue(weather),
                        throwable -> Toast.makeText(getApplication(), "Error while loading forecast by city", Toast.LENGTH_SHORT).show());
    }


    private void loadWeatherTodayNowWidgetData() {
        RestClient.getService().getTodayByCityName(GlobalConstants.CURRENT_CITY_EN, UNITS, KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherToday -> weatherNowWidgetLiveData.postValue(weatherToday),
                        throwable -> Log.d("Widget", "Error while refreshing widget"));
    }
}
