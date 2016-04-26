package com.example.phamngoctuan.midtermproject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by phamngoctuan on 25/04/2016.
 */
public class CustomArrayAdapter extends ArrayAdapter<PhoneContact> {

    PhoneContactCallback callback = null;
    ArrayList<PhoneContact> contacts;
    LayoutInflater inflater;

    public CustomArrayAdapter(Context context, int resource) {
        super(context, resource);
        callback = (PhoneContactCallback)context;
        inflater = LayoutInflater.from(context);
        try {
            contacts = new ArrayList<>();
            contacts.add(new PhoneContact("Taxi Vinasun", "0838272727", callback));
            contacts.add(new PhoneContact("Taxi Mailinh", "0838383838", callback));
            contacts.add(new PhoneContact("Cảnh sát 113", "113", callback));
            contacts.add(new PhoneContact("Cứu hỏa 114", "114", callback));
            contacts.add(new PhoneContact("Xe cấp cứu", "115", callback));
            contacts.add(new PhoneContact("Công an thành phố", "0838291580", callback));
            contacts.add(new PhoneContact("Đường dây nóng bí thư Thăng", "0888247247", callback));
            contacts.add(new PhoneContact("Báo Tuổi trẻ", "0918033133", callback));
        }
        catch (Exception e)
        {
            Log.d("debug", e.getMessage());
        }
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView != null)
            holder = (ViewHolder)convertView.getTag();
        else
        {
            convertView = inflater.inflate(R.layout.list_location_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_name_list_dialog);
            holder.number = (TextView) convertView.findViewById(R.id.tv_number_list_dialog);
            holder.call = (ImageButton) convertView.findViewById(R.id.imb_call_list_dialog);
            convertView.setTag(holder);
        }

        final PhoneContact contact = contacts.get(position);

        holder.name.setText(contact.name);
        holder.number.setText(contact.number);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.OnPhoneContactCallback(contact.number);
            }
        });
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public PhoneContact getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

class ViewHolder
{
    public TextView name;
    public TextView number;
    public ImageButton call;
}

