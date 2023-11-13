package no.kristiania.Database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import no.kristiania.Database.Entities.Chat;
import no.kristiania.Database.Entities.Message;
import no.kristiania.Database.Entities.User;
import org.eclipse.jetty.plus.jndi.Resource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatDaoTest {

    private final EntityManager entityManager;

    private ChatDao chatDao;
    private MessageDao messageDao;
    private UserDao userDao;

    public ChatDaoTest() throws NamingException {
        JdbcDataSource dataSource = InMemoryDataSource.createDataSource();


        new Resource("jdbc/dataSource", dataSource);
        this.entityManager = Persistence.createEntityManagerFactory("chatRoom").createEntityManager();

        messageDao = new JpaMessageDao(entityManager);
        chatDao = new JpaChatDao(entityManager);
        userDao = new JpaUserDao(entityManager);

    }

    @BeforeEach
    void setUp() throws NamingException {
        entityManager.getTransaction().begin();
    }
    @AfterEach
    void tearDown(){
        entityManager.getTransaction().rollback();
    }


    @Test
    void shouldAddAndListChats(){
        var message = new Message();
        var user = new User();
        var chat = new Chat();

        user.setEmail("123@123@gmail.bo");
        user.setPhonenumber("12345678");
        user.setUsername("gudbrand");
        userDao.save(user);
        flush();

        chat.setCreator(user);
        chat.setSubject("Chat subjext");
        chatDao.save(chat);
        flush();

        message.setUser(user);
        message.setBody("Lamo");
        message.setSubject("Lmao subject");
        message.setChat(chat);
        messageDao.save(message);
        flush();

        assertThat(chatDao.retrieve(chat.getChatId()))
                .usingRecursiveComparison()
                .isEqualTo(chat)
                .isNotSameAs(chat);
    }

    private void flush() {
        entityManager.flush();
        entityManager.clear();
    }
}
