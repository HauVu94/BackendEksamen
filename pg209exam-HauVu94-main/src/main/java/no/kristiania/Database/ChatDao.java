package no.kristiania.Database;

import no.kristiania.Database.Entities.Chat;

import java.util.List;

public interface ChatDao {
    void save(Chat chat);

    Chat retrieve(long id);

    List<Chat> listAll();
}
