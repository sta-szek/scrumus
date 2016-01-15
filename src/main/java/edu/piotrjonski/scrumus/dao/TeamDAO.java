package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.dao.model.user.TeamEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Team;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class TeamDAO extends AbstractDAO<TeamEntity, Team> {

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private DeveloperDAO developerDAO;

    public TeamDAO() {
        this(TeamEntity.class);
    }

    private TeamDAO(final Class entityClass) {
        super(entityClass);
    }

    public List<Team> findAllDeveloperTeams(Developer developer) {
        Optional<Developer> savedDeveloper = developerDAO.findById(developer.getId());
        if (savedDeveloper.isPresent()) {
            DeveloperEntity developerEntity = developerDAO.mapToDatabaseModelIfNotNull(savedDeveloper.get());
            return mapToDomainModel(entityManager.createNamedQuery(TeamEntity.FIND_ALL_DEVELOPER_TEAMS)
                                                 .setParameter(TeamEntity.DEVELOPER, developerEntity)
                                                 .getResultList());
        }
        return new ArrayList<>();
    }

    public List<String> findAllNames() {
        return entityManager.createNamedQuery(TeamEntity.FIND_ALL_NAMES)
                            .getResultList();
    }

    @Override
    protected TeamEntity mapToDatabaseModel(final Team domainModel) {
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setName(domainModel.getName());
        teamEntity.setId(domainModel.getId());
        teamEntity.setDeveloperEntities(developerDAO.mapToDatabaseModel(domainModel.getDevelopers()));
        teamEntity.setProjectEntities(projectDAO.mapToDatabaseModel(domainModel.getProjects()));
        return teamEntity;
    }

    @Override
    protected Team mapToDomainModel(final TeamEntity dbModel) {
        Team team = new Team();
        team.setId(dbModel.getId());
        team.setName(dbModel.getName());
        team.setDevelopers(developerDAO.mapToDomainModel(dbModel.getDeveloperEntities()));
        team.setProjects(projectDAO.mapToDomainModel(dbModel.getProjectEntities()));
        return team;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(TeamEntity.FIND_ALL, TeamEntity.class);
    }

}
