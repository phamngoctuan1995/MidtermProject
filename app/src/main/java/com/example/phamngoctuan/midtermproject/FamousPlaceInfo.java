package com.example.phamngoctuan.midtermproject;

import org.jsoup.nodes.Element;

/**
 * Created by phamngoctuan on 26/04/2016.
 */
public class FamousPlaceInfo extends LocationInfo{
    String link = "";
    String description = "";
    String img = "";

    @Override
    public void Parse(Element e) {
        super.Parse(e);
        address = e.attr("address");
        link = e.attr("link");
        description = e.attr("description");
        img = e.attr("img");
    }

    @Override
    public LocationInfo clone()
    {
        return new FamousPlaceInfo();
    }
}
