package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.TeamEntity;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

@Stateless
public class TeamDAO extends AbstractDAO<TeamEntity, edu.piotrjonski.scrumus.domain.Team> {

    @Inject
    private ProjectDAO projectDAO;

    public TeamDAO() {
        this(TeamEntity.class);
    }

    private TeamDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    public TeamEntity mapToDatabaseModel(final edu.piotrjonski.scrumus.domain.Team domainModel) {
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setName(domainModel.getName());
        teamEntity.setId(domainModel.getId());
        teamEntity.setProjects(projectDAO.mapToDatabaseModel(domainModel.getProjects()));
        return teamEntity;
    }

    @Override
    public edu.piotrjonski.scrumus.domain.Team mapToDomainModel(final TeamEntity dbModel) {
        edu.piotrjonski.scrumus.domain.Team team = new edu.piotrjonski.scrumus.domain.Team();
        team.setId(dbModel.getId());
        team.setName(dbModel.getName());
        team.setProjects(projectDAO.mapToDomainModel(dbModel.getProjects()));
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
