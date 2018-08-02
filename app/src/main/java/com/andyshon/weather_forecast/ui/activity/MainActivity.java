package com.andyshon.weather_forecast.ui.activity;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.data.AppDatabase;
import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast_list;
import com.andyshon.weather_forecast.service.LocationDetector;
import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.data.remote.RestClient;
import com.andyshon.weather_forecast.data.entity.weather_today_hour_forecast.WeatherTodayHourForecast_list;
import com.andyshon.weather_forecast.ui.adapter.WeatherAdapterHorizontal;
import com.andyshon.weather_forecast.ui.adapter.WeatherAdapterVertical;
import com.andyshon.weather_forecast.ui.WeatherClickCallback;
import com.andyshon.weather_forecast.ui.viewmodel.WeatherViewModel;
import com.andyshon.weather_forecast.utils.WeatherUtils;
import com.andyshon.weather_forecast.widget.MyWidget;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

public class MainActivity extends AppCompatActivity implements LocationDetector.MyCallback {

    private TextView tvTemp, tvHumidity, tvDate, tvWind, tvCityName;
    private ImageView ivWeatherState, ivChoose_city;
    private ProgressBar progressBar;

    private WeatherViewModel weatherViewModel;

    private WeatherAdapterVertical mAdapterVertical;
    private WeatherAdapterHorizontal mAdapterHorizontal;

    private RecyclerView recyclerViewH, recyclerViewV;

    private static final int REQUEST_CODE_DISPLAY_CITY = 1;

    /*
    * Any fool can write code that a computer can understand. Good programmers write code that humans can understand..
    * */

    private List<WeatherTodayForecast_list> weatherForecastList;
    private List<WeatherTodayHourForecast_list> allTodayHoursForecast;



