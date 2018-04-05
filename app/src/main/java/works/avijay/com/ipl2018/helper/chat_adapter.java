package works.avijay.com.ipl2018.helper;

public class chat_adapter {
    private String username, user_message;
    private long time;

    public chat_adapter(String username, String user_message, long time) {
        this.username = username;
        this.user_message = user_message;
        this.time = time;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUser_message(String user_message) {
        this.user_message = user_message;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUsername() {

        return username;
    }

    public String getUser_message() {
        return user_message;
    }

    public long getTime() {
        return time;
    }
}
