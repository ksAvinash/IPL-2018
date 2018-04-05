package works.avijay.com.ipl2018.helper;

public class chat_adapter {
    private String username, user_message, chat_color;

    public chat_adapter(String username, String user_message, String chat_color) {
        this.username = username;
        this.user_message = user_message;
        this.chat_color = chat_color;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUser_message(String user_message) {
        this.user_message = user_message;
    }

    public void setChat_color(String chat_color) {
        this.chat_color = chat_color;
    }


    public String getUsername() {

        return username;
    }

    public String getUser_message() {
        return user_message;
    }

    public String getChat_color() {
        return chat_color;
    }
}
