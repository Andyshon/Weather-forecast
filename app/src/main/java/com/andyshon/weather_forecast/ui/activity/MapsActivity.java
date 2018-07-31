package com.andyshon.weather_forecast.ui.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.andyshon.weather_forecast.db.entity.WeatherToday;
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
    private ImageView ivWeatherState, ic_searchLoop, ic_back;
    private Button btnAddToFavourites;

    private LiveData<WeatherToday> weatherByCoordinatesLiveData;
    private LiveData<WeatherToday> weatherByNameLiveData;
    private MapsViewModel mapsViewModel;

    private SearchView search;

    private WeatherToday weatherToday;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        mapyFragment = new MapyFragment();

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
        btnAddToFavourites = findViewById(R.id.btnAddToFavourites);
        btnAddToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weatherToday != null) {
//                    Toast.makeText(MapsActivity.this, "add to fav:" + tvCityName.getText().toString() + "\nhum:" +
//                            weatherToday.getTemp().getHumidity() + "\tpres:" + weatherToday.getTemp().getPressure(), Toast.LENGTH_SHORT).show();
                /*
                * todo: добавить этот обьект в избранное - insert to Room
                * */

                    Handler handler = new Handler();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            mapsViewModel.addToFavourite(weatherToday);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent();
                                    intent.putExtra("city", weatherToday.getCity());
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            });
                        }
                    });
                    thread.start();
                }
                else {
                    Toast.makeText(MapsActivity.this, "Can't add to favourites -> weatherToday = null", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bottomSheet = findViewById(R.id.bottom_sheet);

        if (bottomSheet != null)
            sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        /**
         * bottom sheet state change listener
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        System.out.println("hidden");
                        //sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        System.out.println("collapsed");
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        System.out.println("dragging");
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        final MapsListAdapter adapter = new MapsListAdapter(MapsActivity.this, GlobalConstants.getCitiesRuList(), this);
        listView = findViewById(R.id.list_view);
        listView.setVisibility(View.GONE);
        if (bottomSheet != null)
            bottomSheet.setVisibility(View.GONE);

        ic_searchLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textQuery != null && !textQuery.isEmpty())
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

                if (sheetBehavior != null)
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                textQuery = newText;
                System.out.println("new Text:" + newText);
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
        });
    }

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
        weatherByNameLiveData = mapsViewModel.getTodayDataByName(/*WeatherUtils.fromRUtoEN(name)*/name);
        weatherByNameLiveData.observe(this, new Observer<WeatherToday>() {
            @Override
            public void onChanged(@Nullable WeatherToday weatherDay) {

                weatherToday = weatherDay;

                listView.setVisibility(View.GONE);

                if (weatherDay == null) {
                    Toast.makeText(MapsActivity.this, "Не удалось найти " + name + ".\nВведите название на латинице", Toast.LENGTH_LONG).show();
                    return;
                }

                if (bottomSheet != null)
                    bottomSheet.setVisibility(View.VISIBLE);
                if (sheetBehavior != null)
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                mapyFragment.findCityByName(name);

                tvCityName.setText(name);
                System.out.println("OBJECT:" + weatherDay);
                System.out.println("SSS:" + weatherDay.getCity() + ":" + weatherDay.getDt());
                tvDate.setText(WeatherUtils.getDateTitle(weatherDay.getDt()));
                System.out.println("TVDATE:" + tvDate.getText().toString());
                ivWeatherState.setImageResource(WeatherUtils.getIconByWeatherState(weatherDay.getDescription().get(0).getDescription(), false));
                tvTempFull.setText(String.valueOf(weatherDay.getTemp().getTemp_max().intValue()).concat("˚/")
                        .concat(String.valueOf(weatherDay.getTemp().getTemp_min().intValue())).concat("˚"));
                tvTempCur.setText(String.valueOf(weatherDay.getTemp().getTemp().intValue()));
            }
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

        weatherByCoordinatesLiveData = mapsViewModel.getTodayDataByCoordinates(latLng.latitude, latLng.longitude);
        weatherByCoordinatesLiveData.observe(this, new Observer<WeatherToday>() {
            @Override
            public void onChanged(@Nullable WeatherToday weatherDay_coord) {

                weatherToday = weatherDay_coord;

                System.out.println("COORD - city:" + weatherDay_coord.getCity());

                if (weatherDay_coord.getCity().equals(""))
                    tvCityName.setText("Неопознанная местность");
                else {
                    tvCityName.setText(weatherDay_coord.getCity());
                }

                tvDate.setText(WeatherUtils.getDateTitle(weatherDay_coord.getDt()));
                tvTempFull.setText(String.valueOf(weatherToday.getTemp().getTemp_max().intValue()).concat("˚/")
                        .concat(String.valueOf(weatherToday.getTemp().getTemp_min().intValue())).concat("˚"));
                tvTempCur.setText(String.valueOf(weatherToday.getTemp().getTemp().intValue()));
                ivWeatherState.setImageResource(WeatherUtils.getIconByWeatherState(weatherDay_coord.getDescription().get(0).getDescription(), false));
            }
        });
    }
}
