package com.andyshon.weather_forecast.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.andyshon.weather_forecast.db.entity.WeatherToday;

import java.util.List;

/**
 * Created by andyshon on 30.07.18.
 */

public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private MediatorLiveData<List<WeatherToday>> mObservableFavourites;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;
        mObservableFavourites = new MediatorLiveData<>();

        mObservableFavourites.addSource(mDatabase.favouritesDao().loadAllFavourites(),
                items -> {
                    mObservableFavourites.postValue(items);
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of favourites from the database and get notified when the data changes
     */
    public LiveData<List<WeatherToday>> getFavourites() {
        return mObservableFavourites;
    }

    public void addToFavourite(WeatherToday favouriteEntity) {
        mDatabase.favouritesDao().insertFavourite(favouriteEntity);
    }

    public void deleteFromFavourite(WeatherToday favouriteEntity) {
        mDatabase.favouritesDao().deleteFavourite(favouriteEntity);
    }
}
