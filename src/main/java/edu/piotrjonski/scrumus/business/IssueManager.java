package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.*;
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
    private StateDAO stateDAO;

    @Inject
    private PermissionManager permissionManager;

    @Inject
    private BacklogDAO backlogDAO;

    public Optional<Issue> create(Issue issue, Project project) throws Exception {
        if (issueExist(issue)) {
            throw new AlreadyExistException("Issue already exist.");
        }
        if (!projectExist(project)) {
            throw new Exception("Project does not exist");
        }
        issue.setProjectKey(project.getKey());
        Issue savedIssue = saveIssue(issue);
        addIssueToBacklog(savedIssue, project);
        return Optional.of(savedIssue);
    }

    public Optional<Issue> update(Issue issue) {
        if (issueExist(issue)) {
            return issueDAO.saveOrUpdate(issue);
        }
        return Optional.empty();
    }

    public Optional<Priority> createPriority(Priority priority) throws AlreadyExistException {
        if (priorityExist(priority)) {
            throw new AlreadyExistException("Priorytet już istnieje.");
        }
        return priorityDAO.saveOrUpdate(priority);
    }

    public void deletePriority(Priority priority) throws NotExistException {
        if (!priorityExist(priority)) {
            throw new NotExistException("Priorytet nie istnieje.");
        }
        priorityDAO.delete(priority.getId());
    }

    public Optional<Priority> updatePriority(Priority newPriority) throws NotExistException {
        if (!priorityExist(newPriority)) {
            throw new NotExistException("Priorytet nie istnieje.");
        }
        return priorityDAO.saveOrUpdate(newPriority);
    }

    public Optional<State> createState(State state) throws AlreadyExistException {
        if (stateExist(state)) {
            throw new AlreadyExistException("Stan już istnieje.");
        }
        return stateDAO.saveOrUpdate(state);
    }

    public void deleteState(State state) throws NotExistException {
        if (!stateExist(state)) {
            throw new NotExistException("Stan nie istnieje.");
        }
        stateDAO.delete(state.getId());
    }

    public Optional<State> updateState(State state) throws NotExistException {
        if (!stateExist(state)) {
            throw new NotExistException("Stan nie istnieje.");
        }
        return stateDAO.saveOrUpdate(state);
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

    private boolean stateExist(final State state) {
        return stateDAO.exist(state.getId());
    }

}
