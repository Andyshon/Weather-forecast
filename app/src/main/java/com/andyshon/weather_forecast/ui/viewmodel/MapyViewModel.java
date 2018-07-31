package com.andyshon.weather_forecast.ui.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by andyshon on 30.07.18.
 */

public class MapyViewModel extends ViewModel {

    private MarkerOptions currentPositionMarker;
    private Marker currentLocationMarker;
    GoogleMap map;

    private SupportMapFragment supportMapFragment;


    public SupportMapFragment getSupportMapFragment() {
        return supportMapFragment;
    }

    public void setSupportMapFragment(SupportMapFragment supportMapFragment) {
        this.supportMapFragment = supportMapFragment;
    }

    public MarkerOptions getCurrentPositionMarker() {
        return currentPositionMarker;
    }

    public void setCurrentPositionMarker(MarkerOptions currentPositionMarker) {
        this.currentPositionMarker = currentPositionMarker;
    }

    public Marker getCurrentLocationMarker() {
        return currentLocationMarker;
    }

    public void setCurrentLocationMarker(Marker currentLocationMarker) {
        this.currentLocationMarker = currentLocationMarker;
    }

    private List<LatLng> points;

    public List<LatLng> getPoints() {
        return points;
    }

    public void addPoint(LatLng latLng) {
        points.add(latLng);
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }
}
