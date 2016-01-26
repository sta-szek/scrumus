package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.*;
import edu.piotrjonski.scrumus.domain.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
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
    private StoryDAO storyDAO;

    @Inject
    private PermissionManager permissionManager;

    @Inject
    private BacklogDAO backlogDAO;

    @Inject
    private IssueTypeDAO issueTypeDAO;

    public void deleteIssuesFromProject(String projectKey) {
        issueDAO.deleteIssuesFromProject(projectKey);
    }

    public Optional<Issue> create(Issue issue, Project project) throws AlreadyExistException, NotExistException {
        if (issueExist(issue)) {
            throw new AlreadyExistException("Issue already exist.");
        }
        if (!projectExist(project)) {
            throw new NotExistException("Project does not exist");
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

    public void delete(int issueId) {
        Optional<Story> storyOptional = storyDAO.findStoryForIssue(issueId);
        Optional<Issue> issueOptional = issueDAO.findById(issueId);
        if (issueOptional.isPresent()) {
            if (storyOptional.isPresent()) {
                Story story = storyOptional.get();
                story.removeIssue(issueOptional.get());
                storyDAO.saveOrUpdate(story);
            }
            issueDAO.delete(issueId);
        }
    }

    public Optional<Priority> createPriority(Priority priority) throws AlreadyExistException {
        if (priorityExist(priority)) {
            throw new AlreadyExistException("Priorytet już istnieje.");
        }
        return priorityDAO.saveOrUpdate(priority);
    }

    public void deletePriority(Priority priority) throws NotExistException, IllegalOperationException {
        if (!priorityExist(priority)) {
            throw new NotExistException("Priorytet nie istnieje.");
        }
        if (isPriorityInUser(priority)) {
            throw new IllegalOperationException("Priorytet jest używany przez inne zadania.");
        }
        priorityDAO.delete(priority.getId());
    }

    public Optional<Priority> updatePriority(Priority newPriority) throws NotExistException {
        if (!priorityExist(newPriority)) {
            throw new NotExistException("Priorytet nie istnieje.");
        }
        return priorityDAO.saveOrUpdate(newPriority);
    }

    public Optional<IssueType> createIssueType(IssueType issueType) throws AlreadyExistException {
        if (issueTypeExist(issueType)) {
            throw new AlreadyExistException("Rodzaj zadania już istnieje już istnieje.");
        }
        return issueTypeDAO.saveOrUpdate(issueType);
    }

    public void deleteIssueType(IssueType issueType) throws NotExistException, IllegalOperationException {
        if (!issueTypeExist(issueType)) {
            throw new NotExistException("Rodzaj zadania nie istnieje.");
        }
        if (isIssueTypeInUse(issueType)) {
            throw new IllegalOperationException("Rodzaj zadania jest używany przez inne zadania.");
        }
        issueTypeDAO.delete(issueType.getId());
    }

    public Optional<IssueType> updateIssueType(IssueType issueType) throws NotExistException {
        if (!issueTypeExist(issueType)) {
            throw new NotExistException("Rodzaj zadania nie istnieje.");
        }
        return issueTypeDAO.saveOrUpdate(issueType);
    }

    public Optional<State> createState(State state) throws AlreadyExistException {
        if (stateExist(state)) {
            throw new AlreadyExistException("Stan już istnieje.");
        }
        return stateDAO.saveOrUpdate(state);
    }

    public void deleteState(State state) throws NotExistException, IllegalOperationException {
        if (!stateExist(state)) {
            throw new NotExistException("Stan nie istnieje.");
        }
        if (isStateInUse(state)) {
            throw new IllegalOperationException("Stan jest używany przez inne zadania.");
        }
        stateDAO.delete(state.getId());
    }

    public Optional<State> updateState(State state) throws NotExistException {
        if (!stateExist(state)) {
            throw new NotExistException("Stan nie istnieje.");
        }
        return stateDAO.saveOrUpdate(state);
    }

    public List<String> findAllIssueTypeNames() {
        return issueTypeDAO.findAllNames();
    }

    public List<String> findAllStateNames() {
        return stateDAO.findAllNames();
    }

    public List<String> findAllPriorityNames() {return priorityDAO.findAllNames();}

    public List<IssueType> findAllIssueTypes() {
        return issueTypeDAO.findAll();
    }

    public List<State> findAllStates() {return stateDAO.findAll();}

    public List<Priority> findAllPriorities() {return priorityDAO.findAll();}

    public Optional<IssueType> findIssueType(final int issueTypeId) {
        return issueTypeDAO.findById(issueTypeId);
    }

    public Optional<State> findState(final int stateId) {
        return stateDAO.findById(stateId);
    }

    public Optional<Priority> findPriority(final int priorityId) {
        return priorityDAO.findById(priorityId);
    }

    public Optional<Issue> findIssue(final int issueIntId) {
        return issueDAO.findById(issueIntId);
    }

    public List<String> findAllIssueSummaries() {
        return issueDAO.findAllIssueSummaries();
    }

    private boolean isIssueTypeInUse(final IssueType issueType) {
        return issueDAO.isIssueTypeInUse(issueType.getName());
    }

    private boolean isStateInUse(final State state) {
        return issueDAO.isStateInUse(state.getName());
    }

    private boolean isPriorityInUser(final Priority priority) {return issueDAO.isPriorityInUse(priority.getName());}

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

    private boolean issueTypeExist(final IssueType issueType) {
        return issueTypeDAO.exist(issueType.getId());
    }

}
