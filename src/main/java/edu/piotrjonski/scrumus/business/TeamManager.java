package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.DeveloperDAO;
import edu.piotrjonski.scrumus.dao.TeamDAO;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Team;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class TeamManager {

    @Inject
    private TeamDAO teamDAO;

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private PermissionManager permissionManager;

    public Team create(Team team) throws AlreadyExistException {
        if (teamExist(team)) {
            throw new AlreadyExistException("Team already exist.");
        }
        return teamDAO.saveOrUpdate(team)
                      .orElse(team);
    }

    public void delete(int userId) {
        teamDAO.delete(userId);
    }

    public void addUserToTeam(Developer developer, Team team) {
        if (teamExist(team) && developerExist(developer)) {
            team.addDeveloper(developer);
            teamDAO.saveOrUpdate(team);
        }
    }

    public void removeUserFromTeam(Developer developer, Team team) {
        if (teamExist(team) && developerExist(developer)) {
            team.removeDeveloper(developer);
            teamDAO.saveOrUpdate(team);
        }
    }

    private boolean teamExist(Team team) {
        return teamDAO.exist(team.getId());
    }

    private boolean developerExist(Developer developer) {
        return developerDAO.exist(developer.getId());
    }

}
