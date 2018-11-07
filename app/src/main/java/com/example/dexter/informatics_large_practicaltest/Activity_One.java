package com.example.dexter.informatics_large_practicaltest;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Location;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Color;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.net.URL;
import java.util.List;
import java.net.MalformedURLException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.expressions.Expression.all;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gte;
import static com.mapbox.mapboxsdk.style.expressions.Expression.has;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconTranslate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

public class Activity_One extends FragmentActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener, MapboxMap.OnMapClickListener {
    private String tag = "ActivityOne";
    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    FirebaseUser firebaseUser2;
    private Button navigationButton;

    private Point originPosition;
    private Point destinationPosition;
    private Marker destinationMarker;
    private NavigationMapRoute navigationMapRoute;
    private static final  String TAG = "Activity_One";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.access_token));
        setContentView(R.layout.acitivity_one);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        navigationButton = findViewById(R.id.navigation_button);


        navigationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Lauch navigation UI
                    if (originLocation != null) {
                    NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                            .origin(originPosition)
                            .destination(destinationPosition)
                            .shouldSimulateRoute(true)
                            .build();
                    NavigationLauncher.startNavigation(Activity_One.this, options);
                    } else {
                        Toast.makeText(Activity_One.this, "Sorry, we can't get your location.",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });





        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.navigation_map:
//                    Intent intent1 = new Intent(Activity_One.this, Activity_One.class);
//                    intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivityForResult(intent1,0);
//                    overridePendingTransition(0,0);
                    break;

                case R.id.navigation_coins:
                    Intent intent2 = new Intent(Activity_One.this, Activity_Two.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent2,0);
                    overridePendingTransition(0,0);
                    finish();
                    break;

                case R.id.navigation_friends:
                    Intent intent3 = new Intent(Activity_One.this, Acitivity_Three.class);
                    intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent3,0);
                    overridePendingTransition(0,0);
                    finish();
                    break;
                case R.id.navigation_welcome:
                    Intent intent4 = new Intent(Activity_One.this, MainActivity.class);
                    intent4.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent4,0);
                    overridePendingTransition(0,0);
                    finish();
                    break;




            }
            return false;
        });
    }


    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        if(mapboxMap == null){
            Log.d(tag, "[onMapReady] mapBox is null");
        } else {
            map = mapboxMap;
            map.addOnMapClickListener(this);
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            enableLocation();
            initLayerIcons();
            addClusteredGeoJsonSource();
            Toast.makeText(Activity_One.this, R.string.common_google_play_services_enable_text,
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
    public void onMapClick(@NonNull LatLng point) {

        if (destinationMarker != null) {
            map.removeMarker(destinationMarker);
        }
        destinationMarker = map.addMarker(new MarkerOptions().position(point));

        destinationPosition = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        if (originLocation != null) {
            originPosition = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());
            getRoute(originPosition, destinationPosition);

            navigationButton.setEnabled(true);
            navigationButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        }else {
            Toast.makeText(Activity_One.this, "Sorry, we can't get your location.",
                    Toast.LENGTH_SHORT).show();
        }




    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Log.e(TAG,"No routes found, check right user and access token");
                            return;
                        }else if (response.body().routes().size() == 0 ) {
                            Log.e(TAG, "No routes found");
                            return;
                        }
                        DirectionsRoute currentRoute = response.body().routes().get(0);

                        if(navigationMapRoute !=null){
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, map);
                        }

                        navigationMapRoute.addRoute(currentRoute);

                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG,"Error:" + t.getMessage());

                    }
                });

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

        firebaseUser2 = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser2 == null){
            Intent intent = new Intent(Activity_One.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

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
