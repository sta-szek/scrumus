package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.security.RoleEntity;
import edu.piotrjonski.scrumus.domain.Role;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class RoleDAO extends AbstractDAO<RoleEntity, Role> {

    private DeveloperDAO developerDAO = new DeveloperDAO();

    public RoleDAO() {
        this(RoleEntity.class);
    }

    private RoleDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    protected RoleEntity mapToDatabaseModel(final Role domainModel) {
        RoleEntity pictureEntity = new RoleEntity();
        pictureEntity.setId(domainModel.getId());
        pictureEntity.setName(domainModel.getName());
        pictureEntity.setDeveloperEntities(developerDAO.mapToDatabaseModel(domainModel.getDevelopers()));
        return pictureEntity;
    }

    @Override
    protected Role mapToDomainModel(final RoleEntity dbModel) {
        Role picture = new Role();
        picture.setId(dbModel.getId());
        picture.setName(dbModel.getName());
        picture.setDevelopers(developerDAO.mapToDomainModel(dbModel.getDeveloperEntities()));
        return picture;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(RoleEntity.FIND_ALL, RoleEntity.class);
    }

}
