package nmct.howest.be.desproject;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class KotzonesActivity extends Activity implements OnMapReadyCallback {

    private TextView textViewResultaat, textViewLatitude, textViewLongtitude;
    public static final String KOTZONE = "nmct.howest.be.desproject.bereik";
    public static final LatLng DEINZE = new LatLng(50.988755, 3.5121499);
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    public LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = true;
    private ArrayList<double[]> listLocations = new ArrayList<>();
    double[] coordinaten1 = { 1, 2 };
    double[] coordinaten2 = { 3, 4 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kot_zones);

        textViewResultaat = (TextView) findViewById(R.id.textViewBereikResultaat);
        textViewLatitude = (TextView) findViewById(R.id.textViewLatitude);
        textViewLongtitude = (TextView) findViewById(R.id.textViewLongtitude);

        listLocations.add(0, coordinaten1);
        listLocations.add(1, coordinaten2);


        // Haal gekozen bereik op
        Intent intent = getIntent();
        String kotzone = intent.getStringExtra(KOTZONE);
        textViewResultaat.setText("Kotzone: " + String.valueOf(kotzone));

        // Maak instantie van Google Maps API
       // buildGoogleApiClient();
       // createLocationRequest();

        // Haal map op
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(DEINZE)
                .title("U bevindt zich hier."));

        // Al de coördinaten die in de Arraylist zitten krijgen een marker toegewezen
        // TODO: Profielfoto van swarm vrienden tonen op de Marker!
        for (int i = 0; i < listLocations.size(); i++) {
            LatLng pos = new LatLng(listLocations.get(i)[0], listLocations.get(i)[1]);
            googleMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title("Latitude: " + String.valueOf(listLocations.get(i)[0] + "Longtitude: " + listLocations.get(i)[1])));
        }

        // Instellingen van de map
        UiSettings settings =  googleMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        settings.setMyLocationButtonEnabled(true);

        // Zoom in met de camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEINZE, 5));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12),2000, null);
/*
        // Cirkel pulse animatie
        final Circle circle = googleMap.addCircle(new CircleOptions().center(DEINZE)
                .strokeColor(Color.parseColor("#e74c3c")).radius(10000));

        ValueAnimator vAnimator = new ValueAnimator();
        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
        vAnimator.setRepeatMode(ValueAnimator.RESTART);  /* Creëert een pulse effect */
    /*    vAnimator.setIntValues(0, 100);
        vAnimator.setDuration(4000);
        vAnimator.setEvaluator(new IntEvaluator());
        vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                circle.setRadius(animatedFraction * 100);
            }
        });
        vAnimator.start();*/
    }
/* Current location WERKT NIET
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mCurrentLocation != null) {
            textViewLatitude.setText(String.valueOf(mCurrentLocation.getLatitude()));
            textViewLongtitude.setText(String.valueOf(mCurrentLocation.getLongitude()));
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateUI();
    }

    private void updateUI() {
        textViewLatitude.setText(String.valueOf(mCurrentLocation.getLatitude()));
        textViewLongtitude.setText(String.valueOf(mCurrentLocation.getLongitude()));
        //mLastUpdateTimeTextView.setText(mLastUpdateTime);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }*/
}
