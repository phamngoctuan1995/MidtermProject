package com.example.phamngoctuan.midtermproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
        Bitmap bmp = getBitmapMarker();
        Marker mk = mMap.addMarker(new MarkerOptions().position(position).title(name)
                .icon(BitmapDescriptorFactory.fromBitmap(bmp)).anchor((float) 0.5, (float) 1));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
        return mk;
    }

    public Bitmap getBitmapMarker()
    {
        RelativeLayout layout = new RelativeLayout(MainActivity.context);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.context);
        inflater.inflate(R.layout.place_marker, (ViewGroup) layout);

        TextView tvName = (TextView)layout.findViewById(R.id.tv_mk_name);
        TextView tvAddr = (TextView)layout.findViewById(R.id.tv_mk_address);

        tvName.setText(name.substring(0, name.indexOf(",")));
        tvAddr.setText(name.substring(name.indexOf(", ") + 2, name.length()));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)MainActivity.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        layout.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        layout.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        layout.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(layout.getMeasuredWidth(), layout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        layout.draw(canvas);
        return bitmap;
    }
}
