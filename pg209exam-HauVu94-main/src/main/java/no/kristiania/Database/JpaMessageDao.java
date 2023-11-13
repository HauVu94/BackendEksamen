package no.kristiania.Database;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import no.kristiania.Database.Entities.Message;

import java.util.List;

public class JpaMessageDao implements MessageDao {

    private final EntityManager entityManager;


    @Inject
    public JpaMessageDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Message message) {
        entityManager.persist(message);
    }

    @Override
    public Message retrieve(long id) {
        return entityManager.find(Message.class, id);
    }

    @Override
    public List<Message> listAll() {
        return entityManager.createQuery(entityManager.getCriteriaBuilder().createQuery(Message.class)).getResultList();
    }

    @Override
    public List<Message> findByChat(long id) {
        return entityManager.createQuery("SELECT m from Message m where m.chat.chatId = :chatId")
                .setParameter("chatId", id)
                .getResultList();
    }
}
