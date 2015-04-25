package nmct.howest.be.desproject;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainFragment extends Fragment {


    private SeekBar seekBarBereik;
    private TextView textViewBereik;
    private Button buttonToonKoten, buttonKiesKotZone;
    private KotZoneListener Listener;
    public static String EXTRA_KOTZONE;
    private String[] gekozenKotzone;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            Listener = (KotZoneListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement KotZoneListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRange = inflater.inflate(R.layout.fragment_main, container, false);

        buttonToonKoten = (Button) viewRange.findViewById(R.id.buttonToonKoten);
        buttonKiesKotZone = (Button) viewRange.findViewById(R.id.buttonKiesKotzone);


        if (getArguments() != null) {
            gekozenKotzone = getArguments().getStringArray(EXTRA_KOTZONE);
        }

        // Button click events
        buttonKiesKotZone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Listener.onKiesKot();
            }
        });
        buttonToonKoten.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Listener.onKotGekozen(gekozenKotzone);
            }
        });

        return viewRange;
    }

    public static MainFragment newInstance(String[] kotzone) {
        MainFragment fragment = new MainFragment();

        // Steek ontvangen parameters in Bundle (mandje)
        Bundle args = new Bundle();
        args.putStringArray(EXTRA_KOTZONE, kotzone);
        fragment.setArguments(args);

        return fragment;
    }

    public interface KotZoneListener {
        public void onKiesKot();
        public void onKotGekozen(String[] kotzone);
    }
}
