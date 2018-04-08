package works.avijay.com.ipl2018;


import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

import works.avijay.com.ipl2018.helper.BackendHelper;
import works.avijay.com.ipl2018.helper.DatabaseHelper;
import works.avijay.com.ipl2018.helper.points_adapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PointsFragment extends Fragment {


    public PointsFragment() {}

    static Context context;
    View view;
    static ListView pointsList;
    static List<points_adapter> pointsAdapter = new ArrayList<>();
    static MaterialRefreshLayout materialRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_points, container, false);

        initializeViews();

        populateData();

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                if(isNetworkConnected()){
                    BackendHelper.fetch_team_stats fetch_team_stats = new BackendHelper.fetch_team_stats();
                    fetch_team_stats.execute(context, false, false, true);
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


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    private void initializeViews(){
        context = getActivity().getApplicationContext();
        pointsList = view.findViewById(R.id.pointsList);
        materialRefreshLayout = view.findViewById(R.id.swipe_refresh);
    }

    public static void populateData(){
        pointsAdapter.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseHelper helper = new DatabaseHelper(context);
                Cursor cursor = helper.getAllTeamPoints();
                while (cursor.moveToNext()){
                    pointsAdapter.add(new points_adapter(
                            cursor.getString(0),
                            cursor.getInt(4), //points
                            cursor.getInt(3), //remaining
                            (cursor.getInt(2)+cursor.getInt(1)), //played = win+loss
                            cursor.getDouble(5) //rate
                    ));
                }
                cursor.close();
                helper.close();
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


    private static void displayList(){
        ArrayAdapter<points_adapter> adapter = new myPointsAdapterClass();
        pointsList.setAdapter(adapter);
    }


    public static class myPointsAdapterClass extends ArrayAdapter<points_adapter> {

        myPointsAdapterClass() {
            super(context, R.layout.points_list_item, pointsAdapter);
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.points_list_item, parent, false);
            }
            points_adapter current = pointsAdapter.get(position);


            TextView points_team_name = itemView.findViewById(R.id.points_team_name);
            TextView points_rate = itemView.findViewById(R.id.points_rate);
            TextView points_points = itemView.findViewById(R.id.points_points);
            TextView points_remaining_matches = itemView.findViewById(R.id.points_remaining_matches);
            TextView points_played_matches = itemView.findViewById(R.id.points_played_matches);

            switch (current.getTeam_name()){
                case "KINGS XI PUNJAB":
                        points_team_name.setText("KXIP");
                    break;

                case "CHENNAI SUPER KINGS":
                    points_team_name.setText("CSK");
                    break;

                case "KOLKATA KNIGHT RIDERS":
                    points_team_name.setText("KKR");
                    break;

                case "MUMBAI INDIANS":
                    points_team_name.setText("MI");
                    break;

                case "DELHI DAREDEVILS":
                    points_team_name.setText("DD");
                    break;

                case "ROYAL CHALLENGERS BANGALORE":
                    points_team_name.setText("RCB");
                    break;

                case "RAJASTHAN ROYALS":
                    points_team_name.setText("RR");
                    break;

                case "SUNRISERS HYDERABAD":
                    points_team_name.setText("SRH");
                    break;
            }

            if(position<4){
                points_team_name.setTextColor(context.getResources().getColor(R.color.material_green));
            }else {
                points_team_name.setTextColor(context.getResources().getColor(R.color.material_red));
            }
            points_rate.setText(current.getRate()+"");
            points_points.setText(current.getPoints()+"");
            points_remaining_matches.setText(current.getRemaining()+"");
            points_played_matches.setText(current.getPlayed()+"");

            return itemView;
        }
    }


}
