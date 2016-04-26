package com.example.phamngoctuan.midtermproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

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
    static public Dialog contactDialog = null;
    static public Dialog detailDialog = null;
//    static public int[] cb_ids = {R.id.cb_museum, R.id.cb_park, R.id.cb_restaurant, R.id.cb_cinema, R.id.cb_market, R.id.cb_all};
    static public void ShowLocationDialog()
    {
        Context context = MainActivity.context;
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
                ArrayList<String> ids = new ArrayList<String>();
                if (restaurant.isChecked())
                    ids.add("restaurant");
                if (park.isChecked())
                    ids.add("park");
                if (atm.isChecked())
                    ids.add("atm");
                if (cinema.isChecked())
                    ids.add("cinema");
                if (market.isChecked())
                    ids.add("market");
                MainActivity.mMap.clear();
                dialog.dismiss();
                ReadDataAssets(ids, true);
//                ReadDataAssetsAsyntask read = new ReadDataAssetsAsyntask(ids);
//                read.execute();
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

    static public void ShowNearLocationDialog()
    {
        Context context = MainActivity.context;
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

    static public void ShowDetailDialog(final LocationInfo loc)
    {
        try
        {
            if (MyDialog.detailDialog == null) {
                MyDialog.detailDialog = new Dialog(MainActivity.context);
                MyDialog.detailDialog.setContentView(R.layout.detail_dialog);
                MyDialog.detailDialog.setTitle("Thông tin chi tiết");
            }
        }
        catch (Exception e)
        {
            return;
        }
        final Dialog dialog = MyDialog.detailDialog;

        ImageView avatar = (ImageView) dialog.findViewById(R.id.imv_avatar_detaildialog);
        ImageButton direct = (ImageButton) dialog.findViewById(R.id.direct_detail);
        ImageButton browser = (ImageButton) dialog.findViewById(R.id.brower_detail);
        ImageButton dial = (ImageButton) dialog.findViewById(R.id.dial_detail);

        TextView name = (TextView)dialog.findViewById(R.id.tv_name_detaildialog);
        TextView address = (TextView)dialog.findViewById(R.id.tv_address_detaildialog);
        TextView description = (TextView)dialog.findViewById(R.id.tv_des_detaildialog);

        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loc.placeId.equals("cinema"))
                {
                    CinemaInfo cplace = (CinemaInfo)loc;
                    String phone = cplace.tel;
                    ((PhoneContactCallback)MainActivity.context).OnPhoneContactCallback(phone);
                    return;
                }
                Toast.makeText(MainActivity.context, "Không có thông tin liên lạc", Toast.LENGTH_SHORT).show();
            }
        });
        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";
                if (loc.placeId.equals("cinema"))
                {
                    CinemaInfo cplace = (CinemaInfo)loc;
                    url = cplace.link;
                }
                if (loc.placeId.equals("travel"))
                {
                    FamousPlaceInfo fplace = (FamousPlaceInfo)loc;
                    url = fplace.link;
                }

                if (url.equals(""))
                    Toast.makeText(MainActivity.context, "Không có thông tin website", Toast.LENGTH_SHORT).show();
                else
                    ((BrowserCallback)MainActivity.context).onBrowserCallback(url);
            }
        });

        direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = MyConstant.getCurrentLocation();
                if (location == null) {
                    Toast.makeText(MainActivity.context, "Hãy bật GPS để xác định vị trí của bạn", Toast.LENGTH_LONG).show();
                    return;
                }
                LatLng des = loc.position;
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                DirectionFinder direct = new DirectionFinder((DirectionFinderCallback)MainActivity.context, latitude + "," + longitude, des.latitude + "," + des.longitude);
                try {
                    direct.execute();
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.d("debug", "Exception finding direction: " + e.getMessage());
                }
            }
        });

        name.setText(loc.name);
        address.setText(loc.address);
        if (loc.placeId.equals("travel"))
        {
            FamousPlaceInfo fplace = (FamousPlaceInfo)loc;
            description.setText(fplace.description);
            LoadBitmapAsync loadbm = new LoadBitmapAsync(avatar);
            loadbm.execute(fplace.img);
        }
        if (loc.placeId.equals("restaurant"))
        {
            RestaurantInfo rplace = (RestaurantInfo)loc;
            description.append("Giá: " + rplace.price + "\n");
            description.append("Đánh giá: " + rplace.score + "/10");
            LoadBitmapAsync loadbm = new LoadBitmapAsync(avatar);
            loadbm.execute(rplace.avatar);
        }

        dialog.show();
    }

    static public void ShowListDialog()
    {
        if (contactDialog == null) {
            contactDialog = new Dialog(MainActivity.context);
            contactDialog.setContentView(R.layout.list_dialog);
            contactDialog.setTitle("Danh sách số điện thoại");
            ListView listview = (ListView) contactDialog.findViewById(R.id.lv_listdialog);
            Button close = (Button) contactDialog.findViewById(R.id.bt_close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactDialog.dismiss();
                }
            });
            CustomArrayAdapter adapter = new CustomArrayAdapter(MainActivity.context, R.layout.list_location_item);
            listview.setAdapter(adapter);
        }
        contactDialog.show();
    }

    static public ArrayList<LocationInfo> ReadDataAssets(ArrayList<String> ids, boolean isMarker)
    {
        MyConstant.progressDialog.show();
        ArrayList<LocationInfo> locs = new ArrayList<>();
        for (String id : ids) {
            String content = null;
            Elements places;
            try {
                content = MyConstant.readFileFromAssets(id + ".xml");
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            Document doc = Jsoup.parse(content);
            places = doc.getElementsByTag("place");

            int i = 0;
            if (places.size() > 0) {
                MainActivity.mMap.clear();
                MainActivity.placeMarker.clear();
            }
            for (Element e : places) {
                if (i++ == 20)
                    break;
                LocationFactory lf = LocationFactory.getInstance();
                final LocationInfo loc = lf.getLocation(id);
                loc.Parse(e);
                if (isMarker)
                    loc.addMarkerToMap(MainActivity.mMap);
                locs.add(loc);
                MainActivity.placeMarker.add(loc);
            }
            MyConstant.progressDialog.dismiss();
        }
        return locs;
    }
}

class ReadDataAssetsAsyntask extends AsyncTask<Void, Void, Void>
{
    ArrayList<String> ids;

    ReadDataAssetsAsyntask(ArrayList<String> id)
    {
        ids = id;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Handler handler = new Handler(Looper.getMainLooper());
        for (String id : ids) {
            String content = null;
            Elements places;
            try {
                content = MyConstant.readFileFromAssets(id + ".xml");
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            Document doc = Jsoup.parse(content);
            places = doc.getElementsByTag("place");

            int i = 0;
            if (places.size() > 0)
                MainActivity.placeMarker.clear();
            for (Element e : places) {
                if (i++ == 20)
                    break;
                LocationFactory lf = LocationFactory.getInstance();
                final LocationInfo loc = lf.getLocation(id);
                loc.Parse(e);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loc.addMarkerToMap(MainActivity.mMap);
                        MainActivity.placeMarker.add(loc);
                    }
                });
            }
        }
        return null;
    }
}
