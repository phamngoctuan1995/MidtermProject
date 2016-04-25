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

import org.jsoup.nodes.Element;

/**
 * Created by phamngoctuan on 25/04/2016.
 */
public class MarketInfo extends LocationInfo {
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

            if (type.equals("coopmart"))
                imvAvatar.setImageBitmap(BitmapFactory.decodeResource(MainActivity.context.getResources(), R.drawable.coopmart));
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
        super.Parse(e);
        type = e.attr("class");
        address = e.attr("address");

    }

    @Override
    public LocationInfo clone() {
        return new MarketInfo();
    }
}
