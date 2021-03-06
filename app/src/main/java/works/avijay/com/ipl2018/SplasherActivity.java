package works.avijay.com.ipl2018;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;
import java.util.Date;

import works.avijay.com.ipl2018.helper.BackendHelper;
import works.avijay.com.ipl2018.helper.DatabaseHelper;

public class SplasherActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReference2;
    ValueEventListener mylistener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("ipl_sp", MODE_PRIVATE);
        context = getApplicationContext();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splasher);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("match1", 0);
        editor.putInt("match2", 0);
        editor.commit();




        MobileAds.initialize(this, "ca-app-pub-9681985190789334~8534666961");
        FirebaseMessaging.getInstance().subscribeToTopic("ipl_all_users_3");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("match_type");

        databaseReference2 = firebaseDatabase.getReference("ads_value");
        mylistener2 = databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Float value = dataSnapshot.getValue(Float.class);

                SharedPreferences sharedPreferences = context.getSharedPreferences("ipl_sp", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("ads", value);
                editor.commit();
                Log.i("ADS_VALUE", value+"");
                databaseReference2.removeEventListener(mylistener2);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.i("ADS_VALUE", error.toString());
                databaseReference2.removeEventListener(mylistener2);
            }
        });


        //getCricbuzzMatches();


        boolean first_fetch = sharedPreferences.getBoolean("first_fetch_v5", true);
        if(first_fetch){
            DatabaseHelper helper = new DatabaseHelper(context);
            helper.deleteTables();
            helper.close();
            if(isNetworkConnected()){

                BackendHelper.fetch_schedule fetch_schedule = new BackendHelper.fetch_schedule();
                fetch_schedule.execute(context, false, false);

                BackendHelper.fetch_team_stats fetch_team_stats = new BackendHelper.fetch_team_stats();
                fetch_team_stats.execute(context, true, false, false);

                BackendHelper.fetch_players fetch_players = new BackendHelper.fetch_players();
                fetch_players.execute(context);

                BackendHelper.fetch_cards fetch_cards = new BackendHelper.fetch_cards();
                fetch_cards.execute(context);

            }
        }else{
            DatabaseHelper helper = new DatabaseHelper(context);
            helper.deleteTableCards();
            helper.close();

            BackendHelper.fetch_cards fetch_cards = new BackendHelper.fetch_cards();
            fetch_cards.execute(context);
        }



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplasherActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);


    }




/*

    public void getCricbuzzMatches(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    Cricbuzz cricbuzz = new Cricbuzz();
                    Vector<HashMap<String,String>> matches = cricbuzz.matches();
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String data = gson.toJson(matches);
                    Log.d("DATA", data);
                    try {
                        JSONArray matches_list = new JSONArray(data);
                        int match_id = 1;
                        for(int i=0; i<matches_list.length(); i++){
                            JSONObject match = matches_list.getJSONObject(i);
                            String mchstate = match.getString("mchstate");
                            String match_type = match.getString("type");
                            String match_srs = match.getString("srs");

                            //CHANGE TO IPL DURING 3.0 RELEASE
                            if((mchstate.equals("inprogress") || mchstate.equals("innings break")) && match_type.equals("T20") && match_srs.equals("Indian Premier League, 2018")){
                                Log.d("VALID MATCH", match.getInt("id")+"");
                                editor.putInt("match"+match_id, match.getInt("id"));
                                editor.commit();
                                match_id++;
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

    }


*/


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    private boolean get_previous_fetch_history() {
        Date current_date = new Date();
        Date previous_fetch_date = new Date(sharedPreferences.getLong("last_fetch_date", 0));
        int noOfDays = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(previous_fetch_date);
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        Date new_fetch_date = calendar.getTime();

        if(current_date.after(new_fetch_date)){
            Log.d("DATE","FETCH AGAIN YES");
            return true;
        }else{
            Log.d("DATE","FETCH AGAIN NO");
            return false;
        }

    }





}
