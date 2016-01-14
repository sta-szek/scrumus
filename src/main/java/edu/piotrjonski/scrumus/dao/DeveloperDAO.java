package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.CommentEntity;
import edu.piotrjonski.scrumus.dao.model.project.IssueEntity;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Developer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Stateless
public class DeveloperDAO extends AbstractDAO<DeveloperEntity, Developer> {

    @Inject
    private PasswordDAO passwordDAO;

    public DeveloperDAO() {
        this(DeveloperEntity.class);
    }

    private DeveloperDAO(final Class entityClass) {
        super(entityClass);
    }

    public List<String> findAllUsernames() {
        return entityManager.createNamedQuery(DeveloperEntity.FIND_ALL_USERNAMES)
                            .getResultList();
    }

    public List<String> findAllEmails() {
        return entityManager.createNamedQuery(DeveloperEntity.FIND_ALL_EMAILS)
                            .getResultList();
    }

    public Optional<Developer> findByUsername(String username) {
        try {
            DeveloperEntity developerEntity = entityManager.createNamedQuery(DeveloperEntity.FIND_BY_USERNAME, DeveloperEntity.class)
                                                           .setParameter(DeveloperEntity.USERNAME, username)
                                                           .getSingleResult();
            return Optional.of(mapToDomainModelIfNotNull(developerEntity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public boolean emailExist(String email) {
        try {
            entityManager.createNamedQuery(DeveloperEntity.FIND_BY_MAIL, DeveloperEntity.class)
                         .setParameter(DeveloperEntity.EMAIL, email)
                         .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
        //TODO TESTY
    }

    @Override
    public void delete(final Object id) {
        setCommentAuthorToNull(id);
        setAssigneeAndReporterToNull(id);
        passwordDAO.deleteUserPassword((Integer) id);
        super.delete(id);
        //TODO TESTY
    }

    @Override
    protected DeveloperEntity mapToDatabaseModel(final Developer domainModel) {
        DeveloperEntity dbDeveloperEntity = new DeveloperEntity();
        dbDeveloperEntity.setId(domainModel.getId());
        dbDeveloperEntity.setFirstName(domainModel.getFirstName());
        dbDeveloperEntity.setSurname(domainModel.getSurname());
        dbDeveloperEntity.setUsername(domainModel.getUsername());
        dbDeveloperEntity.setEmail(domainModel.getEmail());
        return dbDeveloperEntity;
    }

    @Override
    protected Developer mapToDomainModel(final DeveloperEntity dbModel) {
        edu.piotrjonski.scrumus.domain.Developer domainDeveloper = new edu.piotrjonski.scrumus.domain.Developer();
        domainDeveloper.setId(dbModel.getId());
        domainDeveloper.setFirstName(dbModel.getFirstName());
        domainDeveloper.setSurname(dbModel.getSurname());
        domainDeveloper.setUsername(dbModel.getUsername());
        domainDeveloper.setEmail(dbModel.getEmail());
        return domainDeveloper;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(DeveloperEntity.FIND_ALL, DeveloperEntity.class);
    }

    private void setCommentAuthorToNull(final Object id) {
        List<CommentEntity> comments = entityManager.createNamedQuery(CommentEntity.FIND_ALL_DEVELOPER_COMMENTS)
                                                    .setParameter(DeveloperEntity.ID, id)
                                                    .getResultList();
        comments.forEach(comment -> comment.setDeveloperEntity(null));
        comments.forEach(entityManager::merge);
    }

    private void setAssigneeAndReporterToNull(final Object id) {
        List<IssueEntity> issues = entityManager.createNamedQuery(IssueEntity.FIND_ALL_DEVELOPER_ISSUES)
                                                .setParameter(DeveloperEntity.ID, id)
                                                .getResultList();
        issues.forEach(issue -> issue.setReporter(null));
        issues.forEach(issue -> issue.setAssignee(null));
        issues.forEach(entityManager::merge);
    }

}
