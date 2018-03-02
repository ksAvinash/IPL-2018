package works.avijay.com.ipl2018;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

import works.avijay.com.ipl2018.helper.BackendHelper;
import works.avijay.com.ipl2018.helper.DatabaseHelper;
import works.avijay.com.ipl2018.helper.players_list_adapter;

public class TeamActivity extends AppCompatActivity {

    String image_url, rate, team_name, original_name;
    int win, loss, remaining, points;
    ListView playersList;
    ImageView team_image;
    Context context;
    DatabaseHelper helper;
    List<players_list_adapter> playersAdapter = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        getDetails();
        initializeViews();
        populateData();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Great!, you started following '"+team_name+"'", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                BackendHelper.update_fan_count update_fan_count = new BackendHelper.update_fan_count();
                update_fan_count.execute(getApplicationContext(), original_name);
            }
        });
    }

    public void getDetails(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        image_url = extras.getString("team_image");
        rate = extras.getString("rate");
        original_name = extras.getString("team_name");
        setTeamName(original_name);
        win = extras.getInt("wins");
        loss = extras.getInt("loss");
        remaining = extras.getInt("remaining");
        points = extras.getInt("points");
    }

    public void setTeamName(String name){
        switch (name){
            case "KINGS XI PUNJAB":
                        team_name = "KXIP";
                break;

            case "CHENNAI SUPER KINGS":
                        team_name = "CSK";
                break;

            case "KOLKATA KNIGHT RIDERS":
                        team_name = "KKR";
                break;

            case "MUMBAI INDIANS":
                        team_name = "MI";
                break;

            case "DELHI DAREDEVILS":
                        team_name = "DD";
                break;

            case "ROYAL CHALLENGERS BANGALORE":
                        team_name = "RCB";
                break;

            case "RAJASTHAN ROYALS":
                        team_name = "RR";
                break;

            case "SUNRISERS HYDERABAD":
                        team_name = "SRH";
                break;
        }
    }

    public void initializeViews(){
        context = getApplicationContext();
        team_image = findViewById(R.id.team_image);
        playersList = findViewById(R.id.playersList);
        helper = new DatabaseHelper(context);
        playersAdapter.clear();

        Cursor cursor = helper.getTeamMembers(team_name);
        while (cursor.moveToNext()){
            playersAdapter.add(new players_list_adapter(
                    cursor.getString(0), Integer.parseInt(cursor.getString(1)),
                    cursor.getInt(2)>0,
                    cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6)

            ));
        }

        displayList();

    }

    public void populateData(){
        Glide.with(this).load(image_url)
                .thumbnail(0.5f)
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(team_image);
    }


    public void displayList(){
        ArrayAdapter<players_list_adapter> adapter = new myPlayersAdapterClass();
        playersList.setAdapter(adapter);
    }


    public class myPlayersAdapterClass extends ArrayAdapter<players_list_adapter> {

        myPlayersAdapterClass() {
            super(context, R.layout.team_member_item, playersAdapter);
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(TeamActivity.this);
                itemView = inflater.inflate(R.layout.team_member_item, parent, false);
            }
            players_list_adapter current = playersAdapter.get(position);

            ImageView member_image = itemView.findViewById(R.id.member_image);
            TextView capped = itemView.findViewById(R.id.capped);
            ImageView overseas = itemView.findViewById(R.id.overseas);
            ImageView player_type = itemView.findViewById(R.id.player_type);
            TextView member_name = itemView.findViewById(R.id.member_name);
            TextView country = itemView.findViewById(R.id.country);
            TextView member_bidding_price = itemView.findViewById(R.id.member_bidding_price);


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
