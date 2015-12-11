package edu.piotrjonski.scrumus.business;


import edu.piotrjonski.scrumus.dao.AdminDAO;
import edu.piotrjonski.scrumus.dao.DeveloperDAO;
import edu.piotrjonski.scrumus.domain.Admin;
import edu.piotrjonski.scrumus.domain.Developer;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class PermissionManager {

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private AdminDAO adminDAO;

    public boolean isAdmin(Developer user) {
        return adminDAO.findByDeveloperId(user.getId())
                       .isPresent();
    }

    public void grantAdminPermission(Developer user) {
        if (developerDAO.exist(user.getId())) {
            Admin admin = new Admin();
            admin.setDeveloper(user);
            adminDAO.saveOrUpdate(admin);
        }
    }

    public void removeAdminPermission(Developer user) {
        if (isAdmin(user)) {
            int adminId = adminDAO.findByDeveloperId(user.getId())
                                  .get()
                                  .getId();
            adminDAO.delete(adminId);
        }
    }
}
