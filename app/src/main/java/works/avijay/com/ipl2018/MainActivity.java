package works.avijay.com.ipl2018;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import works.avijay.com.ipl2018.helper.DatabaseHelper;
import works.avijay.com.ipl2018.helper.cards_adapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    ListView cardsList;
    List<cards_adapter> cardsAdapter = new ArrayList<>();
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cardsList = findViewById(R.id.cardsList);
        context = getApplicationContext();


        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        loadCards();
    }

    private void loadCards() {
        cardsAdapter.clear();

        DatabaseHelper helper = new DatabaseHelper(this);
        Cursor cursor = helper.getUnseenCards();
        while (cursor.moveToNext()){
            cardsAdapter.add(new cards_adapter(cursor.getString(0), cursor.getString(1),
                    cursor.getInt(2), cursor.getInt(3), cursor.getString(4)
                    ));
        }

        displayCards();
    }

    public void displayCards(){
        ArrayAdapter<cards_adapter> adapter = new myCardsAdapterClass();
        cardsList.setAdapter(adapter);
    }



    public class myCardsAdapterClass extends ArrayAdapter<cards_adapter> {

        myCardsAdapterClass() {
            super(context, R.layout.opinion_card_item, cardsAdapter);
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.opinion_card_item, parent, false);
            }
            cards_adapter current = cardsAdapter.get(position);

            ImageView card_image = itemView.findViewById(R.id.card_image);
            TextView card_description = itemView.findViewById(R.id.card_description);
            TextView like_points = itemView.findViewById(R.id.like_points);
            TextView dislike_points = itemView.findViewById(R.id.dislike_points);


            card_description.setText(current.getCard_description());
            like_points.setText(current.getCard_approved()+"");
            dislike_points.setText(current.getCard_disapproved()+"");
            Glide.with(context).load(current.getCard_image())
                    .thumbnail(0.5f)
                    .centerCrop()
                    .placeholder(R.drawable.general_player)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(card_image);


            return itemView;
        }
    }



    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if(query.length() > 0){
                            SearchPlayersFragment searchPlayersFragment = new SearchPlayersFragment();
                            Bundle fragment_agruments = new Bundle();

                            fragment_agruments.putString("search", query);
                            searchPlayersFragment.setArguments(fragment_agruments);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_activity_content, searchPlayersFragment).commit();
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(newText.length() > 0){
                            SearchPlayersFragment searchPlayersFragment = new SearchPlayersFragment();
                            Bundle fragment_agruments = new Bundle();

                            fragment_agruments.putString("search", newText);
                            searchPlayersFragment.setArguments(fragment_agruments);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_activity_content, searchPlayersFragment).commit();
                        }
                        return false;
                    }
                }
        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;

        int id = item.getItemId();

        switch (id){
            case R.id.nav_teams:
                TeamsStatsFragment teamsStatsFragment = new TeamsStatsFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_content, teamsStatsFragment).addToBackStack(null).commit();
                break;

            case R.id.nav_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.nav_share:
                String str = "https://play.google.com/store/apps/details?id=" + getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Get set watch\n\nWatch-Monitor-Opinion most of IPL-2018 in one app\nDownload:\n" + str);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case R.id.nav_history:
                HistoryFragment historyFragment = new HistoryFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_content, historyFragment).addToBackStack(null).commit();
                break;

            case R.id.nav_schedule:
                ScheduleFragment scheduleFragment = new ScheduleFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_content, scheduleFragment).addToBackStack(null).commit();
                break;

            case R.id.nav_suggestion:
                SuggestQuestionFragment suggestQuestionFragment = new SuggestQuestionFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_content, suggestQuestionFragment).commit();
                break;

            case R.id.nav_points:
                PointsFragment pointsFragment = new PointsFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_content, pointsFragment).commit();
                break;
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
