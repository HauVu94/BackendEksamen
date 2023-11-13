package no.kristiania.Database;

import no.kristiania.Database.Entities.Chat;
import no.kristiania.Database.Entities.ChatMember;
import no.kristiania.Database.Entities.User;

import java.util.List;

public interface ChatMemberDao {

    void save(ChatMember chatMember);

    List<Chat> findByUserId(long userId);

    List<User> findByChatId(long chatId);
}
