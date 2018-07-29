package com.andyshon.weather_forecast.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.R;

import com.andyshon.weather_forecast.db.entity.WeatherToday;
import com.andyshon.weather_forecast.utils.WeatherUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class MapsActivity extends AppCompatActivity {

    private MapyFragment mapyFragment;
    private ListView listView;
    private String textQuery;

    private View bottomSheet;
    private BottomSheetBehavior sheetBehavior;

    private TextView tvCityName, tvDate, tvTempCur, tvTempFull;
    private ImageView ivWeatherState, ic_searchLoop, ic_back;

    private LiveData<WeatherToday> weatherByCoordinatesLiveData;
    private LiveData<WeatherToday> weatherByNameLiveData;
    private MapsViewModel mapsViewModel;

    private SearchView search;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        mapyFragment = new MapyFragment();

        //setMapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tr = fragmentManager.beginTransaction();
        tr.replace(R.id.container, mapyFragment, "s").commit();


        mapsViewModel = ViewModelProviders.of(this).get(MapsViewModel.class);

        tvCityName = findViewById(R.id.tvCityName);
        tvDate = findViewById(R.id.tvDate);
        tvTempCur = findViewById(R.id.tvTempCur);
        tvTempFull = findViewById(R.id.tvTempFull);
        ivWeatherState = findViewById(R.id.ivWeatherState);
        ic_searchLoop = findViewById(R.id.ic_searchLoop);
        ic_back = findViewById(R.id.ic_back);


        bottomSheet = findViewById(R.id.bottom_sheet);

        sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        /**
         * bottom sheet state change listener
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        final MapsListAdapter adapter = new MapsListAdapter(MapsActivity.this, GlobalConstants.getCitiesRuList());
        listView = findViewById(R.id.list_view);
        listView.setVisibility(View.GONE);
        bottomSheet.setVisibility(View.GONE);

        ic_searchLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findCityByName(textQuery);
            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
        });

        search = findViewById(R.id.search);
        search.onActionViewExpanded();
        search.clearFocus();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                textQuery = newText;
                adapter.getFilter().filter(newText);
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(adapter);

                return false;
            }
        });
    }


    public void setMapFragment() {
        search.clearFocus();
        listView.setVisibility(View.GONE);
        FrameLayout frameLayout = findViewById(R.id.container);
        frameLayout.setVisibility(View.VISIBLE);
    }


    public void findCityByName(String name) {
        search.setQuery(name, false);
        search.clearFocus();
        listView.setVisibility(View.GONE);
        bottomSheet.setVisibility(View.VISIBLE);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mapyFragment.findCityByName(name);

        weatherByNameLiveData = mapsViewModel.getTodayDataByName(WeatherUtils.fromRUtoEN(name));
        weatherByNameLiveData.observe(this, new Observer<WeatherToday>() {
            @Override
            public void onChanged(@Nullable WeatherToday weatherDay) {
                tvCityName.setText(name);
                System.out.println("SSS:" + weatherDay + ":" + weatherDay.getDt());
                tvDate.setText(WeatherUtils.getDateTitle(weatherDay.getDt()));
                System.out.println("TVDATE:" + tvDate.getText().toString());
                ivWeatherState.setImageResource(WeatherUtils.getIconByWeatherState(weatherDay.getDescription().description, false));
                tvTempFull.setText(weatherDay.getTempMaxInteger().concat("˚/").concat(weatherDay.getTempMinInteger()).concat("˚"));
                tvTempCur.setText(weatherDay.getTempInteger());
                System.out.println("SSS wind:" + weatherDay.getSpeed() + ":" +weatherDay.getDeg());
            }
        });
    }


    /*
    * Получаем координаты выбранного места на карте -> передаем их во viewModel -> подписываемся через LiveData -> observe -> update bottomSheet ui
    * */

    public void findCityByCoord(LatLng latLng) {
        listView.setVisibility(View.GONE);
        bottomSheet.setVisibility(View.VISIBLE);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        weatherByCoordinatesLiveData = mapsViewModel.getTodayDataByCoordinates(latLng.latitude, latLng.longitude);
        weatherByCoordinatesLiveData.observe(this, new Observer<WeatherToday>() {
            @Override
            public void onChanged(@Nullable WeatherToday weatherDay_coord) {
                System.out.println("COORD - city:" + weatherDay_coord.getCity());
                System.out.println("COORD -tempMax:" + weatherDay_coord.getTempMaxInteger());
                System.out.println("COORD -tempMin:" + weatherDay_coord.getTempMinInteger());
                System.out.println("COORD -tempCur:" + weatherDay_coord.getTempInteger());
                System.out.println("COORD -descr:" + weatherDay_coord.getDescription().description);
                System.out.println("COORD -descr:" + new Date(weatherDay_coord.getDt()).getHours());


                if (weatherDay_coord.getCity().equals(""))
                    tvCityName.setText("Неопознанный город");
                else {
                    tvCityName.setText(weatherDay_coord.getCity());
                    //search.setQuery(weatherDay_coord.getCity(), false);
                    //search.clearFocus();
                    //listView.setVisibility(View.GONE);
                }
                tvDate.setText(WeatherUtils.getDateTitle(weatherDay_coord.getDt()));
                tvTempCur.setText(weatherDay_coord.getTempInteger());
                tvTempFull.setText(weatherDay_coord.getTempMaxInteger().concat("˚/").concat(weatherDay_coord.getTempMinInteger()).concat("˚"));
                ivWeatherState.setImageResource(WeatherUtils.getIconByWeatherState(weatherDay_coord.getDescription().description, false));
            }
        });
    }
}
