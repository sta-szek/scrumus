package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveEntity;
import edu.piotrjonski.scrumus.dao.model.project.SprintEntity;
import edu.piotrjonski.scrumus.domain.Retrospective;
import edu.piotrjonski.scrumus.domain.Sprint;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.Optional;

@Stateless
public class SprintDAO extends AbstractDAO<SprintEntity, Sprint> {

    @Inject
    private RetrospectiveDAO retrospectiveDAO;

    public SprintDAO() {
        this(SprintEntity.class);
    }

    private SprintDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    protected SprintEntity mapToDatabaseModel(final Sprint domainModel) {
        SprintEntity sprintEntity = new SprintEntity();
        sprintEntity.setDefinitionOfDone(domainModel.getDefinitionOfDone());
        sprintEntity.setId(domainModel.getId());
        sprintEntity.setName(domainModel.getName());
        sprintEntity.setRetrospectiveEntity(findRetrospectiveEntity(domainModel.getRetrospectiveId()));
        sprintEntity.setTimeRange(domainModel.getTimeRange());
        return sprintEntity;
    }

    @Override
    protected Sprint mapToDomainModel(final SprintEntity dbModel) {
        Sprint sprint = new Sprint();
        sprint.setDefinitionOfDone(dbModel.getDefinitionOfDone());
        sprint.setId(dbModel.getId());
        sprint.setName(dbModel.getName());
        sprint.setTimeRange(dbModel.getTimeRange());
        sprint.setRetrospectiveId(getRetrospectiveId(dbModel));
        return sprint;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(SprintEntity.FIND_ALL, SprintEntity.class);
    }

    @Override
    protected Query getDeleteByIdQuery() {
        return entityManager.createNamedQuery(SprintEntity.DELETE_BY_ID);
    }

    @Override
    protected String getId() {
        return SprintEntity.ID;
    }

    private int getRetrospectiveId(final SprintEntity dbModel) {
        RetrospectiveEntity retrospectiveEntity = dbModel.getRetrospectiveEntity();
        return retrospectiveEntity != null
               ? retrospectiveEntity.getId()
               : 0;
    }

    private RetrospectiveEntity findRetrospectiveEntity(final int id) {
        if (id != 0) {
            Optional<Retrospective> retrospective = retrospectiveDAO.findByKey(id);
            if (retrospective.isPresent()) {
                return retrospectiveDAO.mapToDatabaseModelIfNotNull(retrospective.get());
            }
        }
        return null;
    }
}
