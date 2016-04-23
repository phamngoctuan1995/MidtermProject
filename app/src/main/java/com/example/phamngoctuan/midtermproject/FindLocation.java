package com.example.phamngoctuan.midtermproject;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by phamngoctuan on 23/04/2016.
 */
public class FindLocation {
    private static final String GEO_RESQUEST_DIR = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    private FindLocationCallback callback;
    private String place;

    public FindLocation(FindLocationCallback callback, String origin) {
        this.callback = callback;
        this.place = origin;
    }

    public void find() throws UnsupportedEncodingException {
        callback.onFindLocationPrepare();
        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(place, "utf-8");

        return GEO_RESQUEST_DIR + urlOrigin;
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
                parseJSon(res);
            } catch (Exception e) {
                Log.d("debug", "OnPost exception: " + e.getMessage());
            }
        }
    }

    private void parseJSon(String data) throws JSONException {
        if (data == null)
            return;
        ArrayList<LocationInfo> res = new ArrayList<>();
        JSONObject jsonData = new JSONObject(data);

        JSONArray jsonLocations = jsonData.getJSONArray("results");

        for (int i = 0; i < jsonLocations.length(); i++) {
            JSONObject jsonLoc = jsonLocations.getJSONObject(i);
            LocationInfo locInf = new LocationInfo();
            locInf.name = jsonLoc.getString("formatted_address");
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

}
