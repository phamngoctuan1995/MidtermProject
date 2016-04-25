package com.example.phamngoctuan.midtermproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.jsoup.nodes.Element;

/**
 * Created by phamngoctuan on 25/04/2016.
 */
public class CinemaInfo extends LocationInfo {
    String tel= "";
    String link = "";
    String type = "";

    @Override
    public Bitmap getBitmapMarker() {
        RelativeLayout layout = new RelativeLayout(MainActivity.context);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.context);
        inflater.inflate(R.layout.place_marker, (ViewGroup) layout);

        TextView tvName = (TextView) layout.findViewById(R.id.tv_mk_name);
        TextView tvAddr = (TextView) layout.findViewById(R.id.tv_mk_address);
        ImageView imvAvatar = (ImageView) layout.findViewById(R.id.imv_mk_avatar);
        try {
            tvName.setText(name);
            tvAddr.setText(address);

            if (type.equals("galaxy"))
                imvAvatar.setImageBitmap(BitmapFactory.decodeResource(MainActivity.context.getResources(), R.drawable.galaxy_cine));
            else if (type.equals("bhd"))
                imvAvatar.setImageBitmap(BitmapFactory.decodeResource(MainActivity.context.getResources(), R.drawable.bhd_cine));
            else if (type.equals("lotte"))
                imvAvatar.setImageBitmap(BitmapFactory.decodeResource(MainActivity.context.getResources(), R.drawable.lotte_cine));
            else if (type.equals("cgv"))
                imvAvatar.setImageBitmap(BitmapFactory.decodeResource(MainActivity.context.getResources(), R.drawable.cgv_cine));
        } catch (Exception e)
        {
            Log.d("debug", "Exception getBitmapmarker: " + e.getMessage());
        }

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

    @Override
    public void Parse(Element e) {
        name = e.attr("name");
        address = e.attr("address");
        String ll = e.attr("latlng");
        position = new LatLng(Double.parseDouble(e.attr("lat")), Double.parseDouble(e.attr("lng")));
        placeId = e.attr("id");
        type = e.attr("class");
        link = e.attr("link");
        tel = e.attr("tel");
    }

    @Override
    public LocationInfo clone() {
        return new CinemaInfo();
    }
}
