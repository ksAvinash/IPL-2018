package works.avijay.com.ipl2018;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

import works.avijay.com.ipl2018.helper.BackendHelper;
import works.avijay.com.ipl2018.helper.DatabaseHelper;
import works.avijay.com.ipl2018.helper.schedule_list_adapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {


    public ScheduleFragment() {
        // Required empty public constructor
    }

    View view;
    static MaterialRefreshLayout materialRefreshLayout;
    static Context context;
    static ListView scheduleList;
    static DatabaseHelper helper;
    private static List<schedule_list_adapter> scheduleAdapter = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_schedule, container, false);

        initializeViews();
        populateData();

        Toast.makeText(context, "Swipe to refresh contents", Toast.LENGTH_SHORT).show();


        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                if(isNetworkConnected()){
                    BackendHelper.fetch_schedule fetch_schedule = new BackendHelper.fetch_schedule();
                    fetch_schedule.execute(context, false, true);
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

    public void initializeViews(){
        context = getActivity().getApplicationContext();
        scheduleList = view.findViewById(R.id.scheduleList);
        helper = new DatabaseHelper(context);
        materialRefreshLayout = view.findViewById(R.id.swipe_refresh);
    }


    public static void populateData(){
        scheduleAdapter.clear();
        if(materialRefreshLayout.isShown())
            materialRefreshLayout.finishRefresh();


        Cursor cursor = helper.getAllSchedule();
        while (cursor.moveToNext()){
            scheduleAdapter.add(new schedule_list_adapter(
                    cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5)
            ));
        }

        displayList();
    }


    public static void displayList() {
        ArrayAdapter<schedule_list_adapter> adapter = new myScheduleAdapterClass();
        scheduleList.setAdapter(adapter);
    }


    public static class myScheduleAdapterClass extends ArrayAdapter<schedule_list_adapter> {

        myScheduleAdapterClass() {
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
            TextView match_time = itemView.findViewById(R.id.match_time);
            TextView match_date = itemView.findViewById(R.id.match_date);

            match_venu.setText("Venue : "+current.getVenue());
            match_time.setText(current.getTime());
            match_date.setText(current.getDate());

            switch (current.getTeam1()){
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



            switch (current.getTeam2()){
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



            return itemView;
        }
    }



}
