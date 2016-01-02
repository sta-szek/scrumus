package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.ProjectDAO;
import edu.piotrjonski.scrumus.dao.SprintDAO;
import edu.piotrjonski.scrumus.domain.Sprint;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class SprintManager {

    @Inject
    private SprintDAO sprintDAO;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private PermissionManager permissionManager;

    public Optional<Sprint> createSprint(Sprint sprint) throws AlreadyExistException {
        if (sprintExist(sprint)) {
            throw new AlreadyExistException("Sprint już istnieje");
        }
        return sprintDAO.saveOrUpdate(sprint);
    }

    public Optional<Sprint> findSprint(int sprintId) {
        return sprintDAO.findById(sprintId);
    }

    public List<Sprint> findAllSprintsForProject(String projectKey) {
        return sprintDAO.findAllSprints(projectKey);
    }

    private boolean sprintExist(Sprint sprint) {
        return sprintDAO.exist(sprint.getId());
    }
}
