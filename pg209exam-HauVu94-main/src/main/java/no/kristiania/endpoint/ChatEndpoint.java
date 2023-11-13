package no.kristiania.endpoint;

import jakarta.inject.Inject;
import jakarta.persistence.Column;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.Database.ChatDao;
import no.kristiania.Database.ChatMemberDao;
import no.kristiania.Database.Entities.Chat;
import no.kristiania.Database.Entities.ChatMember;
import no.kristiania.Database.Entities.Message;
import no.kristiania.Database.Entities.User;
import no.kristiania.Database.MessageDao;
import no.kristiania.Database.UserDao;
import no.kristiania.dto.NewMemberDto;
import no.kristiania.dto.NewChatDto;

import java.util.List;

@Path("/chats")
public class ChatEndpoint {

    @Inject
     ChatDao chatDao;

    @Inject
     UserDao userDao;
    @Inject
    private ChatMemberDao chatMemberDao;

    @Inject
    private MessageDao messageDao;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Chat> listAll(){
        return chatDao.listAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addChat(NewChatDto newChatDto){
        var user = userDao.retrieve(newChatDto.getCreatorId());
        var chat = new Chat();
        var message = new Message();
        var chatMember = new ChatMember();


        chat.setCreator(user);
        chat.setSubject(newChatDto.getSubject());
        chatDao.save(chat);

        message.setSubject(newChatDto.getSubject());
        message.setChat(chat);
        message.setUser(user);
        message.setBody(newChatDto.getMessage());
        messageDao.save(message);

        chatMember.setChat(chat);
        chatMember.setUser(user);
        chatMemberDao.save(chatMember);

        for (User userId : newChatDto.getUserId()) {
            var receiver = new ChatMember();
            receiver.setChat(chat);
            receiver.setUser(userId);
            chatMemberDao.save(receiver);
        }


    }


    @Path("/member")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void addMemberToChat(NewMemberDto newMemberDto){
        var chatMember = new ChatMember();

        chatMember.setUser(userDao.retrieve(newMemberDto.getUser().getUserId()));
        chatMember.setChat(newMemberDto.getChat());

        chatMemberDao.save(chatMember);
    }


    @Path("/member/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getMember(@PathParam("id") long id){
        return chatMemberDao.findByChatId(id);
    }


    @Path("/message/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> findByChatId(@PathParam("id") long id){
        return messageDao.findByChat(id);
    }


}
