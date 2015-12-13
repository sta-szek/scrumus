package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.BacklogEntity;
import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
import edu.piotrjonski.scrumus.domain.Backlog;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Optional;

@Stateless
public class BacklogDAO extends AbstractDAO<BacklogEntity, Backlog> {

    @Inject
    private IssueDAO issueDAO;

    public BacklogDAO() {
        this(BacklogEntity.class);
    }

    private BacklogDAO(final Class entityClass) {
        super(entityClass);
    }

    public Optional<Backlog> findBacklogForProject(String projectKey) {
        try {
            BacklogEntity backlogEntity = entityManager.createNamedQuery(BacklogEntity.FIND_BY_PROJECT_KEY, BacklogEntity.class)
                                                       .setParameter(ProjectEntity.KEY, projectKey)
                                                       .getSingleResult();
            return Optional.of(mapToDomainModelIfNotNull(backlogEntity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    protected BacklogEntity mapToDatabaseModel(final Backlog domainModel) {
        BacklogEntity backlogEntity = new BacklogEntity();
        backlogEntity.setId(domainModel.getId());
        backlogEntity.setIssueEntities(issueDAO.mapToDatabaseModel(domainModel.getIssues()));
        return backlogEntity;
    }

    @Override
    protected Backlog mapToDomainModel(final BacklogEntity dbModel) {
        Backlog backlog = new Backlog();
        backlog.setId(dbModel.getId());
        backlog.setIssues(issueDAO.mapToDomainModel(dbModel.getIssueEntities()));
        return backlog;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(BacklogEntity.FIND_ALL, BacklogEntity.class);
    }
}
