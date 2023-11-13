package no.kristiania.endpoint;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.Database.ChatMemberDao;
import no.kristiania.Database.Entities.Chat;
import no.kristiania.Database.Entities.User;
import no.kristiania.Database.MessageDao;
import no.kristiania.Database.UserDao;

import java.util.List;


@Path("/users")
public class UserEndpoint {

    @Inject
    private MessageDao messageDao;

    @Inject
    private UserDao userDao;

    @Inject
    private ChatMemberDao chatMemberDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listUsers(){
        return userDao.listAll();
    }

    @Path("/chat/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Chat> listAllChatByUserId(@PathParam("id") long id){
        return chatMemberDao.findByUserId(id);
    }

    @Path("/chat/chat/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<User> listAllUsersByChatId(@PathParam("id") long id){
        return chatMemberDao.findByChatId(id);
    }

    @Path("/user/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void alterUser(@PathParam("id")long id, User alterUser){
        var user = userDao.retrieve(id);
        user.setUsername(alterUser.getUsername());
        user.setEmail(alterUser.getEmail());
        user.setPhonenumber(alterUser.getPhonenumber());
        userDao.save(user);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addUser(User user){
        userDao.save(user);
    }
}
