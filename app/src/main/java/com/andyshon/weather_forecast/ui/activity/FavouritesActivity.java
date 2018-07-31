package com.andyshon.weather_forecast.ui.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.widget.Toast;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.databinding.ActivityFavouritesBinding;
import com.andyshon.weather_forecast.db.FavouritesAdapter;
import com.andyshon.weather_forecast.db.FavouritesViewModel;
import com.andyshon.weather_forecast.db.entity.WeatherToday;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity implements FavouritesAdapter.FavouriteClickCallback {

    private FavouritesViewModel viewModel;
    private ActivityFavouritesBinding mBinding;
    private FavouritesAdapter mBookAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_favourites);

        mBookAdapter = new FavouritesAdapter(this);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mBinding.favouritesList.addItemDecoration(itemDecor);
        mBinding.favouritesList.setAdapter(mBookAdapter);

        viewModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);
        subscribeUi(viewModel);

        getFavouritesServerByName(GlobalConstants.CURRENT_LOCATION_CITY_EN);
    }


    private void subscribeUi(FavouritesViewModel viewModel) {
        // in purpose to get List of cities names -> get Favourites from local db and then ckeck the internet connection
        viewModel.getFavouritesLocal().observe(this, new Observer<List<WeatherToday>>() {
            @Override
            public void onChanged(@Nullable List<WeatherToday> myFavourites) {

                List<String> favouritesNames = new ArrayList<>();

                System.out.println("myFavourites obj:" + myFavourites);
                if (myFavourites != null) {
                    System.out.println("size:" + myFavourites.size());
                    for (WeatherToday f : myFavourites) {
                        favouritesNames.add(f.getCity());
                        System.out.println("id:"+f.getId()+"temp:" + f.getTemp().getTemp_max() + ":" + f.getTemp().getTemp_min()
                                + ":"+ f.getCity() + ":" + f.getWind().getSpeed());
                    }

                    if (GlobalConstants.isNetworkAvailable(FavouritesActivity.this)) {
                        System.out.println("Have internet -> fetch favourites from server");
                        getFavouritesServer(favouritesNames);
                    }
                    else {
                        System.out.println("Don't have internet -> fetch favourites from room");
                        hideProgressBar();
                        System.out.println("weatherTodays size = " + myFavourites.size());
                        mBookAdapter.setFavouritesList(myFavourites);
                    }

                } else { // this is for displaying progressbar
                    showProgressBar();
                }
                mBinding.executePendingBindings();
            }
        });
    }


    private void getFavouritesServer(List<String> list) {
        System.out.println("SIZE:" + list.size());

        if (list.size() == 0) {
            hideProgressBar();
            return;
        }
        LiveData<List<WeatherToday>> weatherTodayLiveData;
        weatherTodayLiveData = viewModel.getFavouritesServer(list);
        weatherTodayLiveData.observe(this, new Observer<List<WeatherToday>>() {
            @Override
            public void onChanged(@Nullable List<WeatherToday> weatherTodays) {
                System.out.println("weatherTodays size = " + weatherTodays.size());
                for (WeatherToday weatherToday : weatherTodays) {
                    System.out.println("TEMMMP:" + weatherToday.getTemp().getTemp() + ":" + weatherToday.getTemp().getTemp_max() + ":"+  weatherToday.getTemp().getTemp_min());
                }
                mBookAdapter.setFavouritesList(weatherTodays);
                hideProgressBar();
            }
        });
    }


    private void getFavouritesServerByName(String name) {

        if (name == null || name.length() == 0) {
            hideProgressBar();
            return;
        }
        LiveData<WeatherToday> weatherTodayLiveData;
        weatherTodayLiveData = viewModel.getCityByName(name);
        weatherTodayLiveData.observe(this, new Observer<WeatherToday>() {
            @Override
            public void onChanged(@Nullable WeatherToday weatherTodays) {
                System.out.println("weatherTodays single = " + weatherTodays.getCity());
                hideProgressBar();
                viewModel.addToFavourite(weatherTodays);
                System.out.println("PRE ADD");
            }
        });
    }


    @Override
    public void onClick(WeatherToday weatherToday) {
        GlobalConstants.setCurrentCityEN(weatherToday.getCity()); // for toolbar title
        GlobalConstants.setCurrentCityRU(weatherToday.getCity()); // for toolbar title
        Intent intent = new Intent();
        intent.putExtra("city", weatherToday.getCity());
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onLongClick(WeatherToday weatherToday) {
        showConfirmDialog(weatherToday);
    }


    private void showConfirmDialog(final WeatherToday weatherToday) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setMessage("Are you sure, You wanted to delete " + weatherToday.getCity() + "?");
        alertDialogBuilder.setPositiveButton("Yes",
                (arg0, arg1) -> {
                    Handler handler = new Handler();
                    Thread thread = new Thread(() -> {
                        viewModel.deleteFromFavourite(weatherToday);
                        handler.post(() -> Toast.makeText(this, weatherToday.getCity() + " was deleted!", Toast.LENGTH_SHORT).show());
                    }); thread.start();
                });

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void showProgressBar() {
        mBinding.progressBar.setVisibility(View.VISIBLE);
    }


    private void hideProgressBar() {
        mBinding.progressBar.setVisibility(View.GONE);
    }
}
