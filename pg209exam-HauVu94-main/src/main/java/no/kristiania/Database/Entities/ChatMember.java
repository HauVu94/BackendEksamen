package no.kristiania.Database.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_members")
public class ChatMember {

    @EmbeddedId
    private ChatMembersId id;

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private Chat chat;


    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;


    public ChatMembersId getId() {
        return id;
    }

    public void setId(ChatMembersId id) {
        this.id = id;
    }

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
