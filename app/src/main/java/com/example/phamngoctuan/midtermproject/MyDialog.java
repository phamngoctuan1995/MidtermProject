package com.example.phamngoctuan.midtermproject;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

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

        Switch sw = (Switch)dialog.findViewById(R.id.sw_near);
        Button search = (Button)dialog.findViewById(R.id.bt_search);
        Button cancel = (Button)dialog.findViewById(R.id.bt_cancel);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
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
