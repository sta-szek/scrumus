package edu.piotrjonski.scrumus.business;


import edu.piotrjonski.scrumus.dao.*;
import edu.piotrjonski.scrumus.domain.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

@Stateless
public class PermissionManager {

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private TeamDAO teamDAO;

    @Inject
    private AdminDAO adminDAO;

    @Inject
    private ProductOwnerDAO productOwnerDAO;

    public boolean isAdmin(Developer user) {
        return adminDAO.findByDeveloperId(user.getId())
                       .isPresent();
    }

    public boolean isProductOwner(Project project, Developer user) {
        if (developerExist(user) && projectExist(project)) {
            Optional<ProductOwner> productOwner = productOwnerDAO.findByDeveloperId(user.getId());
            if (productOwner.isPresent()) {
                return hasProjectPermission(project, productOwner);
            }
        }
        return false;
    }

    public void grantAdminPermission(Developer user) {
        if (developerExist(user) && isNotAdmin(user)) {
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

    public void addTeamToProject(Team team, Project project) {
        if (teamExist(team) && projectExist(project)) {
            team.addProject(project);
            teamDAO.saveOrUpdate(team);
        }
    }

    public void removeTeamFromProject(Team team, Project project) {
        if (teamExist(team) && projectExist(project)) {
            team.removeProject(project);
            teamDAO.saveOrUpdate(team);
        }
    }

    private boolean hasProjectPermission(final Project project, final Optional<ProductOwner> productOwner) {
        return productOwner.get()
                           .getProject()
                           .getKey()
                           .equals(project.getKey());
    }

    private boolean isNotAdmin(final Developer user) {return !isAdmin(user);}

    private boolean developerExist(final Developer user) {return developerDAO.exist(user.getId());}

    private boolean teamExist(final Team team) {return teamDAO.exist(team.getId());}

    private boolean projectExist(final Project project) {return projectDAO.exist(project.getKey());}
}
