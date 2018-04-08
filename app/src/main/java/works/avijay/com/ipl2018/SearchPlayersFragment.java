package works.avijay.com.ipl2018;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
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
import works.avijay.com.ipl2018.helper.players_list_adapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchPlayersFragment extends Fragment {


    public SearchPlayersFragment() {
        // Required empty public constructor
    }

    View view;
    String search_string;
    ListView searchPlayerList;
    Context context;
    private List<players_list_adapter> searchAdapter = new ArrayList<>();
    DatabaseHelper helper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_search_players, container, false);


        initializeViews();

        return view;
    }

    public void initializeViews(){
        Bundle bundle = this.getArguments();
        search_string = bundle.getString("search");
        searchAdapter.clear();
        searchPlayerList = view.findViewById(R.id.searchPlayerList);
        context = getActivity().getApplicationContext();

        helper = new DatabaseHelper(context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = helper.getPlayerByName(search_string);
                while (cursor.moveToNext()){
                    searchAdapter.add(new players_list_adapter(
                            cursor.getString(0), Integer.parseInt(cursor.getString(1)),
                            cursor.getInt(2)>0,
                            cursor.getString(3), cursor.getString(4),
                            cursor.getString(5), cursor.getString(6)

                    ));
                }
                cursor.close();
            }
        }).start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                displayList();
            }
        }, 20);
    }



    private void displayList() {
        ArrayAdapter<players_list_adapter> adapter = new searchPlayersAdapterClass();
        searchPlayerList.setAdapter(adapter);

    }



    public class searchPlayersAdapterClass extends ArrayAdapter<players_list_adapter> {

        searchPlayersAdapterClass() {
            super(context, R.layout.search_players_list, searchAdapter);
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.search_players_list, parent, false);
            }
            players_list_adapter current = searchAdapter.get(position);
            ImageView member_image = itemView.findViewById(R.id.search_image);
            TextView capped = itemView.findViewById(R.id.search_capped);
            ImageView overseas = itemView.findViewById(R.id.search_overseas);
            ImageView player_type = itemView.findViewById(R.id.search_player_type);
            TextView member_name = itemView.findViewById(R.id.search_name);
            TextView country = itemView.findViewById(R.id.search_country);
            TextView member_bidding_price = itemView.findViewById(R.id.search_bidding_price);
            TextView member_team_name = itemView.findViewById(R.id.search_team_name);

            long price = current.getPlayer_bidding_price();
            member_bidding_price.setText((price/10000000.0)+"Cr");




            if(!current.isPlayer_capped()){
                capped.setText("Uncapped");
            }else {
                capped.setText("");
            }

            if(!current.getPlayer_country().equals("IND")){
                overseas.setImageResource(R.drawable.overseas);
                country.setText(current.getPlayer_country());
            }else {
                overseas.setImageResource(0);
                country.setText("IND");
            }

            switch (current.getPlayer_type()) {
                case "Bowl":
                    player_type.setImageResource(R.drawable.ball_icon);
                    break;
                case "AR":
                    player_type.setImageResource(R.drawable.bat_ball_icon);
                    break;
                case "Bat":
                    player_type.setImageResource(R.drawable.bat_icon);
                    break;
                case "WK":
                    player_type.setImageResource(R.drawable.keeper_icon);
                    break;
            }

            member_name.setText(current.getPlayername());
            member_team_name.setText(current.getPlayer_team());

            Glide.with(context).load(current.getPlayer_image())
                    .thumbnail(0.5f)
                    .centerCrop()
                    .placeholder(R.drawable.general_player)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(member_image);
            return itemView;
        }

    }



}
