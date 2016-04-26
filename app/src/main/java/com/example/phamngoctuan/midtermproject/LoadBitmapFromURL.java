package com.example.phamngoctuan.midtermproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by phamn_000 on 4/8/2016.
 */

class LoadBitmapAsync extends AsyncTask<String, Void, Bitmap>
{
    ImageView view;

    LoadBitmapAsync(ImageView v)
    {
        view  = v;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        URL url = null;
        Bitmap bitMap = null;
        try {
            url = new URL(params[0]);
            InputStream is = url.openConnection().getInputStream();
            bitMap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitMap;
    }

    @Override
    protected void onPostExecute(Bitmap bmp) {
        if (bmp != null) {
            view.setImageBitmap(bmp);
        }

        super.onPostExecute(bmp);
    }
}
