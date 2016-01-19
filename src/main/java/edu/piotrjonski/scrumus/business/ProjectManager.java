package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.BacklogDAO;
import edu.piotrjonski.scrumus.dao.ProjectDAO;
import edu.piotrjonski.scrumus.domain.Project;
import edu.piotrjonski.scrumus.domain.Team;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class ProjectManager {

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private PermissionManager permissionManager;

    @Inject
    private IssueManager issueManager;

    @Inject
    private StoryManager storyManager;

    @Inject
    private SprintManager sprintManager;

    @Inject
    private TeamManager teamManager;

    @Inject
    private BacklogDAO backlogDAO;

    public Optional<Project> create(Project project) throws AlreadyExistException {
        if (exists(project)) {
            throw new AlreadyExistException("Project already exists.");
        }
        return projectDAO.saveOrUpdate(project);
    }

    public void update(Project project) throws NotExistException {
        if (!exists(project)) {
            throw new NotExistException("Projekt nie istnieje!");
        }
        Project savedProject = projectDAO.findById(project.getKey())
                                         .get();
        project.setCreationDate(savedProject.getCreationDate());
        projectDAO.saveOrUpdate(project);
    }

    public List<Project> findAllProjects() {
        return projectDAO.findAll();
    }

    public Optional<Project> findProject(String projectKey) {
        return projectDAO.findById(projectKey);
    }

    public void delete(String projectKey) {
        permissionManager.removeProductOwnerFromProject(projectKey);
        storyManager.deleteStoriesFromProject(projectKey);
        sprintManager.deleteSprintsFromProject(projectKey);
        projectDAO.delete(projectKey);
        deleteBacklogForProject(projectKey);
        issueManager.deleteIssuesFromProject(projectKey);
    }

    public List<String> findAllKeys() {
        return projectDAO.findAllKeys();
    }

    public List<String> findAllNames() {
        return projectDAO.findAllNames();
    }

    public List<Project> getUserProjects(final String username) {
        return teamManager.findTeamsForUser(username)
                          .stream()
                          .map(Team::getProjects)
                          .flatMap(Collection::stream)
                          .collect(Collectors.toList());
    }

    private boolean exists(Project project) {
        return projectDAO.exist(project.getKey());
    }

    private void deleteBacklogForProject(String projectKey) {
        backlogDAO.findBacklogForProject(projectKey)
                  .ifPresent(backlog -> backlogDAO.delete(backlog.getId()));
    }

}
