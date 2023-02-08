package niffler.data.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.function.Consumer;

public abstract class JpaService {

    protected final EntityManager em;

    public JpaService(EntityManager em) {
        this.em = em;
    }

    public void persist(Object entity) {
        tx(em -> em.persist(entity));
    }

    public void remove(Object entity) {
        tx(em -> em.remove(entity));
    }

    public void merge(Object entity) {
        tx(em -> em.merge(entity));
    }

    private void tx(Consumer<EntityManager> consumer) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            consumer.accept(em);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
