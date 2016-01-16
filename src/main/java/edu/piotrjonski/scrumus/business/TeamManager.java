package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.DeveloperDAO;
import edu.piotrjonski.scrumus.dao.TeamDAO;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Team;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class TeamManager {

    @Inject
    private transient Logger logger;

    @Inject
    private TeamDAO teamDAO;

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private PermissionManager permissionManager;

    public Optional<Team> create(Team team) throws AlreadyExistException {
        if (teamExist(team)) {
            throw new AlreadyExistException("Team already exist.");
        }
        return teamDAO.saveOrUpdate(team);
    }

    public void delete(int teamId) {
        Optional<Team> teamOptional = teamDAO.findById(teamId);
        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            permissionManager.removeScrumMasterFromTeam(team.getId());
            teamDAO.delete(teamId);
        }
    }

    public Optional<Team> findTeam(int teamId) {
        return teamDAO.findById(teamId);
    }

    public void addUserToTeam(Developer developer, Team team) {
        if (teamExist(team) && developerExist(developer)) {
            team.addDeveloper(developer);
            teamDAO.saveOrUpdate(team);
        }
    }

    public void removeUserFromAllTeams(Developer developer) {
        teamDAO.findAllDeveloperTeams(developer)
               .forEach(team -> {
                   team.removeDeveloper(developer);
                   teamDAO.saveOrUpdate(team);
               });
    }

    public void removeUserFromTeam(Developer developer, Team team) {
        if (teamExist(team) && developerExist(developer)) {
            team.removeDeveloper(developer);
            teamDAO.saveOrUpdate(team);
        }
    }

    public List<String> findAllTeamNames() {return teamDAO.findAllNames();}

    public List<Team> findAllTeams() {
        return teamDAO.findAll();
    }

    public void update(final Team team) throws NotExistException {
        if (!teamExist(team)) {
            throw new NotExistException("Team zadania nie istnieje.");
        }
        teamDAO.saveOrUpdate(team);
    }

    private boolean teamExist(Team team) {
        return teamDAO.exist(team.getId());
    }

    private boolean developerExist(Developer developer) {
        return developerDAO.exist(developer.getId());
    }
}
