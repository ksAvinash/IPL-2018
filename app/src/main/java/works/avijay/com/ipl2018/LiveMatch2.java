package works.avijay.com.ipl2018;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import works.avijay.com.ipl2018.helper.Cricbuzz;
import works.avijay.com.ipl2018.helper.chat_adapter;

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
            bowler2_name, bowler2_overs, bowler2_maidens, bowler2_runs, bowler2_wickets, bowler2_economy;

    CardView team_score_card, batting_score_card, bowling_score_card;
    static float ads_value;
    static Context context;
    static int match_id;
    LikeButton refresh_scores;
    static InterstitialAd interstitialAd ;
    static EditText push_message;
    static ImageView push_icon;
    static FirebaseDatabase database;
    public static DatabaseReference myRef;
    static ListView chatList;
    private static List<chat_adapter> chatAdapter = new ArrayList<>();
    public static ValueEventListener myChats;
    static SharedPreferences sharedPreferences2;
    public static boolean receive_again = true;
    static String user_name;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_live_match1, container, false);
        initializeViews();

        receiveChatMessages(receive_again);


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


    private static void showAd() {
        if(Math.random() < 0.2){
            if(context != null){
                interstitialAd = new InterstitialAd(context);
                interstitialAd.setAdUnitId("ca-app-pub-9681985190789334/8358998025");
                AdRequest adRequest = new AdRequest.Builder().build();
                interstitialAd.loadAd(adRequest);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(interstitialAd.isLoaded())
                            interstitialAd.show();
                    }
                }, 2000);
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    private void initializeViews() {
        context = getActivity();
        final SharedPreferences sharedPreferences = context.getSharedPreferences("ipl_sp", Context.MODE_PRIVATE);
        match_id = sharedPreferences.getInt("match2", 999);
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

        chatList = view.findViewById(R.id.chatList);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("match_"+match_id);
        sharedPreferences2 = context.getSharedPreferences("ipl_profile", Context.MODE_PRIVATE);
        user_name = sharedPreferences2.getString("user_name", "");

        push_message = view.findViewById(R.id.push_message);
        push_icon = view.findViewById(R.id.push_icon);
        push_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bowling_score_card.setVisibility(View.GONE);
                if(validateUser()){
                    if(push_message.getText().length() >0){
                        String key =  myRef.push().getKey();

                        chat_adapter chat_adapter = new chat_adapter(sharedPreferences2.getString("user_name",""), push_message.getText().toString(), sharedPreferences2.getString("user_color","#42a5f5"));
                        myRef.child(key).setValue(chat_adapter);
                        push_message.setText("");
                        receiveChatMessageOnce();
                    }
                }else{
                    createuser();
                }

            }
        });

        team_score_card.setVisibility(View.GONE);
        batting_score_card.setVisibility(View.GONE);
        bowling_score_card.setVisibility(View.GONE);
    }


    private boolean validateUser(){
        int user_valid = sharedPreferences2.getInt("user_valid", 0);
        if(user_valid == 0)
            return false;
        return true;
    }


    public void createuser(){
        final EditText input = new EditText(context);
        input.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);


        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setMessage("Please select an unique USERNAME, this username which will be displayed to all users during the entire period of your chat history and believe us we are not using your information anywhere\n\nNOTE: THIS USERNAME CANNOT BE CHANGED")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(validateusername(input.getText().toString())){
                            Snackbar.make(view, "Profile created successfully", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();

                            SharedPreferences.Editor editor = sharedPreferences2.edit();
                            editor.putInt("user_valid", 1);
                            editor.putString("user_name", input.getText().toString());
                            user_name = input.getText().toString();
                            Random random = new Random();
                            int number = random.nextInt(5)+1;
                            switch (number){
                                case 1:
                                    editor.putString("user_color", "#3f51b5");
                                    break;
                                case 2:
                                    editor.putString("user_color", "#009688");
                                    break;
                                case 3:
                                    editor.putString("user_color", "#9e9e9e");
                                    break;
                                case 4:
                                    editor.putString("user_color", "#90a4ae");
                                    break;
                                case 5:
                                    editor.putString("user_color", "#512da8");
                                    break;
                            }
                            editor.commit();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setView(input)
                .show();
    }

    private boolean validateusername(String username){
        if(username.length() < 6) {
            Snackbar.make(view, "Min 6 characters for username is required", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return false;
        }else if(!username.matches("[a-z0-9_]*")){
            Snackbar.make(view, "Only a-z  0-9  _ can be used", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return false;
        }
        return true;
    }


    private static void receiveChatMessages(boolean value){
        if(value){
            showAd();
            chatAdapter.clear();
            myChats = myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        chatAdapter.add(new chat_adapter(child.child("username").getValue().toString(),
                                child.child("user_message").getValue().toString(),
                                child.child("chat_color").getValue().toString()
                        ));
                    }
                    displayMessages();
                    removeReference(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            receiveChatMessages(receive_again);
                        }
                    }, 5000);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("CHATS", "Failed to read value.", error.toException());
                    myRef.removeEventListener(myChats);
                }
            });
        }else{
            Log.d("RECEIVING MESSAGE 2 : ","LAST");
        }
    }

    public static void removeReference(boolean again){
        myRef.removeEventListener(myChats);

        if(!again)
            receive_again = false;
    }

    public static void initializeAdapterValueEventListenersDatabaseReference(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("match_"+match_id);
        receive_again = true;
        receiveChatMessages(true);
    }


    public static void receiveChatMessageOnce(){
        Log.d("RECEIVING MESSAGE 2 : ","ONCE");
        chatAdapter.clear();
        myChats = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    chatAdapter.add(new chat_adapter(child.child("username").getValue().toString(),
                            child.child("user_message").getValue().toString(),
                            child.child("chat_color").getValue().toString()
                    ));
                }
                displayMessages();
                myRef.removeEventListener(myChats);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("CHATS", "Failed to read value.", error.toException());
                myRef.removeEventListener(myChats);
            }
        });
    }


    private static void displayMessages(){
        ArrayAdapter<chat_adapter> adapter = new myChatAdapterClass();
        chatList.setAdapter(adapter);
    }



    private void populateData(){
        if(match_id != 0){
            team_score_card.setVisibility(View.VISIBLE);
            batting_score_card.setVisibility(View.VISIBLE);
            bowling_score_card.setVisibility(View.VISIBLE);

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


    public static class myChatAdapterClass extends ArrayAdapter<chat_adapter> {

        myChatAdapterClass() {
            super(context, R.layout.chat_ui, chatAdapter);
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.chat_ui, parent, false);
            }

            try {
                chat_adapter current = chatAdapter.get(position);

                TextView chat_message = itemView.findViewById(R.id.chat_message);
                chat_message.setText(current.getUser_message());


                TextView chat_user = itemView.findViewById(R.id.chat_user);
                chat_user.setText(current.getUsername());


                if(user_name.equals(current.getUsername())){
                    CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.END|Gravity.BOTTOM;
                    params.topMargin = 70;
                    params.rightMargin = 3;
                    chat_message.setLayoutParams(params);

                    CoordinatorLayout.LayoutParams params2 = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params2.gravity = Gravity.END|Gravity.TOP;
                    params2.rightMargin = 50;
                    chat_user.setLayoutParams(params2);
                }
                GradientDrawable bgShape = (GradientDrawable)chat_message.getBackground();
                bgShape.setColor(Color.parseColor(current.getChat_color()));

            }catch (IndexOutOfBoundsException e){

            }



            return itemView;
        }
    }





}
