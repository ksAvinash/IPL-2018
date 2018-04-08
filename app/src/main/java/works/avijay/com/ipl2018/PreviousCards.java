package works.avijay.com.ipl2018;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.like.LikeButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import works.avijay.com.ipl2018.helper.BackendHelper;
import works.avijay.com.ipl2018.helper.DatabaseHelper;
import works.avijay.com.ipl2018.helper.cards_adapter;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousCards extends Fragment {


    public PreviousCards() {
        // Required empty public constructor
    }

    View view;
    static Context context;
    static ListView previousCardsList;
    static List<cards_adapter> cardsAdapter = new ArrayList<>();
    static DatabaseHelper helper;
    int seen_value ;
    InterstitialAd interstitialAd;
    static MaterialRefreshLayout materialRefreshLayout;
    float ads_value;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_previous_cards, container, false);

        initializeViews();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCards();
            }
        }, 500);


        BackendHelper.fetch_cards fetch_cards = new BackendHelper.fetch_cards();
        fetch_cards.execute(context, true);

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                if(isNetworkConnected()){
                    BackendHelper.fetch_cards fetch_cards = new BackendHelper.fetch_cards();
                    fetch_cards.execute(context, true);
                }else {
                    Snackbar.make(view, "Oops, No Internet connection!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    if(materialRefreshLayout.isShown())
                        materialRefreshLayout.finishRefresh();
                }


            }
        });

        showAd();
        return view;
    }

    private void showAd() {
        Log.d("ADS : VALUE : ", ads_value+"");

        if(Math.random() < ads_value){
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
            }, 8000);
        }

    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    public void initializeViews(){
        context = getActivity().getApplicationContext();
        previousCardsList = view.findViewById(R.id.previousCardsList);
        helper = new DatabaseHelper(context);
        materialRefreshLayout = view.findViewById(R.id.swipe_refresh);

        SharedPreferences sharedPreferences = context.getSharedPreferences("ipl_sp", MODE_PRIVATE);
        ads_value = sharedPreferences.getFloat("ads", (float) 0.2);

    }


    public static void loadCards() {
        cardsAdapter.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = helper.getSeenCards();
                while (cursor.moveToNext()){
                    cardsAdapter.add(new cards_adapter(cursor.getString(0), cursor.getString(1),
                            cursor.getInt(2), cursor.getInt(3), cursor.getString(4),
                            cursor.getInt(5), cursor.getString(6), cursor.getString(8),
                            cursor.getInt(7)
                    ));
                }
                cursor.close();
            }
        }).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(materialRefreshLayout.isShown())
                    materialRefreshLayout.finishRefresh();
                displayCards();
            }
        }, 200);

    }

    public static  void displayCards(){
        ArrayAdapter<cards_adapter> adapter = new mySeenCardsAdapterClass();
        previousCardsList.setAdapter(adapter);
    }


    public static  class mySeenCardsAdapterClass extends ArrayAdapter<cards_adapter> {

        mySeenCardsAdapterClass() {
            super(context, R.layout.opinion_card_item, cardsAdapter);
        }


        @SuppressLint("ResourceAsColor")
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.opinion_card_item, parent, false);
            }
            final cards_adapter current = cardsAdapter.get(position);
            DecimalFormat decimalFormat = new DecimalFormat("###.#");

            ImageView card_image = itemView.findViewById(R.id.card_image);
            TextView card_description = itemView.findViewById(R.id.card_description);
            TextView like_points = itemView.findViewById(R.id.like_points);
            TextView dislike_points = itemView.findViewById(R.id.dislike_points);
            LikeButton like_icon = itemView.findViewById(R.id.like_icon);
            LikeButton dislike_icon = itemView.findViewById(R.id.dislike_icon);
            LikeButton heart_icon = itemView.findViewById(R.id.heart_icon);
            TextView skip_card = itemView.findViewById(R.id.skip_card);

            skip_card.setText("");
            heart_icon.setVisibility(View.GONE);
            card_image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            switch (current.getCard_team()){
                case "RCB":
                    card_image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_bengaluru));
                    break;

                case "DD":
                    card_image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_delhi));
                    break;

                case "RR":
                    card_image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_rajastan));
                    break;

                case "KKR":
                    card_image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_kolkata));
                    break;

                case "CSK":
                    card_image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_chennai));
                    break;

                case "SRH":
                    card_image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_hyderabad));
                    break;

                case "MI":
                    card_image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_mumbai));
                    break;

                case "KXIP":
                    card_image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_punjab));
                    break;

                case "COMMON":
                    card_image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_common));
                    break;

                default:
                    card_image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_common));
                    break;
            }


            if(current.getCard_type().equals("card")){


                dislike_icon.setVisibility(View.VISIBLE);
                like_icon.setVisibility(View.VISIBLE);

                if(current.getCard_seen_value() == 1){
                    like_icon.setLiked(true);
                    dislike_icon.setLiked(false);
                }else if(current.getCard_seen_value() == 2){
                    like_icon.setLiked(false);
                    dislike_icon.setLiked(true);
                }

                card_description.setText(current.getCard_description());

                if(current.getCard_approved()<1000)
                    like_points.setText(current.getCard_approved()+"");
                else
                    like_points.setText(decimalFormat.format(current.getCard_approved()/1000.0)+"K");

                if(current.getCard_disapproved()<1000)
                    dislike_points.setText(current.getCard_disapproved()+"");
                else
                    dislike_points.setText(decimalFormat.format(current.getCard_disapproved()/1000.0)+"K");


                like_icon.setEnabled(false);
                dislike_icon.setEnabled(false);

            }else if(current.getCard_type().equals("photo")){
                like_icon.setEnabled(false);
                dislike_icon.setEnabled(false);

                like_icon.setVisibility(View.GONE);
                dislike_icon.setVisibility(View.GONE);

                card_description.setText("");

                if(current.getCard_approved()<1000)
                    like_points.setText(current.getCard_approved()+"");
                else
                    like_points.setText(decimalFormat.format(current.getCard_approved()/1000.0)+"K");

                dislike_points.setText("");
            }







            return itemView;
        }
    }




}