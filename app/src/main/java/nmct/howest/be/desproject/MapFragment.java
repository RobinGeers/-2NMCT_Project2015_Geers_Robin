package nmct.howest.be.desproject;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MapFragment extends Fragment {

    public static final String BEREIK = "nmct.howest.be.desproject.bereik";
    private TextView textViewResultaat;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(int bereik) {
        MapFragment fragment = new MapFragment();

        // Steek ontvangen bereik in Bundle
        Bundle args = new Bundle();
        args.putInt(BEREIK, bereik);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewMap = inflater.inflate(R.layout.fragment_map, container, false);

        textViewResultaat = (TextView) viewMap.findViewById(R.id.textViewResultaat);

        // Haal ontvangen bereik uit Bundle
        if (getArguments() != null) {
            Bundle args = getArguments();
            int bereik = args.getInt(BEREIK);
            textViewResultaat.setText(String.valueOf(bereik));
        }


        return viewMap;
    }
}
