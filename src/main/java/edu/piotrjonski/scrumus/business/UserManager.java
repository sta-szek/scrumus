package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.DeveloperDAO;
import edu.piotrjonski.scrumus.domain.Developer;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UserManager {

    @Inject
    private DeveloperDAO developerDAO;

    public Developer create(Developer developer) throws AlreadyExistException {
        if (exists(developer)) {
            throw new AlreadyExistException("User already exists.");
        }
        return developerDAO.saveOrUpdate(developer)
                           .orElse(developer);
    }

    public void delete(int developerId) {
        developerDAO.delete(developerId);
    }

    private boolean exists(Developer developer) {
        return developerDAO.findByKey(developer.getId())
                           .isPresent();
    }

}
