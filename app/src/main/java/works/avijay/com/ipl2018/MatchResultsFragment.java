package works.avijay.com.ipl2018;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import works.avijay.com.ipl2018.helper.BackendHelper;
import works.avijay.com.ipl2018.helper.DatabaseHelper;
import works.avijay.com.ipl2018.helper.schedule_list_adapter;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchResultsFragment extends Fragment implements View.OnClickListener{


    public MatchResultsFragment() {
        // Required empty public constructor
    }

    View view;
    static Context context;
    static ListView matchResultsList;
    static DatabaseHelper helper;
    float ads_value;
    ImageView team_rcb, team_csk, team_srh, team_rr, team_kkr, team_k11p, team_mi, team_dd ;
    static MaterialRefreshLayout materialRefreshLayout;
    InterstitialAd interstitialAd ;
    private static List<schedule_list_adapter> scheduleAdapter = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_schedule, container, false);

        initializeViews();
        populateData();
        showAd();


        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                if(isNetworkConnected()){
                    //delete and recreate the schedule table
                    helper.deleteTableSchedule();
                    helper.createTableSchedule();

                    //fetch the data from the backend
                    BackendHelper.fetch_schedule fetch_schedule = new BackendHelper.fetch_schedule();
                    fetch_schedule.execute(context, false, true);
                    disselectAllTeams();
                }else {
                    Snackbar.make(view, "Oops, No Internet connection!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    if(materialRefreshLayout.isShown())
                        materialRefreshLayout.finishRefresh();
                }
            }
        });



        return view;
    }


    private void showAd() {
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
            }, 4000);
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void initializeViews(){
        context = getActivity().getApplicationContext();
        matchResultsList = view.findViewById(R.id.scheduleList);

        SharedPreferences sharedPreferences = context.getSharedPreferences("ipl_sp", MODE_PRIVATE);
        ads_value = sharedPreferences.getFloat("ads", (float) 0.2);

        team_csk = view.findViewById(R.id.team_csk);
        team_rcb = view.findViewById(R.id.team_rcb);
        team_mi = view.findViewById(R.id.team_mi);
        team_kkr = view.findViewById(R.id.team_kkr);
        team_rr = view.findViewById(R.id.team_rr);
        team_srh = view.findViewById(R.id.team_srh);
        team_k11p = view.findViewById(R.id.team_k11p);
        team_dd = view.findViewById(R.id.team_dd);
        materialRefreshLayout = view.findViewById(R.id.swipe_refresh);

        team_csk.setOnClickListener(this);
        team_rcb.setOnClickListener(this);
        team_mi.setOnClickListener(this);
        team_kkr.setOnClickListener(this);
        team_rr.setOnClickListener(this);
        team_srh.setOnClickListener(this);
        team_k11p.setOnClickListener(this);
        team_dd.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        disselectAllTeams();
        switch (view.getId()){

            case R.id.team_rcb:
                team_rcb.setImageResource(R.drawable.rcb_selected);
                Snackbar.make(view, "Showing only RCB match results", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                populateTeamSpecificMatchResult("RCB");
                break;

            case R.id.team_csk:
                team_csk.setImageResource(R.drawable.csk_selected);
                Snackbar.make(view, "Showing only CSK match results", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                populateTeamSpecificMatchResult("CSK");
                break;

            case R.id.team_mi:
                team_mi.setImageResource(R.drawable.mi_selected);
                Snackbar.make(view, "Showing only MI match results", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                populateTeamSpecificMatchResult("MI");
                break;

            case R.id.team_kkr:
                team_kkr.setImageResource(R.drawable.kkr_selected);
                Snackbar.make(view, "Showing only KKR match results", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                populateTeamSpecificMatchResult("KKR");
                break;

            case R.id.team_rr:
                team_rr.setImageResource(R.drawable.rr_selected);
                Snackbar.make(view, "Showing only RR match results", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                populateTeamSpecificMatchResult("RR");
                break;

            case R.id.team_srh:
                team_srh.setImageResource(R.drawable.srh_selected);
                Snackbar.make(view, "Showing only SRH match results", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                populateTeamSpecificMatchResult("SRH");
                break;

            case R.id.team_k11p:
                team_k11p.setImageResource(R.drawable.k11p_selected);
                Snackbar.make(view, "Showing only K11P match results", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                populateTeamSpecificMatchResult("KXIP");
                break;

            case R.id.team_dd:
                team_dd.setImageResource(R.drawable.dd_selected);
                Snackbar.make(view, "Showing only DD match results", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                populateTeamSpecificMatchResult("DD");
                break;

        }

    }

    public void populateTeamSpecificMatchResult(final String team_name){
        scheduleAdapter.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                helper = new DatabaseHelper(context);
                Cursor cursor = helper.getMatchResultsByTeamName(team_name);
                while (cursor.moveToNext()) {
                    if (!cursor.getString(6).equals("")) {

                        scheduleAdapter.add(new schedule_list_adapter(
                                cursor.getInt(0), cursor.getString(1),
                                cursor.getString(2), cursor.getString(3),
                                cursor.getString(4), cursor.getString(5),
                                cursor.getString(6), cursor.getString(7),
                                cursor.getInt(8), cursor.getInt(9)
                        ));
                    }
                }
                cursor.close();
                helper.close();
            }
        }).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                displayList();
            }
        }, 50);
    }



    public static void populateData(){
        scheduleAdapter.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                helper = new DatabaseHelper(context);
                Cursor cursor = helper.getAllSchedule();
                while (cursor.moveToNext()){
                    Log.d("CURSOR", cursor.getString(6));
                    if(!cursor.getString(6).equals("")){
                        scheduleAdapter.add(new schedule_list_adapter(
                                cursor.getInt(0), cursor.getString(1),
                                cursor.getString(2), cursor.getString(3),
                                cursor.getString(4), cursor.getString(5),
                                cursor.getString(6), cursor.getString(7),
                                cursor.getInt(8), cursor.getInt(9)
                        ));
                    }

                }
                cursor.close();
            }
        }).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(materialRefreshLayout.isShown())
                    materialRefreshLayout.finishRefresh();
                displayList();

            }
        }, 300);

    }

    public static void displayList() {
        ArrayAdapter<schedule_list_adapter> adapter = new myMatchResultsAdapterClass();
        matchResultsList.setAdapter(adapter);
    }


    public void disselectAllTeams(){
        team_csk.setImageResource(R.drawable.csk_disselected);
        team_rcb.setImageResource(R.drawable.rcb_disselected);
        team_mi.setImageResource(R.drawable.mi_disselected);
        team_kkr.setImageResource(R.drawable.kkr_disselected);
        team_rr.setImageResource(R.drawable.rr_disselected);
        team_srh.setImageResource(R.drawable.srh_disselected);
        team_k11p.setImageResource(R.drawable.k11p_disselected);
        team_dd.setImageResource(R.drawable.dd_disselected);
    }



    public static class myMatchResultsAdapterClass extends ArrayAdapter<schedule_list_adapter> {

        myMatchResultsAdapterClass() {
            super(context, R.layout.schedule_item, scheduleAdapter);
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.schedule_item, parent, false);
            }
            schedule_list_adapter current = scheduleAdapter.get(position);

            ImageView team1 = itemView.findViewById(R.id.team1);
            ImageView team2 = itemView.findViewById(R.id.team2);
            TextView match_venu = itemView.findViewById(R.id.match_venue);
            TextView match_date = itemView.findViewById(R.id.match_date);

            match_venu.setText(current.getWin_description());
            match_date.setText(current.getDate());

            String win_team = current.getWin_team(), team_1 = current.getTeam1(), team_2 = current.getTeam2();


            switch (team_1){
                case "TBC":
                    team1.setImageResource(R.drawable.general_player);
                    break;

                case "CSK":
                    team1.setImageResource(R.drawable.csk_icon);
                    break;

                case "RCB":
                    team1.setImageResource(R.drawable.rcb_icon);
                    break;

                case "MI":
                    team1.setImageResource(R.drawable.mi_icon);
                    break;

                case "KXIP":
                    team1.setImageResource(R.drawable.k11_icon);

                    break;

                case "SRH":
                    team1.setImageResource(R.drawable.srh_icon);
                    break;

                case "DD":
                    team1.setImageResource(R.drawable.dd_icon);

                    break;

                case "KKR":
                    team1.setImageResource(R.drawable.kkr_icon);

                    break;

                case "RR":
                    team1.setImageResource(R.drawable.rr_icon);
                    break;
            }



            switch (team_2){
                case "TBC":
                    team2.setImageResource(R.drawable.general_player);
                    break;

                case "CSK":
                    team2.setImageResource(R.drawable.csk_icon);
                    break;

                case "RCB":
                    team2.setImageResource(R.drawable.rcb_icon);
                    break;

                case "MI":
                    team2.setImageResource(R.drawable.mi_icon);
                    break;

                case "KXIP":
                    team2.setImageResource(R.drawable.k11_icon);
                    break;

                case "SRH":
                    team2.setImageResource(R.drawable.srh_icon);

                    break;

                case "DD":
                    team2.setImageResource(R.drawable.dd_icon);

                    break;

                case "KKR":
                    team2.setImageResource(R.drawable.kkr_icon);

                    break;

                case "RR":
                    team2.setImageResource(R.drawable.rr_icon);
                    break;
            }

            switch (win_team){
                case "RCB":
                    if(team_1.equals("RCB")){
                        team2.setAlpha((float) 0.3);
                        team1.setAlpha((float) 1);
                    }else{
                        team1.setAlpha((float) 0.3);
                        team2.setAlpha((float) 1);
                    }
                    break;

                case "CSK":
                    if(team_1.equals("CSK")){
                        team2.setAlpha((float) 0.3);
                        team1.setAlpha((float) 1);
                    }else{
                        team1.setAlpha((float) 0.3);
                        team2.setAlpha((float) 1);
                    }
                    break;

                case "SRH":
                    if(team_1.equals("SRH")){
                        team2.setAlpha((float) 0.3);
                        team1.setAlpha((float) 1);
                    }else{
                        team1.setAlpha((float) 0.3);
                        team2.setAlpha((float) 1);
                    }
                    break;

                case "MI":
                    if(team_1.equals("MI")){
                        team2.setAlpha((float) 0.3);
                        team1.setAlpha((float) 1);
                    }else{
                        team1.setAlpha((float) 0.3);
                        team2.setAlpha((float) 1);
                    }
                    break;

                case "KXIP":
                    if(team_1.equals("KXIP")){
                        team2.setAlpha((float) 0.3);
                        team1.setAlpha((float) 1);
                    }else{
                        team1.setAlpha((float) 0.3);
                        team2.setAlpha((float) 1);
                    }
                    break;

                case "KKR":
                    if(team_1.equals("KKR")){
                        team2.setAlpha((float) 0.3);
                        team1.setAlpha((float) 1);
                    }else{
                        team1.setAlpha((float) 0.3);
                        team2.setAlpha((float) 1);
                    }
                    break;

                case "DD":
                    if(team_1.equals("DD")){
                        team2.setAlpha((float) 0.3);
                        team1.setAlpha((float) 1);
                    }else{
                        team1.setAlpha((float) 0.3);
                        team2.setAlpha((float) 1);
                    }
                    break;

                case "RR":
                    if(team_1.equals("RR")){
                        team2.setAlpha((float) 0.3);
                        team1.setAlpha((float) 1);
                    }else{
                        team1.setAlpha((float) 0.3);
                        team2.setAlpha((float) 1);
                    }
                    break;

                default:
                    team1.setAlpha((float) 1);
                    team2.setAlpha((float) 1);
            }




            return itemView;
        }
    }

}
