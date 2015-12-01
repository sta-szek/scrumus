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

    @Inject
    private StoryDAO storyDAO;

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
        sprintEntity.setStories(storyDAO.mapToDatabaseModel(storyDAO.findStoriesForSprint(domainModel.getId())));
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
        if (dbModel.getRetrospectiveEntity() != null) {
            return dbModel.getRetrospectiveEntity()
                          .getId();
        } else {
            return 0;
        }
    }

    private RetrospectiveEntity findRetrospectiveEntity(final int id) {
        if (id != 0) {
            Optional<Retrospective> retrospective = retrospectiveDAO.findByKey(id);
            return retrospectiveDAO.mapToDatabaseModelIfNotNull(retrospective.get());
        } else {
            return null;
        }
    }
}
