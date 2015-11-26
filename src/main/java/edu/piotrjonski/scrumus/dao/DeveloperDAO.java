package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class DeveloperDAO extends AbstractDAO<DeveloperEntity, edu.piotrjonski.scrumus.domain.Developer> {


    public DeveloperDAO() {
        this(DeveloperEntity.class);
    }

    private DeveloperDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    public DeveloperEntity mapToDatabaseModel(final edu.piotrjonski.scrumus.domain.Developer domainModel) {
        DeveloperEntity dbDeveloperEntity = new DeveloperEntity();
        dbDeveloperEntity.setId(domainModel.getId());
        dbDeveloperEntity.setFirstName(domainModel.getFirstName());
        dbDeveloperEntity.setSurname(domainModel.getSurname());
        dbDeveloperEntity.setUsername(domainModel.getUsername());
        dbDeveloperEntity.setEmail(domainModel.getEmail());
        return dbDeveloperEntity;
    }

    @Override
    public edu.piotrjonski.scrumus.domain.Developer mapToDomainModel(final DeveloperEntity dbModel) {
        edu.piotrjonski.scrumus.domain.Developer domainDeveloper = new edu.piotrjonski.scrumus.domain.Developer();
        domainDeveloper.setId(dbModel.getId());
        domainDeveloper.setFirstName(dbModel.getFirstName());
        domainDeveloper.setSurname(dbModel.getSurname());
        domainDeveloper.setUsername(dbModel.getUsername());
        domainDeveloper.setEmail(dbModel.getEmail());
        return domainDeveloper;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(DeveloperEntity.FIND_ALL, DeveloperEntity.class);
    }

    @Override
    protected Query getDeleteByIdQuery() {
        return entityManager.createNamedQuery(DeveloperEntity.DELETE_BY_ID);
    }

    @Override
    protected String getId() {
        return DeveloperEntity.ID;
    }
}
