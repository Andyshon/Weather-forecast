package com.andyshon.weather_forecast.ui.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.R;

import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast;
import com.andyshon.weather_forecast.ui.adapter.MapsListAdapter;
import com.andyshon.weather_forecast.ui.viewmodel.MapsViewModel;
import com.andyshon.weather_forecast.utils.WeatherUtils;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends AppCompatActivity implements MapsListAdapter.MapsCallback {

    private MapyFragment mapyFragment;
    private ListView listView;
    private String textQuery;

    private View bottomSheet;
    private BottomSheetBehavior sheetBehavior;

    private TextView tvCityName, tvDate, tvTempCur, tvTempFull;
    private ImageView ivWeatherState, ivSearchLoop, ivBack;
    private Button btnAddToFavourites;

    private MapsViewModel mapsViewModel;

    private SearchView search;

    private WeatherTodayForecast weatherToday;

    private MapsListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        initUI();
    }


    /*
    * init UI components
    * */
    private void initUI() {
        mapyFragment = new MapyFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tr = fragmentManager.beginTransaction();
        tr.replace(R.id.container, mapyFragment, "mapyFragment").commit();


        mapsViewModel = ViewModelProviders.of(this).get(MapsViewModel.class);

        tvCityName = findViewById(R.id.tvCityName);
        tvDate = findViewById(R.id.tvDate);
        tvTempCur = findViewById(R.id.tvTempCur);
        tvTempFull = findViewById(R.id.tvTempFull);
        ivWeatherState = findViewById(R.id.ivWeatherState);
        ivSearchLoop = findViewById(R.id.ic_searchLoop);
        ivBack = findViewById(R.id.ic_back);
        btnAddToFavourites = findViewById(R.id.btnAddToFavourites);
        btnAddToFavourites.setOnClickListener(addListener);


        bottomSheet = findViewById(R.id.bottom_sheet);
        if (bottomSheet != null)
            sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        sheetBehavior.setBottomSheetCallback(bottomSheetCallback);


        adapter = new MapsListAdapter(MapsActivity.this, GlobalConstants.getCitiesRuList(), this);
        listView = findViewById(R.id.list_view);
        listView.setVisibility(View.GONE);
        if (bottomSheet != null)
            bottomSheet.setVisibility(View.GONE);

        ivSearchLoop.setOnClickListener(view -> {
            if (textQuery != null && !textQuery.isEmpty())
                findCityByName(textQuery);
        });

        ivBack.setOnClickListener(view -> {
            setResult(RESULT_CANCELED, new Intent());
            finish();
        });

        search = findViewById(R.id.search);
        search.onActionViewExpanded();
        search.clearFocus();
        search.setOnQueryTextListener(queryTextListener);
    }


    /*
    * no text in searchView -> hide listView, show map fragment
    * */
    @Override
    public void onSetMapFragment() {
        listView.setVisibility(View.GONE);
        FrameLayout frameLayout = findViewById(R.id.container);
        frameLayout.setVisibility(View.VISIBLE);
    }


    public void findCityByName(String n) {
        search.setQuery(n, false);
        search.clearFocus();
        listView.setVisibility(View.GONE);

        String name = n.trim();

        /*
        * WeatherUtils.fromRUtoEN() -- no need since OpenWeatherApi can find city from russian name, but sometimes isn't understand all the provided names :(
        * */
        LiveData<WeatherTodayForecast> weatherByNameLiveData = mapsViewModel.getTodayDataByName(name);
        weatherByNameLiveData.observe(this, weatherDay -> {

            weatherToday = weatherDay;

            listView.setVisibility(View.GONE);

            if (weatherToday == null) {
                Toast.makeText(MapsActivity.this, "Не удалось найти " + name + ".\nВведите название на латинице", Toast.LENGTH_LONG).show();
                return;
            }

            if (bottomSheet != null)
                bottomSheet.setVisibility(View.VISIBLE);
            if (sheetBehavior != null)
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            mapyFragment.findCityByName(name);

            tvCityName.setText(name);
            setBottomSheetUI();
        });
    }

    /*
    * Получаем координаты выбранного места на карте -> передаем их во viewModel -> подписываемся через LiveData -> observe -> update bottomSheet ui
    * */

    public void findCityByCoord(LatLng latLng) {
        search.clearFocus();
        listView.setVisibility(View.GONE);
        if (bottomSheet != null)
            bottomSheet.setVisibility(View.VISIBLE);
        if (sheetBehavior != null)
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        LiveData<WeatherTodayForecast> weatherByCoordinatesLiveData = mapsViewModel.getTodayDataByCoordinates(latLng.latitude, latLng.longitude);
        weatherByCoordinatesLiveData.observe(this, weatherDay_coord -> {

            weatherToday = weatherDay_coord;

            if (weatherToday.getItems_city().getCityName().equals(""))
                tvCityName.setText(R.string.LocalityUnidentified);
            else {
                tvCityName.setText(weatherToday.getItems_city().getCityName());
            }

            setBottomSheetUI();
        });
    }


    private void setBottomSheetUI() {
        tvDate.setText(WeatherUtils.getDateTitle(weatherToday.getItems().get(0).getDt()));
        tvTempFull.setText(String.valueOf(weatherToday.getItems().get(0).getTempMax()).concat("˚/")
                .concat(String.valueOf(weatherToday.getItems().get(0).getTempMin())).concat("˚"));
        tvTempCur.setText(String.valueOf(weatherToday.getItems().get(0).getTempDay()));
        ivWeatherState.setImageResource(WeatherUtils.getIconByWeatherState(weatherToday.getItems().get(0).getDescription().description, false));
    }


    /**
     * add to favourites button click listener
     * */
    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (weatherToday != null) {
                Handler handler = new Handler();
                Thread thread = new Thread(() -> {

                    mapsViewModel.addToFavourite(weatherToday);

                    handler.post(() -> {
                        GlobalConstants.CURRENT_CITY_EN = weatherToday.getItems_city().getCityName();
                        GlobalConstants.CURRENT_CITY_RU = weatherToday.getItems_city().getCityName();
                        Intent intent = new Intent();
                        intent.putExtra("city", weatherToday.getItems_city().getCityName());
                        setResult(RESULT_OK, intent);
                        finish();
                    });
                });
                thread.start();
            }
            else {
                Toast.makeText(MapsActivity.this, R.string.favouritesAddFailed, Toast.LENGTH_SHORT).show();
            }
        }
    };


    /**
     * bottom sheet state change listener
     * */
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            switch (newState) {
                case BottomSheetBehavior.STATE_HIDDEN:break;
                case BottomSheetBehavior.STATE_EXPANDED:break;
                case BottomSheetBehavior.STATE_COLLAPSED:
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    break;
                case BottomSheetBehavior.STATE_DRAGGING:
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    break;
                case BottomSheetBehavior.STATE_SETTLING:break;
            }
        }
        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
    };


    /**
     * searchView query text listener
     * */
    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            search.clearFocus();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {

            if (sheetBehavior != null)
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            textQuery = newText;
            if (newText.isEmpty()) {
                listView.setVisibility(View.GONE);
            }
            else {
                listView.setVisibility(View.VISIBLE);
            }
            adapter.getFilter().filter(newText);
            listView.setAdapter(adapter);

            return false;
        }
    };
}
