package nmct.howest.be.desproject;


import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;

public class SelecteerKotFragment extends ListFragment {
    final static String URL_JSON = "http://datatank.gent.be/Onderwijs&Opvoeding/Kotzones.json";
    private static final String COORDS = "Coordinates";
    private static final String ID = "Id";
    private static final String FID = "fid";
    private static final String KOTZONE = "Kotzone";
    ListView listView;
    HttpClient client;
    JSONObject json;
    private TextView textViewTest;

    public SelecteerKotFragment() {
        // Required empty public constructor
    }

    public static SelecteerKotFragment newInstance() {
        SelecteerKotFragment fragment = new SelecteerKotFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewKotzones = inflater.inflate(R.layout.fragment_selecteer_kot, container, false);

        listView = (ListView) viewKotzones.findViewById(android.R.id.list);
        textViewTest = (TextView) viewKotzones.findViewById(R.id.textViewTest);
        client = new DefaultHttpClient();
        new Read().execute("Kotzones");
        return viewKotzones;
    }

    public JSONObject getKotZone() throws ClientProtocolException, IOException, JSONException {
        StringBuilder url = new StringBuilder(URL_JSON);

        HttpGet get = new HttpGet(url.toString());
        HttpResponse response = client.execute(get);
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 200) { // Success
            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity);
            JSONArray kotZones = new JSONArray(data);
            JSONObject kotzone = kotZones.getJSONObject(0);
            return kotzone;
        }
        else {
            Toast.makeText(getActivity(), "Statuscode is not 200", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public class Read extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                json = getKotZone();
                return json.getString(strings[0]); // "coords"
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
            textViewTest.setText(s);
        }
    }
}
