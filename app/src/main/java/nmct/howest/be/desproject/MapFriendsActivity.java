package nmct.howest.be.desproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFriendsActivity extends Activity implements OnMapReadyCallback {

    private TextView textViewResultaat;
    public static final String BEREIK = "nmct.howest.be.desproject.bereik";
    public static final LatLng DEINZE = new LatLng(50.988755, 3.5121499);
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_friends);

        textViewResultaat = (TextView) findViewById(R.id.textViewBereikResultaat);

        // Haal gekozen bereik op
        Intent intent = getIntent();
        int bereik = intent.getIntExtra(BEREIK, 30);
        textViewResultaat.setText("Bereik: " + String.valueOf(bereik) + " km");

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

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEINZE, 5));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10),4000, null);
    }
}
