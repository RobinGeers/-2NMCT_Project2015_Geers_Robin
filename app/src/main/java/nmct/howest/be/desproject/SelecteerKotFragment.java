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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SelecteerKotFragment extends ListFragment {
    private static String urlJSON = "http://datatank.gent.be/Onderwijs&Opvoeding/Kotzones.json";
    private static final String COORDS = "Coordinates";
    private static final String ID = "Id";
    private static final String FID = "fid";
    private static final String KOTZONE = "Kotzone";
    private Context context;
    ListView listView;
    ArrayList<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();

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
        new ProgressTask(SelecteerKotFragment.this).execute();
        return viewKotzones;
    }

    private class ProgressTask extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog progressDialog;
        private ListFragment listFragment;

        private ProgressTask(ListFragment listFragment) {
            this.listFragment = listFragment;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            String[] arrProperties = new String[] { KOTZONE };
            int[] arrViews = new int[] { R.id.textViewKotzone };

            ListAdapter adapter = new SimpleAdapter(context, jsonList, R.layout.row_kotzone, arrProperties, arrViews);
            setListAdapter(adapter);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            JSONArray json = jsonParser.getJSONFromUrl(urlJSON);

            for (int i = 0; i < json.length(); i++) {
                try {
                    JSONObject obj = json.getJSONObject(i);
                    String kotZone = obj.getString(KOTZONE);

                    HashMap<String, String> map = new HashMap<String, String>();

                    // Hang property aan Hashmap Key & Value
                    map.put(KOTZONE, kotZone);

                    // Voeg hashmap toe aan lijst
                    jsonList.add(map);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
