package works.avijay.com.ipl2018;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.like.LikeButton;

import java.util.ArrayList;
import java.util.List;

import works.avijay.com.ipl2018.helper.BackendHelper;
import works.avijay.com.ipl2018.helper.DatabaseHelper;
import works.avijay.com.ipl2018.helper.cards_adapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousCards extends Fragment {


    public PreviousCards() {
        // Required empty public constructor
    }

    View view;
    Context context;
    ListView previousCardsList;
    List<cards_adapter> cardsAdapter = new ArrayList<>();
    DatabaseHelper helper;
    int seen_value ;
    InterstitialAd interstitialAd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_previous_cards, container, false);

        initializeViews();

        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(getString(R.string.admob_interstitial_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);

        BackendHelper.fetch_cards fetch_cards = new BackendHelper.fetch_cards();
        fetch_cards.execute(context);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCards();
            }
        }, 500);


        showAd();
        return view;
    }

    private void showAd() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(interstitialAd.isLoaded())
                    interstitialAd.show();
            }
        }, 2000);


    }


    public void initializeViews(){
        context = getActivity().getApplicationContext();
        previousCardsList = view.findViewById(R.id.previousCardsList);
        helper = new DatabaseHelper(context);

    }


    private void loadCards() {
        cardsAdapter.clear();

        Cursor cursor = helper.getSeenCards();
        while (cursor.moveToNext()){
            cardsAdapter.add(new cards_adapter(cursor.getString(0), cursor.getString(1),
                    cursor.getInt(2), cursor.getInt(3), cursor.getString(4), cursor.getInt(5)
            ));


        }

        displayCards();
    }

    public void displayCards(){
        ArrayAdapter<cards_adapter> adapter = new mySeenCardsAdapterClass();
        previousCardsList.setAdapter(adapter);
    }


    public class mySeenCardsAdapterClass extends ArrayAdapter<cards_adapter> {

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

            ImageView card_image = itemView.findViewById(R.id.card_image);
            TextView card_description = itemView.findViewById(R.id.card_description);
            TextView like_points = itemView.findViewById(R.id.like_points);
            TextView dislike_points = itemView.findViewById(R.id.dislike_points);
            final LikeButton like_icon = itemView.findViewById(R.id.like_icon);
            final LikeButton dislike_icon = itemView.findViewById(R.id.dislike_icon);

            if(current.getCard_seen_value() == 1){
                like_icon.setLiked(true);
                dislike_icon.setLiked(false);
            }else if(current.getCard_seen_value() == 2){
                like_icon.setLiked(false);
                dislike_icon.setLiked(true);
            }else {
                like_icon.setLiked(false);
                dislike_icon.setLiked(false);
            }

            card_description.setText(current.getCard_description());
            like_points.setText(current.getCard_approved()+"");
            dislike_points.setText(current.getCard_disapproved()+"");
            Glide.with(context).load(current.getCard_image())
                    .thumbnail(0.5f)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(card_image);

            like_icon.setEnabled(false);
            dislike_icon.setEnabled(false);


            return itemView;
        }
    }




}
