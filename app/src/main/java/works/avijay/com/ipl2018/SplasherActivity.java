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

import works.avijay.com.ipl2018.helper.BackendHelper;

public class SplasherActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("ipl_sp", MODE_PRIVATE);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splasher);

        MobileAds.initialize(this, "ca-app-pub-9681985190789334~8534666961");
        FirebaseMessaging.getInstance().subscribeToTopic("ipl_all_users");



        BackendHelper.fetch_cards fetch_cards = new BackendHelper.fetch_cards();
        fetch_cards.execute(getApplicationContext());


        boolean first_fetch = sharedPreferences.getBoolean("first_fetch", true);
        if(first_fetch){
            if(isNetworkConnected()){
                BackendHelper.fetch_schedule fetch_schedule = new BackendHelper.fetch_schedule();
                fetch_schedule.execute(getApplicationContext());


                BackendHelper.fetch_team_stats fetch_team_stats = new BackendHelper.fetch_team_stats();
                fetch_team_stats.execute(getApplicationContext(), true, false);


                BackendHelper.fetch_players fetch_players = new BackendHelper.fetch_players();
                fetch_players.execute(getApplicationContext());
            }
        }else {
            BackendHelper.fetch_team_stats fetch_team_stats = new BackendHelper.fetch_team_stats();
            fetch_team_stats.execute(getApplicationContext(), false, false);
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



}
