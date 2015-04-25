package nmct.howest.be.desproject;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends FragmentActivity implements MainFragment.KotZoneListener, SelecteerKotFragment.SelecteerKotzoneListener {

    private final int REQUEST_CODE_POSITION = 1;
    public static final String GESELECTEERD_KOT_LATITUDE = "nmct.howest.be.desproject.latitude";
    public static final String GESELECTEERD_KOT_LONGITUDE = "nmct.howest.be.desproject.longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment(), "MainFragment")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onKiesKot() {
        showSelecteerKotFragment();
    }

    private void showSelecteerKotFragment() {
        Fragment selecteerKotFragment = SelecteerKotFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.container, selecteerKotFragment);
        fragmentTransaction.addToBackStack("MainFragment");

        fragmentTransaction.commit();
    }

    @Override
    public void onKotGekozen(String[] kotzone) {
        showMapFragment(kotzone);
    }


    private void showMapFragment(String[] kotzone) {
        Bundle args = new Bundle();
        args.putStringArray(KotzonesActivity.EXTRA_ARRAY_GEKOZEN_KOTZONE, kotzone);
        Intent intent = new Intent(MainActivity.this, KotzonesActivity.class);
        intent.putExtras(args);
        startActivityForResult(intent, REQUEST_CODE_POSITION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        // Controleer op welke vraag (request) we een antwoord hebben gekregen
        switch (requestCode) {
            case REQUEST_CODE_POSITION:
                switch (resultCode) {// Welk antwoord (result) hebben we gekregen?
                    case RESULT_CANCELED: Toast.makeText(MainActivity.this, "Er was geen kot geselecteerd", Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_OK:
                        double latitude = data.getDoubleExtra(GESELECTEERD_KOT_LATITUDE, 0);
                        double longitude = data.getDoubleExtra(GESELECTEERD_KOT_LONGITUDE, 0);
                        showKotzoneDetailsFragment(latitude, longitude);
                        break;
                }
                break;
        }
    }

    private void showKotzoneDetailsFragment(double latitude, double longitude) {
        Fragment detailsFragment = KotzoneDetailsFragment.newInstance(latitude, longitude);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.container, detailsFragment);
        

        fragmentTransaction.commit();
    }

    @Override
    public void onGekozenKotzone(String[] kotzone) {
        // Geef de geselecteerde kotzone door aan MainFragment
        Fragment mainFragment = MainFragment.newInstance(kotzone);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.container, mainFragment);
        fragmentTransaction.addToBackStack("SelecteerKotFragment");

        fragmentTransaction.commit();
    }
}
