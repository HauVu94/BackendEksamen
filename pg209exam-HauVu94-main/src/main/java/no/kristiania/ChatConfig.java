package no.kristiania;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import no.kristiania.Database.*;
import no.kristiania.endpoint.ChatEndpoint;
import no.kristiania.endpoint.MessageEndpoint;
import no.kristiania.endpoint.UserEndpoint;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

public class ChatConfig extends ResourceConfig {

    private final ThreadLocal<EntityManager> requestEntityManager = new ThreadLocal<EntityManager>();
    private final EntityManagerFactory entityManagerFactory;

    public ChatConfig(EntityManagerFactory entityManagerFactory) {
        super(UserEndpoint.class, MessageEndpoint.class, ChatEndpoint.class);

        this.entityManagerFactory = entityManagerFactory;
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(JpaMessageDao.class).to(MessageDao.class); // vi må lage en JPA DAO class
                bind(JpaUserDao.class).to(UserDao.class); // vi må lage en JPA DAO class
                bind(JpaChatDao.class).to(ChatDao.class); // vi må lage en JPA DAO class
                bind(JpaChatMemberDao.class).to(ChatMemberDao.class); // vi må lage en JPA DAO class
                bindFactory(requestEntityManager::get)
                        .to(EntityManager.class)
                        .in(RequestScoped.class);
            }
        });
    }

    public EntityManager createEntityManagerForRequest() {
        requestEntityManager.set(entityManagerFactory.createEntityManager());
        return requestEntityManager.get();
    }
    public void cleanRequestEntityManager(){
        requestEntityManager.remove();
    }
}
