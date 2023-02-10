package niffler.data.jpa;

import jakarta.persistence.Cache;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.persistence.Query;
import jakarta.persistence.SynchronizationType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.metamodel.Metamodel;

import java.util.Map;

public class ThreadLocalEntityManagerFactory implements EntityManagerFactory {

    private final EntityManagerFactory delegate;
    private final ThreadLocal<EntityManager> emtl;

    public ThreadLocalEntityManagerFactory(EntityManagerFactory delegate) {
        this.delegate = delegate;
        emtl = ThreadLocal.withInitial(delegate::createEntityManager);
    }

    @Override
    public EntityManager createEntityManager() {
        return emtl.get();
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        emtl.set(delegate.createEntityManager(map));
        return emtl.get();
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        emtl.set(delegate.createEntityManager(synchronizationType));
        return emtl.get();
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        emtl.set(delegate.createEntityManager(synchronizationType, map));
        return emtl.get();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return delegate.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return delegate.getMetamodel();
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public Map<String, Object> getProperties() {
        return delegate.getProperties();
    }

    @Override
    public Cache getCache() {
        return delegate.getCache();
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return delegate.getPersistenceUnitUtil();
    }

    @Override
    public void addNamedQuery(String name, Query query) {
        delegate.addNamedQuery(name, query);
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        return delegate.unwrap(cls);
    }

    @Override
    public <T> void addNamedEntityGraph(String graphName, EntityGraph<T> entityGraph) {
        delegate.addNamedEntityGraph(graphName, entityGraph);
    }
}
