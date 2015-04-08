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

public class RangeFragment extends Fragment {


    private SeekBar seekBarBereik;
    private TextView textViewBereik;
    private Button buttonBereik;
    private RangeListener Listener;
    public static int EXTRA_BEREIK = 30;


    public RangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            Listener = (RangeListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement RangeListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRange = inflater.inflate(R.layout.fragment_range, container, false);

        seekBarBereik = (SeekBar) viewRange.findViewById(R.id.seekBarBereik);
        textViewBereik = (TextView) viewRange.findViewById(R.id.textViewBereik);
        buttonBereik = (Button) viewRange.findViewById(R.id.buttonBereik);
        textViewBereik.setText(EXTRA_BEREIK + " km");

        // Slider event
        seekBarBereik.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                veranderLabel(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Button click event
        buttonBereik.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Listener.onRange(EXTRA_BEREIK);
            }
        });

        return viewRange;
    }

    private void veranderLabel(int progress) {
        EXTRA_BEREIK = progress;
        textViewBereik.setText(String.valueOf(progress) + " km");
    }

    public interface RangeListener {
        public void onRange(int bereik);
    }
}
