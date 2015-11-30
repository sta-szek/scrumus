package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.TeamEntity;
import edu.piotrjonski.scrumus.domain.Team;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

@Stateless
public class TeamDAO extends AbstractDAO<TeamEntity, Team> {

    @Inject
    private ProjectDAO projectDAO;

    public TeamDAO() {
        this(TeamEntity.class);
    }

    private TeamDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    protected TeamEntity mapToDatabaseModel(final edu.piotrjonski.scrumus.domain.Team domainModel) {
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setName(domainModel.getName());
        teamEntity.setId(domainModel.getId());
        teamEntity.setProjectEntities(projectDAO.mapToDatabaseModel(domainModel.getProjects()));
        return teamEntity;
    }

    @Override
    protected edu.piotrjonski.scrumus.domain.Team mapToDomainModel(final TeamEntity dbModel) {
        edu.piotrjonski.scrumus.domain.Team team = new edu.piotrjonski.scrumus.domain.Team();
        team.setId(dbModel.getId());
        team.setName(dbModel.getName());
        team.setProjects(projectDAO.mapToDomainModel(dbModel.getProjectEntities()));
        return team;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(TeamEntity.FIND_ALL, TeamEntity.class);
    }

    @Override
    protected Query getDeleteByIdQuery() {
        return entityManager.createNamedQuery(TeamEntity.DELETE_BY_ID);
    }

    @Override
    protected String getId() {
        return TeamEntity.ID;
    }
}
