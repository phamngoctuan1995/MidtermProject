package com.example.phamngoctuan.midtermproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by phamngoctuan on 23/04/2016.
 */
public class MyConstant {
    static public ProgressDialog progressDialog = null;
    static public onClickMarkerDirection onClickMkDirect = null;
    static public boolean isDirecting = false;
    static public onClickFabUndoDirection onClickFabUndoDirect = null;
    static public View directionView = null;
    static public View directionSubView = null;
    static public TextView directionDistance = null;
    static public TextView directionDuration = null;
    static public EditText directionOri = null;
    static public EditText directionDes = null;
    static public Button directionSearch = null;
    static public View directionInfoView = null;
    static public boolean isReduce = false;

    static void resetDirectionView() {
        directionDistance.setText("0");
        directionDuration.setText("0");
        directionOri.setText("");
        directionDes.setText("");
        directionSearch.setText("Tìm kiếm");
    }

    static void directionMode() {
        MainActivity.fab.setImageBitmap(BitmapFactory.
                decodeResource(MainActivity.context.getResources(), R.drawable.ic_direction));
        MainActivity.fab.setVisibility(View.VISIBLE);
        MainActivity.fab.setOnClickListener(MyConstant.onClickFabUndoDirect);
        MyConstant.isDirecting = true;
        MyConstant.directionView.setVisibility(View.VISIBLE);
    }

    static void endDirectionMode()
    {
        MainActivity.mMap.clear();
        MainActivity.fab.setVisibility(View.INVISIBLE);
        MyConstant.isDirecting = false;
        MyConstant.directionView.animate().translationY(-MyConstant.directionView.getHeight())
                .setDuration(500);
    }

    static boolean checkInternetAvailable() {
        ConnectivityManager conMgr = (ConnectivityManager) MainActivity.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    @NonNull
    static String readFileFromAssets(String pathname) throws IOException {

        InputStream file = MainActivity.context.getAssets().open(pathname);

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    static Location getCurrentLocation()
    {
        Context context = MainActivity.context;
        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return location;
    }
}

class onClickMarkerDirection implements GoogleMap.OnMarkerClickListener
{

    @Override
    public boolean onMarkerClick(final Marker marker) {
        marker.showInfoWindow();
//        MyDialog.ShowDetailDialog(MainActivity.placeMarker.get(0));
        Snackbar.make(MainActivity.fab, "Tìm đường đến địa điểm này?", Snackbar.LENGTH_LONG)
                .setAction("Có", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = MainActivity.context;
                        LatLng des = marker.getPosition();

                        Location location = MyConstant.getCurrentLocation();
                        if (location == null) {
                            Toast.makeText(context, "Hãy bật GPS để xác định vị trí của bạn", Toast.LENGTH_LONG).show();
                            return;
                        }
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        DirectionFinder direct = new DirectionFinder((DirectionFinderCallback)context, latitude + "," + longitude, des.latitude + "," + des.longitude);
                        try {
                            direct.execute();
                        } catch (Exception e) {
                            Log.d("debug", "Exception finding direction: " + e.getMessage());
                        }
                    }
                }).setActionTextColor(Color.RED).show();
        return true;
    }
}

class onClickFabUndoDirection implements View.OnClickListener
{

    @Override
    public void onClick(View v) {

        Snackbar.make(MainActivity.fab, "Thoát khỏi chế độ tìm đường?", Snackbar.LENGTH_LONG)
                .setAction("Có", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyConstant.endDirectionMode();
                    }
                }).setActionTextColor(Color.RED).show();
    }
}
