package no.kristiania.Database;

import no.kristiania.Database.Entities.Message;
import no.kristiania.Database.Entities.User;

import java.util.List;

public interface UserDao {

    void save(User user);

    User retrieve(long id);

    List<User> listAll();

}
