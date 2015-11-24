package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.Team;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

@Stateless
public class TeamDAO extends AbstractDAO<Team, edu.piotrjonski.scrumus.domain.Team> {

    @Inject
    private ProjectDAO projectDAO;

    public TeamDAO() {
        this(Team.class);
    }

    private TeamDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    public Team mapToDatabaseModel(final edu.piotrjonski.scrumus.domain.Team domainModel) {
        Team team = new Team();
        team.setName(domainModel.getName());
        team.setId(domainModel.getId());
        team.setProjects(projectDAO.mapToDatabaseModel(domainModel.getProjects()));
        return team;
    }

    @Override
    public edu.piotrjonski.scrumus.domain.Team mapToDomainModel(final Team dbModel) {
        edu.piotrjonski.scrumus.domain.Team team = new edu.piotrjonski.scrumus.domain.Team();
        team.setId(dbModel.getId());
        team.setName(dbModel.getName());
        team.setProjects(projectDAO.mapToDomainModel(dbModel.getProjects()));
        return team;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(Team.FIND_ALL, Team.class);
    }

    @Override
    protected Query getDeleteByIdQuery() {
        return entityManager.createNamedQuery(Team.DELETE_BY_ID);
    }

    @Override
    protected String getId() {
        return Team.ID;
    }
}
