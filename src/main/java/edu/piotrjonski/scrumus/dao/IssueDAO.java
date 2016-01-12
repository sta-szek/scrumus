package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.IssueEntity;
import edu.piotrjonski.scrumus.dao.model.project.IssueTypeEntity;
import edu.piotrjonski.scrumus.dao.model.project.PriorityEntity;
import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Issue;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Stateless
public class IssueDAO extends AbstractDAO<IssueEntity, Issue> {

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private CommentDAO commentDAO;

    @Inject
    private PriorityDAO priorityDAO;

    @Inject
    private IssueTypeDAO issueTypeDAO;

    @Inject
    private StateDAO stateDAO;

    public IssueDAO() {
        this(IssueEntity.class);
    }

    private IssueDAO(final Class entityClass) {
        super(entityClass);
    }

    public void deleteIssuesFromProject(String projectKey) {
        entityManager.createNamedQuery(IssueEntity.DELETE_PROJECT_ISSUES)
                     .setParameter(ProjectEntity.KEY, projectKey)
                     .executeUpdate();
    }

    public List<Issue> findAllIssuesWithIssueType(String issueTypeName) {
        return entityManager.createNamedQuery(IssueEntity.FIND_ALL_ISSUES_WITH_ISSUE_TYPE)
                            .setParameter(IssueTypeEntity.ISSUE_TYPE_NAME, issueTypeName)
                            .getResultList();
    }

    public boolean isIssueTypeInUse(String issueTypeName) {
        return findAllIssuesWithIssueType(issueTypeName).size() > 0;
    }

    public boolean isPriorityInUse(final String priorityName) {
        return findAllIssuesWithPriority(priorityName).size() > 0;
    }

    public List<Issue> findAllIssuesWithPriority(final String priorityName) {
        return entityManager.createNamedQuery(IssueEntity.FIND_ALL_ISSUES_WITH_PRIORITY)
                            .setParameter(PriorityEntity.PRIORITY_NAME, priorityName)
                            .getResultList();
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
        issueEntity.setProjectKey(domainModel.getProjectKey());
        issueEntity.setReporter(findDeveloperEntity(domainModel.getReporterId()));
        issueEntity.setSummary(domainModel.getSummary());
        issueEntity.setPriorityEntity(priorityDAO.mapToDatabaseModelIfNotNull(domainModel.getPriority()));
        issueEntity.setStateEntity(stateDAO.mapToDatabaseModelIfNotNull(domainModel.getState()));
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
        issue.setProjectKey(dbModel.getProjectKey());
        issue.setSummary(dbModel.getSummary());
        issue.setAssigneeId(getDeveloperId(dbModel));
        issue.setReporterId(getDeveloperId(dbModel));
        issue.setPriority(priorityDAO.mapToDomainModelIfNotNull(dbModel.getPriorityEntity()));
        issue.setState(stateDAO.mapToDomainModelIfNotNull(dbModel.getStateEntity()));
        return issue;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(IssueEntity.FIND_ALL, IssueEntity.class);
    }

    private int getDeveloperId(final IssueEntity dbModel) {
        DeveloperEntity reporter = dbModel.getReporter();
        return reporter != null
               ? reporter.getId()
               : 0;
    }

    private DeveloperEntity findDeveloperEntity(final int id) {
        if (id != 0) {
            Optional<Developer> developer = developerDAO.findById(id);
            if (developer.isPresent()) {
                return developerDAO.mapToDatabaseModelIfNotNull(developer.get());
            }
        }
        return null;
    }

}
