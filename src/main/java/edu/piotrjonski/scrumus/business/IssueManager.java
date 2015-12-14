package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.BacklogDAO;
import edu.piotrjonski.scrumus.dao.IssueDAO;
import edu.piotrjonski.scrumus.dao.PriorityDAO;
import edu.piotrjonski.scrumus.dao.ProjectDAO;
import edu.piotrjonski.scrumus.domain.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

@Stateless
public class IssueManager {

    @Inject
    private IssueDAO issueDAO;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private PriorityDAO priorityDAO;

    @Inject
    private PermissionManager permissionManager;

    @Inject
    private BacklogDAO backlogDAO;

    public void changePriority(Issue issue, Priority newPriority, Developer user) {
        Optional<Project> project = projectDAO.findById(issue.getProjectKey());
        if (allValuesExist(issue, newPriority, project) && permissionManager.isProductOwner(project.get(), user)) {
            issue.setPriority(newPriority);
            issueDAO.saveOrUpdate(issue);
        }
    }

    public Issue create(Issue issue, Project project) throws Exception {
        if (issueExist(issue)) {
            throw new AlreadyExistException("Issue already exist.");
        }
        if (!projectExist(project)) {
            throw new Exception("Project does not exist");
        }
        issue.setProjectKey(project.getKey());
        Issue savedIssue = saveIssue(issue);
        addIssueToBacklog(savedIssue, project);
        return savedIssue;
    }

    private boolean allValuesExist(final Issue issue, final Priority newPriority, final Optional<Project> project) {
        return project.isPresent() && issueExist(issue) && priorityExist(newPriority);
    }

    private void addIssueToBacklog(final Issue issue, Project project) {
        Backlog backlog = backlogDAO.findBacklogForProject(project.getKey())
                                    .get();
        backlog.addIssue(issue);
        backlogDAO.saveOrUpdate(backlog);
    }

    private Issue saveIssue(Issue issue) {
        return issueDAO.saveOrUpdate(issue)
                       .get();
    }

    private boolean issueExist(final Issue issue) {
        return issueDAO.exist(issue.getId());
    }

    private boolean projectExist(final Project project) {
        return projectDAO.exist(project.getKey());
    }

    private boolean priorityExist(final Priority priority) {
        return priorityDAO.exist(priority.getId());
    }

}
