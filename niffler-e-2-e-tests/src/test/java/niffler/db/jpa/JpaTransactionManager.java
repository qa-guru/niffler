package niffler.db.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class JpaTransactionManager {

  protected final EntityManager em;

  public JpaTransactionManager(EntityManager em) {
    this.em = em;
  }

  protected void persist(Object entity) {
    transaction(em -> em.persist(entity));
  }

  protected void remove(Object entity) {
    transaction(em -> em.remove(entity));
  }

  protected void merge(Object entity) {
    transaction(em -> em.merge(entity));
  }

  protected void transaction(Consumer<EntityManager> consumer) {
    EntityTransaction transaction = em.getTransaction();
    transaction.begin();
    try {
      consumer.accept(em);
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
    }
  }

}
