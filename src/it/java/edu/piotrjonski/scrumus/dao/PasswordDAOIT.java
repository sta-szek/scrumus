package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.PasswordEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Password;
import edu.piotrjonski.scrumus.utils.UtilsTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class PasswordDAOIT {

    public static final int ID = 1;
    public static int nextUniqueValue = 0;

    @Inject
    private PasswordDAO passwordDAO;

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;
    private int currentlySavedDeveloperId;

    @Deployment
    public static WebArchive createDeployment() {
        return UtilsTest.createDeployment();
    }

    @Before
    public void dropAllUsersAndStartTransaction() throws Exception {
        clearData();
        startTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        userTransaction.commit();
    }

    @Test
    public void shouldSave() {
        // given
        Password password = createPassword();

        // when
        passwordDAO.saveOrUpdate(password);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldUpdate() {
        // given
        String passwordString = "updatedPassword";
        Password password = createPassword();
        password = passwordDAO.mapToDomainModelIfNotNull(entityManager.merge(passwordDAO.mapToDatabaseModelIfNotNull(
                password)));
        password.setPassword(passwordString);
        password.setDeveloperId(currentlySavedDeveloperId);

        // when
        password = passwordDAO.saveOrUpdate(password)
                              .get();

        // then
        assertThat(password.getPassword()).isEqualTo(passwordString);
    }

    @Test
    public void shouldDelete() {
        // given
        Password password = createPassword();
        password = passwordDAO.mapToDomainModelIfNotNull(entityManager.merge(passwordDAO.mapToDatabaseModelIfNotNull(
                password)));

        // when
        passwordDAO.delete(password.getId());
        int allPasswords = findAll().size();

        // then
        assertThat(allPasswords).isEqualTo(0);
    }

    @Test
    public void shouldThrowExceptionWhenTryToGetAllPasswords() {
        // given
        Password password = createPassword();
        passwordDAO.saveOrUpdate(password);

        // when
        List<Password> result = passwordDAO.findAll();

        // then
        assertThat(result).hasSize(0);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Password password1 = createPassword();
        int id = entityManager.merge(passwordDAO.mapToDatabaseModelIfNotNull(password1))
                              .getId();
        password1.setId(id);

        // when
        Password password = passwordDAO.findByKey(id)
                                       .get();

        // then
        assertThat(password).isEqualTo(password1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Password> user = passwordDAO.findByKey(0);

        // then
        assertThat(user).isEmpty();
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
        Developer developer = new Developer();
        developer.setEmail("Email" + nextUniqueValue);
        developer.setFirstName("firstname" + nextUniqueValue);
        developer.setSurname("surname" + nextUniqueValue);
        developer.setUsername("username" + nextUniqueValue);
        currentlySavedDeveloperId = developerDAO.saveOrUpdate(developer)
                                                .get()
                                                .getId();
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM PasswordEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM DeveloperEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Password createPassword() {
        Password password = new Password();
        password.setId(ID + nextUniqueValue);
        password.setDeveloperId(currentlySavedDeveloperId);
        password.setPassword("initialPassword");
        nextUniqueValue++;
        return password;
    }

    private List<PasswordEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM PasswordEntity d")
                            .getResultList();
    }
}