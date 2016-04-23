package com.example.phamngoctuan.midtermproject;

import java.util.ArrayList;

/**
 * Created by phamngoctuan on 23/04/2016.
 */
public interface FindLocationCallback
{
    public void onFindLocationPrepare();
    public void onFindLocationSuccess(ArrayList<LocationInfo> res);
}
