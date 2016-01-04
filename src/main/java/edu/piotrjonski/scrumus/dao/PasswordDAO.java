package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.security.PasswordEntity;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Password;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class PasswordDAO extends AbstractDAO<PasswordEntity, Password> {

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
        // zwraca pustą listę, bo kontener + arquillian + assertj nie potrawią sobie poradzić z wyjątkiem OperationNotSupporterException
        // który przeistacza się w EJBTransactionRolledBackException. Następnie Rzucany jest kolejny wyjątek, który powoduje fail testu.
        return new ArrayList<>();
    }

    public void deleteUserPassword(int userId) {
        PasswordEntity password = entityManager.createNamedQuery(PasswordEntity.FIND_USER_PASSWORD, PasswordEntity.class)
                                               .setParameter(DeveloperEntity.ID, userId)
                                               .getSingleResult();
        entityManager.remove(password);
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
