package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.*;
import edu.piotrjonski.scrumus.domain.*;
import edu.piotrjonski.scrumus.services.ProjectKeyGenerator;
import edu.piotrjonski.scrumus.ui.configuration.I18NProvider;
import edu.piotrjonski.scrumus.ui.configuration.PathProvider;
import edu.piotrjonski.scrumus.ui.view.DashboardTitledColumn;
import lombok.Data;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudItem;
import org.primefaces.model.tagcloud.TagCloudModel;
import org.slf4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Data
public class ProjectService implements Serializable {

    @Inject
    private transient Logger logger;

    @Inject
    private ProjectManager projectManager;

    @Inject
    private PathProvider pathProvider;

    @Inject
    private I18NProvider i18NProvider;

    @Inject
    private OccupiedChecker occupiedChecker;

    @Inject
    private ProjectKeyGenerator projectKeyGenerator;

    @Inject
    private PermissionManager permissionManager;

    @Inject
    private TeamManager teamManager;

    @Inject
    private SprintManager sprintManager;

    @Inject
    private StoryManager storyManager;

    @Inject
    private IssueManager issueManager;

    @Size(max = 255, message = "{validator.size.projectName}")
    private String projectName;

    @Size(max = 8, message = "{validator.size.projectKey}")
    private String projectKey;

    @Size(max = 4096, message = "{validator.size.description}")
    private String description;

    @Size(max = 4096, message = "{validator.size.definitionOfDone}")
    private String definitionOfDone;

    private String projectToDelete;

    private String addTeamProjectKey;

    private String teamToAdd;

    public List<Issue> findIssuesForStory(String storyId) {
        try {
            int storyIntId = Integer.parseInt(storyId);
            return findIssuesForStory(storyIntId);
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }
    }

