package works.avijay.com.ipl2018;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
//import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import works.avijay.com.ipl2018.helper.BackendHelper;
import works.avijay.com.ipl2018.helper.DatabaseHelper;
import works.avijay.com.ipl2018.helper.teams_list_adapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamsStatsFragment extends Fragment{

    public TeamsStatsFragment() {
        // Required empty public constructor
    }

    View view;
    static Context context, activity;
    static ListView teamsList;
    static DatabaseHelper helper;
    private static List<teams_list_adapter> teamAdapter = new ArrayList<>();
    static MaterialRefreshLayout materialRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_teams, container, false);


        initializeViews();
        populateData();

        Toast.makeText(context, "Swipe to refresh contents", Toast.LENGTH_SHORT).show();


        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
             @Override
             public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                if(isNetworkConnected()){
                    BackendHelper.fetch_team_stats fetch_team_stats = new BackendHelper.fetch_team_stats();
                    fetch_team_stats.execute(context, false, true);
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



    public void initializeViews(){
        context = getActivity().getApplicationContext();
        activity = getActivity();
        teamsList = view.findViewById(R.id.teamsList);
        helper = new DatabaseHelper(context);
        materialRefreshLayout = view.findViewById(R.id.swipe_refresh);

    }



    public static void populateData(){
        teamAdapter.clear();
        if(materialRefreshLayout.isShown())
            materialRefreshLayout.finishRefresh();


        Cursor cursor = helper.getAllTeamStats();
        while (cursor.moveToNext()){
            teamAdapter.add(new teams_list_adapter(
                    cursor.getString(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7)
                    ));
        }

        displayList();
    }



    public static void displayList(){
        ArrayAdapter<teams_list_adapter> adapter = new myTeamsAdapterClass();
        teamsList.setAdapter(adapter);
        teamsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                teams_list_adapter current = teamAdapter.get(position);
                Intent intent = new Intent(context, TeamActivity.class);
                    intent.putExtra("team_name", current.getTeam_name());
                    intent.putExtra("wins", current.getWins());
                    intent.putExtra("loss", current.getLoss());
                    intent.putExtra("remaining", current.getRemaining());
                    intent.putExtra("team_image", current.getTeam_image());
                    intent.putExtra("rate", current.getRate());
                    intent.putExtra("points", current.getPoints());
                    intent.putExtra("fan_count", current.getFan_count());
                activity.startActivity(intent);


            }

        });
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static class myTeamsAdapterClass extends ArrayAdapter<teams_list_adapter> {

        myTeamsAdapterClass() {
            super(context, R.layout.team_stats_item, teamAdapter);
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.team_stats_item, parent, false);
            }
            teams_list_adapter current = teamAdapter.get(position);

            TextView fan_count = itemView.findViewById(R.id.fan_count);
            fan_count.setText(current.getFan_count()+"");


            ImageView team_image = itemView.findViewById(R.id.team_image);
            Glide.with(context).load(current.getTeam_image())
                    .thumbnail(0.5f)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(team_image);


            return itemView;
        }
    }


}
