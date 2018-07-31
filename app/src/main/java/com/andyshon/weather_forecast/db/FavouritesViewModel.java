package com.andyshon.weather_forecast.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.db.entity.WeatherToday;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by andyshon on 30.07.18.
 */

public class FavouritesViewModel extends AndroidViewModel {
    private final MediatorLiveData<List<WeatherToday>> mObservableFavouritesLocal;
    private List<WeatherToday> list = new ArrayList<>();
    private DataRepository dataRepository;

    private MutableLiveData<List<WeatherToday>> listLiveData;
    private MutableLiveData<WeatherToday> listLiveDataSingle;


    public FavouritesViewModel(Application application) {
        super(application);

        mObservableFavouritesLocal = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableFavouritesLocal.setValue(null);

        dataRepository = ((BasicApp) application).getRepository();

        LiveData<List<WeatherToday>> favourites = dataRepository.getFavourites();

        // observe the changes of the books from the database and forward them
        mObservableFavouritesLocal.addSource(favourites, new Observer<List<WeatherToday>>() {
            @Override
            public void onChanged(@Nullable List<WeatherToday> bookEntities) {
                mObservableFavouritesLocal.setValue(bookEntities);
            }
        });
    }

    /**
     * Expose the LiveData Favourites query so the UI can observe it
     */
    public LiveData<List<WeatherToday>> getFavouritesLocal() {
        //loadForecastDataByCityName();
        return mObservableFavouritesLocal;
    }


    public void deleteFromFavourite(WeatherToday weatherToday) {
        dataRepository.deleteFromFavourite(weatherToday);
    }


    public void addToFavourite(WeatherToday favouriteEntity) {
        Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                dataRepository.addToFavourite(favouriteEntity);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("POST ADD :)");
                    }
                });
            }
        });
        thread.start();
    }


    public LiveData<List<WeatherToday>> getFavouritesServer(List<String> mFavouritesList) {
        if (listLiveData == null) {
            listLiveData = new MutableLiveData<>();
            loadListByCityName(mFavouritesList);
        }
        return listLiveData;
    }

    public void loadListByCityName(List<String> mFavouritesList) {
        for (int i=0; i<mFavouritesList.size(); i++) {
            int finalI = i;
            RestClient.getService().getTodayByCityName(mFavouritesList.get(i), GlobalConstants.ApiConstants.UNITS, GlobalConstants.ApiConstants.KEY)
                    .enqueue(new Callback<WeatherToday>() {
                        @Override
                        public void onResponse(Call<WeatherToday> call, Response<WeatherToday> response) {
                            list.add(response.body());
                            if (finalI == mFavouritesList.size()-1) {
                                System.out.println("end of list");
                                listLiveData.postValue(list);
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherToday> call, Throwable t) {
                            System.out.println("FavouritesViewModel onFailure !!!");
                        }
                    });
        }
    }


    public LiveData<WeatherToday> getCityByName(String mFavouritesList) {
        if (listLiveDataSingle == null) {
            listLiveDataSingle = new MutableLiveData<>();
            loadListByCityName(mFavouritesList);
        }
        return listLiveDataSingle;
    }

    public void loadListByCityName(String mFavouritesList) {
        //for (int i=0; i<mFavouritesList.size(); i++) {
            //int finalI = i;
            RestClient.getService().getTodayByCityName(mFavouritesList, GlobalConstants.ApiConstants.UNITS, GlobalConstants.ApiConstants.KEY)
                    .enqueue(new Callback<WeatherToday>() {
                        @Override
                        public void onResponse(Call<WeatherToday> call, Response<WeatherToday> response) {
                            //list.add(response.body());
                            //if (finalI == mFavouritesList.size()-1) {
                                System.out.println("end of single name");
                            listLiveDataSingle.postValue(response.body());
                            //}
                        }

                        @Override
                        public void onFailure(Call<WeatherToday> call, Throwable t) {
                            System.out.println("FavouritesViewModel onFailure !!!");
                        }
                    });
        //}
    }
}
