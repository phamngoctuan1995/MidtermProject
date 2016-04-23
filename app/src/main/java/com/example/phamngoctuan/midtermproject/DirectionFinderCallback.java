package com.example.phamngoctuan.midtermproject;

import java.util.List;

/**
 * Created by phamngoctuan on 14/04/2016.
 */
public interface DirectionFinderCallback {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);

}
