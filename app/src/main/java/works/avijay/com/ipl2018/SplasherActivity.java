package works.avijay.com.ipl2018;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SafeBrowsingResponse;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;
import java.util.Date;

import works.avijay.com.ipl2018.helper.BackendHelper;
import works.avijay.com.ipl2018.helper.DatabaseHelper;

public class SplasherActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("ipl_sp", MODE_PRIVATE);
        context = getApplicationContext();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splasher);

        MobileAds.initialize(this, "ca-app-pub-9681985190789334~8534666961");
        FirebaseMessaging.getInstance().subscribeToTopic("ipl_all_users");






        boolean first_fetch = sharedPreferences.getBoolean("first_fetch_v2", true);
        if(first_fetch){
            DatabaseHelper helper = new DatabaseHelper(context);
            helper.deleteTables();

            if(isNetworkConnected()){

                BackendHelper.fetch_schedule fetch_schedule = new BackendHelper.fetch_schedule();
                fetch_schedule.execute(context, false);

                BackendHelper.fetch_team_stats fetch_team_stats = new BackendHelper.fetch_team_stats();
                fetch_team_stats.execute(context, true, false);

                BackendHelper.fetch_players fetch_players = new BackendHelper.fetch_players();
                fetch_players.execute(context);

                BackendHelper.fetch_cards fetch_cards = new BackendHelper.fetch_cards();
                fetch_cards.execute(context, false);

                BackendHelper.fetch_setting fetch_setting = new BackendHelper.fetch_setting();
                fetch_setting.execute(context, "ads");

            }
        }else if(get_previous_fetch_history()){

            if(isNetworkConnected()){
                BackendHelper.fetch_cards fetch_cards = new BackendHelper.fetch_cards();
                fetch_cards.execute(context, false);


                BackendHelper.fetch_setting fetch_setting = new BackendHelper.fetch_setting();
                fetch_setting.execute(context, "ads");

                Date current_date = new Date();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("last_fetch_date", current_date.getTime());
                editor.apply();
            }

        }else {
            Log.d("FETCH AGAIN", "NOT FETCHING ANY DATA");
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
