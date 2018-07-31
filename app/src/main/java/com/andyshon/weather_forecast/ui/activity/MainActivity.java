package com.andyshon.weather_forecast.ui.activity;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.LocationDetector;
import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.db.RestClient;
import com.andyshon.weather_forecast.db.entity.WeatherDayHourForecast;
import com.andyshon.weather_forecast.db.entity.WeatherDayHourForecastList;
import com.andyshon.weather_forecast.db.entity.WeatherFiveDaysForecast;
import com.andyshon.weather_forecast.db.entity.WeatherForecast;
import com.andyshon.weather_forecast.ui.adapter.WeatherAdapterHorizontal;
import com.andyshon.weather_forecast.ui.adapter.WeatherAdapterVertical;
import com.andyshon.weather_forecast.ui.WeatherClickCallback;
import com.andyshon.weather_forecast.ui.viewmodel.WeatherViewModel;
import com.andyshon.weather_forecast.utils.WeatherUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LocationDetector.MyCallback {

    private String TAG = "WEATHER";
    private TextView tvTemp, tvHumidity, tvDate, tvWind, tvCityName;
    private ImageView ivWeatherState;
    private Button button;
    private ImageView ic_choose_city;
    private ProgressBar progressBar;

    private WeatherViewModel weatherViewModel;
    private LiveData<WeatherForecast> weatherForecastLiveData;
    private LiveData<WeatherDayHourForecastList> weatherDayHourForecastLiveData;

    private WeatherAdapterVertical mAdapterVertical;
    private WeatherAdapterHorizontal mAdapterHorizontal;

    private RecyclerView recyclerView, recyclerView2;

    private static final int REQUEST_CODE_DISPLAY_CITY = 1;

    /*
    * Any fool can write code that a computer can understand. Good programmers write code that humans can understand..
    * */

    private List<WeatherFiveDaysForecast> weatherForecastList;
    private List<WeatherDayHourForecast> allDaysHoursForecast;



    private final WeatherClickCallback mWeatherClickCallback = weatherDay -> {
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
                Toast.makeText(this, "Wrong requestCode :(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //deleteDatabase(AppDatabase.DATABASE_NAME);


        GlobalConstants.Preferences.loadLastUserLocation(this);
        System.out.println("loadLastUserLocation:" + GlobalConstants.CURRENT_LOCATION_CITY_EN);
        System.out.println("loadLastUserLocation:" + GlobalConstants.CURRENT_LOCATION_CITY_RU);
        System.out.println("loadLastUserLocation:" + GlobalConstants.CURRENT_CITY_EN);
        System.out.println("loadLastUserLocation:" + GlobalConstants.CURRENT_CITY_RU);



        progressBar = findViewById(R.id.progressBar);
        hideProgressbar();


        ic_choose_city = findViewById(R.id.ic_choose_city);
        ic_choose_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DISPLAY_CITY);
            }
        });

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "refresh today weather", Toast.LENGTH_SHORT).show();
                weatherDayHourForecastLiveData = weatherViewModel.getHourForecastData();
                weatherForecastLiveData = weatherViewModel.getForecastData();
            }
        });

        tvCityName = findViewById(R.id.tvCityName);
        setTitle();

        weatherForecastList = new ArrayList<>();
        allDaysHoursForecast = new ArrayList<>();


        mAdapterVertical = new WeatherAdapterVertical(mWeatherClickCallback);
        recyclerView = findViewById(R.id.recyclerView1);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager1);

        mAdapterHorizontal = new WeatherAdapterHorizontal(MainActivity.this);
        recyclerView2 = findViewById(R.id.recyclerView2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(layoutManager2);


        tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        tvDate = findViewById(R.id.tvDate);
        tvWind = findViewById(R.id.tvWind);
        ivWeatherState = (ImageView) findViewById(R.id.ivWeatherState);


        RestClient.initService();

        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);

        // for emulator turn off LocationDetector and set CURRENT_CITY_EN directly

        if (!GlobalConstants.IsLocationDetected) {
            System.out.println("Location is not detected yet");
            new LocationDetector(this, this);
        }


        subscribeUI();

    }

    private void setTitle(){
        if (GlobalConstants.CURRENT_CITY_RU != null)
            tvCityName.setText(GlobalConstants.CURRENT_CITY_RU);
        else
            tvCityName.setText(GlobalConstants.CURRENT_CITY_EN);
    }


    private void subscribeUI() {
        if (!GlobalConstants.isNetworkAvailable(this)) {
            Toast.makeText(this, "Сеть недоступна.", Toast.LENGTH_LONG).show();
            return;
        }
        showProgressbar();
        weatherDayHourForecastLiveData = weatherViewModel.getHourForecastData();
        weatherDayHourForecastLiveData.observe(this, new Observer<WeatherDayHourForecastList>() {
            @Override
            public void onChanged(@Nullable WeatherDayHourForecastList weatherDayHourForecast) {
                System.out.println("onChanged hour forecast");

                hideProgressbar();

                allDaysHoursForecast.clear();
                allDaysHoursForecast.addAll(weatherDayHourForecast.getItems());

                mAdapterHorizontal.setProductList(allDaysHoursForecast);
                recyclerView.setAdapter(mAdapterHorizontal);
            }
        });


        weatherForecastLiveData = weatherViewModel.getForecastData();
        weatherForecastLiveData.observe(this, new Observer<WeatherForecast>() {
            @Override
            public void onChanged(@Nullable WeatherForecast weatherForecast) {

                hideProgressbar();

                weatherForecastList.clear();

                // size = 5
                weatherForecastList.addAll(weatherForecast.getItems());

                updateTodayUI(weatherForecastList.get(0));
                mAdapterVertical.setProductList(weatherForecastList);

                recyclerView2.setAdapter(mAdapterVertical);
            }
        });
    }


    private void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }


    private void updateTodayUI(WeatherFiveDaysForecast weatherFiveDaysForecast) {
        if (weatherFiveDaysForecast != null) {
            setTitle();

            String strTemp = splitByDot(weatherFiveDaysForecast.getTempMax()).concat("˚/").concat(splitByDot(weatherFiveDaysForecast.getTempMin()));
            tvTemp.setText(strTemp);
            String strHumidity = splitByDot(weatherFiveDaysForecast.getHumidity()).concat("%");
            tvHumidity.setText(strHumidity);
            tvDate.setText(WeatherUtils.getDateTitle(weatherFiveDaysForecast.getDt()));
            String strWind = splitByDot(weatherFiveDaysForecast.getSpeed()).concat("м/сек");
            tvWind.setText(strWind);
            int windIcon = WeatherUtils.getIconByWindState(weatherFiveDaysForecast.getDeg());
            tvWind.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_wind,0, windIcon, 0);
            int weatherStateIcon = WeatherUtils.getIconByWeatherState(weatherFiveDaysForecast.getDescription().description, true);
            ivWeatherState.setImageResource(weatherStateIcon);
        }
    }


    @Override
    public void onGetUserLocation() {
        Toast.makeText(MainActivity.this, "Город " + GlobalConstants.CURRENT_CITY_RU, Toast.LENGTH_SHORT).show();

        GlobalConstants.Preferences.saveLastUserLocation(this);

        weatherDayHourForecastLiveData = weatherViewModel.getHourForecastData();
        weatherForecastLiveData = weatherViewModel.getForecastData();
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
