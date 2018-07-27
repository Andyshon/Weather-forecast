package com.andyshon.weather_forecast.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.LocationDetector;
import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.db.IService;
import com.andyshon.weather_forecast.db.RestClient;
import com.andyshon.weather_forecast.db.entity.WeatherDay;
import com.andyshon.weather_forecast.db.entity.WeatherForecast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationDetector.MyCallback {

    private String TAG = "WEATHER";
    private TextView tvTemp, tvHumidity, tvDate, tvWind;
    private ImageView tvImage;
    private IService api;
    private Toolbar toolbar;
    private Button button;

    private WeatherViewModel weatherViewModel;
    private LiveData<WeatherDay> weatherDayLiveData;

    private LiveData<WeatherForecast> weatherForecastLiveData;

    /*
    * Any fool can write code that a computer can understand. Good programmers write code that humans can understand..
    * */

    private ListView listView;
    private List<WeatherDay> weatherDays;
    private List<WeatherDay> weatherForecastList;
    private List<WeatherDay> allDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "refresh today weather", Toast.LENGTH_SHORT).show();
                weatherDayLiveData = weatherViewModel.getTodayData();
                weatherForecastLiveData = weatherViewModel.getForecastData();
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(GlobalConstants.CURRENT_CITY_UA);
        Drawable drawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_my_location);
        toolbar.setNavigationIcon(drawable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        weatherDays = new ArrayList<>();
        weatherForecastList = new ArrayList<>();
        allDays = new ArrayList<>();

        tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        tvDate = findViewById(R.id.tvDate);
        tvWind = findViewById(R.id.tvWind);
        tvImage = (ImageView) findViewById(R.id.ivWeatherState);

        api = RestClient.initService();


        if (!GlobalConstants.IsLocationDetected) {
            System.out.println("Location is not detected yet");
            new LocationDetector(this, this);
        }
        else {
            System.out.println("Location already detected");
        }


        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        //GlobalConstants.setCurrentCity("Запорожье");
        weatherDayLiveData = weatherViewModel.getTodayData();
        weatherDayLiveData.observe(this, new Observer<WeatherDay>() {
            @Override
            public void onChanged(@Nullable WeatherDay weatherDay) {
                System.out.println("GET WEATHERDAY :" + weatherDay);
                setTodayUI(weatherDay);
            }
        });

        weatherForecastLiveData = weatherViewModel.getForecastData();
        weatherForecastLiveData.observe(this, new Observer<WeatherForecast>() {
            @Override
            public void onChanged(@Nullable WeatherForecast weatherForecast) {
                System.out.println("GET WEATHERFORECAST:" + weatherForecast);

                weatherDays.clear();
                weatherForecastList.clear();
                allDays.clear();


                Log.d(TAG, "size="+weatherForecast.getItems().size());

                for (WeatherDay day : weatherForecast.getItems()) {

                    allDays.add(day);

                    Log.d(TAG, "dt_Txt:" + day.getDt_txt());
                    Log.d(TAG, "temp:" + day.getTemp());
                    Log.d(TAG, "dt:" + day.getDate().get(Calendar.HOUR_OF_DAY));

                    if (day.getDate().get(Calendar.HOUR_OF_DAY) == 15) {
                        String date = String.format("%d.%d.%d %d:%d",
                                day.getDate().get(Calendar.DAY_OF_MONTH),
                                day.getDate().get(Calendar.WEEK_OF_MONTH),
                                day.getDate().get(Calendar.YEAR),
                                day.getDate().get(Calendar.HOUR_OF_DAY),
                                day.getDate().get(Calendar.MINUTE)
                        );
                        Log.d(TAG, "date:"+date + "\ttemp:" + day.getTempInteger());
                        Log.d(TAG, "------------------");

                        weatherForecastList.add(day);


                        weatherDays.add(day);
                    }
                }

                WeatherAdapter adapter = new WeatherAdapter(MainActivity.this, R.layout.list_item, weatherForecastList, allDays);
                listView = findViewById(R.id.listview);
                listView.setAdapter(adapter);


                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                RecyclerViewHorizontalListAdapter horizontalListAdapter = new RecyclerViewHorizontalListAdapter(weatherDays, getApplicationContext());
                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(horizontalListAdapter);
                //horizontalListAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onGetCurrentLocation(String currentAddress) {

        Toast.makeText(MainActivity.this, "currentAddress:" + currentAddress, Toast.LENGTH_SHORT).show();
        System.out.println("currentAddress:" + currentAddress);

        GlobalConstants.IsLocationDetected = true;

        weatherDayLiveData = weatherViewModel.getTodayData();
        weatherForecastLiveData = weatherViewModel.getForecastData();
    }


    private void setTodayUI(WeatherDay weatherDay) {
        System.out.println("weatherDay :: " + weatherDay);
        if (weatherDay != null) {

            System.out.println("CITY EN:" + GlobalConstants.CURRENT_CITY_EN);
            System.out.println("CITY UA:" + GlobalConstants.CURRENT_CITY_UA);
            toolbar.setTitle(GlobalConstants.CURRENT_CITY_UA);
            System.out.println("WEATHER:" + weatherDay.getCity());

            String strTemp = weatherDay.getTempMax().substring(0,weatherDay.getTempMax().length()-2).concat("˚/")
                    .concat(weatherDay.getTempMin().substring(0, weatherDay.getTempMin().length()-2).concat("˚"));
            tvTemp.setText(strTemp);
            String strHumidity = weatherDay.getHumidity().substring(0, weatherDay.getHumidity().length()-2).concat("%");
            tvHumidity.setText(strHumidity);
            tvDate.setText(weatherDay.getDescription().getDescription());
            String strWind = weatherDay.getSpeed().substring(0, weatherDay.getSpeed().length()-2).concat("м/сек");
            tvWind.setText(strWind);
            //tvDate.setText(data.getDate());
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);

                searchView.clearFocus();
                /*if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void getWeatherToday() {

        toolbar.setTitle(GlobalConstants.CURRENT_CITY_UA);
        Call<WeatherDay> dayCall = api.getToday(GlobalConstants.CURRENT_CITY_EN, GlobalConstants.UNITS, GlobalConstants.KEY);
        dayCall.enqueue(new Callback<WeatherDay>() {
            @Override
            public void onResponse(Call<WeatherDay> call, Response<WeatherDay> response) {
                WeatherDay data = response.body();
                if (data != null) {
                    String strTemp = data.getTempInteger().concat("˚/").concat(data.getTempInteger().concat("˚"));
                    tvTemp.setText(strTemp);
                    String strHumidity = data.getHumidity().concat("%");
                    tvHumidity.setText(strHumidity);
                    String strWind = data.getSpeed().concat("м/сек");
                    tvWind.setText(strWind);
                }
            }

            @Override
            public void onFailure(Call<WeatherDay> call, Throwable t) {
                Log.d(TAG, "ERROR:" + t.getMessage());
            }
        });

    }

    public void getWeatherForecast(View view) {
        Log.d(TAG, "CURRENT_CITY:" + GlobalConstants.CURRENT_CITY_EN);
        toolbar.setTitle(GlobalConstants.CURRENT_CITY_UA);

        // get weather forecast
        Call<WeatherForecast> callForecast = api.getForecast(GlobalConstants.CURRENT_CITY_EN, GlobalConstants.UNITS, GlobalConstants.KEY);
        callForecast.enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                Log.d(TAG, "response2:" + response.toString());
                WeatherForecast data = response.body();
                Log.d(TAG,response.toString());

                if (response.isSuccessful()) {
                    weatherDays.clear();

                    Log.d(TAG, "size="+data.getItems().size());

                    for (WeatherDay day : data.getItems()) {

                        Log.d(TAG, "dt_Txt:" + day.getDt_txt());

                        //weatherDays.add(day);

                        if (day.getDate().get(Calendar.HOUR_OF_DAY) == 15) {
                            String date = String.format("%d.%d.%d %d:%d",
                                    day.getDate().get(Calendar.DAY_OF_MONTH),
                                    day.getDate().get(Calendar.WEEK_OF_MONTH),
                                    day.getDate().get(Calendar.YEAR),
                                    day.getDate().get(Calendar.HOUR_OF_DAY),
                                    day.getDate().get(Calendar.MINUTE)
                            );
                            Log.d(TAG, date);
                            Log.d(TAG, day.getTempInteger());
                            Log.d(TAG, "---");

                            weatherDays.add(day);
                        }
                    }

                    WeatherAdapter adapter = new WeatherAdapter(MainActivity.this, R.layout.list_item, weatherDays, allDays);
                    listView = findViewById(R.id.listview);
                    listView.setAdapter(adapter);


                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    RecyclerViewHorizontalListAdapter horizontalListAdapter = new RecyclerViewHorizontalListAdapter(weatherDays, getApplicationContext());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(horizontalListAdapter);
                    //horizontalListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                Log.e(TAG, "onFailure");
                Log.e(TAG, t.toString());
            }
        });

    }
}
