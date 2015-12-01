package edu.piotrjonski.scrumus.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractDAO<T, V> {

    @PersistenceContext
    protected EntityManager entityManager;

    private Class<T> entityClass;

    public AbstractDAO() {}

    protected AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Optional<V> findByKey(Object key) {
        return Optional.ofNullable(mapToDomainModelIfNotNull(entityManager.find(entityClass, key)));
    }

    public List<V> findAll() {
        List<T> dbEntities = getFindAllQuery().getResultList();
        return mapToDomainModel(dbEntities);
    }

    public Optional<V> saveOrUpdate(V domainObject) {
        T dbEntity = entityManager.merge(mapToDatabaseModelIfNotNull(domainObject));
        return Optional.ofNullable(mapToDomainModelIfNotNull(dbEntity));
    }

    public void delete(Object id) {
        getDeleteByIdQuery().setParameter(getId(), id)
                            .executeUpdate();
    }

    public void delete(List<V> domainObjects) {
        domainObjects.forEach(this::delete);
    }

    public List<T> mapToDatabaseModel(List<V> domainModels) {
        if (domainModels != null) {
            return domainModels.stream()
                               .map(this::mapToDatabaseModelIfNotNull)
                               .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public List<V> mapToDomainModel(List<T> dbModels) {
        if (dbModels != null) {
            return dbModels.stream()
                           .map(this::mapToDomainModelIfNotNull)
                           .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public T mapToDatabaseModelIfNotNull(V domainModel) {
        return domainModel != null
               ? mapToDatabaseModel(domainModel)
               : null;
    }

    public V mapToDomainModelIfNotNull(T dbModel) {
        return dbModel != null
               ? mapToDomainModel(dbModel)
               : null;
    }

    protected abstract T mapToDatabaseModel(V domainModel);

    protected abstract V mapToDomainModel(T dbModel);

    protected abstract Query getFindAllQuery();

    protected abstract Query getDeleteByIdQuery();

    protected abstract String getId();

}
