package works.avijay.com.ipl2018.helper;

/**
 * Created by avinashk on 28/02/18.
 */

public class teams_list_adapter {

    private String team_name, rate, team_image;
    private int loss, wins, remaining, points, fan_count;


    public teams_list_adapter(String team_name, int loss, int wins, int remaining, int points, String rate, String team_image, int fan_count){
        this.team_name = team_name;
        this.rate = rate;
        this.team_image = team_image;
        this.loss = loss;
        this.wins = wins;
        this.remaining = remaining;
        this.points = points;
        this.fan_count = fan_count;
    }


    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setTeam_image(String team_image) {
        this.team_image = team_image;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setFan_count(int fan_count) {
        this.fan_count = fan_count;
    }

    public String getTeam_name() {

        return team_name;
    }

    public String getRate() {
        return rate;
    }

    public String getTeam_image() {
        return team_image;
    }

    public int getLoss() {
        return loss;
    }

    public int getWins() {
        return wins;
    }

    public int getRemaining() {
        return remaining;
    }

    public int getPoints() {
        return points;
    }

    public int getFan_count() {
        return fan_count;
    }
}
