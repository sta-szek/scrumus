package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.user.AdminEntity;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Admin;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Optional;

@Stateless
public class AdminDAO extends AbstractDAO<AdminEntity, Admin> {

    @Inject
    private DeveloperDAO developerDAO;

    public AdminDAO() {
        this(AdminEntity.class);
    }

    private AdminDAO(final Class entityClass) {
        super(entityClass);
    }

    public Optional<Admin> findByDeveloperId(int developerId) {
        try {
            AdminEntity adminEntity = entityManager.createNamedQuery(AdminEntity.FIND_BY_DEVELOPER_ID, AdminEntity.class)
                                                   .setParameter(DeveloperEntity.ID, developerId)
                                                   .getSingleResult();
            return Optional.of(mapToDomainModelIfNotNull(adminEntity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    protected AdminEntity mapToDatabaseModel(final Admin domainModel) {
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setDeveloperEntity(developerDAO.mapToDatabaseModelIfNotNull(domainModel.getDeveloper()));
        adminEntity.setId(domainModel.getId());
        return adminEntity;
    }

    @Override
    protected Admin mapToDomainModel(final AdminEntity dbModel) {
        Admin admin = new Admin();
        admin.setDeveloper(developerDAO.mapToDomainModelIfNotNull(dbModel.getDeveloperEntity()));
        admin.setId(dbModel.getId());
        return admin;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(AdminEntity.FIND_ALL, AdminEntity.class);
    }

}
