package com.andyshon.weather_forecast.ui.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
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
import com.andyshon.weather_forecast.ui.viewmodel.MapyViewModel;
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

    private MapyViewModel mapyViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);

        /*mContext = getActivity();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);


        // Restoring the markers on configuration changes
        System.out.println("TT:" + savedInstanceState);
        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey("points")){
                pointList = savedInstanceState.getParcelableArrayList("points");
                if(pointList!=null){
                    for(int i=0;i<pointList.size();i++){
                        System.out.println("POINT:" + pointList.get(i).latitude + ":" + pointList.get(i).longitude);
                        drawMarker(pointList.get(i));
                    }
                }
            }
        }*/


        mapyViewModel = ViewModelProviders.of(this).get(MapyViewModel.class);
        //mapyViewModel.setMap();

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
       /*mapyViewModel.setSupportMapFragment((SupportMapFragment)fm.findFragmentById(R.id.map_container));
       if (mapyViewModel.getSupportMapFragment() == null) {
           mapyViewModel.setSupportMapFragment(SupportMapFragment.newInstance());
           fm.beginTransaction().replace(R.id.map_container, mapyViewModel.getSupportMapFragment()).commit();
       }*/


        // Restoring the markers on configuration changes
        /*System.out.println("TT:" + savedInstanceState);
        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey("points")){
                pointList = savedInstanceState.getParcelableArrayList("points");
                if(pointList!=null){
                    for(int i=0;i<pointList.size();i++){
                        System.out.println("POINT:" + pointList.get(i).latitude + ":" + pointList.get(i).longitude);
                        drawMarker(pointList.get(i));
                    }
                }
            }
        }*/
    }


    // A callback method, which is invoked on configuration is changed
    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        // Adding the pointList arraylist to Bundle
        System.out.println("SAVEEE");
        outState.putParcelableArrayList("points", pointList);

        // Saving the bundle
        super.onSaveInstanceState(outState);
    }*/


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapyViewModel.setMap(googleMap);
        map = googleMap;
        System.out.println("MAP:" + map);
        //map = googleMap;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        //LatLng latLng = new LatLng(49.608267753910546, 34.513846188783646);
        /*mapyViewModel.getMap()*///map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //map.animateCamera(CameraUpdateFactory.zoomTo(7));
        System.out.println("CURR:" + GlobalConstants.CURRENT_LOCATION_CITY_EN);
        ((MapsActivity)getContext()).findCityByName(GlobalConstants.CURRENT_LOCATION_CITY_EN);

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.d("DEBUG","Map clicked [" + latLng.latitude + " / " + latLng.longitude + "]");
                ((MapsActivity)getContext()).findCityByCoord(latLng);
                updateCurrentLocationMarker(latLng);
            }
        });

    }


    public void updateCurrentLocationMarker(LatLng latLng){
        if(map != null){
            map.clear();
            map.addMarker(new MarkerOptions().position(latLng).title("My Location"));
        }
    }


    public void findCityByName(String location) {
        System.out.println("location:" + location);
        List<Address> addressList = null;

        Geocoder geocoder = new Geocoder(getContext());
        try {
            addressList = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressList != null && addressList.size() != 0) {
            Address address = addressList.get(0);
            System.out.println("address = " + address.getCountryCode() + ":" + address.getLocality());
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            System.out.println("latLng = " + latLng.latitude+":"+latLng.longitude);

            updateCurrentLocationMarker(latLng);
            //map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6));
        }
    }
}