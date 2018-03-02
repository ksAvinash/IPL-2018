package works.avijay.com.ipl2018;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    public HistoryFragment() {
        // Required empty public constructor
    }
    View view;
    ImageView winner_2017, winner_2016, winner_2015, winner_2014, winner_2012, winner_2013, winner_2011, winner_2010, winner_2009, winner_2008;
    TextView winner_2017_text, winner_2016_text, winner_2015_text, winner_2014_text, winner_2013_text, winner_2012_text, winner_2011_text, winner_2010_text, winner_2009_text, winner_2008_text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_history, container, false);

         initializeViews();


         displayData();

        return view;
    }



    public void initializeViews(){
        winner_2017 = view.findViewById(R.id.winner_2017);
        winner_2016 = view.findViewById(R.id.winner_2016);
        winner_2015 = view.findViewById(R.id.winner_2015);
        winner_2014 = view.findViewById(R.id.winner_2014);
        winner_2013 = view.findViewById(R.id.winner_2013);
        winner_2012 = view.findViewById(R.id.winner_2012);
        winner_2011 = view.findViewById(R.id.winner_2011);
        winner_2010 = view.findViewById(R.id.winner_2010);
        winner_2009 = view.findViewById(R.id.winner_2009);
        winner_2008 = view.findViewById(R.id.winner_2008);


        winner_2017_text = view.findViewById(R.id.winner_2017_text);
        winner_2016_text = view.findViewById(R.id.winner_2016_text);
        winner_2015_text = view.findViewById(R.id.winner_2015_text);
        winner_2014_text = view.findViewById(R.id.winner_2014_text);
        winner_2013_text = view.findViewById(R.id.winner_2013_text);
        winner_2012_text = view.findViewById(R.id.winner_2012_text);
        winner_2011_text = view.findViewById(R.id.winner_2011_text);
        winner_2010_text = view.findViewById(R.id.winner_2010_text);
        winner_2009_text = view.findViewById(R.id.winner_2009_text);
        winner_2008_text = view.findViewById(R.id.winner_2008_text);

    }


    public void displayData(){
        winner_2008_text.setText("2008 - RR");
        winner_2009_text.setText("2009 - Deccan Chargers");
        winner_2010_text.setText("2010 - CSK");
        winner_2011_text.setText("2011 - CSK");
        winner_2012_text.setText("2012 - KKR");
        winner_2013_text.setText("2013 - MI");
        winner_2014_text.setText("2014 - KKR");
        winner_2015_text.setText("2015 - MI");
        winner_2016_text.setText("2016 - SRH");
        winner_2017_text.setText("2017 - MI");



    }




}
