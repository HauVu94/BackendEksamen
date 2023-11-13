package no.kristiania.dto;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import no.kristiania.Database.Entities.User;

import java.util.List;

public class NewChatDto {


    private long creatorId;

    private String subject;

    private User user;

    private List<User> userId;

    private String message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCreatorId() {
        return creatorId;
    }


    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<User> getUserId() {
        return userId;
    }

    public void setUserId(List<User> userId) {
        this.userId = userId;
    }
}
