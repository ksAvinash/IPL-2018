package works.avijay.com.ipl2018;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChampionsFragment extends Fragment {


    public ChampionsFragment() {
        // Required empty public constructor
    }
    View view;
    ImageView winner_2017, winner_2016, winner_2015, winner_2014, winner_2012, winner_2013, winner_2011, winner_2010, winner_2009, winner_2008;
    TextView winner_2017_text, winner_2016_text, winner_2015_text, winner_2014_text, winner_2013_text, winner_2012_text, winner_2011_text, winner_2010_text, winner_2009_text, winner_2008_text;
    Context context;
    InterstitialAd interstitialAd ;
    float ads_value;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_history, container, false);

        initializeViews();



        displayData();
        showAd();

        return view;
    }

    private void showAd() {
        if(Math.random() < ads_value){
            Toast.makeText(context, "An ad appears in a few moments", Toast.LENGTH_SHORT).show();
            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(getString(R.string.admob_interstitial_id));
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(interstitialAd.isLoaded())
                        interstitialAd.show();
                }
            }, 4000);
        }
    }



    public void initializeViews(){
        context = getActivity().getApplicationContext();

        SharedPreferences sharedPreferences = context.getSharedPreferences("ipl_sp", MODE_PRIVATE);
        ads_value = sharedPreferences.getFloat("ads", (float) 0.2);

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

        winner_2017.setScaleType(ImageView.ScaleType.CENTER_CROP);
        winner_2017.setImageDrawable(context.getResources().getDrawable(R.mipmap.image_2017));

        winner_2016.setScaleType(ImageView.ScaleType.CENTER_CROP);
        winner_2016.setImageDrawable(context.getResources().getDrawable(R.mipmap.image_2016));

        winner_2015.setScaleType(ImageView.ScaleType.CENTER_CROP);
        winner_2015.setImageDrawable(context.getResources().getDrawable(R.mipmap.image_2015));

        winner_2014.setScaleType(ImageView.ScaleType.CENTER_CROP);
        winner_2014.setImageDrawable(context.getResources().getDrawable(R.mipmap.image_2014));

        winner_2013.setScaleType(ImageView.ScaleType.CENTER_CROP);
        winner_2013.setImageDrawable(context.getResources().getDrawable(R.mipmap.image_2013));

        winner_2012.setScaleType(ImageView.ScaleType.CENTER_CROP);
        winner_2012.setImageDrawable(context.getResources().getDrawable(R.mipmap.image_2012));

        winner_2011.setScaleType(ImageView.ScaleType.CENTER_CROP);
        winner_2011.setImageDrawable(context.getResources().getDrawable(R.mipmap.image_2011));

        winner_2010.setScaleType(ImageView.ScaleType.CENTER_CROP);
        winner_2010.setImageDrawable(context.getResources().getDrawable(R.mipmap.image_2010));

        winner_2009.setScaleType(ImageView.ScaleType.CENTER_CROP);
        winner_2009.setImageDrawable(context.getResources().getDrawable(R.mipmap.image_2009));

        winner_2008.setScaleType(ImageView.ScaleType.CENTER_CROP);
        winner_2008.setImageDrawable(context.getResources().getDrawable(R.mipmap.image_2008));

    }




}
