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


public class MainActivity extends FragmentActivity implements MainFragment.KotZoneListener {

    private HttpClient httpClient;
    final static String URL_JSON = "http://datatank.gent.be/Onderwijs&Opvoeding/Kotzones.json";
    JSONObject json;
    private TextView textViewJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment(), "MainFragment")
                    .commit();
        }*/
        textViewJSON = (TextView) findViewById(R.id.textViewJSON);
        httpClient = new DefaultHttpClient();
        new Read().execute("Kotzones");
    }

    public JSONObject getKotZone() throws ClientProtocolException, IOException, JSONException {
        StringBuilder url = new StringBuilder(URL_JSON);

        HttpGet get = new HttpGet(url.toString());
        HttpResponse response = httpClient.execute(get);
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 200) { // Success
            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity);
            JSONObject kotZones = new JSONObject(data); // Opgehaalde json
            for (int i = 0; i < kotZones.length(); i++) {
            }

            return kotZones;
        }
        else {
            Toast.makeText(MainActivity.this, "Statuscode is not 200", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public class Read extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                json = getKotZone();
                return json.getString("Kotzones"); // "coords"
                // Roept methode onpostExecute op
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textViewJSON.setText(s);
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
    public void onKotGekozen(String kotzone) {
        showMapFragment(kotzone);
    }


    private void showMapFragment(String kotzone) {
        Intent intent = new Intent(MainActivity.this, KotzonesActivity.class);
        intent.putExtra(KotzonesActivity.KOTZONE, kotzone);
        startActivity(intent);
    }
}
