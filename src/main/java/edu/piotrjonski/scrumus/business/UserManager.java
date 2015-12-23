package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.DeveloperDAO;
import edu.piotrjonski.scrumus.dao.PasswordDAO;
import edu.piotrjonski.scrumus.domain.Developer;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UserManager {

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private PasswordDAO passwordDAO;

    @Inject
    private PermissionManager permissionManager;

    public Developer create(Developer developer) throws AlreadyExistException {
        if (developerExist(developer)) {
            throw new AlreadyExistException("User already exist.");
        }
        return developerDAO.saveOrUpdate(developer)
                           .orElse(developer);
    }

    public Developer update(Developer developer) throws NotExistException {
        if (!developerExist(developer)) {
            throw new NotExistException("User does not exist.");
        }
        return developerDAO.saveOrUpdate(developer)
                           .get();
    }

    public void delete(int userId) {
        developerDAO.delete(userId);
    }

    private boolean developerExist(Developer developer) {
        return developerDAO.exist(developer.getId());
    }

}
