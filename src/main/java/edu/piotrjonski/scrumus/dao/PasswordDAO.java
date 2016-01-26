package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.security.PasswordEntity;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Password;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class PasswordDAO extends AbstractDAO<PasswordEntity, Password> {

    @Inject
    private transient Logger logger;

    @Inject
    private DeveloperDAO developerDAO;

    public PasswordDAO() {
        this(PasswordEntity.class);
    }

    private PasswordDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    public List<Password> findAll() {
        return new ArrayList<>();
    }

    public void deleteUserPassword(int userId) {
        try {
            PasswordEntity password = entityManager.createNamedQuery(PasswordEntity.FIND_USER_PASSWORD, PasswordEntity.class)
                                                   .setParameter(DeveloperEntity.ID, userId)
                                                   .getSingleResult();
            entityManager.remove(password);
        } catch (NoResultException e) {
            logger.warn("User with id " + userId + " hadn't password in database.");
        }
    }

    public Optional<Password> findUserPassword(int userId) {
        try {
            PasswordEntity password = entityManager.createNamedQuery(PasswordEntity.FIND_USER_PASSWORD, PasswordEntity.class)
                                                   .setParameter(DeveloperEntity.ID, userId)
                                                   .getSingleResult();
            return Optional.of(mapToDomainModel(password));
        } catch (NoResultException e) {
            logger.warn("User with id " + userId + " hadn't password in database.");
            return Optional.empty();
        }
    }

    @Override
    protected PasswordEntity mapToDatabaseModel(final Password domainModel) {
        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setPassword(domainModel.getPassword());
        passwordEntity.setDeveloperEntity(findDeveloperEntity(domainModel.getDeveloperId()));
        passwordEntity.setId(domainModel.getId());
        return passwordEntity;
    }

    @Override
    protected Password mapToDomainModel(final PasswordEntity dbModel) {
        Password password = new Password();
        password.setDeveloperId(getDeveloperId(dbModel));
        password.setId(dbModel.getId());
        password.setPassword(dbModel.getPassword());
        return password;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(PasswordEntity.FIND_ALL, PasswordEntity.class);
    }

    private DeveloperEntity findDeveloperEntity(final int id) {
        if (id != 0) {
            Optional<Developer> developer = developerDAO.findById(id);
            if (developer.isPresent()) {
                return developerDAO.mapToDatabaseModelIfNotNull(developer.get());
            }
        }
        return null;
    }

    private int getDeveloperId(final PasswordEntity dbModel) {
        DeveloperEntity reporter = dbModel.getDeveloperEntity();
        return reporter != null
               ? reporter.getId()
               : 0;
    }
}
