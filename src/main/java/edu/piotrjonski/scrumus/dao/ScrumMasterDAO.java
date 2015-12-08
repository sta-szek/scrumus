package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.user.ProductOwnerEntity;
import edu.piotrjonski.scrumus.dao.model.user.ScrumMasterEntity;
import edu.piotrjonski.scrumus.domain.ScrumMaster;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

@Stateless
public class ScrumMasterDAO extends AbstractDAO<ScrumMasterEntity, ScrumMaster> {

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private TeamDAO teamDAO;

    public ScrumMasterDAO() {
        this(ProductOwnerEntity.class);
    }

    private ScrumMasterDAO(final Class entityClass) {
        super(entityClass);
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