package no.kristiania.Database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;import no.kristiania.Database.Entities.Message;
import no.kristiania.Database.Entities.User;
import org.eclipse.jetty.plus.jndi.Resource;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {

    private final JpaUserDao dao;
    private final EntityManager entityManager;

    public UserDaoTest() throws NamingException {
        JdbcDataSource dataSource = InMemoryDataSource.createDataSource();
//
//        var dataSource = new JdbcDataSource();
//        dataSource.setURL("jdbc:h2:mem:testDatabase;DB_CLOSE_DELAY=-1;MODE=LEGACY");
//
//        var flyway = Flyway.configure().dataSource(dataSource).load();
//        flyway.migrate();

        new Resource("jdbc/dataSource", dataSource);
        this.entityManager = Persistence.createEntityManagerFactory("chatRoom").createEntityManager();
        dao = new JpaUserDao(entityManager);

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
    void shouldRetrieveSavedUser() {
        var user = sampleUser();
        dao.save(user);
        flush();
        assertThat(dao.retrieve(user.getUserId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(user)
                .isNotSameAs(user);
    }

    private User sampleUser() {
        var sampleUser = new User();
        sampleUser.setUsername("hau");
        sampleUser.setEmail("hei@hei.com");
        sampleUser.setPhonenumber("33466427");

        return sampleUser;
    }

    private void flush() {
        entityManager.flush();
        entityManager.clear();
    }



    private Message sampleMessage(User user) {
        Message message = new Message();
        message.setUser(user);
        return message;
    }


}
