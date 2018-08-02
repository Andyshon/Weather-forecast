package com.andyshon.weather_forecast.ui.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.databinding.ActivityFavouritesBinding;
import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast;
import com.andyshon.weather_forecast.ui.adapter.FavouritesAdapter;
import com.andyshon.weather_forecast.ui.viewmodel.FavouritesViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity implements FavouritesAdapter.FavouriteClickCallback {

    private static final String TAG = "FavouritesActivity";
    private FavouritesViewModel viewModel;
    private ActivityFavouritesBinding mBinding;
    private FavouritesAdapter mBookAdapter;


    private LiveData<List<WeatherTodayForecast>> weatherTodayLiveData;


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
    }

    public void moveback(View view) {
        setResult(RESULT_CANCELED, new Intent());
        finish();
    }


    private void subscribeUi(FavouritesViewModel viewModel) {

        // get favourites from Room
        viewModel.getFavouritesLocal().observe(this, myFavourites -> {

            List<String> favouritesNames = new ArrayList<>();
            List<String> favouritesIds = new ArrayList<>();

            if (myFavourites != null) {
                if (myFavourites.size() != 0) {

                    for (WeatherTodayForecast w2 : myFavourites) {
                        favouritesNames.add(w2.getItems_city().getCityName());
                        favouritesIds.add(String.valueOf(w2.getId()));
                    }

                /*
                * display favourites locations from server
                * */
                    if (GlobalConstants.isNetworkAvailable(FavouritesActivity.this)) {
                        Log.d(TAG, "Have internet -> fetch favourites from server");
                        getFavouritesServer(favouritesNames, favouritesIds, myFavourites);
                        //mBookAdapter.setFavouritesList(myFavourites);
                    }
                /*
                * display favourites locations from local db
                * */
                    else {
                        Log.d(TAG, "Don't have internet -> fetch favourites from room");
                        hideProgressBar();
                        mBookAdapter.setFavouritesList(myFavourites);
                    }
                }
                else {
                    hideProgressBar();
                }
            }
            else { // this is for displaying progressbar
                showProgressBar();
            }
            mBinding.executePendingBindings();
        });

    }


    private void getFavouritesServer(List<String> list, List<String> ids, List<WeatherTodayForecast> myFavourites) {

        if (list.size() == 0) {
            hideProgressBar();
            return;
        }
        weatherTodayLiveData = viewModel.getFavouritesServer(list);
        weatherTodayLiveData.observe(this, weatherTodays -> {
            for (int i=0; i<weatherTodays.size(); i++) {
                weatherTodays.get(i).setId(Integer.parseInt(ids.get(i)));
            }
            //mBookAdapter.setFavouritesList(weatherTodays);
            hideProgressBar();
        });
        mBookAdapter.setFavouritesList(myFavourites);
    }


    @Override
    public void onClick(WeatherTodayForecast weatherToday) {
        GlobalConstants.setCurrentCityEN(weatherToday.getItems_city().getCityName()); // for toolbar title
        GlobalConstants.setCurrentCityRU(weatherToday.getItems_city().getCityName()); // for toolbar title
        Intent intent = new Intent();
        intent.putExtra("city", weatherToday.getItems_city().getCityName());
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onLongClick(WeatherTodayForecast weatherToday) {
        showConfirmDialog(weatherToday);
    }


    private void showConfirmDialog(final WeatherTodayForecast weatherToday) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.delete);
        alertDialogBuilder.setMessage(getString(R.string.confirmQuestion) + weatherToday.getItems_city().getCityName() + "?");
        alertDialogBuilder.setPositiveButton(R.string.yes,
                (arg0, arg1) -> {
                    Handler handler = new Handler();
                    Thread thread = new Thread(() -> {
                        List<WeatherTodayForecast> ll = weatherTodayLiveData.getValue();
                        for (int i=0; i<ll.size(); i++) {
                            if (ll.get(i).getId() == weatherToday.getId()) {
                                ll.remove(i);
                            }
                        }
                        viewModel.deleteFromFavourite(weatherToday);
                        handler.post(() -> {
                            Toast.makeText(this, weatherToday.getItems_city().getCityName() + " was deleted!", Toast.LENGTH_SHORT).show();
                        });
                    }); thread.start();
                });

        alertDialogBuilder.setNegativeButton(R.string.no, (dialog, which) -> Toast.makeText(this, R.string.cancel, Toast.LENGTH_SHORT).show());

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
