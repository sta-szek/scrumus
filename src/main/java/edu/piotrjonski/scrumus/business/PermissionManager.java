package edu.piotrjonski.scrumus.business;


import edu.piotrjonski.scrumus.dao.*;
import edu.piotrjonski.scrumus.dao.model.security.RoleType;
import edu.piotrjonski.scrumus.domain.*;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

@Stateless
public class PermissionManager {

    @Inject
    private transient Logger logger;
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
    private ScrumMasterDAO scrumMasterDAO;
    @Inject
    private ProductOwnerDAO productOwnerDAO;

    public void divestProductOwner(String projectKey) {
        productOwnerDAO.findByProjectKey(projectKey)
                       .ifPresent(productOwner -> {
                           removeProjectFromProductOwner(productOwner);
                           removeRole(RoleType.PRODUCT_OWNER, productOwner.getDeveloper());
                       });
    }

    public void setProductOwner(String projectKey, String usrname) throws NotExistException, IllegalOperationException {
        Optional<Developer> userOptional = developerDAO.findByUsername(usrname);
        if (!userOptional.isPresent()) {
            throw new NotExistException("User with username '" + usrname + "' does not exist.");
        } else if (!projectExist(projectKey)) {
            throw new NotExistException("Project with key '" + projectKey + "' does not exist.");
        } else if (hasRole(RoleType.PRODUCT_OWNER, userOptional.get())) {
            throw new IllegalOperationException("User is already product owner.");
        }
        Developer user = userOptional.get();
        Project project = projectDAO.findById(projectKey)
                                    .get();
        Role role = createRoleIfNotExist(RoleType.PRODUCT_OWNER);
        createProductOwner(userOptional.get(), project);
        grantRole(role, user);

    }

    public boolean isAdmin(Developer user) {
        return adminDAO.findByDeveloperId(user.getId())
                       .isPresent() && hasRole(RoleType.ADMIN, user);
    }

    public boolean isProductOwner(Project project, Developer user) {
        if (developerExist(user) && projectExist(project.getKey())) {
            Optional<ProductOwner> productOwner = productOwnerDAO.findByDeveloperId(user.getId());
            if (productOwner.isPresent()) {
                return hasProjectPermission(project, productOwner) && hasRole(RoleType.PRODUCT_OWNER, user);
            }
        }
        return false;
    }

    public void grantAdminPermission(Developer user) {
        if (isNotAdmin(user)) {
            Role role = createRoleIfNotExist(RoleType.ADMIN);
            saveAdmin(user);
            grantRole(role, user);
            logger.info("User " + user.getUsername() + " (" + user.getFirstName() + " " + user.getSurname() +
                        ") was granted admin permission.");
        }
    }

    public void removeAdminPermission(Developer user) {
        if (isAdmin(user)) {
            deleteAdmin(user);
            removeRole(RoleType.ADMIN, user);
            logger.info("User " + user.getUsername() + " (" + user.getFirstName() + " " + user.getSurname() + ") lost admin permission.");
        }
    }

    public void addTeamToProject(Team team, Project project) {
        if (teamExist(team) && projectExist(project.getKey())) {
            team.addProject(project);
            teamDAO.saveOrUpdate(team);
        }
    }

    public void removeTeamFromProject(Team team, Project project) {
        if (teamExist(team) && projectExist(project.getKey())) {
            team.removeProject(project);
            teamDAO.saveOrUpdate(team);
        }
    }

    public void removeAllRolesFromUser(Developer user) {
        removeRole(RoleType.ADMIN, user);
        removeRole(RoleType.DEVELOPER, user);
        removeRole(RoleType.PRODUCT_OWNER, user);
        removeRole(RoleType.SCRUM_MASTER, user);
        deleteAdmin(user);
        deleteProductOwner(user);
        deleteScrumMaster(user);
    }

    private void createProductOwner(final Developer user, final Project project) {
        ProductOwner productOwner = new ProductOwner();
        productOwner.setDeveloper(user);
        productOwner.setProject(project);
        productOwnerDAO.saveOrUpdate(productOwner);
    }

    private void removeProjectFromProductOwner(ProductOwner productOwner) {
        removeRole(RoleType.PRODUCT_OWNER, productOwner.getDeveloper());
        deleteProductOwner(productOwner.getDeveloper());
    }

    private void removeRole(final RoleType roleType, final Developer user) {
        roleDAO.findRoleByRoleType(roleType)
               .ifPresent(x -> {
                   x.removeDeveloper(user);
                   roleDAO.saveOrUpdate(x);
               });

    }

    private void deleteAdmin(final Developer user) {
        adminDAO.findByDeveloperId(user.getId())
                .map(Admin::getId)
                .ifPresent(adminDAO::delete);
    }

    private void deleteProductOwner(final Developer user) {
        productOwnerDAO.findByDeveloperId(user.getId())
                       .map(ProductOwner::getId)
                       .ifPresent(productOwnerDAO::delete);
    }

    private void deleteScrumMaster(final Developer user) {
        scrumMasterDAO.findByDeveloperId(user.getId())
                      .map(ScrumMaster::getId)
                      .ifPresent(scrumMasterDAO::delete);
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
        return roleDAO.findRoleByRoleType(roleType)
                      .get();
    }

    private boolean hasRole(RoleType roleType, final Developer user) {return roleDAO.existByRoleTypeAndDeveloperId(roleType, user.getId());}

    private boolean hasProjectPermission(final Project project, final Optional<ProductOwner> productOwner) {
        return productOwner.get()
                           .getProject()
                           .getKey()
                           .equals(project.getKey());
    }

    private boolean isNotAdmin(final Developer user) {return developerExist(user) && !isAdmin(user);}

    private boolean developerExist(final Developer user) {return developerDAO.exist(user.getId());}

    private boolean teamExist(final Team team) {return teamDAO.exist(team.getId());}

    private boolean projectExist(final String projectKey) {return projectDAO.exist(projectKey);}
}
