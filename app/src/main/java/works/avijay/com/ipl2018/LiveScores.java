package works.avijay.com.ipl2018;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
