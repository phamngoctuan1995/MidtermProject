package com.example.phamngoctuan.midtermproject;

import android.graphics.Bitmap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jsoup.nodes.Element;

/**
 * Created by phamngoctuan on 25/04/2016.
 */
public class ATMInfo extends LocationInfo {
    String type = "";

    @Override
    public Marker addMarkerToMap(GoogleMap mMap)
    {
        Bitmap bmp = getBitmapMarker();
        Marker mk = mMap.addMarker(new MarkerOptions().position(position).title(type)
                .snippet(name));
        return mk;
    }

    @Override
    public void Parse(Element e)
    {
        name = e.attr("address");
        String ll = e.attr("latlng");
        position = new LatLng(Double.parseDouble(ll.substring(0, ll.indexOf(",")))
                , Double.parseDouble(ll.substring(ll.indexOf(",") + 1, ll.length())));
        placeId = e.attr("id");
        type = e.attr("class");
    }

    @Override
    public LocationInfo clone()
    {
        return new ATMInfo();
    }
}
