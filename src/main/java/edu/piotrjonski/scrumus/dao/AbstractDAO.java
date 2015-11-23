package edu.piotrjonski.scrumus.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
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

    public Optional<V> findByKey(@NotNull(message = "{edu.piotrjonski.scrumus.dao.developer.key.null}") Object key) {
        return Optional.ofNullable(mapToDomainModel(entityManager.find(entityClass, key)));
    }

    public List<V> findAll() {
        List<T> dbEntities = getFindAllQuery().getResultList();
        return mapToDomainModel(dbEntities);
    }

    public Optional<V> saveOrUpdate(
            @NotNull(message = "{edu.piotrjonski.scrumus.dao.developer.domainObject.null}") V domainObject) {
        T dbEntity = entityManager.merge(mapToDatabaseModel(domainObject));
        return Optional.ofNullable(mapToDomainModel(dbEntity));
    }

    public void delete(Object id) {
        getDeleteByIdQuery().setParameter(getId(), id)
                            .executeUpdate();
    }

    public void delete(List<V> domainObjects) {
        domainObjects.forEach(this::delete);
    }

    public abstract T mapToDatabaseModel(V developer);

    public abstract V mapToDomainModel(T developer);

    public List<T> mapToDatabaseModel(List<V> entities) {
        return entities.stream()
                       .map(this::mapToDatabaseModel)
                       .collect(Collectors.toList());
    }

    public List<V> mapToDomainModel(List<T> entities) {
        return entities.stream()
                       .map(this::mapToDomainModel)
                       .collect(Collectors.toList());
    }

    protected abstract Query getFindAllQuery();

    protected abstract Query getDeleteByIdQuery();

    protected abstract String getId();
}
