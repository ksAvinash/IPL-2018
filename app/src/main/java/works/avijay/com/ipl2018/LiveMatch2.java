package works.avijay.com.ipl2018;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import works.avijay.com.ipl2018.helper.Cricbuzz;


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
            batsman2_name, batsman2_balls, batsman2_4s, batsman2_6s, batsman2_sr, batsman2_runs;
    Context context;
    int match_id;

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

        return view;
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void initializeViews() {
        context = getActivity().getApplicationContext();

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

        SharedPreferences sharedPreferences = context.getSharedPreferences("ipl_sp", Context.MODE_PRIVATE);
        match_id = sharedPreferences.getInt("match2", 0);

    }



    private void populateData(){
        try {
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
            double bat1_sr = Integer.parseInt(bat1.getString("runs")) * 100 / Integer.parseInt(bat1.getString("balls"));
            batsman1_sr.setText(bat1_sr+"");

            JSONObject bat2 = batsmen.getJSONObject(1);
            batsman2_name.setText(bat2.getString("name"));
            batsman2_4s.setText(bat2.getString("fours"));
            batsman2_6s.setText(bat2.getString("six"));
            batsman2_balls.setText(bat2.getString("balls"));
            batsman2_runs.setText(bat2.getString("runs"));
            double bat2_sr = Integer.parseInt(bat2.getString("runs")) * 100 / Integer.parseInt(bat2.getString("balls"));
            batsman2_sr.setText(bat2_sr+"");

        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
