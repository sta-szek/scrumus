package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.ProjectManager;
import edu.piotrjonski.scrumus.domain.Project;
import edu.piotrjonski.scrumus.services.ProjectKeyGenerator;
import edu.piotrjonski.scrumus.ui.configuration.I18NProvider;
import edu.piotrjonski.scrumus.ui.configuration.PathProvider;
import lombok.Data;
import org.slf4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Size(max = 255, message = "{validator.size.projectName}")
    private String projectName;

    @Size(max = 8, message = "{validator.size.projectKey}")
    private String projectKey;

    @Size(max = 4096, message = "{validator.size.description}")
    private String description;

    @Size(max = 4096, message = "{validator.size.definitionOfDone}")
    private String definitionOfDone;

    private String projectToDelete;

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
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey.toUpperCase();
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
