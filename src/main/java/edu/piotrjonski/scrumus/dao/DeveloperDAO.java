package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Developer;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Stateless
public class DeveloperDAO extends AbstractDAO<DeveloperEntity, Developer> {

    public DeveloperDAO() {
        this(DeveloperEntity.class);
    }

    private DeveloperDAO(final Class entityClass) {
        super(entityClass);
    }

    public boolean emailExist(String email) {
        try {
            entityManager.createNamedQuery(DeveloperEntity.FIND_BY_MAIL, DeveloperEntity.class)
                         .setParameter(DeveloperEntity.EMAIL, email)
                         .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
        //TODO TESTY
    }

    @Override
    protected DeveloperEntity mapToDatabaseModel(final Developer domainModel) {
        DeveloperEntity dbDeveloperEntity = new DeveloperEntity();
        dbDeveloperEntity.setId(domainModel.getId());
        dbDeveloperEntity.setFirstName(domainModel.getFirstName());
        dbDeveloperEntity.setSurname(domainModel.getSurname());
        dbDeveloperEntity.setUsername(domainModel.getUsername());
        dbDeveloperEntity.setEmail(domainModel.getEmail());
        return dbDeveloperEntity;
    }

    @Override
    protected Developer mapToDomainModel(final DeveloperEntity dbModel) {
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

}
