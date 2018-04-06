package works.avijay.com.ipl2018.helper;

/**
 * Created by avinashk on 02/03/18.
 */

public class cards_adapter {
    private String card_id, card_description;
    private int card_approved, card_disapproved, card_seen_value;
    private String card_image, card_type, card_team;
    private int order;

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {

        return order;
    }

    public String getCard_team() {
        return card_team;
    }

    public void setCard_team(String card_team) {
        this.card_team = card_team;
    }

    public int getCard_seen_value() {
        return card_seen_value;
    }

    public void setCard_seen_value(int card_seen_value) {
        this.card_seen_value = card_seen_value;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public void setCard_description(String card_description) {
        this.card_description = card_description;
    }

    public void setCard_approved(int card_approved) {
        this.card_approved = card_approved;
    }

    public void setCard_disapproved(int card_disapproved) {
        this.card_disapproved = card_disapproved;
    }

    public void setCard_image(String card_image) {
        this.card_image = card_image;
    }

    public String getCard_id() {

        return card_id;
    }

    public String getCard_description() {
        return card_description;
    }

    public int getCard_approved() {
        return card_approved;
    }

    public int getCard_disapproved() {
        return card_disapproved;
    }

    public String getCard_image() {
        return card_image;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getCard_type() {

        return card_type;
    }

    public cards_adapter(String card_id, String card_description, int card_approved, int card_disapproved, String card_image, int card_seen_value, String card_type, String card_team, int order) {
        this.card_id = card_id;
        this.card_description = card_description;
        this.card_approved = card_approved;
        this.card_disapproved = card_disapproved;
        this.card_image = card_image;
        this.card_seen_value = card_seen_value;
        this.card_type = card_type;
        this.card_team = card_team;
        this.order = order;
    }


}
