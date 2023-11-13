package no.kristiania.dto;

import no.kristiania.Database.Entities.Chat;
import no.kristiania.Database.Entities.User;

public class NewMemberDto {
    private User user;
    private Chat chat;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
