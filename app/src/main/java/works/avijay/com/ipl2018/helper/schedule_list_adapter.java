package works.avijay.com.ipl2018.helper;

/**
 * Created by avinashk on 02/03/18.
 */

public class schedule_list_adapter {

    private int match_id;
    private String date, team1, team2, time, venue, win_team, win_description;
    private int team1_predictions, team2_predictions;

    public schedule_list_adapter(int match_id, String date, String team1, String team2, String time, String venue, String win_team, String win_description, int team1_predictions, int team2_predictions) {
        this.match_id = match_id;
        this.date = date;
        this.team1 = team1;
        this.team2 = team2;
        this.time = time;
        this.venue = venue;
        this.win_team = win_team;
        this.win_description = win_description;
        this.team1_predictions = team1_predictions;
        this.team2_predictions = team2_predictions;
    }

    public void setTeam1_predictions(int team1_predictions) {
        this.team1_predictions = team1_predictions;
    }

    public void setTeam2_predictions(int team2_predictions) {
        this.team2_predictions = team2_predictions;
    }

    public int getTeam1_predictions() {

        return team1_predictions;
    }

    public int getTeam2_predictions() {
        return team2_predictions;
    }

    public void setWin_team(String win_team) {
        this.win_team = win_team;
    }

    public void setTeam_description(String team_description) {
        this.win_description = team_description;
    }

    public String getWin_team() {

        return win_team;
    }

    public String getWin_description() {
        return win_description;
    }

    public void setWin_description(String win_description) {
        this.win_description = win_description;
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
