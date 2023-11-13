package no.kristiania.endpoint;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.Database.ChatDao;
import no.kristiania.Database.Entities.Message;
import no.kristiania.Database.MessageDao;
import no.kristiania.Database.UserDao;
import no.kristiania.dto.NewMessageDto;

import java.util.List;

@Path("/messages")
public class MessageEndpoint {

    @Inject
    MessageDao messageDao;
    @Inject
    ChatDao chatDao;
    @Inject
    UserDao userDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> listAllMessages() {
        return messageDao.listAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addNewMessage(NewMessageDto messageDTO){
            var message = new Message();
            message.setUser(userDao.retrieve(messageDTO.getSenderId()));
            message.setBody(messageDTO.getBody());
            message.setSubject(messageDTO.getTitle());
            //message.setUser(userDao.retrieve(messageDTO.getReceiverId()));
        message.setChat(chatDao.retrieve(messageDTO.getChatId())) ;
            messageDao.save(message);
    }

}
