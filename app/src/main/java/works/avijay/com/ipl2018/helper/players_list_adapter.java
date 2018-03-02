package works.avijay.com.ipl2018.helper;

/**
 * Created by avinashk on 01/03/18.
 */

public class players_list_adapter {

    private String playername, player_country, player_team, player_type, player_image;
    private long player_bidding_price;
    private boolean player_capped;

    public players_list_adapter(String playername, long player_bidding_price, boolean player_capped, String player_country, String player_team, String player_type, String player_image) {
        this.playername = playername;
        this.player_bidding_price = player_bidding_price;
        this.player_country = player_country;
        this.player_team = player_team;
        this.player_type = player_type;
        this.player_image = player_image;
        this.player_capped = player_capped;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public void setPlayer_bidding_price(int player_bidding_price) {
        this.player_bidding_price = player_bidding_price;
    }

    public void setPlayer_country(String player_country) {
        this.player_country = player_country;
    }

    public void setPlayer_team(String player_team) {
        this.player_team = player_team;
    }

    public void setPlayer_type(String player_type) {
        this.player_type = player_type;
    }

    public void setPlayer_image(String player_image) {
        this.player_image = player_image;
    }

    public void setPlayer_capped(boolean player_capped) {
        this.player_capped = player_capped;
    }

    public String getPlayername() {

        return playername;
    }

    public long getPlayer_bidding_price() {
        return player_bidding_price;
    }

    public String getPlayer_country() {
        return player_country;
    }

    public String getPlayer_team() {
        return player_team;
    }

    public String getPlayer_type() {
        return player_type;
    }

    public String getPlayer_image() {
        return player_image;
    }

    public boolean isPlayer_capped() {
        return player_capped;
    }
}
