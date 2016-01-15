package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.AlreadyExistException;
import edu.piotrjonski.scrumus.business.TeamManager;
import edu.piotrjonski.scrumus.domain.Team;
import edu.piotrjonski.scrumus.ui.configuration.I18NProvider;
import edu.piotrjonski.scrumus.ui.configuration.PathProvider;
import lombok.Data;
import org.slf4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Data
public class TeamService implements Serializable {

    @Inject
    private transient Logger logger;

    @Inject
    private TeamManager teamManager;

    @Inject
    private OccupiedChecker occupiedChecker;

    @Inject
    private I18NProvider i18NProvider;

    @Inject
    private PathProvider pathProvider;

    @Size(max = 30, message = "{validator.size.team.name}")
    private String teamName;

    private String teamToDelete;

    public Team findByTeamId(String teamId) {
        try {
            int tamIntId = Integer.parseInt(teamId);
            Optional<Team> teamOptional = teamManager.findTeam(tamIntId);
            teamOptional.ifPresent(project -> teamName = project.getName());
            return teamOptional.orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public List<Team> getAllTeams() {
        return teamManager.findAllTeams();
    }

    public void deleteTeam() {
        try {
            int teamId = Integer.parseInt(teamToDelete);
            teamManager.delete(teamId);
            logger.info("Team with id '" + teamId + "' was deleted.");
        } catch (NumberFormatException e) {

        }
    }

    public String editTeam() {
        Team teamFromField = createTeamFromField();
        int teamId = Integer.parseInt(FacesContext.getCurrentInstance()
                                                  .getExternalContext()
                                                  .getRequestParameterMap()
                                                  .get("teamId"));
        teamFromField.setId(teamId);
        try {
            teamManager.update(teamFromField);
            return pathProvider.getRedirectPath("admin.listTeams");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.edit.team", null);
            return null;
        }
    }

    public String createTeam() {
        if (validateTeamName()) {
            return null;
        }
        try {
            Team team = teamManager.create(createTeamFromField())
                                   .get();
            logger.info("Created team with id '" + team.getId() + "'.");
            return pathProvider.getRedirectPath("admin.listTeams");
        } catch (AlreadyExistException e) {
            createFacesMessage("system.fatal.create.team.exist", null);
            return null;
        }
    }

    private Team createTeamFromField() {
        Team team = new Team();
        team.setName(teamName);
        return team;
    }

    private boolean validateTeamName() {
        if (occupiedChecker.isTeamNameOccupied(teamName)) {
            createFacesMessage("page.validator.occupied.team.name", "createTeamForm:teamName");
            return true;
        }
        return false;
    }

    private void createFacesMessage(String property, String field) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                     i18NProvider.getMessage(property),
                                                     null);
        facesContext.addMessage(field, facesMessage);
    }
}
