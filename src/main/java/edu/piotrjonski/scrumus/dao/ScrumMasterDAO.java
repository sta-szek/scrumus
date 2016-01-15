package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.dao.model.user.ScrumMasterEntity;
import edu.piotrjonski.scrumus.dao.model.user.TeamEntity;
import edu.piotrjonski.scrumus.domain.ScrumMaster;
import edu.piotrjonski.scrumus.domain.Team;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Optional;

@Stateless
public class ScrumMasterDAO extends AbstractDAO<ScrumMasterEntity, ScrumMaster> {

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private TeamDAO teamDAO;

    public ScrumMasterDAO() {
        this(ScrumMasterEntity.class);
    }

    private ScrumMasterDAO(final Class entityClass) {
        super(entityClass);
    }

    public Optional<ScrumMaster> findByDeveloperId(int developerId) {
        try {
            ScrumMasterEntity scrumMasterEntity = entityManager.createNamedQuery(ScrumMasterEntity.FIND_BY_DEVELOPER_ID,
                                                                                 ScrumMasterEntity.class)
                                                               .setParameter(DeveloperEntity.ID, developerId)
                                                               .getSingleResult();
            return Optional.of(mapToDomainModelIfNotNull(scrumMasterEntity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<ScrumMaster> findByDeveloperTeam(Team team) {
        try {
            TeamEntity teamEntity = entityManager.find(TeamEntity.class, team.getId());
            ScrumMasterEntity scrumMasterEntity = entityManager.createNamedQuery(ScrumMasterEntity.FIND_BY_TEAM,
                                                                                 ScrumMasterEntity.class)
                                                               .setParameter(ScrumMasterEntity.TEAM, teamEntity)
                                                               .getSingleResult();
            return Optional.of(mapToDomainModelIfNotNull(scrumMasterEntity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    protected ScrumMasterEntity mapToDatabaseModel(final ScrumMaster domainModel) {
        ScrumMasterEntity scrumMasterEntity = new ScrumMasterEntity();
        scrumMasterEntity.setDeveloperEntity(developerDAO.mapToDatabaseModelIfNotNull(domainModel.getDeveloper()));
        scrumMasterEntity.setId(domainModel.getId());
        scrumMasterEntity.setTeamEntities(teamDAO.mapToDatabaseModel(domainModel.getTeams()));
        return scrumMasterEntity;
    }

    @Override
    protected ScrumMaster mapToDomainModel(final ScrumMasterEntity dbModel) {
        ScrumMaster scrumMaster = new ScrumMaster();
        scrumMaster.setDeveloper(developerDAO.mapToDomainModelIfNotNull(dbModel.getDeveloperEntity()));
        scrumMaster.setId(dbModel.getId());
        scrumMaster.setTeams(teamDAO.mapToDomainModel(dbModel.getTeamEntities()));
        return scrumMaster;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(ScrumMasterEntity.FIND_ALL, ScrumMasterEntity.class);
    }

}
