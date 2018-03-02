package works.avijay.com.ipl2018.helper;

/**
 * Created by avinashk on 02/03/18.
 */

public class schedule_list_adapter {

    private int match_id;
    private String date, team1, team2, time, venue;


    public schedule_list_adapter(int match_id, String date, String team1, String team2, String time, String venue) {
        this.match_id = match_id;
        this.date = date;
        this.team1 = team1;
        this.team2 = team2;
        this.time = time;
        this.venue = venue;
    }


    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getMatch_id() {

        return match_id;
    }

    public String getDate() {
        return date;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getTime() {
        return time;
    }

    public String getVenue() {
        return venue;
    }
}
