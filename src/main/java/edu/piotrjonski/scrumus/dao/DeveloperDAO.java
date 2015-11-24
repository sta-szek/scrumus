package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.user.Developer;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class DeveloperDAO extends AbstractDAO<Developer, edu.piotrjonski.scrumus.domain.Developer> {


    public DeveloperDAO() {
        this(Developer.class);
    }

    private DeveloperDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    public Developer mapToDatabaseModel(final edu.piotrjonski.scrumus.domain.Developer domainModel) {
        Developer dbDeveloper = new Developer();
        dbDeveloper.setId(domainModel.getId());
        dbDeveloper.setFirstName(domainModel.getFirstName());
        dbDeveloper.setSurname(domainModel.getSurname());
        dbDeveloper.setUsername(domainModel.getUsername());
        dbDeveloper.setEmail(domainModel.getEmail());
        return dbDeveloper;
    }

    @Override
    public edu.piotrjonski.scrumus.domain.Developer mapToDomainModel(final Developer dbModel) {
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
        return entityManager.createNamedQuery(Developer.FIND_ALL, Developer.class);
    }

    @Override
    protected Query getDeleteByIdQuery() {
        return entityManager.createNamedQuery(Developer.DELETE_BY_ID);
    }

    @Override
    protected String getId() {
        return Developer.ID;
    }
}
