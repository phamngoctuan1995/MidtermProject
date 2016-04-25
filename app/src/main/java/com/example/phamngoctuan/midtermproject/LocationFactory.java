package com.example.phamngoctuan.midtermproject;

import java.util.HashMap;

/**
 * Created by phamngoctuan on 25/04/2016.
 */
public class LocationFactory {
    static private LocationFactory lf = null;
    private HashMap<String, LocationInfo> map;

    private LocationFactory()
    {
        map = new HashMap<>();
        map.put("park", new LocationInfo());
        map.put("atm", new ATMInfo());
        map.put("restaurant", new RestaurantInfo());
        map.put("cinema", new CinemaInfo());
        map.put("market", new MarketInfo());
    }

    static public LocationFactory getInstance()
    {
        if (lf == null)
            lf = new LocationFactory();
        return lf;
    }

    public LocationInfo getLocation(String id)
    {
        return map.get(id).clone();
    }
}
