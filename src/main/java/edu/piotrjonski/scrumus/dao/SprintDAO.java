package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveEntity;
import edu.piotrjonski.scrumus.dao.model.project.SprintEntity;
import edu.piotrjonski.scrumus.domain.Project;
import edu.piotrjonski.scrumus.domain.Retrospective;
import edu.piotrjonski.scrumus.domain.Sprint;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Stateless
public class SprintDAO extends AbstractDAO<SprintEntity, Sprint> {

    @Inject
    private RetrospectiveDAO retrospectiveDAO;

    @Inject
    private ProjectDAO projectDAO;

    public SprintDAO() {
        this(SprintEntity.class);
    }

    private SprintDAO(final Class entityClass) {
        super(entityClass);
    }

    public List<Sprint> findAllSprints(String projectKey) {
        return mapToDomainModel(entityManager.createNamedQuery(ProjectEntity.FIND_ALL_SPRINTS)
                                             .setParameter(ProjectEntity.KEY, projectKey)
                                             .getResultList());
    }

    @Override
    protected SprintEntity mapToDatabaseModel(final Sprint domainModel) {
        SprintEntity sprintEntity = new SprintEntity();
        sprintEntity.setDefinitionOfDone(domainModel.getDefinitionOfDone());
        sprintEntity.setId(domainModel.getId());
        sprintEntity.setName(domainModel.getName());
        sprintEntity.setRetrospectiveEntity(findRetrospectiveEntity(domainModel.getRetrospectiveId()));
        sprintEntity.setTimeRange(domainModel.getTimeRange());
        sprintEntity.setProjectEntity(findProjectEntity(domainModel.getProjectKey()));
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
        sprint.setProjectKey(getProjectKey(dbModel));
        return sprint;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(SprintEntity.FIND_ALL, SprintEntity.class);
    }

    private int getRetrospectiveId(final SprintEntity dbModel) {
        RetrospectiveEntity retrospectiveEntity = dbModel.getRetrospectiveEntity();
        return retrospectiveEntity != null
               ? retrospectiveEntity.getId()
               : 0;
    }

    private RetrospectiveEntity findRetrospectiveEntity(final int id) {
        if (id != 0) {
            Optional<Retrospective> retrospective = retrospectiveDAO.findById(id);
            if (retrospective.isPresent()) {
                return retrospectiveDAO.mapToDatabaseModelIfNotNull(retrospective.get());
            }
        }
        return null;
    }

    private String getProjectKey(final SprintEntity dbModel) {
        ProjectEntity projectEntity = dbModel.getProjectEntity();
        return projectEntity != null
               ? projectEntity.getKey()
               : null;
    }

    private ProjectEntity findProjectEntity(final String projetKey) {
        if (projetKey != null) {
            Optional<Project> project = projectDAO.findById(projetKey);
            if (project.isPresent()) {
                return projectDAO.mapToDatabaseModelIfNotNull(project.get());
            }
        }
        return null;
    }
}