    public DashboardModel createDashboardModelForStory(String storyId) {
        try {
            int storyIntId = Integer.parseInt(storyId);
            List<Issue> issuesForStory = findIssuesForStory(storyIntId);
            List<String> allStateNames = issueManager.findAllStateNames();
            return createDashboardModel(issuesForStory, allStateNames);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public List<Story> findStoriesForSprint(String sprintId) {
        try {
            int sprintIntId = Integer.parseInt(sprintId);
            return storyManager.findAllStoriesForSprint(sprintIntId);
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }
    }

    public Sprint findSprint(String sprintId) {
        try {
            int sprintIntId = Integer.parseInt(sprintId);
            return sprintManager.findSprint(sprintIntId)
                                .orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public List<Project> getUserProjects(String username) {
        return projectManager.getUserProjects(username);
    }

    public String createProject() {
        if (validateFields()) {
            return null;
        }
        Project project = createProjectFromFields();
        try {
            projectManager.create(project);
            logger.info("Created project with key '" + project.getKey() + "'.");
            return pathProvider.getRedirectPath("admin.listProjects");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.create.project", null);
            return null;
        }
    }

    public void setProjectToDelete(String projectKey) {
        projectToDelete = projectKey;
    }

    public void deleteProject() {
        projectManager.delete(projectToDelete);
        logger.info("Project with key '" + projectToDelete + "' was deleted.");
    }

    public Project findByProjectKey(String projKey) {
        Optional<Project> projectOptional = projectManager.findProject(projKey);
        projectOptional.ifPresent(project -> {
            projectName = project.getName();
            projectKey = project.getKey();
            description = project.getDescription();
            definitionOfDone = project.getDefinitionOfDone();
        });

        return projectOptional.orElse(null);
    }

    public List<Project> getAllProjects() {
        return projectManager.findAllProjects();
    }

    public void generateProjectKey() {
        projectKey = projectKeyGenerator.generateProjectKey(projectName);
    }

    public String editProject() {
        Project projectFromFields = createProjectFromFields();
        String projectKey = getProjectKeyFromFacesParameter();
        projectFromFields.setKey(projectKey);
        try {
            projectManager.update(projectFromFields);
            return pathProvider.getRedirectPath("admin.listProjects");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.edit.project", null);
            return null;
        }
    }

    public void clearFields() {
        projectKey = null;
        projectName = null;
        definitionOfDone = null;
        description = null;
        addTeamProjectKey = null;
        teamToAdd = null;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey.toUpperCase();
    }

    public TagCloudModel getTeamTagCloudModel(String projectKey) {
        List<Team> teams = teamManager.findTeamsForProject(projectKey);
        return createTagCloudModelForTeams(teams);
    }

    public void addTeam() {
        if (teamToAdd != null && addTeamProjectKey != null) {
            permissionManager.addTeamToProject(teamToAdd, addTeamProjectKey);
            logger.info("Team with name '" + teamToAdd + "' was added to project with key '" + addTeamProjectKey + "'");
            clearFields();
        }
    }

    public void deleteTeamFromProject(SelectEvent event) {
        TagCloudItem item = (TagCloudItem) event.getObject();
        String teamName = item.getLabel();
        String projectKey = event.getComponent()
                                 .getAttributes()
                                 .get("projectKey")
                                 .toString();
        permissionManager.removeTeamFromProject(teamName, projectKey);
        logger.info("Team with name '" + teamName + "' was removed from project with key '" + projectKey + "'");
    }

    public List<State> getAllStates() {
        return issueManager.findAllStates();
    }

    public void updateIssue(DashboardReorderEvent event) {
        logger.info("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: " +
                    event.getSenderColumnIndex());
    }

    public void updateIssue(AjaxBehaviorEvent event) {
        logger.info("Item index: " + event.getSource() + ", Column index: " + event.getBehavior() + ", Sender index: " +
                    event.getPhaseId());
    }

    private List<Issue> findIssuesForStory(int storyId) {
        return storyManager.findIssuesForStory(storyId);
    }

    private DashboardModel createDashboardModel(final List<Issue> issuesForStory, final List<String> allStateNames) {
        DashboardModel dashboardModel = new DefaultDashboardModel();
        allStateNames.stream()
                     .map(DashboardTitledColumn::new)
                     .forEach(column -> {
                         addIssues(issuesForStory, column);
                         dashboardModel.addColumn(column);
                     });
        return dashboardModel;
    }

    private void addIssues(final List<Issue> issuesForStory, final DashboardTitledColumn column) {
        issuesForStory.stream()
                      .filter(issue -> issueStateEqualsColumnTitle(issue, column))
                      .map(this::createIssueUniqueId)
                      .forEach(column::addWidget);

    }

    private String createIssueUniqueId(final Issue issue) {
        return issue.getProjectKey() + issue.getId();
    }

    private boolean issueStateEqualsColumnTitle(final Issue issue, final DashboardTitledColumn column) {
        return issue.getState()
                    .getName()
                    .equals(column.getTitle());
    }

    private TagCloudModel createTagCloudModelForTeams(final List<Team> teams) {
        TagCloudModel tagCloudModel = new DefaultTagCloudModel();

        teams.stream()
             .map(team -> new DefaultTagCloudItem(team.getName(), nextRandom()))
             .forEach(tagCloudModel::addTag);
        return tagCloudModel;
    }

    private int nextRandom() {
        Random random = new Random();
        return random.nextInt(5) + 1;
    }

    private String getProjectKeyFromFacesParameter() {
        return FacesContext.getCurrentInstance()
                           .getExternalContext()
                           .getRequestParameterMap()
                           .get("projectKey");
    }

    private boolean validateProjectName(String projectName) {
        if (occupiedChecker.isProjectNameOccupied(projectName)) {
            createFacesMessage("page.validator.occupied.project.name", "createProjectForm:projectName");
            return true;
        }
        return false;
    }

    private boolean validateProjectKey(String projectKey) {
        if (occupiedChecker.isProjectKeyOccupied(projectKey)) {
            createFacesMessage("page.validator.occupied.project.key", "createProjectForm:projectKey");
            return true;
        }
        return false;
    }

    private void createFacesMessage(String property, String field) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                     i18NProvider.getMessage(property),
                                                     null);
        facesContext.addMessage(field, facesMessage);
    }

    private boolean validateFields() {
        return validateProjectName(projectName) || validateProjectKey(projectKey);
    }

    private Project createProjectFromFields() {
        Project project = new Project();
        project.setCreationDate(LocalDateTime.now());
        project.setDefinitionOfDone(definitionOfDone);
        project.setDescription(description);
        project.setKey(projectKey);
        project.setName(projectName);
        clearFields();
        return project;
    }
}
