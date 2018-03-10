package works.avijay.com.ipl2018.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import works.avijay.com.ipl2018.PreviousCards;
import works.avijay.com.ipl2018.R;
import works.avijay.com.ipl2018.TeamsStatsFragment;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by avinashk on 28/02/18.
 */

public class BackendHelper {


    public static class fetch_team_stats extends AsyncTask<Object, String, String> {
        Context context;
        boolean insert;
        boolean stoprefresh;
        @Override
        protected void onPostExecute(final String str) {
            super.onPostExecute(str);

            if(str != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(str);
                            boolean response = object.getBoolean("ipl_fetch_data");
                            if (response) {
                                DatabaseHelper helper = new DatabaseHelper(context);
                                JSONObject data = object.getJSONObject("data");
                                JSONArray items = data.getJSONArray("Items");
                                if(insert){
                                    Log.d("IPL : TEAMS : ", "INSERTING DATA");
                                    for (int i = 0; i < data.getInt("Count"); i++) {
                                        JSONObject current_team = items.getJSONObject(i);
                                        helper.insertIntoTeamStats(current_team.getString("team_name"),
                                                current_team.getInt("loss"), current_team.getInt("wins"),
                                                current_team.getInt("remaining"), current_team.getInt("points"),
                                                current_team.getString("rate"), current_team.getString("team_image"),
                                                current_team.getInt("fan_count"));

                                    }
                                }else {
                                    Log.d("IPL : TEAMS : ", "UPDATING DATA");
                                    for (int i = 0; i < data.getInt("Count"); i++) {
                                        JSONObject current_team = items.getJSONObject(i);
                                        helper.updateIntoTeamStats(current_team.getString("team_name"),
                                                current_team.getInt("loss"), current_team.getInt("wins"),
                                                current_team.getInt("remaining"), current_team.getInt("points"),
                                                current_team.getString("rate"), current_team.getString("team_image"),
                                                current_team.getInt("fan_count"));
                                    }
                                }

                                helper.close();
                            } else {
                                Log.d("IPL : TEAMS : ", "Error fetching team stats");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    }).start();

                    //stop the refreshing
                    if(stoprefresh)
                        TeamsStatsFragment.populateData();

            }
        }

