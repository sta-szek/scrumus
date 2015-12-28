package edu.piotrjonski.scrumus.business;


import edu.piotrjonski.scrumus.dao.*;
import edu.piotrjonski.scrumus.dao.model.security.RoleType;
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
    private RoleDAO roleDAO;

    @Inject
    private ProductOwnerDAO productOwnerDAO;

    public boolean isAdmin(Developer user) {
        return adminDAO.findByDeveloperId(user.getId())
                       .isPresent() && hasRole(RoleType.ADMIN, user);
    }

    public boolean isProductOwner(Project project, Developer user) {
        if (developerExist(user) && projectExist(project)) {
            Optional<ProductOwner> productOwner = productOwnerDAO.findByDeveloperId(user.getId());
            if (productOwner.isPresent()) {
                return hasProjectPermission(project, productOwner) && hasRole(RoleType.PRODUCT_OWNER, user);
            }
        }
        return false;
    }

    public void grantAdminPermission(Developer user) {
        if (developerExist(user) && isNotAdmin(user)) {
            Role role = createRoleIfNotExist(RoleType.ADMIN);
            saveAdmin(user);
            grantRole(role, user);
        }
    }

    public void removeAdminPermission(Developer user) {
        if (isAdmin(user)) {
            deleteAdmin(user);
            removeRole(RoleType.ADMIN, user);
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

    private void removeRole(final RoleType roleType, final Developer user) {
        Role role = roleDAO.findRoleByRoleType(roleType)
                           .get();
        role.removeDeveloper(user);
        roleDAO.saveOrUpdate(role);
    }

    private void deleteAdmin(final Developer user) {
        int adminId = adminDAO.findByDeveloperId(user.getId())
                              .get()
                              .getId();
        adminDAO.delete(adminId);
    }

    private void grantRole(Role role, final Developer user) {
        role.addDeveloper(user);
        roleDAO.saveOrUpdate(role);
    }

    private void saveAdmin(final Developer user) {
        Admin admin = new Admin();
        admin.setDeveloper(user);
        adminDAO.saveOrUpdate(admin);
    }

    private Role createRoleIfNotExist(final RoleType roleType) {
        if (!roleDAO.existByRoleType(roleType)) {
            Role role = new Role();
            role.setRoleType(roleType);
            return roleDAO.saveOrUpdate(role)
                          .get();
        }
        return null;
    }

    private boolean hasRole(RoleType roleType, final Developer user) {return roleDAO.existByRoleTypeAndDeveloperId(roleType, user.getId());}

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
