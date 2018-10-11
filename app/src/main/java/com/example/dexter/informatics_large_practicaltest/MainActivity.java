package com.example.dexter.informatics_large_practicaltest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.Toolbar;
import android.graphics.Color;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerOptions;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URL;

import static com.mapbox.mapboxsdk.style.expressions.Expression.all;
import static com.mapbox.mapboxsdk.style.expressions.Expression.division;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gte;
import static com.mapbox.mapboxsdk.style.expressions.Expression.has;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconTranslate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener {
    private String tag = "MainActivity";
    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }






        @Override
    public void onMapReady(MapboxMap mapboxMap) {
        if(mapboxMap == null){
            Log.d(tag, "[onMapReady] mapBox is null");
        } else {
            map = mapboxMap;
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            enableLocation();
            initLayerIcons();
            addClusteredGeoJsonSource();
            Toast.makeText(MainActivity.this, R.string.common_google_play_services_enable_text,
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)){
            Log.d(tag, "Permission are granted");
            initializeLocationEngine();
            initializeLocationLayer();
        } else {
            Log.d(tag, "Permission are not granted");
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }
    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setInterval(5000); // preferably every 5 seconds
        locationEngine.setFastestInterval(1000); // at most every second
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();


        Location lastLocation = locationEngine.getLastLocation();
        if(lastLocation != null) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);

        }

    }
    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer() {
        if (mapView == null) {
            Log.d(tag, "mapView is null");
        } else {
            if (map == null) {
                Log.d(tag, "map is null");
            } else {
                locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
                locationLayerPlugin.setLocationLayerEnabled(true);
                locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
                locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
            }
        }

    }


    private void setCameraPosition(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(),
                location.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }


    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        Log.d(tag, "[onConnected] requesting location updates");
        locationEngine.requestLocationUpdates();

    }


    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Log.d(tag, "[onLocationChanged] location is null");
        } else {
            Log.d(tag, "[onLocationChanged] location is not null");
            originLocation = location;
            setCameraPosition(location);
        }

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        // Present toast or dialog.
        Log.d(tag, "Permissions: " + permissionsToExplain.toString());
// Present toast or dialog.

    }

    @Override
    public void onPermissionResult(boolean granted) {
        Log.d(tag, "[onPermissionResult] granted == " + granted);
        if (granted) {
            enableLocation();//TODO Open a dialogue with the user
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @SuppressWarnings("MissingPermission")
    @Override
    protected void onStart(){
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
        }
        if (locationLayerPlugin !=null) {
            locationLayerPlugin.onStart();
        }
        mapView.onStart();


    }


    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationLayerPlugin !=null) {
            locationLayerPlugin.onStop();
        }
        mapView.onStop();


    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine !=null) {
            locationEngine.deactivate();
        }
        mapView.onDestroy();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void initLayerIcons() {
        map.addImage("SHIL-marker", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.mipmap.green_marker)));
        map.addImage("DOLR-marker", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.mipmap.purple_marker)));
        map.addImage("QUID-marker", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.mipmap.yellow_marker)));
        map.addImage("PENY-marker", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.mipmap.blue_marker)));

        map.addImage("quake-triangle-icon-id", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.mapbox_marker_icon_default)));
    }


    private String getURL(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        String URL = "http://homepages.inf.ed.ac.uk/stg/coinz/"+strDate+"/coinzmap.geojson";
        return URL;
    }




    private void addClusteredGeoJsonSource() {
        // Add a new source from the GeoJSON data and set the 'cluster' option to true.
        try {
            map.addSource(
                    new GeoJsonSource("coins",
                            new URL(getURL()),
                            new GeoJsonOptions()
                                    .withCluster(true)
                                    .withClusterMaxZoom(15)
                                    .withClusterRadius(50)
                    )
            );
        } catch (MalformedURLException malformedUrlException) {
            Log.e("dataClusterActivity", "Check the URL " + malformedUrlException.getMessage());
        }

        //Creating a SymbolLayer icon layer for single data/icon points
        SymbolLayer unclustered = new SymbolLayer("unclustered-points", "coins");

        unclustered.setProperties(
                iconImage("{currency}-marker")
        );

        map.addLayer(unclustered);


        // Use the earthquakes GeoJSON source to create three point ranges.
        int[] layers = new int[] {150, 20, 0};

        for (int i = 0; i < layers.length; i++) {
            //Add clusters' SymbolLayers images
            SymbolLayer symbolLayer = new SymbolLayer("cluster-" + i, "coins");

            symbolLayer.setProperties(
                    iconImage("quake-triangle-icon-id"),
                    iconTranslate(new Float[] {0f, -9f})
            );
            Expression pointCount = toNumber(get("point_count"));

            // Add a filter to the cluster layer that hides the icons based on "point_count"
                    symbolLayer.setFilter(
                    i == 0
                            ? all(has("point_count"),
                            gte(pointCount, literal(layers[i]))
                    ) : all(has("point_count"),
                            gt(pointCount, literal(layers[i])),
                            lt(pointCount, literal(layers[i - 1]))
                    )
            );
            map.addLayer(symbolLayer);
        }

        //Add a SymbolLayer for the cluster data number point count
        SymbolLayer count = new SymbolLayer("count", "coins");
        count.setProperties(
                textField(Expression.toString(get("point_count"))),
                textSize(12f),
                textColor(Color.BLACK),
                textIgnorePlacement(true),
                textAllowOverlap(true)
        );
        map.addLayer(count);
    }


}
