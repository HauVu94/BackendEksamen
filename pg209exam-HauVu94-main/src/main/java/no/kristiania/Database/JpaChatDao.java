package no.kristiania.Database;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import no.kristiania.Database.Entities.Chat;
import no.kristiania.Database.Entities.User;

import java.util.List;

public class JpaChatDao implements ChatDao{

    private final EntityManager entityManager;

    @Inject
    public JpaChatDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void save(Chat chat) {
        entityManager.persist(chat);
    }

    @Override
    public Chat retrieve(long id) {
        return entityManager.find( Chat.class,id);
    }

    @Override
    public List<Chat> listAll() {
        return entityManager.createQuery(entityManager.getCriteriaBuilder().createQuery(Chat.class)).getResultList();
    }
}
