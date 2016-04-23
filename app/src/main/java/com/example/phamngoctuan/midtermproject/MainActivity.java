package com.example.phamngoctuan.midtermproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.MatrixCursor;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements FindLocationCallback, DirectionFinderCallback, NavigationView.OnNavigationItemSelectedListener {

    private SimpleCursorAdapter mAdapter;
    static public GoogleMap mMap = null;
    static public FloatingActionButton fab = null;
    static Context context;

    void initVariables()
    {
        context = this;
        MyConstant.onClickMkDirect = new onClickMarkerDirection();
        MyConstant.onClickFabUndoDirect = new onClickFabUndoDirection();
    }

    void initDirection()
    {
        try {
            MyConstant.directionView = findViewById(R.id.direction_layer);
            MyConstant.directionSubView = findViewById(R.id.direction_layer_ori_des);
            MyConstant.directionInfoView = findViewById(R.id.direction_layer_info);

            MyConstant.directionOri = (EditText) findViewById(R.id.etOrigin);
            MyConstant.directionDes = (EditText) findViewById(R.id.etDestination);
            MyConstant.directionSearch = (Button) findViewById(R.id.btnFindPath);
            MyConstant.directionDuration = (TextView) findViewById(R.id.tvDuration);
            MyConstant.directionDistance = (TextView) findViewById(R.id.tvDistance);
        }
        catch (Exception e)
        {
            Log.d("debug", "Exception InitDirection: " + e.getMessage());
        }

        try {
            MyConstant.directionSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        if (MyConstant.isReduce)
                        {
                            MyConstant.directionView.animate().translationY(0).setDuration(500);
                            MyConstant.isReduce = false;
                            return;
                        }
                        String ori = MyConstant.directionOri.getText().toString();
                        String des = MyConstant.directionDes.getText().toString();
                        if (ori.equals("") || des.equals("")) {
                            Toast.makeText(context, "Xin nhập đầy đủ điểm bắt đầu/kết thúc", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        DirectionFinder finder = new DirectionFinder((DirectionFinderCallback) context, ori, des);
                        try {
                            finder.execute();
                        } catch (Exception e) {
                            Log.d("debug", "Exception Find derection ori-des");
                        }
                }
            });
        }
        catch (Exception e)
        {
            Log.d("debug", "listener " + e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            setSupportActionBar(toolbar);
        } catch (Exception e) {
            Log.d("debug", e.getMessage());
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            initVariables();
        }
        catch (Exception e)
        {
            Log.d("debug", "initVa " + e.getMessage ());
        }
        try {
            initDirection();
        }
        catch (Exception e)
        {
            Log.d("debug", "init direct " + e.getMessage());
        }
        try{
            initMap();
        }
        catch (Exception e)
        {
            Log.d("debug", "Initmap" + e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        final SearchView searchView;
        final MenuItem search_item = menu.findItem(R.id.action_search);
        searchView = (SearchView) search_item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                FindLocation find = new FindLocation((FindLocationCallback) context, query);
                try {
                    Log.d("debug", "Submit query");
                    find.find();
                } catch (Exception e) {
                    Log.d("debug", "Exception query " + e.getMessage());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });

        String[] columnNames = {"_id", "text"};
        MatrixCursor cursor = new MatrixCursor(columnNames);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_location) {
            MyDialog.ShowDialogLocation(context);
        } else if (id == R.id.nav_direction) {
            MyConstant.resetDirectionView();
            MyConstant.directionMode();
            MyConstant.directionView.animate().translationY(0);

            if (MyConstant.isReduce)
            {
                MyConstant.isReduce = false;
            }

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        LatLng benthanh = new LatLng(10.7725986, 106.697616);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(benthanh, 14));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setOnMarkerClickListener(MyConstant.onClickMkDirect);
    }

    @Override
    public void onFindLocationPrepare() {

    }

    @Override
    public void onFindLocationSuccess(ArrayList<LocationInfo> res) {
        mMap.clear();
        for (int i = 0; i < res.size(); ++i)
        {
            LocationInfo loc = res.get(i);
            loc.addMarkerToMap(mMap);
        }
    }

    @Override
    public void onDirectionFinderStart() {
        mMap.clear();
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {
        if (route.size() < 1)
            return;
        for (int i = 0; i < route.size(); ++i)
            route.get(i).addToMap(mMap);
        MyConstant.directionMode();
        MyConstant.directionView.animate().translationY(-MyConstant.directionSubView.getHeight()).setDuration(500);
        MyConstant.isReduce = true;
    }
}

