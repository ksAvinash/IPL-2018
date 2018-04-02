package works.avijay.com.ipl2018;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import works.avijay.com.ipl2018.helper.Cricbuzz;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class LiveMatch2 extends Fragment {


    public LiveMatch2() {
        // Required empty public constructor
    }

    View view;
    TextView current_team, current_score, match_description,
            batsman1_name, batsman1_balls, batsman1_4s, batsman1_6s, batsman1_sr, batsman1_runs,
            batsman2_name, batsman2_balls, batsman2_4s, batsman2_6s, batsman2_sr, batsman2_runs,
            bowler1_name, bowler1_overs, bowler1_maidens, bowler1_runs, bowler1_wickets, bowler1_economy,
            bowler2_name, bowler2_overs, bowler2_maidens, bowler2_runs, bowler2_wickets, bowler2_economy, coming_soon;

    CardView team_score_card, batting_score_card, bowling_score_card;
    float ads_value;
    Context context;
    int match_id;
    LikeButton refresh_scores;
    InterstitialAd interstitialAd ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_live_match1, container, false);
        initializeViews();

        if(isNetworkConnected())
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    populateData();
                }
            }, 200);

        refresh_scores.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                refresh_scores.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh_scores.setEnabled(true);
                        refresh_scores.setLiked(false);
                    }
                }, 4000);
                Snackbar.make(view, "Refreshing scores..", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                populateData();
                showAd();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
            }
        });


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
            }, 3000);
        }

    }



    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void initializeViews() {
        context = getActivity().getApplicationContext();

        SharedPreferences sharedPreferences = context.getSharedPreferences("ipl_sp", Context.MODE_PRIVATE);
        match_id = sharedPreferences.getInt("match2", 0);
        ads_value = sharedPreferences.getFloat("ads", (float) 0.2);

        match_description = view.findViewById(R.id.match_description);
        current_team = view.findViewById(R.id.current_team);
        current_score = view.findViewById(R.id.current_score);

        batsman1_name = view.findViewById(R.id.batsman1_name);
        batsman1_runs = view.findViewById(R.id.batsman1_runs);
        batsman1_balls = view.findViewById(R.id.batsman1_balls);
        batsman1_4s = view.findViewById(R.id.batsman1_4s);
        batsman1_6s = view.findViewById(R.id.batsman1_6s);
        batsman1_sr = view.findViewById(R.id.batsman1_sr);

        batsman2_name = view.findViewById(R.id.batsman2_name);
        batsman2_runs = view.findViewById(R.id.batsman2_runs);
        batsman2_balls = view.findViewById(R.id.batsman2_balls);
        batsman2_4s = view.findViewById(R.id.batsman2_4s);
        batsman2_6s = view.findViewById(R.id.batsman2_6s);
        batsman2_sr = view.findViewById(R.id.batsman2_sr);


        bowler1_name = view.findViewById(R.id.bowler1_name);
        bowler1_overs = view.findViewById(R.id.bowler1_overs);
        bowler1_maidens = view.findViewById(R.id.bowler1_maidens);
        bowler1_wickets = view.findViewById(R.id.bowler1_wickets);
        bowler1_economy = view.findViewById(R.id.bowler1_economy);
        bowler1_runs = view.findViewById(R.id.bowler1_runs);

        bowler2_name = view.findViewById(R.id.bowler2_name);
        bowler2_overs = view.findViewById(R.id.bowler2_overs);
        bowler2_maidens = view.findViewById(R.id.bowler2_maidens);
        bowler2_wickets = view.findViewById(R.id.bowler2_wickets);
        bowler2_economy = view.findViewById(R.id.bowler2_economy);
        bowler2_runs = view.findViewById(R.id.bowler2_runs);

        refresh_scores = view.findViewById(R.id.refresh_scores);

        team_score_card = view.findViewById(R.id.team_score_card);
        batting_score_card = view.findViewById(R.id.batting_score_card);
        bowling_score_card = view.findViewById(R.id.bowling_score_card);

        coming_soon = view.findViewById(R.id.coming_soon);

    }



    private void populateData(){
        if(match_id == 0){
            team_score_card.setVisibility(View.GONE);
            batting_score_card.setVisibility(View.GONE);
            bowling_score_card.setVisibility(View.GONE);
            coming_soon.setText("Coming soon..!");
        }else{
            try {
                DecimalFormat format = new DecimalFormat("###.##");
                Cricbuzz cricbuzz = new Cricbuzz();
                Map<String,Map> score = cricbuzz.livescore(match_id+"");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String data = gson.toJson(score);

                JSONObject _data = new JSONObject(data);
                JSONObject matchinfo = _data.getJSONObject("matchinfo");
                match_description.setText(matchinfo.getString("mchdesc"));

                JSONObject batting = _data.getJSONObject("batting");
                JSONArray team = batting.getJSONArray("team");
                JSONObject _team = team.getJSONObject(0);
                current_team.setText(_team.getString("team"));

                JSONArray _score = batting.getJSONArray("score");
                JSONObject __score = _score.getJSONObject(0);
                current_score.setText(__score.getString("runs")+"-"+__score.getString("wickets")+" ("+__score.getString("overs")+")");

                JSONArray batsmen = batting.getJSONArray("batsman");
                JSONObject bat1 = batsmen.getJSONObject(0);
                batsman1_name.setText(bat1.getString("name"));
                batsman1_4s.setText(bat1.getString("fours"));
                batsman1_6s.setText(bat1.getString("six"));
                batsman1_balls.setText(bat1.getString("balls"));
                batsman1_runs.setText(bat1.getString("runs"));
                double bat1_sr = (float)Integer.parseInt(bat1.getString("runs")) * 100 / Integer.parseInt(bat1.getString("balls"));
                batsman1_sr.setText(format.format(bat1_sr));

                JSONObject bowling = _data.getJSONObject("bowling");
                JSONArray bowlers = bowling.getJSONArray("bowler");
                JSONObject ball1 = bowlers.getJSONObject(0);
                bowler1_name.setText(ball1.getString("name"));
                bowler1_overs.setText(ball1.getString("overs"));
                bowler1_maidens.setText(ball1.getString("maidens"));
                bowler1_wickets.setText(ball1.getString("wickets"));
                bowler1_runs.setText(ball1.getString("runs"));
                double ball1_eco = Integer.parseInt(ball1.getString("runs")) / Double.parseDouble(ball1.getString("overs"));
                bowler1_economy.setText(format.format(ball1_eco));

                JSONObject bat2 = batsmen.getJSONObject(1);
                batsman2_name.setText(bat2.getString("name"));
                batsman2_4s.setText(bat2.getString("fours"));
                batsman2_6s.setText(bat2.getString("six"));
                batsman2_balls.setText(bat2.getString("balls"));
                batsman2_runs.setText(bat2.getString("runs"));
                double bat2_sr = (float)Integer.parseInt(bat2.getString("runs")) * 100 / Integer.parseInt(bat2.getString("balls"));
                batsman2_sr.setText(format.format(bat2_sr));

                JSONObject ball2 = bowlers.getJSONObject(1);
                bowler2_name.setText(ball2.getString("name"));
                bowler2_overs.setText(ball2.getString("overs"));
                bowler2_maidens.setText(ball2.getString("maidens"));
                bowler2_wickets.setText(ball2.getString("wickets"));
                bowler2_runs.setText(ball2.getString("runs"));
                double ball2_eco = Integer.parseInt(ball2.getString("runs")) / Double.parseDouble(ball2.getString("overs"));
                bowler2_economy.setText(format.format(ball2_eco));

            } catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NumberFormatException e){
                e.printStackTrace();
            }
        }

    }







}