        @Override
        protected String doInBackground(Object... objects) {
            context = (Context) objects[0];
            insert = (boolean) objects[1];
            stoprefresh = (boolean) objects[2];

            try{
                URL url = new URL(context.getResources().getString(R.string.backend_fetch_data));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("type", "team_stats");


                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String response = serverAnswer.readLine();
                Log.d("IPL : TEAMS : ", "RESPONSE : "+response);

                os.close();
                conn.disconnect();

                return response;
            }catch (Exception e){
                Log.d("IPL : TEAMS : ", "Error fetching team stats");
                Log.d("IPL : TEAMS : ", e.toString());
            }

            return null;
        }
    }

    public static class fetch_schedule extends AsyncTask<Context, String, String> {
        Context context;

        @Override
        protected void onPostExecute(final String str) {
            super.onPostExecute(str);

            if(str != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(str);
                            boolean response = object.getBoolean("ipl_fetch_data");
                            if (response) {
                                DatabaseHelper helper = new DatabaseHelper(context);
                                JSONObject data = object.getJSONObject("data");
                                JSONArray items = data.getJSONArray("Items");
                                    for (int i = 0; i < data.getInt("Count"); i++) {
                                        JSONObject current = items.getJSONObject(i);
                                        helper.insertIntoSchedule(current.getInt("match_id"), current.getString("Date"),
                                                current.getString("team1"), current.getString("team2"), current.getString("time"),
                                                current.getString("venue")
                                        );
                                    }
                                helper.close();

                            } else {
                                Log.d("IPL : SCHEDULE : ", "Error fetching schedule");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();




            }
        }

        @Override
        protected String doInBackground(Context... contexts) {
            context = (Context) contexts[0];

            try{
                URL url = new URL(context.getResources().getString(R.string.backend_fetch_data));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("type", "schedule");


                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String response = serverAnswer.readLine();
                Log.d("IPL : SCHEDULE : ", "RESPONSE : "+response);

                os.close();
                conn.disconnect();

                return response;
            }catch (Exception e){
                Log.d("IPL : SCHEDULE : ", "Error fetching schedule");
                Log.d("IPL : SCHEDULE : ", e.toString());
            }

            return null;
        }
    }

    public static class fetch_players extends AsyncTask<Context, String, String> {
        Context context;

        @Override
        protected void onPostExecute(final String str) {
            super.onPostExecute(str);

            if(str != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(str);
                            boolean response = object.getBoolean("ipl_fetch_data");
                            if (response) {
                                DatabaseHelper helper = new DatabaseHelper(context);
                                JSONObject data = object.getJSONObject("data");
                                JSONArray items = data.getJSONArray("Items");
                                for (int i = 0; i < data.getInt("Count"); i++) {
                                    JSONObject current = items.getJSONObject(i);
                                    helper.insertIntoPlayers(current.getString("player_name"),
                                            Integer.parseInt(current.getString("player_bidding_price")), current.getBoolean("player_capped"),
                                            current.getString("player_country"), current.getString("player_team"),
                                            current.getString("player_type"), current.optString("player_image")
                                        );
                                }
                                helper.close();

                                SharedPreferences sharedPreferences = context.getSharedPreferences("ipl_sp", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("first_fetch_v2", false);
                                editor.apply();
                            } else {
                                Log.d("IPL : PLAYERS : ", "Error fetching players");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

            }
        }

        @Override
        protected String doInBackground(Context... contexts) {
            context = contexts[0];
            try{
                URL url = new URL(context.getResources().getString(R.string.backend_fetch_data));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("type", "players");


                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String response = serverAnswer.readLine();
                Log.d("IPL : PLAYERS : ", "RESPONSE : "+response);

                os.close();
                conn.disconnect();

                return response;
            }catch (Exception e){
                Log.d("IPL : PLAYERS : ", "Error fetching total players");
                Log.d("IPL : PLAYERS : ", e.toString());
            }

            return null;
        }
    }

    public static class update_fan_count extends AsyncTask<Object, String, String>{

        @Override
        protected String doInBackground(Object... objects) {
            Context context = (Context) objects[0];
            String team_name = (String) objects[1];
            try{
                URL url = new URL(context.getResources().getString(R.string.backend_update_fan_count));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("team_name", team_name);


                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String response = serverAnswer.readLine();
                Log.d("IPL : PLAYERS : ", "RESPONSE : "+response);

                os.close();
                conn.disconnect();

                return response;
            }catch (Exception e){
                Log.d("IPL : PLAYERS : ", "Error fetching total players");
                Log.d("IPL : PLAYERS : ", e.toString());
            }

            return null;
        }
    }

    public static class ask_question extends AsyncTask<Object, String, String>{

        @Override
        protected String doInBackground(Object... objects) {
            Context context = (Context) objects[0];
            String question = (String) objects[1];
            try{
                URL url = new URL(context.getResources().getString(R.string.backend_suggest_question));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("question", question);


                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String response = serverAnswer.readLine();
                Log.d("IPL : QUESTION : ", "RESPONSE : "+response);

                os.close();
                conn.disconnect();

                return response;
            }catch (Exception e){
                Log.d("IPL : QUESTION : ", "Error pushing question to backend");
                Log.d("IPL : QUESTION : ", e.toString());
            }

            return null;
        }
    }

    public static class update_card_count extends AsyncTask<Object, String, String>{

        @Override
        protected String doInBackground(Object... objects) {
            Context context = (Context) objects[0];
            String card_id = (String) objects[1];
            String action = (String) objects[2];
            try{
                URL url = new URL(context.getResources().getString(R.string.backend_update_cards_count));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("card_id", card_id);
                jsonParam.put("action", action);



                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String response = serverAnswer.readLine();
                Log.d("IPL : CARDS : ", "RESPONSE : "+response);

                os.close();
                conn.disconnect();

                return response;
            }catch (Exception e){
                Log.d("IPL : CARDS : ", "Error pushing card response to backend");
                Log.d("IPL : CARDS : ", e.toString());
            }

            return null;
        }
    }

    public static class fetch_setting extends AsyncTask<Object, String, String>{
        Context context;

        @Override
        protected void onPostExecute(final String str) {
            super.onPostExecute(str);

            if(str!=null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(str);
                            boolean response = object.getBoolean("ipl_fetch_settings");
                            if (response) {
                                JSONObject data = object.getJSONObject("data");
                                JSONObject item = data.getJSONObject("Item");

                                SharedPreferences sharedPreferences = context.getSharedPreferences("ipl_sp", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putFloat("ads", (float) item.getDouble("value"));
                                editor.commit();

                                Log.d("IPL : SETTINGS : ","SUCCESS : "+item.getDouble("value"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }


        }

        @Override
        protected String doInBackground(Object... objects) {
            context = (Context) objects[0];
            String setting = (String) objects[1];

            try{
                URL url = new URL(context.getResources().getString(R.string.backend_fetch_settings));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("setting", setting);



                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String response = serverAnswer.readLine();
                Log.d("IPL : SETTINGS : ", "RESPONSE : "+response);

                os.close();
                conn.disconnect();

                return response;
            }catch (Exception e){
                Log.d("IPL : SETTINGS : ", "Error fetching settings");
                Log.d("IPL : SETTINGS : ", e.toString());
            }

            return null;
        }
    }

    public static class fetch_cards extends AsyncTask<Object, String, String>{
        Context context;
        boolean stopRefresh;
        @Override
        protected void onPostExecute(final String str) {
            super.onPostExecute(str);
            if(str != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(str);
                            boolean response = object.getBoolean("ipl_cards_data");
                            if (response) {
                                DatabaseHelper helper = new DatabaseHelper(context);
                                JSONObject data = object.getJSONObject("data");
                                JSONArray items = data.getJSONArray("Items");
                                    Log.d("IPL : CARDS : ", "INSERTING DATA");
                                    for (int i = 0; i < data.getInt("Count"); i++) {
                                        JSONObject current_team = items.getJSONObject(i);
                                        helper.insertIntoCards(current_team.getString("card_id"),
                                                current_team.getString("card_description"), current_team.getLong("card_approved"),
                                                current_team.getLong("card_disapproved"), current_team.getString("card_image"), current_team.getString("card_type")
                                                );
                                    }

                                helper.close();
                            } else {
                                Log.d("IPL : CARDS : ", "Error fetching  cards");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();


                if(stopRefresh){
                    PreviousCards.loadCards();
                }
            }

        }
        @Override
        protected String doInBackground(Object... objects) {
            context =  (Context) objects[0];
            stopRefresh = (boolean) objects[1];
            try{
                URL url = new URL(context.getResources().getString(R.string.backend_cards_fetch));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("type", "app");


                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String response = serverAnswer.readLine();

                os.close();
                conn.disconnect();

                return response;
            }catch (Exception e){
                Log.d("IPL : CARDS : ", "Error fetching cards");
                Log.d("IPL : CARDS : ", e.toString());
            }

            return null;
        }
    }

}
