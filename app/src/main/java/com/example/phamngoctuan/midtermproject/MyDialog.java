package com.example.phamngoctuan.midtermproject;

import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by phamngoctuan on 23/04/2016.
 */
public class MyDialog {
//    static public int[] cb_ids = {R.id.cb_museum, R.id.cb_park, R.id.cb_restaurant, R.id.cb_cinema, R.id.cb_market, R.id.cb_all};
    static public void ShowDialogLocation(final Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.location_dialog);
        dialog.setTitle("Tìm kiếm địa điểm");
        dialog.setCanceledOnTouchOutside(false);
        // set the custom dialog components - text, image and button

        
        final CheckBox atm = (CheckBox)dialog.findViewById(R.id.cb_atm);
        final CheckBox park = (CheckBox)dialog.findViewById(R.id.cb_park);
        final CheckBox restaurant = (CheckBox)dialog.findViewById(R.id.cb_restaurant);
        final CheckBox cinema = (CheckBox)dialog.findViewById(R.id.cb_cinema);
        final CheckBox market = (CheckBox)dialog.findViewById(R.id.cb_market);
        final Button all = (Button)dialog.findViewById(R.id.bt_all);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (all.getText().toString().equals("Tất cả")) {
                    atm.setChecked(true);
                    park.setChecked(true);
                    restaurant.setChecked(true);
                    cinema.setChecked(true);
                    market.setChecked(true);
                    all.setText("Bỏ chọn");
                }
                else
                {
                    atm.setChecked(false);
                    park.setChecked(false);
                    restaurant.setChecked(false);
                    cinema.setChecked(false);
                    market.setChecked(false);
                    all.setText("Tất cả");
                }
            }
        });

        Button search = (Button)dialog.findViewById(R.id.bt_search);
        Button cancel = (Button)dialog.findViewById(R.id.bt_cancel);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (restaurant.isChecked())
                {
                    MainActivity.mMap.clear();
                    String content;
                    Elements places;
                    try {
                        content = MyConstant.readFileFromAssets("restaurant.xml");
                        Document doc = Jsoup.parse(content);
                        places = doc.getElementsByTag("place");
                    } catch (Exception e) {
                        Log.d("debug", "Exception parse doc: " + e.getMessage());
                        return;
                    }
                    finally {
                        dialog.dismiss();
                    }
                    int i = 0;
                    for (Element e : places)
                    {
                        if (i++ == 40)
                            break;
                        RestaurantInfo atm = new RestaurantInfo();

                        atm.Parse(e);
                        atm.addMarkerToMap(MainActivity.mMap);
                    }
                    Location myLoc = MyConstant.getCurrentLocation();
                    if (myLoc == null) {
                        MainActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.benthanh, 13));
                        return;
                    }
                    LatLng myLatlng = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
                    MainActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 15));
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    static public void ShowDialogNearLocation(final Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.near_location_dialog);
        dialog.setTitle("Chọn loại địa điểm");
        dialog.setCanceledOnTouchOutside(false);
        // set the custom dialog components - text, image and button

        final RadioButton atm = (RadioButton)dialog.findViewById(R.id.rdb_atm_neardialog);
        atm.setChecked(true);
        final RadioButton restaurant = (RadioButton)dialog.findViewById(R.id.rdb_restaurant_neardialog);
        final RadioButton hotel = (RadioButton)dialog.findViewById(R.id.rdb_hotel_neardialog);
        final TextView radius = (TextView)dialog.findViewById(R.id.radius_neardialog);


        final SeekBar sb = (SeekBar) dialog.findViewById(R.id.seekbar_neardialog);
        sb.setMax(2900);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius.setText("" + (progress + 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button search = (Button)dialog.findViewById(R.id.bt_search_neardialog);
        Button cancel = (Button)dialog.findViewById(R.id.bt_cancel_neardialog);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location myLocation = MyConstant.getCurrentLocation();
                if (myLocation == null)
                {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.context, "Bật GPS để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
                    return;
                }
                int r = sb.getProgress() + 100;
                String type = "";
                if (restaurant.isChecked())
                    type = "restaurant";
                if (atm.isChecked())
                    type = "atm";
                if (hotel.isChecked())
                    type = "hotel";

                FindLocation find = new FindLocation((FindLocationCallback)MainActivity.context, myLocation, type, r);
                try {
                    find.find();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
