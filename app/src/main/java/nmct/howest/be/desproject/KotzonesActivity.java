package nmct.howest.be.desproject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class KotzonesActivity extends Activity implements OnMapReadyCallback {

    private TextView textViewResultaat;
    public static final String EXTRA_ARRAY_GEKOZEN_KOTZONE = "";
    public static LatLng KOTZONE_LOCATIE = new LatLng(51.05434, 3.71742);
    private GoogleMap googleMap;
    private ArrayList<double[]> listKotenLocaties = new ArrayList<>();
    private Button buttonBackKiesKotzone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kot_zones);

        // Koppel attribuut aan Control-View
        textViewResultaat = (TextView) findViewById(R.id.textViewBereikResultaat);
        buttonBackKiesKotzone = (Button) findViewById(R.id.buttonBackKiesKotzone);

        buttonBackKiesKotzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainFragment();
            }
        });
        ActionBar ab = getActionBar();
        ab.setTitle("Beschikbare studentenkoten");

        // Haal gekozen kotzone op
        Bundle args = this.getIntent().getExtras();
        String[] gekozenKotzone = args.getStringArray(EXTRA_ARRAY_GEKOZEN_KOTZONE);

        // Vul properties in variabelen
        String kotZoneId = gekozenKotzone[0];
        String kotzoneCoordinaten = gekozenKotzone[1];
        String kotzoneNaam = gekozenKotzone[2];

        // Splits coordinaten en vul ze in een lijst
        String[] coordinatenGesplitst = kotzoneCoordinaten.split(",0");
        int coordinatenCount = coordinatenGesplitst.length;

        for (int i = 0; i < coordinatenCount -1; i++) {
            String[] coordinaten = kotzoneCoordinaten.split(",0");
            String coordinaat = coordinaten[i];

            String[] coordinaatGesplitst = coordinaat.split(",");

            double dLatidude = Double.parseDouble(coordinaatGesplitst[1]);
            double dLongitude = Double.parseDouble(coordinaatGesplitst[0]);

            double[] beideCoordinaten = new double[] {
                    dLatidude, dLongitude
            };
            listKotenLocaties.add(beideCoordinaten);
        }

        double latitude = listKotenLocaties.get(0)[0];
        double longitude = listKotenLocaties.get(0)[1];

        KOTZONE_LOCATIE = new LatLng(latitude, longitude);

        // Toon properties van kotzone
        textViewResultaat.setText("Kotzone: " + kotzoneNaam);

        // Haal map op
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void openMainFragment() {
        Intent intent = new Intent(KotzonesActivity.this, MainActivity.class);
        startActivity(intent);
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
        // Al de coördinaten die in de Arraylist zitten krijgen een marker toegewezen
        for (int i = 0; i < listKotenLocaties.size(); i++) {
            LatLng pos = new LatLng(listKotenLocaties.get(i)[0], listKotenLocaties.get(i)[1]);
            googleMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title("Klik voor meer info"));
                    //.title("Latitude: " + String.valueOf(listKotenLocaties.get(i)[0] + "Longtitude: " + listKotenLocaties.get(i)[1])));
        }

        // Instellingen van de map
        UiSettings settings = googleMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        settings.setMyLocationButtonEnabled(true);

        // Zoom in met de camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KOTZONE_LOCATIE, 5));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);

        // Info Window click event
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                geefInfoAanActivity(marker);
            }
        });
    }

    private void geefInfoAanActivity(Marker marker) {
        // Positie van de geselecteerde marker
        LatLng positie = marker.getPosition();

        // Stuur positie naar MainActivity
        Intent intent = new Intent();
        intent.putExtra(MainActivity.GESELECTEERD_KOT_LATITUDE, positie.latitude);
        intent.putExtra(MainActivity.GESELECTEERD_KOT_LONGITUDE, positie.longitude);
        setResult(RESULT_OK, intent);
        finish();
    }
}