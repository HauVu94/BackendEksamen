package no.kristiania.Database;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import no.kristiania.Database.Entities.User;

import java.util.List;

public class JpaUserDao implements UserDao{

    private final EntityManager entityManager;

    @Inject
    public JpaUserDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public User retrieve(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> listAll() {
        return entityManager.createQuery(entityManager.getCriteriaBuilder().createQuery(User.class)).getResultList();
    }
}
