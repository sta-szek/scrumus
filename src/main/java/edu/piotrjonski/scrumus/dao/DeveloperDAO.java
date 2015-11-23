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
    public Developer mapToDatabaseModel(final edu.piotrjonski.scrumus.domain.Developer developer) {
        if (developer == null) {
            return null;
        }
        Developer dbDeveloper = new Developer();
        dbDeveloper.setId(developer.getId());
        dbDeveloper.setFirstName(developer.getFirstName());
        dbDeveloper.setSurname(developer.getSurname());
        dbDeveloper.setUsername(developer.getUsername());
        dbDeveloper.setEmail(developer.getEmail());
        return dbDeveloper;
    }

    @Override
    public edu.piotrjonski.scrumus.domain.Developer mapToDomainModel(final Developer developer) {
        if (developer == null) {
            return null;
        }
        edu.piotrjonski.scrumus.domain.Developer domainDeveloper = new edu.piotrjonski.scrumus.domain.Developer();
        domainDeveloper.setId(developer.getId());
        domainDeveloper.setFirstName(developer.getFirstName());
        domainDeveloper.setSurname(developer.getSurname());
        domainDeveloper.setUsername(developer.getUsername());
        domainDeveloper.setEmail(developer.getEmail());
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
