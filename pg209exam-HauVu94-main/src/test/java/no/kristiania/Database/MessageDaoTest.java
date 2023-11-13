package no.kristiania.Database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import no.kristiania.Database.Entities.Message;
import no.kristiania.Database.Entities.User;
import org.eclipse.jetty.plus.jndi.Resource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageDaoTest {


    private final MessageDao messageDao;
    private final UserDao userDao;
    private final EntityManager entityManager;

    public MessageDaoTest() throws NamingException {
        JdbcDataSource dataSource = InMemoryDataSource.createDataSource();


        new Resource("jdbc/dataSource", dataSource);
        this.entityManager = Persistence.createEntityManagerFactory("chatRoom").createEntityManager();
        messageDao = new JpaMessageDao(entityManager);
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
    void shouldAddAndListMessages(){
        var message = new Message();
        var user = new User();
        user.setEmail("123@123@gmail.bo");
        user.setPhonenumber("12345678");
        user.setUsername("gudbrand");
        userDao.save(user);
        flush();
        message.setUser(userDao.retrieve(1));
        message.setBody("Lamo");
        message.setSubject("Lmao subject");
        messageDao.save(message);
        flush();
        assertThat(messageDao.retrieve(message.getMessageId()))
                .usingRecursiveComparison()
                .isEqualTo(message)
                .isNotSameAs(message);
    }

    private void flush() {
        entityManager.flush();
        entityManager.clear();
    }
}
