package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.BacklogDAO;
import edu.piotrjonski.scrumus.dao.ProductOwnerDAO;
import edu.piotrjonski.scrumus.dao.ProjectDAO;
import edu.piotrjonski.scrumus.domain.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
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

    @Inject
    private UserManager userManager;

    @Inject
    private ProductOwnerDAO productOwnerDAO;

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
        deleteBacklogForProject(projectKey);
        Project project = removeCurrentSprint(projectKey);
        removeAllTeams(projectKey, project);
        deleteAllIssuesFromBacklog(projectKey);
        permissionManager.removeProductOwnerFromProject(projectKey);
        storyManager.deleteStoriesFromProject(projectKey);
        sprintManager.deleteSprintsFromProject(projectKey);
        projectDAO.delete(projectKey);
        issueManager.deleteIssuesFromProject(projectKey);
    }

    public List<String> findAllKeys() {
        return projectDAO.findAllKeys();
    }

    public List<String> findAllNames() {
        return projectDAO.findAllNames();
    }

    public List<Project> getUserProjects(final String username) {
        Project project = findProjectIfUserIsProductOwner(username);
        List<Project> lisOfUserProjects = teamManager.findTeamsForUser(username)
                                                     .stream()
                                                     .map(Team::getProjects)
                                                     .flatMap(Collection::stream)
                                                     .collect(Collectors.toList());
        if (project != null) {
            lisOfUserProjects.add(project);
        }
        return lisOfUserProjects;
    }

    public Optional<Backlog> findBacklogForProject(final String projectKey) {
        return backlogDAO.findBacklogForProject(projectKey);
    }

    private Project findProjectIfUserIsProductOwner(final String username) {
        Optional<Developer> userOptional = userManager.findByUsername(username);
        if (userOptional.isPresent()) {
            Optional<ProductOwner> productOwnerOptional = productOwnerDAO.findByDeveloperId(userOptional.get()
                                                                                                        .getId());
            if (productOwnerOptional.isPresent()) {
                return productOwnerOptional.get()
                                           .getProject();
            }

        }
        return null;
    }

    private void removeAllTeams(final String projectKey, final Project project) {
        teamManager.findTeamsForProject(projectKey)
                   .forEach(team -> permissionManager.removeTeamFromProject(team, project));
    }

    private Project removeCurrentSprint(final String projectKey) {
        Project project = projectDAO.findById(projectKey)
                                    .get();
        project.setCurrentSprintId(0);
        project = projectDAO.saveOrUpdate(project)
                            .get();
        return project;
    }

    private boolean exists(Project project) {
        return projectDAO.exist(project.getKey());
    }

    private void deleteBacklogForProject(String projectKey) {
        backlogDAO.findBacklogForProject(projectKey)
                  .map(Backlog::getId)
                  .ifPresent(backlogDAO::delete);
    }

    private void deleteAllIssuesFromBacklog(String projectKey) {
        Optional<Backlog> backlogForProject = backlogDAO.findBacklogForProject(projectKey);
        if (backlogForProject.isPresent()) {
            Backlog backlog = backlogForProject.get();
            backlog.setIssues(new ArrayList<>());
            backlogDAO.saveOrUpdate(backlog);
        }
    }

}
