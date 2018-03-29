package works.avijay.com.ipl2018.helper;

public class points_adapter {
    private String team_name;
    private int points, remaining, played;
    private double rate;


    public points_adapter(String team_name, int points, int remaining, int played, double rate) {
        this.team_name = team_name;
        this.points = points;
        this.remaining = remaining;
        this.played = played;
        this.rate = rate;
    }


    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getTeam_name() {

        return team_name;
    }

    public int getPoints() {
        return points;
    }

    public int getRemaining() {
        return remaining;
    }

    public int getPlayed() {
        return played;
    }

    public double getRate() {
        return rate;
    }
}
