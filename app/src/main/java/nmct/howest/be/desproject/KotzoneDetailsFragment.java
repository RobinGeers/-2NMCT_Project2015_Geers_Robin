package nmct.howest.be.desproject;


import android.app.ActionBar;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class KotzoneDetailsFragment extends Fragment {

    private TextView textViewDetailsAdres, textViewDetailsStad, textViewDetailsPostcode;
    private Geocoder geocoder;
    private Button buttonBack;
    private List<Address> listAddressen;

    public static final String EXTRA_MARKER_LATITUDE = "";
    public static final String EXTRA_MARKER_LONGITUDE = "";
    public static String EXTRA_GEKOZEN_KOTZONE;
    public static double EXTRA_LATITUDE = 0;
    public static double EXTRA_LONGITUDE = 0;



    public KotzoneDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewKotzoneDetails = inflater.inflate(R.layout.fragment_kotzone_details, container, false);

        textViewDetailsAdres = (TextView) viewKotzoneDetails.findViewById(R.id.textViewDetailsAdres);
        textViewDetailsStad = (TextView) viewKotzoneDetails.findViewById(R.id.textViewDetailsStad);
        textViewDetailsPostcode = (TextView) viewKotzoneDetails.findViewById(R.id.textViewDetailsPostcode);
        buttonBack = (Button) viewKotzoneDetails.findViewById(R.id.buttonBack);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle("Details studentenkot");

        if (getArguments() != null) {
            double latitude = getArguments().getDouble(EXTRA_MARKER_LATITUDE);
            double longitude = getArguments().getDouble(EXTRA_MARKER_LONGITUDE);
            EXTRA_LATITUDE = latitude;
            EXTRA_LONGITUDE = longitude;

            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openKotzonesActivity();
                }
            });



        }
        return viewKotzoneDetails;
    }

    private void openKotzonesActivity() {
        String[] gekozenKotzone = getArguments().getStringArray(EXTRA_GEKOZEN_KOTZONE);

        Bundle args = new Bundle();
        args.putStringArray(KotzonesActivity.EXTRA_ARRAY_GEKOZEN_KOTZONE, gekozenKotzone);
        Intent intent = new Intent(getActivity(), KotzonesActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }

    public static KotzoneDetailsFragment newInstance(double latitude, double longitude, String[] gekozenKotzone) {
        KotzoneDetailsFragment fragment = new KotzoneDetailsFragment();

        // Steek ontvangen parameters in Bundle (mandje)
        Bundle args = new Bundle();
        args.putDouble(EXTRA_MARKER_LATITUDE, latitude);
        args.putDouble(EXTRA_MARKER_LONGITUDE, longitude);
        args.putStringArray(EXTRA_GEKOZEN_KOTZONE, gekozenKotzone);
        fragment.setArguments(args);

        return fragment;
    }
}
