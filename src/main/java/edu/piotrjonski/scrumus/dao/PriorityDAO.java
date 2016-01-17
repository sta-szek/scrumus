package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.PriorityEntity;
import edu.piotrjonski.scrumus.domain.Priority;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class PriorityDAO extends AbstractDAO<PriorityEntity, Priority> {

    public PriorityDAO() {
        this(PriorityEntity.class);
    }

    private PriorityDAO(final Class entityClass) {
        super(entityClass);
    }

    public List<String> findAllNames() {
        return entityManager.createNamedQuery(PriorityEntity.FIND_ALL_NAMES)
                            .getResultList();
    }

    @Override
    protected PriorityEntity mapToDatabaseModel(final Priority domainModel) {
        PriorityEntity priorityEntity = new PriorityEntity();
        priorityEntity.setId(domainModel.getId());
        priorityEntity.setName(domainModel.getName());
        return priorityEntity;
    }

    @Override
    protected Priority mapToDomainModel(final PriorityEntity dbModel) {
        Priority priority = new Priority();
        priority.setId(dbModel.getId());
        priority.setName(dbModel.getName());
        return priority;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(PriorityEntity.FIND_ALL, PriorityEntity.class);
    }

}
