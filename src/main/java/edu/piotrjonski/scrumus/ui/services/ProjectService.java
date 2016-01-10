package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.ProjectManager;
import edu.piotrjonski.scrumus.domain.Project;
import edu.piotrjonski.scrumus.services.ProjectKeyGenerator;
import edu.piotrjonski.scrumus.ui.configuration.I18NProvider;
import edu.piotrjonski.scrumus.ui.configuration.PathProvider;
import lombok.Data;
import org.slf4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@SessionScoped
@Named
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


    public String createProject() {
        if (validateFields()) {
            return null;
        }
        Project project = createProjectFromFields();
        try {
            projectManager.create(project);
            return pathProvider.getRedirectPath("admin.listProjects");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.create.project", null);
            return null;
        }
    }

    public Project findByProjectKey(String projectKey) {
        Project project = projectManager.findProject(projectKey)
                                        .orElse(null);
        projectName = project.getName();
        this.projectKey = project.getKey();
        description = project.getDescription();
        definitionOfDone = project.getDefinitionOfDone();
        return project;
    }

    public List<Project> getAllProjects() {
        return projectManager.findAllProjects();
    }

    public void generateProjectKey() {
        projectKey = projectKeyGenerator.generateProjectKey(projectName);
    }

    public String editProject() {
        Project projectFromFields = createProjectFromFields();
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

    private boolean validateProjectName(String projectName) {
        if (occupiedChecker.isProjectNameOccupied(projectName)) {
            return createFacesMessage("page.validator.occupied.project.name", "createProjectForm:projectName");
        }
        return false;
    }

    private boolean validateProjectKey(String projectKey) {
        if (occupiedChecker.isProjectKeyOccupied(projectKey)) {
            return createFacesMessage("page.validator.occupied.project.key", "createProjectForm:projectKey");
        }
        return false;
    }

    private boolean createFacesMessage(String property, String field) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                     i18NProvider.getPath(property),
                                                     null);
        facesContext.addMessage(field, facesMessage);
        return true;
    }

    private boolean validateFields() {
        return validateProjectName(projectName) && validateProjectKey(projectKey);
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
