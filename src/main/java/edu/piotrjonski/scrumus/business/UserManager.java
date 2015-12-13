package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.DeveloperDAO;
import edu.piotrjonski.scrumus.domain.Developer;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UserManager {

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private PermissionManager permissionManager;

    public Developer create(Developer developer) throws AlreadyExistException {
        if (developerExist(developer)) {
            throw new AlreadyExistException("User already exist.");
        }
        return developerDAO.saveOrUpdate(developer)
                           .orElse(developer);
    }

    public void delete(int userId) {
        developerDAO.delete(userId);
    }

    public boolean grantAdminPermission(Developer user) {
        if (developerExist(user)) {
            permissionManager.grantAdminPermission(user);
            return true;
        }
        return false;
    }

    private boolean developerExist(Developer developer) {
        return developerDAO.exist(developer.getId());
    }

}
