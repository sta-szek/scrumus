package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.ProjectDAO;
import edu.piotrjonski.scrumus.domain.Project;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

@Stateless
public class ProjectManager {

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private PermissionManager permissionManager;

    public Optional<Project> create(Project project) throws AlreadyExistException {
        if (exists(project)) {
            throw new AlreadyExistException("Project already exists.");
        }
        return projectDAO.saveOrUpdate(project);
    }

    public Optional<Project> findProject(String projectKey) {
        return projectDAO.findById(projectKey);
    }

    public void delete(String projectKey) {
        projectDAO.delete(projectKey);
    }

    private boolean exists(Project project) {
        return projectDAO.exist(project.getKey());
    }

}
