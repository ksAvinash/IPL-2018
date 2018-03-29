package works.avijay.com.ipl2018;

import android.Manifest;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.File;
import java.io.FileOutputStream;
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
    float ads_value;
    SharedPreferences sharedPreferences;
    View view;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cardsList = findViewById(R.id.cardsList);
        context = getApplicationContext();
        mCvCountdownView = findViewById(R.id.count_down);

        sharedPreferences = getSharedPreferences("ipl_sp", MODE_PRIVATE);
        view = findViewById(android.R.id.content);


        //allow sharing cards
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


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
        }, 100);

        startCountDown();
        showAd();
    }


    private void showAd() {
        ads_value = sharedPreferences.getFloat("ads", (float) 0.2);

        Log.d("ADS : VALUE : ", ads_value+"");

        if(Math.random() < ads_value){
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
            }, 10000);
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


            final CardView card_view = itemView.findViewById(R.id.card_view);
            final ImageView card_image = itemView.findViewById(R.id.card_image);
            TextView card_description = itemView.findViewById(R.id.card_description);
            TextView like_points = itemView.findViewById(R.id.like_points);
            TextView dislike_points = itemView.findViewById(R.id.dislike_points);
            final LikeButton like_icon = itemView.findViewById(R.id.like_icon);
            final LikeButton dislike_icon = itemView.findViewById(R.id.dislike_icon);
            TextView skip_card = itemView.findViewById(R.id.skip_card);
            LikeButton heart_icon = itemView.findViewById(R.id.heart_icon);
            ImageView share_card = itemView.findViewById(R.id.share_card);


            if(current.getCard_type().equals("card")){
                card_image.setAlpha((float)0.85);

                heart_icon.setVisibility(View.GONE);

                like_icon.setVisibility(View.VISIBLE);
                dislike_icon.setVisibility(View.VISIBLE);

                like_icon.setLiked(false);
                dislike_icon.setLiked(false);

                card_description.setText(current.getCard_description());
                like_points.setText(current.getCard_approved()+"");
                dislike_points.setText(current.getCard_disapproved()+"");


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


            }else{
                card_image.setAlpha((float)1.0);


                dislike_icon.setVisibility(View.GONE);
                like_icon.setVisibility(View.GONE);



                heart_icon.setLiked(false);
                heart_icon.setVisibility(View.VISIBLE);


                card_description.setText("");
                like_points.setText(current.getCard_approved()+"");
                dislike_points.setText("");

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



            share_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        {
                            //take screenshot of the current card
                            card_view.setDrawingCacheEnabled(true);
                            Bitmap bitmap = Bitmap.createBitmap(card_view.getDrawingCache());
                            card_view.setDrawingCacheEnabled(false);



                            //store the card
                            File file = null;
                            if(bitmap != null){
                                final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
                                File dir = new File(dirPath);
                                if(!dir.exists())
                                    dir.mkdirs();
                                file = new File(dirPath, current.getCard_id()+".jpg");
                                try {
                                    FileOutputStream fOut = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                    fOut.flush();
                                    fOut.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                            //share card
                            Uri uri = Uri.fromFile(file);
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.setType("image/jpeg");
                            String str = "https://play.google.com/store/apps/details?id=" + getPackageName();
                            intent.putExtra(android.content.Intent.EXTRA_TEXT, "What do you say ?\n\nDownload IPL 2018:\n"+str);
                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                            try {
                                startActivity(Intent.createChooser(intent, "Share Card"));
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(context, "No App Available", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 909);
                            Toast.makeText(context, "Enable Storage permissions!", Toast.LENGTH_SHORT).show();


                        }
                    }else {
                        //take screenshot of the current card
                        card_view.setDrawingCacheEnabled(true);
                        Bitmap bitmap = Bitmap.createBitmap(card_view.getDrawingCache());
                        card_view.setDrawingCacheEnabled(false);



                        //store the card
                        File file = null;
                        if(bitmap != null){
                            final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
                            File dir = new File(dirPath);
                            if(!dir.exists())
                                dir.mkdirs();
                            file = new File(dirPath, current.getCard_id()+".jpg");
                            try {
                                FileOutputStream fOut = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                        //share card
                        Uri uri = Uri.fromFile(file);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("image/jpeg");
                        String str = "https://play.google.com/store/apps/details?id=" + getPackageName();
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, "What do you say ?\n\nDownload IPL 2018:\n"+str);
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        try {
                            startActivity(Intent.createChooser(intent, "Share Card"));
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context, "No App Available", Toast.LENGTH_SHORT).show();
                        }
                    }




                }
            });



            return itemView;
        }
    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fragmentManager.getBackStackEntryCount() > 0)
                super.onBackPressed();
            else{
                if(doubleBackToExitPressedOnce)
                    super.onBackPressed();

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Double tap BACK to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 1000);

            }



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
//                LiveUpdatesFragment liveUpdatesFragment = new LiveUpdatesFragment();
//                fragmentManager = getSupportFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.main_activity_content, liveUpdatesFragment).commit();

                intent = new Intent(MainActivity.this, LiveScores.class);
                startActivity(intent);
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
