package nmct.howest.be.desproject;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
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
import android.widget.SimpleCursorAdapter;
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

import nmct.howest.be.desproject.Loader.Contract;
import nmct.howest.be.desproject.Loader.KotzonesLoader;

public class SelecteerKotFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView textViewKotzoneNaam;
    private KotzonesAdapter mAdapter;
    private SelecteerKotzoneListener Listener;

    public SelecteerKotFragment() {
        // Required empty public constructor
    }

    public class KotzonesAdapter extends SimpleCursorAdapter {

        public KotzonesAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public void bindView(View row, Context context, Cursor cursor) {
            super.bindView(row, context, cursor);

            ViewHolder holder = (ViewHolder) row.getTag();

            if (holder == null) {
                holder = new ViewHolder(row);
                row.setTag(holder);
            }

            // Koppel attributen aan Control-View van Row!!!!
            textViewKotzoneNaam = holder.textViewKotzoneNaam;
        }

        class ViewHolder {
            public TextView textViewKotzoneNaam = null;

            public ViewHolder(View row) {
                this.textViewKotzoneNaam = (TextView) row.findViewById(R.id.textViewKotzoneNaam);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] arrColumnNames = new String[] {
                Contract.KotzonesColumns.COLUMN_KOTZONES_NAAM
        };

        int[] arrViews = new int[] {
                R.id.textViewKotzoneNaam
        };

        // Koppel gegevens aan Adapter, koppel daarna Adapter aan ListView
        mAdapter = new KotzonesAdapter(getActivity(), R.layout.row_kotzone, null, arrColumnNames, arrViews, 0);
        setListAdapter(mAdapter);

        // Data inladen via Loader
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new KotzonesLoader(getActivity());
        // GEEN LOADINBACKGROUND GEBRUIKEN -> Anders is het niet Asynchroon
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        // Koppel cursor aan Adapter
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // Ledig de Adapter
        mAdapter.swapCursor(null);
    }

    public static SelecteerKotFragment newInstance() {
        SelecteerKotFragment fragment = new SelecteerKotFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Controleren of Activity de Listener implementeerd
        try {
            Listener = (SelecteerKotzoneListener) activity;
        }
        catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + " must implement SelecteerKotzoneListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewKotzones = inflater.inflate(R.layout.fragment_selecteer_kot, container, false);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle("Kotzones Gent");
        return viewKotzones;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Geef geselecteerde kotzone mee aan MainActivity
        String[] kotzone = KotzonesLoader.getListKotzones().get(position);
        Listener.onGekozenKotzone(kotzone);

    }

    public interface SelecteerKotzoneListener {
        public void onGekozenKotzone(String[] kotzone);
    }
}
