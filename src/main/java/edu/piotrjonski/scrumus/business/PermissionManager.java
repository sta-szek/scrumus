package edu.piotrjonski.scrumus.business;


import edu.piotrjonski.scrumus.dao.AdminDAO;
import edu.piotrjonski.scrumus.dao.DeveloperDAO;
import edu.piotrjonski.scrumus.dao.ProjectDAO;
import edu.piotrjonski.scrumus.dao.TeamDAO;
import edu.piotrjonski.scrumus.domain.Admin;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Project;
import edu.piotrjonski.scrumus.domain.Team;

import javax.ejb.Stateless;
import javax.inject.Inject;

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

    public boolean isAdmin(Developer user) {
        return adminDAO.findByDeveloperId(user.getId())
                       .isPresent();
    }

    public void grantAdminPermission(Developer user) {
        if (exist(user) && isNotAdmin(user)) {
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
        if (exist(team) && exist(project)) {
            team.addProject(project);
            teamDAO.saveOrUpdate(team);
        }
    }

    public void removeTeamFromProject(Team team, Project project) {
        if (exist(team) && exist(project)) {
            team.removeProject(project);
            teamDAO.saveOrUpdate(team);
        }
    }

    private boolean isNotAdmin(final Developer user) {return !isAdmin(user);}

    private boolean exist(final Developer user) {return developerDAO.exist(user.getId());}

    private boolean exist(final Team team) {return teamDAO.exist(team.getId());}

    private boolean exist(final Project project) {return projectDAO.exist(project.getKey());}
}
