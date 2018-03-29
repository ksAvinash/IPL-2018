package works.avijay.com.ipl2018;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
            batsman1_name, batsman1_balls, batsman1_4s, batsman1_6s, batsman1_sr,
            batsman2_name, batsman2_balls, batsman2_4s, batsman2_6s, batsman2_sr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_live_match2, container, false);

        initializeViews();
        populateData();

        return view;
    }

    private void initializeViews() {
        match_description = view.findViewById(R.id.match_description);
        current_team = view.findViewById(R.id.current_team);
        current_score = view.findViewById(R.id.current_score);

        batsman1_name = view.findViewById(R.id.batsman1_name);
        batsman1_balls = view.findViewById(R.id.batsman1_balls);
        batsman1_4s = view.findViewById(R.id.batsman1_4s);
        batsman1_6s = view.findViewById(R.id.batsman1_6s);
        batsman1_sr = view.findViewById(R.id.batsman1_sr);

        batsman2_name = view.findViewById(R.id.batsman2_name);
        batsman2_balls = view.findViewById(R.id.batsman2_balls);
        batsman2_4s = view.findViewById(R.id.batsman2_4s);
        batsman2_6s = view.findViewById(R.id.batsman2_6s);
        batsman2_sr = view.findViewById(R.id.batsman2_sr);
    }


    private void populateData(){
        try {
            Cricbuzz cricbuzz = new Cricbuzz();

            Vector<HashMap<String,String>> matches = cricbuzz.matches();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();


                Map<String,Map> score = cricbuzz.livescore("20124");
                String json = gson.toJson(score);
                JSONObject data = new JSONObject(json);
                Log.d("MATCH2", data.toString());

            JSONObject match_info = data.getJSONObject("matchinfo");
                String ipl_check = match_info.getString("type");

                if(ipl_check.equals("T20")){
                    String match_status = match_info.getString("mchstate");
//                    if(match_status.equals("inprogress")){
                        match_description.setText(match_info.getString("mchdesc"));

                        JSONObject batting = data.getJSONObject("batting");
                        JSONArray team = batting.getJSONArray("team");
                        JSONArray live_score = batting.getJSONArray("score");

                        JSONObject _current_team = team.getJSONObject(0);
                        current_team.setText(_current_team.getString("team"));

                        JSONObject _current_score = live_score.getJSONObject(0);
                        current_score.setText(_current_score.getString("runs")+" - "+_current_score.getString("wickets")+"  ("+_current_score.getString("overs")+")");
//                    }else{
//                        Log.d("MATCH1", "NO ACTIVE MATCHES");
//                    }
                }else{
                    Log.d("MATCH1", "NO ODI MATCHES");
                }


        } catch (IOException e) {
            Log.d("MATCH1", e.toString());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d("MATCH1", e.toString());
            e.printStackTrace();
        }
    }

}
