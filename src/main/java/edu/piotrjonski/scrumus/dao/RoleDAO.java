package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.security.RoleEntity;
import edu.piotrjonski.scrumus.dao.model.security.RoleType;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Role;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Stateless
public class RoleDAO extends AbstractDAO<RoleEntity, Role> {

    @Inject
    private DeveloperDAO developerDAO;

    public RoleDAO() {
        this(RoleEntity.class);
    }

    private RoleDAO(final Class entityClass) {
        super(entityClass);
    }

    public Optional<Role> findRoleByRoleType(RoleType roleType) {
        try {
            RoleEntity roleEntity = entityManager.createNamedQuery(RoleEntity.FIND_BY_NAME, RoleEntity.class)
                                                 .setParameter(RoleEntity.NAME, roleType)
                                                 .getSingleResult();
            return Optional.of(mapToDomainModelIfNotNull(roleEntity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Role> findRoleByRoleTypeAndDeveloperId(RoleType roleType, int developerId) {
        try {
            RoleEntity roleEntity = entityManager.createNamedQuery(RoleEntity.FIND_BY_NAME, RoleEntity.class)
                                                 .setParameter(RoleEntity.NAME, roleType)
                                                 .getSingleResult();
            Role role = mapToDomainModelIfNotNull(roleEntity);
            if (isDeveloperInRole(developerId, role)) {
                return Optional.of(role);
            } else {
                return Optional.empty();
            }
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public boolean existByRoleType(final RoleType roleType) {
        return findRoleByRoleType(roleType).isPresent();
    }

    public boolean existByRoleTypeAndDeveloperId(final RoleType roleType, int developerId) {
        return findRoleByRoleTypeAndDeveloperId(roleType, developerId).isPresent();
    }

    @Override
    protected RoleEntity mapToDatabaseModel(final Role domainModel) {
        RoleEntity pictureEntity = new RoleEntity();
        pictureEntity.setId(domainModel.getId());
        pictureEntity.setRoleType(domainModel.getRoleType());
        pictureEntity.setDeveloperEntities(developerDAO.mapToDatabaseModel(domainModel.getDevelopers()));
        return pictureEntity;
    }

    @Override
    protected Role mapToDomainModel(final RoleEntity dbModel) {
        Role picture = new Role();
        picture.setId(dbModel.getId());
        picture.setRoleType(dbModel.getRoleType());
        picture.setDevelopers(developerDAO.mapToDomainModel(dbModel.getDeveloperEntities()));
        return picture;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(RoleEntity.FIND_ALL, RoleEntity.class);
    }

    private boolean isDeveloperInRole(final int developerId, final Role role) {
        List<Developer> developers = role.getDevelopers();
        Developer developerById = developerDAO.findById(developerId)
                                              .orElse(new Developer());
        if (developers.contains(developerById)) {
            return true;
        }
        return false;
    }

}
