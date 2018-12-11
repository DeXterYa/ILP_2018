package com.example.dexter.informatics_large_practicaltest;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Location;
import java.io.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.*;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dexter.informatics_large_practicaltest.Model.Markersonmap;
import com.example.dexter.informatics_large_practicaltest.Model.Time;
import com.example.dexter.informatics_large_practicaltest.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
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
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Queue;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Activity_One extends FragmentActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener, MapboxMap.OnMapClickListener {
    String tag = "ActivityOne";
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

    String nameOfCoin = "";
    Double valueOfCoin = 0.0;
    Queue<String> namesOfCoins = new LinkedList<>();
    Queue<Double> valuesOfCoins = new LinkedList<>();
    Queue<String> addNamesOfCoins = new LinkedList<>();
    Queue<String> addTitlesOfCoins = new LinkedList<>();
    Queue<Double> addValuesOfCoins = new LinkedList<>();


    HashMap<String, String>coinsOnMap = new HashMap<>();
    FirebaseUser firebaseUser;
    boolean ifAddMarkers;
    boolean ifCompleteDownload;
    boolean ifCompleteAdd;
    TextView valueOfShil;
    TextView valueOfPeny;
    TextView valueOfQuid;
    TextView valueOfDolr;
    ImageView imageOfShil;
    ImageView imageOfPeny;
    ImageView imageOfQuid;
    ImageView imageOfDolr;

    Double floatValueOfDolr;
    Double floatValueOfPeny;
    Double floatValueOfQuid;
    Double floatValueOfShil;

    Double interestOfBank;




    boolean a;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ifAddMarkers = true;
        ifCompleteAdd = false;
        ifCompleteDownload = false;


        Mapbox.getInstance(this,getString(R.string.access_token));
        setContentView(R.layout.acitivity_one);


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Initialize the value shown in the screen
        valueOfDolr = findViewById(R.id.valueofdolr);
        valueOfPeny = findViewById(R.id.valueofpeny);
        valueOfQuid = findViewById(R.id.valueofquid);
        valueOfShil = findViewById(R.id.valueofshil);
        imageOfDolr = findViewById(R.id.dolr);
        imageOfPeny = findViewById(R.id.peny);
        imageOfQuid = findViewById(R.id.quid);
        imageOfShil = findViewById(R.id.shil);



        navigationButton = findViewById(R.id.navigation_button);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        navigationButton.setOnClickListener((View v) -> {

                    //Launch navigation UI
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


            });




        // Bottom Navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.navigation_map:

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

        // Get the time of today
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Date now = new Date();
        String strDate = sdfDate.format(now);



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
                            else {
                                ifCompleteDownload = true;
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

    // Change the value of collected coins
    private class ChangeCoinsValue2 extends AsyncTask<Void,Void,Void>{



        @Override
        protected Void doInBackground(Void... voids) {

                floatValueOfDolr = 0.0;
                floatValueOfPeny = 0.0;
                floatValueOfQuid = 0.0;
                floatValueOfShil = 0.0;
                CollectionReference collectionReference3 = FirebaseFirestore.getInstance()
                        .collection("Icons").document(firebaseUser.getUid())
                        .collection("features");
                collectionReference3.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                                Markersonmap markersonmap1 = d.toObject(Markersonmap.class);
                                if ((markersonmap1.getIsCollected_1() == 1)&&(markersonmap1.getIsStored() == 0)&&(markersonmap1.getIsInMarket() == 0)) {
                                    switch (markersonmap1.getCurrency()) {
                                        case "DOLR":
                                            floatValueOfDolr += Double.parseDouble(markersonmap1.getValue());
                                            break;

                                        case "PENY":
                                            floatValueOfPeny += Double.parseDouble(markersonmap1.getValue());
                                            break;

                                        case "QUID":
                                            floatValueOfQuid += Double.parseDouble(markersonmap1.getValue());
                                            break;

                                        case "SHIL":
                                            floatValueOfShil += Double.parseDouble(markersonmap1.getValue());
                                            break;

                                    }
                                }
                            }

                            valueOfDolr.setText(String.format(Locale.getDefault(),"%.2f", floatValueOfDolr));
                            valueOfPeny.setText(String.format(Locale.getDefault(),"%.2f", floatValueOfPeny));
                            valueOfQuid.setText(String.format(Locale.getDefault(),"%.2f", floatValueOfQuid));
                            valueOfShil.setText(String.format(Locale.getDefault(),"%.2f", floatValueOfShil));
                        }
                    }
                });

            return null;
        }
    }




    // Download GeoJSON file from the server and upload to the Firebase
    private class DownloadIcon extends AsyncTask<Void,Void,Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... voids) {


                String data = "";

                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                Date now = new Date();
                String strDate = sdfDate.format(now);



                try {
                    // Read the GeoJSON file from the server as String
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

                    // Get rates
                    Double SHIL = (Double) jsonObject_rates.get("SHIL");
                    Double DOLR = (Double) jsonObject_rates.get("DOLR");
                    Double QUID = (Double) jsonObject_rates.get("QUID");
                    Double PENY = (Double) jsonObject_rates.get("PENY");

                    // Update the value of GOLD with interest
                    DocumentReference documentReference_gold = FirebaseFirestore.getInstance()
                            .collection("User").document(firebaseUser.getUid());
                    documentReference_gold.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            if (documentSnapshot != null) {
                                User user = documentSnapshot.toObject(User.class);
                                if (user != null) {
                                    Double value = user.getGOLD() / 2000;
                                    interestOfBank = value.intValue() * 0.001;
                                    value = user.getGOLD() * (1 + interestOfBank);
                                    HashMap<String,Object> update = new HashMap<>();
                                    update.put("GOLD", value);
                                    documentReference_gold.update(update);
                                }
                            }
                        }
                    });

                    // Reset the coins in the shopping bag
                    DocumentReference documentReference_USER = FirebaseFirestore.getInstance()
                            .collection("User").document(firebaseUser.getUid());
                    HashMap<String, Object> hashMap_USER = new HashMap<>();
                    hashMap_USER.put("DOLR", 0.0);
                    hashMap_USER.put("PENY", 0.0);
                    hashMap_USER.put("QUID", 0.0);
                    hashMap_USER.put("SHIL", 0.0);
                    documentReference_USER.update(hashMap_USER);

                    //Upload new time
                    DocumentReference documentReference = FirebaseFirestore.getInstance()
                            .collection("Icons").document(firebaseUser.getUid())
                            .collection("times").document("time");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("time", strDate);
                    documentReference.set(hashMap);


                    // Upload the new rates
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

                    // Upload the information of 50 coins
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
                        hashMap_features.put("isCollected_1", 0);
                        hashMap_features.put("isStored", 0);
                        hashMap_features.put("isInMarket", 0);
                        properties.set(hashMap_features);

        }
                    ifCompleteDownload = true;

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
            Toast.makeText(Activity_One.this, "Cannot show the map",
                    Toast.LENGTH_SHORT).show();
        } else {
            map = mapboxMap;
            map.addOnMapClickListener(this);
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            enableLocation();


            AddMarkers addMarkers = new AddMarkers();
            addMarkers.execute();

            ChangeCoinsValue2 changeCoinsValue2 = new ChangeCoinsValue2();
            changeCoinsValue2.execute();



            Toast.makeText(Activity_One.this, R.string.common_google_play_services_enable_text,
                    Toast.LENGTH_SHORT).show();
        }

    }

    // Add markers to the map
    private class AddMarkers extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            CollectionReference mapMarkers = FirebaseFirestore.getInstance()
                    .collection("Icons").document(firebaseUser.getUid())
                    .collection("features");
            mapMarkers.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    ifAddMarkers = !ifCompleteAdd && ifCompleteDownload;
                    if (ifAddMarkers) {
                        if (queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                                Markersonmap markersonmap = d.toObject(Markersonmap.class);
                                if ((markersonmap.getIsCollected_1() == 0) && (markersonmap.getIsStored() == 0) && (markersonmap.getIsInMarket() == 0)) {
                                    switch (markersonmap.getCurrency()) {
                                        case "SHIL":
                                            coinsOnMap.put(d.getId(), "SHIL");
                                            IconFactory iconFactory1 = IconFactory.getInstance(Activity_One.this);
                                            Icon icon1 = iconFactory1.fromResource(R.mipmap.shil);
                                            map.addMarker(new MarkerOptions()
                                                    .position(new LatLng(markersonmap.getLatitude(), markersonmap.getLongitude()))
                                                    .title(d.getId())
                                                    .snippet(markersonmap.getValue())
                                                    .icon(icon1));
                                            break;

                                        case "DOLR":
                                            coinsOnMap.put(d.getId(), "DOLR");
                                            IconFactory iconFactory2 = IconFactory.getInstance(Activity_One.this);
                                            Icon icon2 = iconFactory2.fromResource(R.mipmap.dolr);
                                            map.addMarker(new MarkerOptions()
                                                    .position(new LatLng(markersonmap.getLatitude(), markersonmap.getLongitude()))
                                                    .title(d.getId())
                                                    .snippet(markersonmap.getValue())
                                                    .icon(icon2));
                                            break;

                                        case "PENY":
                                            coinsOnMap.put(d.getId(), "PENY");
                                            IconFactory iconFactory3 = IconFactory.getInstance(Activity_One.this);
                                            Icon icon3 = iconFactory3.fromResource(R.mipmap.peny);
                                            map.addMarker(new MarkerOptions()
                                                    .position(new LatLng(markersonmap.getLatitude(), markersonmap.getLongitude()))
                                                    .title(d.getId())
                                                    .snippet(markersonmap.getValue())
                                                    .icon(icon3));
                                            break;

                                        case "QUID":
                                            coinsOnMap.put(d.getId(), "QUID");
                                            IconFactory iconFactory4 = IconFactory.getInstance(Activity_One.this);
                                            Icon icon4 = iconFactory4.fromResource(R.mipmap.quid);
                                            map.addMarker(new MarkerOptions()
                                                    .position(new LatLng(markersonmap.getLatitude(), markersonmap.getLongitude()))
                                                    .title(d.getId())
                                                    .snippet(markersonmap.getValue())
                                                    .icon(icon4));
                                            break;


                                    }
                                }


                            }
                        }
                        ifCompleteAdd = true;

                    }
                }
            });
            return null;
        }
    }






    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)){
            initializeLocationEngine();
            initializeLocationLayer();
        } else {
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
            Toast.makeText(Activity_One.this, "Cannot load the map",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (map == null) {
                Toast.makeText(Activity_One.this, "Cannot load the map",
                        Toast.LENGTH_SHORT).show();
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
        if (Mapbox.getAccessToken() != null) {
            NavigationRoute.builder()
                    .accessToken(Mapbox.getAccessToken())
                    .origin(origin)
                    .destination(destination)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            if (response.body() == null) {
                                return;
                            } else if (response.body().routes().size() == 0) {
                                return;
                            }
                            DirectionsRoute currentRoute = response.body().routes().get(0);

                            if (navigationMapRoute != null) {
                                navigationMapRoute.removeRoute();
                            } else {
                                navigationMapRoute = new NavigationMapRoute(null, mapView, map);
                            }

                            navigationMapRoute.addRoute(currentRoute);

                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });
        }

    }


    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {

        locationEngine.requestLocationUpdates();

    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Log.d(tag, "[onLocationChanged] location is null");
        } else {

            // Calculate the distance between current location and different markers when location changes
            originLocation = location;
            setCameraPosition(location);
            Location currentLocation = locationEngine.getLastLocation();
            if(currentLocation != null) {

                Iterator<Marker> iter = map.getMarkers().iterator();
                while (iter.hasNext()) {
                    Marker marker = iter.next();
                    Double latitude = marker.getPosition().getLatitude();
                    Double longitude = marker.getPosition().getLongitude();
                    Location newLocation = new Location("newlocation");
                    newLocation.setLatitude(latitude);
                    newLocation.setLongitude(longitude);

                    if (currentLocation.distanceTo(newLocation) <= 25) {
                        DocumentReference documentReference = FirebaseFirestore.getInstance()
                                .collection("Icons").document(firebaseUser.getUid())
                                .collection("features").document(marker.getTitle());
                        HashMap<String, Object> check = new HashMap<>();
                        check.put("isCollected_1", 1);
                        documentReference.update(check);

                        nameOfCoin = coinsOnMap.get(marker.getTitle());
                        valueOfCoin = Double.parseDouble(marker.getSnippet());
                        namesOfCoins.add(nameOfCoin);
                        valuesOfCoins.add(valueOfCoin);

                        addNamesOfCoins.add(nameOfCoin);
                        addValuesOfCoins.add(valueOfCoin);
                        addTitlesOfCoins.add(marker.getTitle());





                        iter.remove();
                        map.removeMarker(marker);
                    }
                }
                ChangeCoinsValue2 changeCoinsValue = new ChangeCoinsValue2();
                changeCoinsValue.execute();


            }

        }

    }





    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
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

        ifCompleteAdd = false;
        ifCompleteDownload = false;


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


    // Method to get the URL of today
    private String getURL(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
//        String URL = "http://homepages.inf.ed.ac.uk/stg/coinz/"+strDate+"/coinzmap.geojson";
        return "http://homepages.inf.ed.ac.uk/stg/coinz/"+strDate+"/coinzmap.geojson";
    }









}
