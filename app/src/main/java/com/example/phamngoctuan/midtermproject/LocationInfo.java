package com.example.phamngoctuan.midtermproject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by phamngoctuan on 23/04/2016.
 */
public class LocationInfo {
    public String name;
    public LatLng position;
    public String placeId;

    public Marker addMarkerToMap(GoogleMap mMap)
    {
        Marker mk = mMap.addMarker(new MarkerOptions().position(position).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
        return mk;
    }
}
