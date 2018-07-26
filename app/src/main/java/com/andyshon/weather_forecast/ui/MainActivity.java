package com.andyshon.weather_forecast.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.andyshon.weather_forecast.GetAddressIntentService;
import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.db.IService;
import com.andyshon.weather_forecast.db.RestClient;
import com.andyshon.weather_forecast.db.entity.WeatherDay;
import com.andyshon.weather_forecast.db.entity.WeatherForecast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String TAG = "WEATHER";
    private TextView tvTemp, tvHumidity, tvDate, tvWind;
    private ImageView tvImage;
    private IService api;
    private Toolbar toolbar;


    /*
    * Any fool can write code that a computer can understand. Good programmers write code that humans can understand..
    * */

    private FusedLocationProviderClient fusedLocationClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    private LocationAddressResultReceiver addressResultReceiver;

    private Location currentLocation;

    private LocationCallback locationCallback;

    private ListView listView;
    private List<WeatherDay> weatherDays;



    @SuppressWarnings("MissingPermission")
    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LocationRequest locationRequest = new LocationRequest();
            //locationRequest.setInterval(5000);
            //locationRequest.setFastestInterval(10000);
            //locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getAddress() {

        if (!Geocoder.isPresent()) {
            Toast.makeText(MainActivity.this, "Can't find current address, ", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, GetAddressIntentService.class);
        intent.putExtra("add_receiver", addressResultReceiver);
        intent.putExtra("add_location", currentLocation);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Location permission not granted, " + "restart the app if you want the feature", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    private class LocationAddressResultReceiver extends ResultReceiver {
        LocationAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == 0) {
                //Last Location can be null for various reasons
                //for example the api is called first time
                //so retry till location is set
                //since intent service runs on background thread, it doesn't block main thread
                Log.d("Address", "Location null retrying");
                getAddress();
            }

            if (resultCode == 1) {
                Toast.makeText(MainActivity.this, "Address not found, " , Toast.LENGTH_SHORT).show();
            }

            String currentAdd = resultData.getString("address_result");

            showResults(currentAdd);
        }
    }

    private void showResults(String currentAdd){
        Toast.makeText(this, "currentAdd:" + currentAdd, Toast.LENGTH_SHORT).show();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        getWeather(null);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(GlobalConstants.CURRENT_CITY_UA);
        Drawable drawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_my_location);
        toolbar.setNavigationIcon(drawable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        weatherDays = new ArrayList<>();

        tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        tvDate = findViewById(R.id.tvDate);
        tvWind = findViewById(R.id.tvWind);
        tvImage = (ImageView) findViewById(R.id.ivWeatherState);

        api = RestClient.initService();


        addressResultReceiver = new LocationAddressResultReceiver(new Handler());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                currentLocation = locationResult.getLocations().get(0);
                getAddress();
            };
        };
        startLocationUpdates();
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



    public void getWeather(View view) {
        String units = "metric";
        String key = GlobalConstants.KEY;

        Log.d(TAG, "OK");


        Log.d(TAG, "CURRENT_CITY:" + GlobalConstants.CURRENT_CITY_EN);
        toolbar.setTitle(GlobalConstants.CURRENT_CITY_UA);
        Call<WeatherDay> dayCall = api.getToday(GlobalConstants.CURRENT_CITY_EN, units, key);
        dayCall.enqueue(new Callback<WeatherDay>() {
            @Override
            public void onResponse(Call<WeatherDay> call, Response<WeatherDay> response) {
                Log.d(TAG, "response:" + response.toString());
                WeatherDay data = response.body();
                if (data != null) {

                    String strTemp = data.getTempInteger().concat("˚/").concat(data.getTempInteger().concat("˚"));
                    tvTemp.setText(strTemp);
                    String strHumidity = data.getHumidity().concat("%");
                    tvHumidity.setText(strHumidity);
                    //tvDate.setText(data.getDate());
                    String strWind = data.getSpeed().concat("м/сек");
                    tvWind.setText(strWind);
                }
            }

            @Override
            public void onFailure(Call<WeatherDay> call, Throwable t) {
                Log.d(TAG, "ERROR:" + t.getMessage());
            }
        });


        // get weather forecast
        Call<WeatherForecast> callForecast = api.getForecast(GlobalConstants.CURRENT_CITY_EN, units, key);
        callForecast.enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                Log.e(TAG, "onResponse");
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

                    ProductAdapter adapter = new ProductAdapter(MainActivity.this, R.layout.list_item, weatherDays);
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

    public int convertDPtoPX(int dp, Context ctx) {
        float density = ctx.getResources().getDisplayMetrics().density;
        int px = (int)(dp * density);
        return px;
    }
}
