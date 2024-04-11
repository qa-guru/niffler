package guru.qa.niffler.data.jpa;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

import java.util.List;
import java.util.Map;

public class ThreadSafeEntityManager implements EntityManager {

    private final EntityManagerFactory emf;
    private final ThreadLocal<EntityManager> threadEmStore = new ThreadLocal<>();

    public ThreadSafeEntityManager(EntityManager delegate) {
        this.emf = delegate.getEntityManagerFactory();
        threadEmStore.set(delegate);
    }

    private EntityManager emForThread() {
        if (threadEmStore.get() == null) {
            threadEmStore.set(emf.createEntityManager());
        }
        return threadEmStore.get();
    }

    @Override
    public void close() {
        if (threadEmStore.get() != null) {
            threadEmStore.get().close();
            threadEmStore.remove();
        }
    }

    @Override
    public void persist(Object entity) {
        emForThread().persist(entity);
    }

    @Override
    public <T> T merge(T entity) {
        return emForThread().merge(entity);
    }

    @Override
    public void remove(Object entity) {
        emForThread().remove(entity);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        return emForThread().find(entityClass, primaryKey);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        return emForThread().find(entityClass, primaryKey, properties);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        return emForThread().find(entityClass, primaryKey, lockMode);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
        return emForThread().find(entityClass, primaryKey, lockMode, properties);
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return emForThread().getReference(entityClass, primaryKey);
    }

    @Override
    public void flush() {
        emForThread().flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushMode) {
        emForThread().setFlushMode(flushMode);
    }

    @Override
    public FlushModeType getFlushMode() {
        return emForThread().getFlushMode();
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {
        emForThread().lock(entity, lockMode);
    }

    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        emForThread().lock(entity, lockMode, properties);
    }

    @Override
    public void refresh(Object entity) {
        emForThread().refresh(entity);
    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties) {
        emForThread().refresh(entity, properties);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode) {
        emForThread().refresh(entity, lockMode);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        emForThread().refresh(entity, lockMode, properties);
    }

    @Override
    public void clear() {
        emForThread().clear();
    }

    @Override
    public void detach(Object entity) {
        emForThread().detach(entity);
    }

    @Override
    public boolean contains(Object entity) {
        return emForThread().contains(entity);
    }

    @Override
    public LockModeType getLockMode(Object entity) {
        return emForThread().getLockMode(entity);
    }

    @Override
    public void setProperty(String propertyName, Object value) {
        emForThread().setProperty(propertyName, value);
    }

    @Override
    public Map<String, Object> getProperties() {
        return emForThread().getProperties();
    }

    @Override
    public Query createQuery(String qlString) {
        return emForThread().createQuery(qlString);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return emForThread().createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate updateQuery) {
        return emForThread().createQuery(updateQuery);
    }

    @Override
    public Query createQuery(CriteriaDelete deleteQuery) {
        return emForThread().createQuery(deleteQuery);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        return emForThread().createQuery(qlString, resultClass);
    }

    @Override
    public Query createNamedQuery(String name) {
        return emForThread().createNamedQuery(name);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        return emForThread().createNamedQuery(name, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString) {
        return emForThread().createNativeQuery(sqlString);
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        return emForThread().createNativeQuery(sqlString, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return emForThread().createNativeQuery(sqlString, resultSetMapping);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
        return emForThread().createNamedStoredProcedureQuery(name);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
        return emForThread().createStoredProcedureQuery(procedureName);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
        return emForThread().createStoredProcedureQuery(procedureName, resultClasses);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
        return emForThread().createStoredProcedureQuery(procedureName, resultSetMappings);
    }

    @Override
    public void joinTransaction() {
        emForThread().joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return emForThread().isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        return emForThread().unwrap(cls);
    }

    @Override
    public Object getDelegate() {
        return emForThread().getDelegate();
    }

    @Override
    public boolean isOpen() {
        return emForThread().isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return emForThread().getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return emForThread().getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return emForThread().getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return emForThread().getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
        return emForThread().createEntityGraph(rootType);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String graphName) {
        return emForThread().createEntityGraph(graphName);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String graphName) {
        return emForThread().getEntityGraph(graphName);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
        return emForThread().getEntityGraphs(entityClass);
    }
}
