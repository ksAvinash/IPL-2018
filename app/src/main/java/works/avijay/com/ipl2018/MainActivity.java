package works.avijay.com.ipl2018;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import works.avijay.com.ipl2018.helper.BackendHelper;
import works.avijay.com.ipl2018.helper.DatabaseHelper;
import works.avijay.com.ipl2018.helper.cards_adapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    ListView cardsList;
    List<cards_adapter> cardsAdapter = new ArrayList<>();
    Context context;
    InterstitialAd interstitialAd ;
    CountdownView mCvCountdownView;
    boolean ad_valid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cardsList = findViewById(R.id.cardsList);
        context = getApplicationContext();
        mCvCountdownView = findViewById(R.id.count_down);

        ad_valid = true;

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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCards(0);
            }
        }, 50);

        startCountDown();
        showAd();
    }


    private void showAd() {
        if(Math.random() > 0.4){
            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(getString(R.string.admob_interstitial_id));
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(interstitialAd.isLoaded())
                        interstitialAd.show();
                }
            }, 15000);
        }

    }

    public void startCountDown(){
        Date date2 = new Date();
        Date date1 = new Date(118, 3, 7, 20, 0);

        final long mills = date1.getTime() - date2.getTime();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCvCountdownView.start(mills);
            }
        }, 1000);
    }

    private void loadCards(int position) {
        cardsAdapter.clear();
        int i=0;
        DatabaseHelper helper = new DatabaseHelper(this);
        Cursor cursor = helper.getUnseenCards();
            while (cursor.moveToNext()){
                i++;
                if(i <= 20){
                    cardsAdapter.add(new cards_adapter(cursor.getString(0), cursor.getString(1),
                            cursor.getInt(2), cursor.getInt(3), cursor.getString(4),
                            cursor.getInt(5), cursor.getString(6)
                    ));
                }

            }


        displayCards(position);
    }

    public void displayCards(int position){
        ArrayAdapter<cards_adapter> adapter = new myCardsAdapterClass();
        cardsList.setAdapter(adapter);
        cardsList.setSelection(position);
    }


    public class myCardsAdapterClass extends ArrayAdapter<cards_adapter> {

        myCardsAdapterClass() {
            super(context, R.layout.opinion_card_item, cardsAdapter);
        }


        @NonNull
        @Override
        public View getView(final int position, final View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.opinion_card_item, parent, false);
            }
            final cards_adapter current = cardsAdapter.get(position);

            ImageView card_image = itemView.findViewById(R.id.card_image);
            TextView card_description = itemView.findViewById(R.id.card_description);
            TextView like_points = itemView.findViewById(R.id.like_points);
            TextView dislike_points = itemView.findViewById(R.id.dislike_points);
            final LikeButton like_icon = itemView.findViewById(R.id.like_icon);
            final LikeButton dislike_icon = itemView.findViewById(R.id.dislike_icon);
            TextView skip_card = itemView.findViewById(R.id.skip_card);
            LikeButton heart_icon = itemView.findViewById(R.id.heart_icon);

            if(current.getCard_type().equals("card")){
                heart_icon.setVisibility(View.GONE);
                like_icon.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        BackendHelper.update_card_count update_card_count = new BackendHelper.update_card_count();
                        update_card_count.execute(context, current.getCard_id(), "approve");

                        DatabaseHelper helper = new DatabaseHelper(context);
                        helper.setCardAsSeen(current.getCard_id(), 1, current.getCard_approved());

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadCards(position);
                            }
                        }, 400);

                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {

                    }
                });

                dislike_icon.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        BackendHelper.update_card_count update_card_count = new BackendHelper.update_card_count();
                        update_card_count.execute(context, current.getCard_id(), "disapprove");

                        DatabaseHelper helper = new DatabaseHelper(context);
                        helper.setCardAsSeen(current.getCard_id(),2, current.getCard_disapproved());

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadCards(position);
                            }
                        }, 400);
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {

                    }
                });

                like_icon.setLiked(false);
                dislike_icon.setLiked(false);

            }else{
                like_icon.setEnabled(false);
                heart_icon.setVisibility(View.VISIBLE);
                dislike_icon.setVisibility(View.GONE);
                dislike_points.setVisibility(View.GONE);
                heart_icon.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        BackendHelper.update_card_count update_card_count = new BackendHelper.update_card_count();
                        update_card_count.execute(context, current.getCard_id(), "approve");

                        DatabaseHelper helper = new DatabaseHelper(context);
                        helper.setCardAsSeen(current.getCard_id(), 1, current.getCard_approved());

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadCards(position);
                            }
                        }, 400);

                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                    }
                });

            }



            card_description.setText(current.getCard_description());
            like_points.setText(current.getCard_approved()+"");
            dislike_points.setText(current.getCard_disapproved()+"");
            Glide.with(context).load(current.getCard_image())
                    .thumbnail(0.5f)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(card_image);



            skip_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper helper = new DatabaseHelper(context);
                    helper.setCardAsSeen(current.getCard_id(),3, 0);
                    loadCards(position);
                }
            });

            return itemView;
        }
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
                        "Get Set Watch\n\nTrack-Support-Follow all of IPL 2018 in one app!\n\nDownload IPL 2018:\n" + str);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case R.id.nav_trophy:
                ChampionsFragment championsFragment = new ChampionsFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_content, championsFragment).addToBackStack(null).commit();
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

            case R.id.nav_rate:
                rateapp();
                break;

            case R.id.nav_previous_cards:
                PreviousCards previousCards = new PreviousCards();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_content, previousCards).addToBackStack(null).commit();
                break;

            case R.id.nav_match_updates:
                MatchHighlightsFragment matchHighlightsFragment = new MatchHighlightsFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_content, matchHighlightsFragment).commit();
                break;

            case R.id.nav_live_scores:
                LiveUpdatesFragment liveUpdatesFragment = new LiveUpdatesFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_content, liveUpdatesFragment).commit();
                break;
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void rateapp() {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }
}
