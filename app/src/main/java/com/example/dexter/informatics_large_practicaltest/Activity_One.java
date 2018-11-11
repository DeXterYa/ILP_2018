package com.example.dexter.informatics_large_practicaltest;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Location;
import java.io.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dexter.informatics_large_practicaltest.Model.Markersonmap;
import com.example.dexter.informatics_large_practicaltest.Model.Time;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
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
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
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
    private int ifDownloadIcon;

    private String mapMode;

    private String geoJsonString;

    FirebaseUser firebaseUser;


    String dataNow;
    boolean a;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.access_token));
        setContentView(R.layout.acitivity_one);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        navigationButton = findViewById(R.id.navigation_button);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


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

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        String name = firebaseUser.getUid();


        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("Icons").document(firebaseUser.getUid())
                .collection("times").document("time");
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Time time = documentSnapshot.toObject(Time.class);
                        if (time != null) {
                            if (!time.getTime().equals(strDate)) {
                                DownloadIcon downloadIcon = new DownloadIcon();
                                downloadIcon.execute();
                            }
                        }
                        if(time == null) {
                            DownloadIcon downloadIcon = new DownloadIcon();
                            downloadIcon.execute();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DownloadIcon downloadIcon = new DownloadIcon();
                downloadIcon.execute();
            }
        });




    }



    private class DownloadIcon extends AsyncTask<Void,Void,Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... voids) {


                String data = "";

                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");//dd/MM/yyyy
                Date now = new Date();
                String strDate = sdfDate.format(now);
                String name = firebaseUser.getUid();


                try {

                    URL url = new URL(getURL());
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while (line != null) {
                        line = bufferedReader.readLine();
                        data = data + line;
                    }

                    JSONObject jsonObject = new JSONObject(data);
                    JSONObject jsonObject_rates = (JSONObject) jsonObject.get("rates");
                    Double SHIL = (Double) jsonObject_rates.get("SHIL");
                    Double DOLR = (Double) jsonObject_rates.get("DOLR");
                    Double QUID = (Double) jsonObject_rates.get("QUID");
                    Double PENY = (Double) jsonObject_rates.get("PENY");


                    DocumentReference documentReference = FirebaseFirestore.getInstance()
                            .collection("Icons").document(firebaseUser.getUid())
                            .collection("times").document("time");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("time", strDate);
                    documentReference.set(hashMap);

                    DocumentReference rates = FirebaseFirestore.getInstance()
                            .collection("Icons").document(firebaseUser.getUid())
                            .collection("Rates").document("rate");
                    HashMap<String, Object> hashMap_rate = new HashMap<>();
                    hashMap_rate.put("SHIL", SHIL);
                    hashMap_rate.put("DOLR", DOLR);
                    hashMap_rate.put("QUID", QUID);
                    hashMap_rate.put("PENY", PENY);
                    rates.set(hashMap_rate);


                    JSONArray features = (JSONArray) jsonObject.get("features");

                    for (int i=0; i<features.length(); i++) {
                        JSONObject jo = (JSONObject) features.get(i);

                        // Store the properties of the coins
                        JSONObject jsonObject_properties = (JSONObject) jo.get("properties");

                        String ID = (String) jsonObject_properties.get("id");
                        String VALUE = (String) jsonObject_properties.get("value");
                        String CURRENCY = (String) jsonObject_properties.get("currency");
                        String SYMBOL = (String) jsonObject_properties.get("marker-symbol");
                        String COLOR = (String) jsonObject_properties.get("marker-color");


                        JSONObject jsonObject_geometry = (JSONObject) jo.get("geometry");
                        JSONArray coordinates  = (JSONArray) jsonObject_geometry.get("coordinates");
                        Double longitude = (Double) coordinates.get(0);
                        Double latitude = (Double) coordinates.get(1);



                        DocumentReference properties = FirebaseFirestore.getInstance()
                                .collection("Icons").document(firebaseUser.getUid())
                                .collection("features").document("coins"+Integer.toString(i+1));
                        HashMap<String, Object> hashMap_features = new HashMap<>();
                        hashMap_features.put("id", ID);
                        hashMap_features.put("value", VALUE);
                        hashMap_features.put("currency", CURRENCY);
                        hashMap_features.put("symbol", SYMBOL);
                        hashMap_features.put("color", COLOR);
                        hashMap_features.put("longitude", longitude);
                        hashMap_features.put("latitude", latitude);
                        properties.set(hashMap_features);

        }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }return null;


        }

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

