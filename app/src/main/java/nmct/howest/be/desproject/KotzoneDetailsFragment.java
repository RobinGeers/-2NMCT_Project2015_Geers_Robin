package nmct.howest.be.desproject;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class KotzoneDetailsFragment extends Fragment {

    private TextView textViewDetailsStraat, textViewDetailsStad, textViewDetailsLand;
    //private Button buttonBack;
    private List<Address> listAddressen;

    public static final String EXTRA_MARKER_LATITUDE = "latitude";
    public static final String EXTRA_MARKER_LONGITUDE = "longitude";
    public static String EXTRA_GEKOZEN_KOTZONE;



    public KotzoneDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewKotzoneDetails = inflater.inflate(R.layout.fragment_kotzone_details, container, false);

        textViewDetailsStraat = (TextView) viewKotzoneDetails.findViewById(R.id.textViewDetailsStraat);
        textViewDetailsStad = (TextView) viewKotzoneDetails.findViewById(R.id.textViewDetailsStad);
        textViewDetailsLand = (TextView) viewKotzoneDetails.findViewById(R.id.textViewDetailsLand);

       // buttonBack = (Button) viewKotzoneDetails.findViewById(R.id.buttonBack);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle("Details studentenkot");

        if (getArguments() != null) {
            double latitude = getArguments().getDouble(EXTRA_MARKER_LATITUDE);
            double longitude = getArguments().getDouble(EXTRA_MARKER_LONGITUDE);


            LatLng locatie = new LatLng(latitude, longitude);
            try {
                String[] adres = getLocation(locatie);
                String straat = adres[0];
                String stad = adres[1];
                String land = adres[2];

                textViewDetailsStraat.setText(straat);
                textViewDetailsStad.setText(stad);
                textViewDetailsLand.setText(land);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

       /* buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openKotzonesActivity();
            }
        });*/

        return viewKotzoneDetails;
    }

    public String[] getLocation(LatLng latlng) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        Context context;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        addresses = geocoder.getFromLocation(latlng.latitude,latlng.longitude,1);

        String[] arr = new String[4];

        arr[0] = addresses.get(0).getAddressLine(0); // Straat + nr
        arr[1] = addresses.get(0).getAddressLine(1); // Postcode + stad
        arr[2] = addresses.get(0).getAddressLine(2); // Land
        return arr;
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
