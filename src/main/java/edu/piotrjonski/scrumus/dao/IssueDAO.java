package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.IssueEntity;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Issue;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.Optional;

@Stateless
public class IssueDAO extends AbstractDAO<IssueEntity, Issue> {

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private CommentDAO commentDAO;

    @Inject
    private IssueTypeDAO issueTypeDAO;

    public IssueDAO() {
        this(IssueEntity.class);
    }

    private IssueDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    protected IssueEntity mapToDatabaseModel(final Issue domainModel) {
        IssueEntity issueEntity = new IssueEntity();
        issueEntity.setId(domainModel.getId());
        issueEntity.setAssignee(findDeveloperEntity(domainModel.getAssigneeId()));
        issueEntity.setCommentEntities(commentDAO.mapToDatabaseModel(domainModel.getComments()));
        issueEntity.setCreationDate(domainModel.getCreationDate());
        issueEntity.setDefinitionOfDone(domainModel.getDefinitionOfDone());
        issueEntity.setDescription(domainModel.getDescription());
        issueEntity.setId(domainModel.getId());
        issueEntity.setIssueTypeEntity(issueTypeDAO.mapToDatabaseModelIfNotNull(domainModel.getIssueType()));
        issueEntity.setKey(domainModel.getKey());
        issueEntity.setReporter(findDeveloperEntity(domainModel.getReporterId()));
        issueEntity.setSummary(domainModel.getSummary());
        return issueEntity;
    }

    @Override
    protected Issue mapToDomainModel(final IssueEntity dbModel) {
        Issue issue = new Issue();
        issue.setId(dbModel.getId());
        issue.setComments(commentDAO.mapToDomainModel(dbModel.getCommentEntities()));
        issue.setCreationDate(dbModel.getCreationDate());
        issue.setDefinitionOfDone(dbModel.getDefinitionOfDone());
        issue.setDescription(dbModel.getDescription());
        issue.setId(dbModel.getId());
        issue.setIssueType(issueTypeDAO.mapToDomainModelIfNotNull(dbModel.getIssueTypeEntity()));
        issue.setKey(dbModel.getKey());
        issue.setSummary(dbModel.getSummary());
        issue.setAssigneeId(dbModel.getAssignee()
                                   .getId());
        issue.setReporterId(dbModel.getReporter()
                                   .getId());
        return issue;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(IssueEntity.FIND_ALL, IssueEntity.class);
    }

    @Override
    protected Query getDeleteByIdQuery() {
        return entityManager.createNamedQuery(IssueEntity.DELETE_BY_ID);
    }

    @Override
    protected String getId() {
        return IssueEntity.ID;
    }

    private DeveloperEntity findDeveloperEntity(final int id) {
        if (id != 0) {
            Optional<Developer> developer = developerDAO.findByKey(id);
            return developerDAO.mapToDatabaseModelIfNotNull(developer.get());
        } else {
            return null;
        }
    }
}