//            initLayerIcons();
//            addClusteredGeoJsonSource();

//            geoJsonString = getStringFromFile(Uri(getURL()), Activity_One.this);
//
//
//            GeoJsonSource source = new GeoJsonSource("geojson", geoJsonString);
//            mapboxMap.addSource(source);
//            mapboxMap.addLayer(new LineLayer("geojson", "geojson"));


            addMarkers(mapMode);


            Toast.makeText(Activity_One.this, R.string.common_google_play_services_enable_text,
                    Toast.LENGTH_SHORT).show();
        }

    }




    private  void addMarkers(String mode) {
        CollectionReference mapMarkers = FirebaseFirestore.getInstance()
                .collection("Icons").document(firebaseUser.getUid())
                .collection("features");
        mapMarkers.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        Markersonmap markersonmap = d.toObject(Markersonmap.class);
                        map.addMarker(new MarkerOptions()
                        .position(new LatLng(markersonmap.getLatitude(), markersonmap.getLongitude()))
                        .title(markersonmap.getCurrency())
                        .snippet(markersonmap.getValue()));

                    }
                }
            }
        });
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
                getResources().getDrawable(R.mipmap.letter_s)));
        map.addImage("DOLR-marker", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.mipmap.letter_d)));
        map.addImage("QUID-marker", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.mipmap.letter_q)));
        map.addImage("PENY-marker", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.mipmap.letter_p)));

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



    private static String convertStreamToString(InputStream is) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }



    public static String getStringFromFile(Uri fileUri, Context context) throws Exception {
        InputStream fin = context.getContentResolver().openInputStream(fileUri);

        String ret = convertStreamToString(fin);

        fin.close();
        return ret;
    }








    private void addClusteredGeoJsonSource() {
        // Add a new source from the GeoJSON data and set the 'cluster' option to true.
        try {
            map.addSource(
                    new GeoJsonSource("coins",
                            new URL(getURL()),
                            new GeoJsonOptions()
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
//        int[] layers = new int[] {150, 20, 0};
//
//        for (int i = 0; i < layers.length; i++) {
//            //Add clusters' SymbolLayers images
//            SymbolLayer symbolLayer = new SymbolLayer("cluster-" + i, "coins");
//
//            symbolLayer.setProperties(
//                    iconImage("quake-triangle-icon-id"),
//                    iconTranslate(new Float[] {0f, -9f})
//            );
//            Expression pointCount = toNumber(get("point_count"));
//
//            // Add a filter to the cluster layer that hides the icons based on "point_count"
//            symbolLayer.setFilter(
//                    i == 0
//                            ? all(has("point_count"),
//                            gte(pointCount, literal(layers[i]))
//                    ) : all(has("point_count"),
//                            gt(pointCount, literal(layers[i])),
//                            lt(pointCount, literal(layers[i - 1]))
//                    )
//            );
//            map.addLayer(symbolLayer);
        }

        //Add a SymbolLayer for the cluster data number point count
//        SymbolLayer count = new SymbolLayer("count", "coins");
//        count.setProperties(
//                textField(Expression.toString(get("point_count"))),
//                textSize(12f),
//                textColor(Color.BLACK),
//                textIgnorePlacement(true),
//                textAllowOverlap(true)
//        );
//        map.addLayer(count);
//    }


}
