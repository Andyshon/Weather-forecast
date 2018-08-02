package com.andyshon.weather_forecast.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.andyshon.weather_forecast.data.BasicApp;
import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast;
import com.andyshon.weather_forecast.data.remote.RestClient;
import com.andyshon.weather_forecast.data.local.DataRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.andyshon.weather_forecast.GlobalConstants.ApiConstants.*;

/**
 * Created by andyshon on 30.07.18.
 */

public class FavouritesViewModel extends AndroidViewModel {

    private MutableLiveData<List<WeatherTodayForecast>> listLiveData;

    private DataRepository dataRepository;

    public FavouritesViewModel(Application application) {
        super(application);

        dataRepository = ((BasicApp) application).getRepository();
    }


    public LiveData<List<WeatherTodayForecast>> getFavouritesLocal() {
        return dataRepository.getFavourites();
    }



    public void deleteFromFavourite(WeatherTodayForecast weatherToday) {
        dataRepository.deleteFromFavourite(weatherToday);
    }


    public LiveData<List<WeatherTodayForecast>> getFavouritesServer(List<String> mFavouritesList) {
        if (listLiveData == null) {
            listLiveData = new MutableLiveData<>();
            loadListByCityName(mFavouritesList);
        }
        return listLiveData;
    }

    /*
    * load every city from list
    * */
    public void loadListByCityName(List<String> mFavouritesList) {
        for (int i=0; i<mFavouritesList.size(); i++) {
            int finalI = i;
            List<WeatherTodayForecast> list = new ArrayList<>();
            RestClient.getService().getTodayForecastByCityName(mFavouritesList.get(i), UNITS, KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(weatherForecastToday_list -> {
                        list.add(weatherForecastToday_list);
                        if (finalI == mFavouritesList.size()-1) {
                            listLiveData.postValue(list);
                        }
                    });
        }
    }
}