    private final WeatherClickCallback mAdapterVClickCallback = weatherDay -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            updateTodayUI(weatherDay);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_DISPLAY_CITY) {
                String city = data.getStringExtra("city");
                tvCityName.setText(city);

                subscribeUI();
            }
            else {
                Toast.makeText(this, R.string.wrongRequestCode, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        String check = intent.getStringExtra("key");
        if (check == null || !check.equals("widget")) {
            GlobalConstants.Preferences.loadLastUserLocation(this);
        }

        RestClient.initService();
        initUI();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!GlobalConstants.IsLocationDetected) {
            if (GlobalConstants.isNetworkAvailable(this)) {
                new LocationDetector(this, this);
            }
        }
        else subscribeUI();
    }


    /*
    * init UI components
    * */
    private void initUI() {
        progressBar = findViewById(R.id.progressBar);
        showProgressbar();


        ivChoose_city = findViewById(R.id.ic_choose_city);
        ivChoose_city.setOnClickListener(view -> {
            Intent intent1 = new Intent(MainActivity.this, MapsActivity.class);
            startActivityForResult(intent1, REQUEST_CODE_DISPLAY_CITY);
        });


        tvCityName = findViewById(R.id.tvCityName);
        setTitle();

        weatherForecastList = new ArrayList<>();
        allTodayHoursForecast = new ArrayList<>();


        mAdapterVertical = new WeatherAdapterVertical(mAdapterVClickCallback);
        recyclerViewH = findViewById(R.id.recyclerView1);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewH.setLayoutManager(layoutManager1);

        mAdapterHorizontal = new WeatherAdapterHorizontal();
        recyclerViewV = findViewById(R.id.recyclerView2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewV.setLayoutManager(layoutManager2);


        tvTemp = findViewById(R.id.tvTemp);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvDate = findViewById(R.id.tvDate);
        tvWind = findViewById(R.id.tvWind);
        ivWeatherState = findViewById(R.id.ivWeatherState);

        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
    }


    private void setTitle(){
        if (GlobalConstants.CURRENT_CITY_RU.length() != 0)
            tvCityName.setText(GlobalConstants.CURRENT_CITY_RU);
        else if (GlobalConstants.CURRENT_CITY_EN.length() != 0)
            tvCityName.setText(GlobalConstants.CURRENT_CITY_EN);
        else
            tvCityName.setText(GlobalConstants.CURRENT_LOCATION_CITY_EN);
    }


    /*
    * update forecasts data
    * */
    private void subscribeUI() {
        if (!GlobalConstants.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.networkUnavailable, Toast.LENGTH_LONG).show();
            return;
        }

        showProgressbar();
        weatherViewModel.getHourForecastData().observe(this, weatherDayHourForecast -> {

            allTodayHoursForecast.clear();
            allTodayHoursForecast.addAll(weatherDayHourForecast.getItems());

            mAdapterHorizontal.setWeatherList(allTodayHoursForecast);
            recyclerViewH.setAdapter(mAdapterHorizontal);
        });


        weatherViewModel.getForecastData().observe(this, weatherForecast -> {

            weatherForecastList.clear();
            // size = 5
            weatherForecastList.addAll(weatherForecast.getItems());

            updateTodayUI(weatherForecastList.get(0));
            mAdapterVertical.setWeatherList(weatherForecastList);

            recyclerViewV.setAdapter(mAdapterVertical);
        });
    }

    private void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }


    private void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }


    /*
    * update main today UI
    * */
    private void updateTodayUI(WeatherTodayForecast_list weatherFiveDaysForecast) {
        if (weatherFiveDaysForecast != null) {

            setTitle();

            String strTemp = splitByDot(weatherFiveDaysForecast.getTempMax()).concat("˚/").concat(splitByDot(weatherFiveDaysForecast.getTempMin()));
            tvTemp.setText(strTemp);
            String strHumidity = splitByDot(weatherFiveDaysForecast.getHumidity()).concat("%");
            tvHumidity.setText(strHumidity);
            tvDate.setText(WeatherUtils.getDateTitle(weatherFiveDaysForecast.getDt()));
            String strWind = splitByDot(weatherFiveDaysForecast.getSpeed()).concat(getString(R.string.speedms));
            tvWind.setText(strWind);
            int windIcon = WeatherUtils.getIconByWindState(weatherFiveDaysForecast.getDeg());
            tvWind.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wind, 0, windIcon, 0);
            int weatherStateIcon = WeatherUtils.getIconByWeatherState(weatherFiveDaysForecast.getDescription().description, true);
            ivWeatherState.setImageResource(weatherStateIcon);

            hideProgressbar();

            updateWidget();
        }
    }


    /*
    * update Widget with current weather condition
    * */
    private void updateWidget() {
        weatherViewModel.getWeatherNowWidget()
                .observe(this, weatherToday -> {
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplication());
                    RemoteViews remoteViews = new RemoteViews(getApplication().getPackageName(), R.layout.widget);
                    ComponentName thisWidget = new ComponentName(getApplication(), MyWidget.class);
                    remoteViews.setTextViewText(R.id.tvCityName, GlobalConstants.CURRENT_CITY_EN);
                    remoteViews.setTextViewText(R.id.tvTempCur, String.valueOf(weatherToday.getTemp().getTemp().intValue()).concat("˚"));
                    remoteViews.setImageViewResource(R.id.ivWeatherState,
                            WeatherUtils.getIconByWeatherState(weatherToday.getDescription().get(0).getDescription(), true));
                    appWidgetManager.updateAppWidget(thisWidget, remoteViews);
                });
    }


    /*
    * callback is trigger when LocationDetector find current location
    * */
    @Override
    public void onGetUserLocation() {
        Toast.makeText(MainActivity.this, "Город " + GlobalConstants.CURRENT_CITY_EN, Toast.LENGTH_SHORT).show();

        GlobalConstants.Preferences.saveLastUserLocation(this);

        weatherViewModel.addCurLocToFav(GlobalConstants.CURRENT_CITY_EN);
        subscribeUI();
    }


    private String splitByDot(String string) {
        String[] splitMas = string.split("\\.");
        // since i need first element -> no need to check splitMas length
        return splitMas[0];
    }

    public void onFavourites(View view) {
        Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
        startActivityForResult(intent, REQUEST_CODE_DISPLAY_CITY);
    }
}
