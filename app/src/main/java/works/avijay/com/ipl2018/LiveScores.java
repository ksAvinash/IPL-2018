package works.avijay.com.ipl2018;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import works.avijay.com.ipl2018.helper.MatchesTabsAdapter;

public class LiveScores extends AppCompatActivity {


    MatchesTabsAdapter matchesTabsAdapter;

    ViewPager matchesPager;
    TabLayout matchesTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_scores);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initializeViews();


        Toast.makeText(getApplicationContext(), "Autorefresh of 10s set", Toast.LENGTH_LONG).show();

        LiveMatch1.initializeAdapterAndStartAutoRefresh();
        LiveMatch2.initializeAdapterAndStartAutoRefresh();
    }



    public void initializeViews(){
        matchesTabsAdapter = new MatchesTabsAdapter(getSupportFragmentManager());
        matchesTab = findViewById(R.id.matchesTab);
        matchesPager = findViewById(R.id.matchesPager);

        matchesPager.setAdapter(matchesTabsAdapter);
        matchesTab.setupWithViewPager(matchesPager);
    }


    @Override
    public void onBackPressed() {
        LiveMatch1.stopChats(false);
        LiveMatch2.stopChats(false);
        LiveMatch1.stopAutoRefresh();
        LiveMatch2.stopAutoRefresh();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.live_scores_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_stop_chat){
            Toast.makeText(getApplicationContext(), "Stopping chats..", Toast.LENGTH_LONG).show();

            LiveMatch1.stopChats(false);
            LiveMatch2.stopChats(false);
        }

        return super.onOptionsItemSelected(item);
    }
}
