package com.example.phamngoctuan.midtermproject;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by phamngoctuan on 23/04/2016.
 */
public class FindLocation {
    private static final String GEO_RESQUEST_DIR = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyDqOe33zsLexu3OKthx6hqEKpDnlv_kpIY";
    private FindLocationCallback callback;
    private String place = "";
    Location position = null;
    int radius = 0;
    String type;

    public FindLocation(FindLocationCallback callback, String origin) {
        this.callback = callback;
        this.place = origin;
    }

    public FindLocation(FindLocationCallback callback, Location p, String t, int ra)
    {
        this.callback = callback;
        position = p;
        radius = ra;
        type = t;
    }

    public void find() throws UnsupportedEncodingException {
        if (!MyConstant.checkInternetAvailable())
        {
            Toast.makeText(MainActivity.context, "Không có kết nối Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        callback.onFindLocationPrepare();
        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(place, "utf-8");
        if (position == null)
            return GEO_RESQUEST_DIR + urlOrigin;
        else
            return DIRECTION_URL_API + "location=" + String.valueOf(position.getLatitude()) + "," + String.valueOf(position.getLongitude()) + "&radius=500&type=" + type + "&key=" + GOOGLE_API_KEY;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (position == null)
                    parseJSon1(res);
                else
                    parseJSon2(res);
            } catch (Exception e) {
                Log.d("debug", "OnPost exception: " + e.getMessage());
            }
        }
    }

    private void parseJSon1(String data) throws JSONException {
        if (data == null)
            return;
        ArrayList<LocationInfo> res = new ArrayList<>();
        JSONObject jsonData = new JSONObject(data);

        JSONArray jsonLocations = jsonData.getJSONArray("results");

        for (int i = 0; i < jsonLocations.length(); i++) {
            JSONObject jsonLoc = jsonLocations.getJSONObject(i);
            LocationInfo locInf = new LocationInfo();
            locInf.name = jsonLoc.getString("formatted_address");
            if (locInf.name.contains(","))
            {
                locInf.address = locInf.name.substring(locInf.name.indexOf(", ") + 2, locInf.name.length());
                locInf.name = locInf.name.substring(0, locInf.name.indexOf(","));
            }
            Log.d("debug", "Name: " + locInf.name);

            locInf.placeId = jsonLoc.getString("place_id");
            Log.d("debug", "Place_id: " + locInf.placeId);

            JSONObject latlng = jsonLoc.getJSONObject("geometry").getJSONObject("location");
            locInf.position = new LatLng(latlng.getDouble("lat"), latlng.getDouble("lng"));

            Log.d("debug", "Pos: " + locInf.position.toString());

            res.add(locInf);
        }
        callback.onFindLocationSuccess(res);
    }
    private void parseJSon2(String data) throws JSONException {
        if (data == null)
            return;

        ArrayList<LocationInfo> locs = new ArrayList<>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonATMs = jsonData.getJSONArray("results");

        for (int i = 0; i < jsonATMs.length(); i++) {
            JSONObject jsonATM = jsonATMs.getJSONObject(i);
            LocationInfo loc = new LocationInfo();

            JSONObject jsonLocation = jsonATM.getJSONObject("geometry").getJSONObject("location");
            loc.position = new LatLng(jsonLocation.getDouble("lat"), jsonLocation.getDouble("lng"));
            loc.name = jsonATM.getString("name");
            loc.address = jsonATM.getString("vicinity");
            loc.placeId = type;
            locs.add(loc);
        }

        callback.onFindLocationSuccess(locs);
    }
}
