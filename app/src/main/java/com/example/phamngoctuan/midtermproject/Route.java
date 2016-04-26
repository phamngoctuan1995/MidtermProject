package com.example.phamngoctuan.midtermproject;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by phamngoctuan on 14/04/2016.
 */
public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;

    public PolylineOptions getPolylineOption()
    {
        PolylineOptions res = new PolylineOptions().color(Color.BLUE).width(10);
        for (int i = 0; i < points.size(); ++i)
            res.add(points.get(i));
        return res;
    }

    public PolylineOptions addToMap(GoogleMap mMap)
    {
        PolylineOptions polylineOptions = getPolylineOption();
        MyConstant.directionDuration.setText(duration.text);
        MyConstant.directionDistance.setText(distance.text);
        MyConstant.directionOri.setText(startAddress);
        MyConstant.directionDes.setText(endAddress);

        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                .title(startAddress)
                .position(startLocation).snippet("Điểm bắt đầu"));
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                .title(endAddress)
                .position(endLocation).snippet("Điểm kết thúc"));
        mMap.addPolyline(polylineOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 16));
        return polylineOptions;
    }
}
