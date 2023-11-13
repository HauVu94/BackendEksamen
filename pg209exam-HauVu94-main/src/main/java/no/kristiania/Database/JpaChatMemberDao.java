package no.kristiania.Database;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import no.kristiania.Database.Entities.Chat;
import no.kristiania.Database.Entities.ChatMember;
import no.kristiania.Database.Entities.User;

import java.util.List;

public class JpaChatMemberDao implements ChatMemberDao{


    private final EntityManager entityManager;

    @Inject
    public JpaChatMemberDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(ChatMember chatMember) {
        entityManager.persist(chatMember);
    }

    @Override
    public List<Chat> findByUserId(long userId) {
        return entityManager.createQuery("SELECT u.chat from ChatMember u WHERE u.user.userId = :userId")
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<User> findByChatId(long chatId) {
        return entityManager.createQuery("SELECT u.user from ChatMember u WHERE u.chat.chatId = :chatId")
                .setParameter("chatId", chatId)
                .getResultList();
    }
}
