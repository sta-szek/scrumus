package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.ProjectManager;
import edu.piotrjonski.scrumus.domain.Project;
import edu.piotrjonski.scrumus.services.ProjectKeyGenerator;
import edu.piotrjonski.scrumus.ui.configuration.I18NProvider;
import edu.piotrjonski.scrumus.ui.configuration.PathProvider;
import lombok.Data;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@RequestScoped
@Named
public class ProjectService {

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
            return pathProvider.getProperty("admin.listProjects");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.create.project", null);
            return null;
        }
    }

    public boolean validateProjectName(String projectName) {
        if (occupiedChecker.isProjectNameOccupied(projectName)) {
            return createFacesMessage("page.validator.occupied.project.name", "createProjectForm:projectName");
        }
        return false;
    }

    public boolean validateProjectKey(String projectKey) {
        if (occupiedChecker.isProjectKeyOccupied(projectKey)) {
            return createFacesMessage("page.validator.occupied.project.key", "createProjectForm:projectKey");
        }
        return false;
    }

    public void generateProjectKey() {
        projectKey = projectKeyGenerator.generateProjectKey(projectName);
    }

    private boolean createFacesMessage(String property, String field) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                     i18NProvider.getProperty(property),
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
        return project;
    }
}
