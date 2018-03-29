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
public class LiveUpdatesFragment extends Fragment {


    public LiveUpdatesFragment() {
        // Required empty public constructor
    }

    TextView result, match_description, current_team, current_score, current_overs;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view =  inflater.inflate(R.layout.fragment_live_updates, container, false);


        initializeViews();

        try {
            Cricbuzz cricbuzz = new Cricbuzz();

            Vector<HashMap<String,String>> matches = cricbuzz.matches();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();


            for(int i = 0; i< matches.size(); i++){
                String id = matches.get(i).get("id");
                Map<String,Map> score = cricbuzz.livescore(id);
                String json = gson.toJson(score);
                Log.d("MATCH_ID", id);

                JSONObject data = new JSONObject(json);
                JSONObject match_info = data.getJSONObject("matchinfo");
                String ipl_check = match_info.getString("type");

                if(ipl_check.equals("ODI")){
                    String match_status = match_info.getString("mchstate");
                    if(match_status.equals("inprogress")){
                        match_description.setText(match_info.getString("mchdesc"));

                        JSONObject batting = data.getJSONObject("batting");
                        JSONArray team = batting.getJSONArray("team");
                        JSONArray live_score = batting.getJSONArray("score");

                        JSONObject _current_team = team.getJSONObject(0);
                        current_team.setText(_current_team.getString("team"));

                        JSONObject _current_score = live_score.getJSONObject(0);
                        current_score.setText(_current_score.getString("runs")+" - "+_current_score.getString("wickets")+"  ("+_current_score.getString("overs")+")");
                        break;
                    }else{
                        result.setText("No active matches");
                    }
                }else{
                    result.setText("No active matches");
                }


            }





        } catch (IOException e) {
            result.setText("Something went wrong!");
            e.printStackTrace();
        } catch (JSONException e) {
            result.setText("Something went wrong!");
            e.printStackTrace();
        }


        return view;
    }


    public void initializeViews(){
        match_description = view.findViewById(R.id.match_description);
        result = view.findViewById(R.id.result);
        current_team = view.findViewById(R.id.current_team);
        current_score = view.findViewById(R.id.current_score);


    }

}
