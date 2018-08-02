package com.andyshon.weather_forecast.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by andyshon on 29.07.18.
 */

public class MapyFragment extends Fragment implements OnMapReadyCallback {

    private Context mContext;
    private SupportMapFragment supportMapFragment;
    private GoogleMap map;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        mContext = getActivity();
        return inflater.inflate(R.layout.fragment_map, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        mContext = getActivity();


        FragmentManager fm = getActivity().getSupportFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        ((MapsActivity)getContext()).findCityByName(GlobalConstants.CURRENT_LOCATION_CITY_EN);

        map.setOnMapLongClickListener(latLng -> {
            Log.d("DEBUG","Map clicked [" + latLng.latitude + " / " + latLng.longitude + "]");
            ((MapsActivity)getContext()).findCityByCoord(latLng);
            updateCurrentLocationMarker(latLng);
        });
    }


    public void updateCurrentLocationMarker(LatLng latLng){
        if(map != null){
            map.clear();
            map.addMarker(new MarkerOptions().position(latLng).title("My Location"));
        }
    }


    public void findCityByName(String location) {
        List<Address> addressList = null;

        Geocoder geocoder = new Geocoder(getContext());
        try {
            addressList = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressList != null && addressList.size() != 0) {
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            updateCurrentLocationMarker(latLng);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6));
        }
    }
}