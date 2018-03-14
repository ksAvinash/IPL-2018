package works.avijay.com.ipl2018.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by avinashk on 28/02/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ipl_database";

    private static final String TABLE_TEAMS = "ipl_teams";
    private static final String TABLE_SCHEDULE = "ipl_schedule";
    private static final String TABLE_PLAYERS = "ipl_players";
    private static final String TABLE_CARDS = "ipl_cards";


    private static final String TEAM_NAME = "team_name";
    private static final String TEAM_LOSS = "loss";
    private static final String TEAM_WINS = "wins";
    private static final String TEAM_REMAINING = "remaining";
    private static final String TEAM_POINTS = "points";
    private static final String TEAM_RATE = "rate";
    private static final String TEAM_IMAGE = "team_image";
    private static final String TEAM_FAN_COUNT = "fan_count";

    private static final String SCHEDULE_MATCH_ID = "match_id";
    private static final String SCHEDULE_MATCH_DATE = "match_date";
    private static final String SCHEDULE_MATCH_TEAM1 = "team1";
    private static final String SCHEDULE_MATCH_TEAM2 = "team2";
    private static final String SCHEDULE_MATCH_TIME = "time";
    private static final String SCHEDULE_MATCH_VENUE = "venue";

    private static final String PLAYER_NAME = "player_name";
    private static final String PLAYER_BIDDING_PRICE = "player_bidding_price";
    private static final String PLAYER_CAPPED = "player_capped";
    private static final String PLAYER_COUNTRY = "player_country";
    private static final String PLAYER_TEAM = "player_team";
    private static final String PLAYER_TYPE = "player_type";
    private static final String PLAYER_IMAGE = "player_image";

    private static final String CARD_ID = "card_id";
    private static final String CARD_DESCRIPTION  = "card_description";
    private static final String CARD_APPROVED = "card_approved";
    private static final String CARD_DISAPPROVED = "card_disapproved";
    private static final String CARD_IMAGE = "card_image";
    private static final String CARD_SEEN = "card_seen";
    private static final String CARD_TYPE = "card_type";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_team_stats = "create table "
                +TABLE_TEAMS+" ("+TEAM_NAME+" text primary key, "+TEAM_LOSS+" number, "+TEAM_WINS+
                " number, "+TEAM_REMAINING+" number, "+TEAM_POINTS+" number, "+TEAM_RATE+" text, "+TEAM_IMAGE+" text, "+TEAM_FAN_COUNT+" number);";
        db.execSQL(create_team_stats);

        String create_schedule_table = "create table "+TABLE_SCHEDULE+
                " ("+SCHEDULE_MATCH_ID+" number primary key, "+SCHEDULE_MATCH_DATE+
                " text, "+SCHEDULE_MATCH_TEAM1+" text, "+SCHEDULE_MATCH_TEAM2+" text, "+SCHEDULE_MATCH_TIME+
                " text, "+SCHEDULE_MATCH_VENUE+" text)";
        db.execSQL(create_schedule_table);

        String create_table_players = "create table "+TABLE_PLAYERS+" ("+PLAYER_NAME+" text primary key, "
                +PLAYER_BIDDING_PRICE+" number, "+PLAYER_CAPPED+" boolean, "+PLAYER_COUNTRY+" text, "
                +PLAYER_TEAM+" text, "+PLAYER_TYPE+" text, "+PLAYER_IMAGE+" text);";
        db.execSQL(create_table_players);


        String create_cards_table = "create table "+TABLE_CARDS+" ("+CARD_ID+" text primary key, "+CARD_DESCRIPTION+" text, "+CARD_APPROVED+" number, "+CARD_DISAPPROVED+
                " number, "+CARD_IMAGE+" text, "+CARD_SEEN+" number, "+CARD_TYPE+" text);";
        db.execSQL(create_cards_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_TEAMS);
        db.execSQL("drop table if exists "+TABLE_SCHEDULE);
        db.execSQL("drop table if exists "+TABLE_PLAYERS);
        db.execSQL("drop table if exists "+TABLE_CARDS);
        onCreate(db);
    }

    public void deleteTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db,0,1);
    }


    public void insertIntoTeamStats(String team_name, int loss, int wins, int remaining, int points, String rate, String team_image, int fan_count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TEAM_NAME, team_name);
        contentValues.put(TEAM_LOSS, loss);
        contentValues.put(TEAM_WINS, wins);
        contentValues.put(TEAM_REMAINING, remaining);
        contentValues.put(TEAM_POINTS, points);
        contentValues.put(TEAM_RATE, rate);
        contentValues.put(TEAM_IMAGE, team_image);
        contentValues.put(TEAM_FAN_COUNT, fan_count);

        db.insert(TABLE_TEAMS, null, contentValues);
    }


    public void updateIntoTeamStats(String team_name, int loss, int wins, int remaining, int points, String rate, String team_image, int fan_count){
        String where = TEAM_NAME+"=?";
        String[] whereArgs = new String[] {team_name};

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TEAM_LOSS, loss);
        contentValues.put(TEAM_WINS, wins);
        contentValues.put(TEAM_REMAINING, remaining);
        contentValues.put(TEAM_POINTS, points);
        contentValues.put(TEAM_RATE, rate);
        contentValues.put(TEAM_IMAGE, team_image);
        contentValues.put(TEAM_FAN_COUNT, fan_count);

        db.update(TABLE_TEAMS, contentValues, where, whereArgs);
    }

    public void updateIntoCards(String card_id, String description, long approved, long disapproved, String image, String card_type) {
        String where = CARD_ID+"=?";
        String[] whereArgs = new String[] {card_id};

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CARD_DESCRIPTION, description);
        contentValues.put(CARD_APPROVED, approved);
        contentValues.put(CARD_DISAPPROVED, disapproved);
        contentValues.put(CARD_IMAGE, image);
        contentValues.put(CARD_TYPE, card_type);

        db.update(TABLE_CARDS, contentValues, where, whereArgs);

    }


    public void updateIntoSchedule(int match_id, String date, String team1, String team2, String time, String venue){
        String where = SCHEDULE_MATCH_ID+"="+match_id;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SCHEDULE_MATCH_DATE, date);
        contentValues.put(SCHEDULE_MATCH_TEAM1, team1);
        contentValues.put(SCHEDULE_MATCH_TEAM2, team2);
        contentValues.put(SCHEDULE_MATCH_TIME, time);
        contentValues.put(SCHEDULE_MATCH_VENUE, venue);
        db.update(TABLE_SCHEDULE, contentValues, where, null);
    }



    public Cursor getAllTeamStats(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_TEAMS+" order by "+TEAM_FAN_COUNT+" desc;",null);

    }


    public void deleteTableSchedule(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists "+TABLE_SCHEDULE);
        db.close();
    }


    public void createTableSchedule(){
        SQLiteDatabase db = this.getWritableDatabase();
        String create_schedule_table = "create table "+TABLE_SCHEDULE+
                " ("+SCHEDULE_MATCH_ID+" number primary key, "+SCHEDULE_MATCH_DATE+
                " text, "+SCHEDULE_MATCH_TEAM1+" text, "+SCHEDULE_MATCH_TEAM2+" text, "+SCHEDULE_MATCH_TIME+
                " text, "+SCHEDULE_MATCH_VENUE+" text)";
        db.execSQL(create_schedule_table);
        db.close();
    }

    public void insertIntoSchedule(int match_id, String date, String team1, String team2, String time, String venue){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SCHEDULE_MATCH_ID, match_id);
        contentValues.put(SCHEDULE_MATCH_DATE, date);
        contentValues.put(SCHEDULE_MATCH_TEAM1, team1);
        contentValues.put(SCHEDULE_MATCH_TEAM2, team2);
        contentValues.put(SCHEDULE_MATCH_TIME, time);
        contentValues.put(SCHEDULE_MATCH_VENUE, venue);
        db.insert(TABLE_SCHEDULE, null, contentValues);
    }


    public void insertIntoPlayers(String player_name, int bidding_price, boolean capped, String country, String team, String type, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PLAYER_NAME, player_name);
        contentValues.put(PLAYER_BIDDING_PRICE, bidding_price);
        contentValues.put(PLAYER_CAPPED, capped);
        contentValues.put(PLAYER_COUNTRY, country);
        contentValues.put(PLAYER_TEAM, team);
        contentValues.put(PLAYER_TYPE, type);
        contentValues.put(PLAYER_IMAGE, image);
        db.insert(TABLE_PLAYERS, null, contentValues);
    }



    public Cursor getTeamMembers(String team_name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLAYERS+" where "+PLAYER_TEAM+" = '"+team_name+"' order by "+PLAYER_BIDDING_PRICE+" desc;", null);
    }

    public Cursor getScheduleByTeamName(String team){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_SCHEDULE+" where "+SCHEDULE_MATCH_TEAM1+" = '"+team+"' or "+SCHEDULE_MATCH_TEAM2+" = '"+team+"' ;", null);
    }


    public Cursor getAllSchedule(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_SCHEDULE+" order by "+SCHEDULE_MATCH_ID+";", null);
    }


    public Cursor getPlayerByName(String str){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLAYERS+" where "+PLAYER_NAME+" like '%"+str+"%' ;",null);
    }


    public Cursor getUnseenCards(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_CARDS+" where "+CARD_SEEN+" = 0;", null);
    }


    public Cursor getSeenCards(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_CARDS+" where "+CARD_SEEN+" != 0 and "+CARD_SEEN+" != 3;", null);
    }


    public void setCardAsSeen(String card_id, int value, int count){
        String where = CARD_ID+"=?";
        String[] whereArgs = new String[] {card_id};
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //value == 1 for approve
        //value == 2 for disapprove
        //value == 3 for skipped
        switch (value){
            case 1:
                contentValues.put(CARD_APPROVED, count+1);
                break;
            case 2:
                contentValues.put(CARD_DISAPPROVED, count+1);
                break;
        }

        contentValues.put(CARD_SEEN, value);

        db.update(TABLE_CARDS, contentValues, where, whereArgs);
    }





    public void insertIntoCards(String card_id, String description, long approved, long disapproved, String image, String card_type){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from "+TABLE_CARDS+" where "+CARD_ID+" = '"+card_id+"';", null);
        if(cursor.getCount() > 0){
            updateIntoCards(card_id, description, approved, disapproved, image, card_type);
        }else{
            ContentValues contentValues = new ContentValues();
            contentValues.put(CARD_ID, card_id);
            contentValues.put(CARD_DESCRIPTION, description);
            contentValues.put(CARD_APPROVED, approved);
            contentValues.put(CARD_DISAPPROVED, disapproved);
            contentValues.put(CARD_IMAGE, image);
            contentValues.put(CARD_SEEN, 0);
            contentValues.put(CARD_TYPE, card_type);
            db.insert(TABLE_CARDS, null, contentValues);
        }
        cursor.close();
    }





}
