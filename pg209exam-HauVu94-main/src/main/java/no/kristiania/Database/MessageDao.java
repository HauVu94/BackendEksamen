package no.kristiania.Database;

import no.kristiania.Database.Entities.Message;

import java.util.List;

public interface MessageDao {

    void save(Message message);

    Message retrieve(long id);

    List<Message> listAll();

    List<Message> findByChat(long id);
}
