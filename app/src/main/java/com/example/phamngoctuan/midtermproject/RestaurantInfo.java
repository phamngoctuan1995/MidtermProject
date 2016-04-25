package com.example.phamngoctuan.midtermproject;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by phamngoctuan on 25/04/2016.
 */
public class RestaurantInfo extends LocationInfo {
    double score;
    ArrayList<String> imgs = null;
    ArrayList<String> styles = null;
    String link;
    String type;
    String avatar;
    String price;

    @Override
    public void Parse(Element e) {
        name = e.attr("name");
        address = e.attr("address");
        avatar = e.attr("avatar").substring(2);
        score = Double.parseDouble(e.attr("score"));
        type = e.attr("type");
        price = e.attr("price");
        link = "http://www.foody.vn/ho-chi-minh/" + e.attr("id");
        placeId = e.attr("id");
        String lat = e.attr("lat");
        String lng = e.attr("lng");

        position = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

        Elements stylesE = e.getElementsByTag("style");
        Elements imgsE = e.getElementsByTag("img");

        if (stylesE.size() > 0)
            styles = new ArrayList<>();

        for (Element sE : stylesE)
            styles.add(sE.attr("text"));

        if (imgsE.size() > 0)
            imgs = new ArrayList<>();

        for (Element iE : imgsE)
            imgs.add(iE.attr("src"));
    }

    @Override
    public LocationInfo clone() {
        return new RestaurantInfo();
    }
}
