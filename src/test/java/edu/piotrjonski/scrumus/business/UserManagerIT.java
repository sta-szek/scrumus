package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.DeveloperDAO;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.utils.TestUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(Arquillian.class)
public class UserManagerIT {
    public static final String EMAIL = "yoyo@wp.eu";
    public static final String USERNAME = "jako";
    public static final String SURNAME = "Kowalski";
    public static final String FIRSTNAME = "Jan";
    public static int nextUniqueValue = 0;

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private UserManager userManager;

    @Inject
    private PermissionManager permissionManager;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return TestUtils.createDeployment();
    }

    @Before
    public void dropAllDevelopersAndStartTransaction() throws Exception {
        clearData();
        startTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        userTransaction.commit();
    }

    @Test
    public void shouldCreateDeveloper()
            throws AlreadyExistException, UnsupportedEncodingException, NoSuchAlgorithmException, MessagingException, CreateUserException {
        // given
        Developer developer = createDeveloper();

        // when
        Developer savedDeveloper = userManager.create(developer)
                                              .get();
        boolean result = developerDAO.exist(savedDeveloper.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldThrowExceptionWhenUserAlreadyExist()
            throws AlreadyExistException, UnsupportedEncodingException, NoSuchAlgorithmException, CreateUserException {
        // given
        Developer developer = createDeveloper();
        Developer savedDeveloper = userManager.create(developer)
                                              .get();

        // when
        Throwable throwable = catchThrowable(() -> userManager.create(savedDeveloper));

        // then
        assertThat(throwable).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void shouldDeleteUser() throws AlreadyExistException, UnsupportedEncodingException, NoSuchAlgorithmException,
                                          CreateUserException {
        // given
        Developer developer = createDeveloper();
        developer = userManager.create(developer)
                               .get();

        // when
        userManager.delete(developer);
        int result = findAllUsersSize();

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void shouldUpdateUserIfExist()
            throws AlreadyExistException, NotExistException, UnsupportedEncodingException, NoSuchAlgorithmException, CreateUserException {
        // given
        Developer developer = createDeveloper();
        Developer savedDeveloper = userManager.create(developer)
                                              .get();

        String newEmail = "newEmail";
        String newFirstname = "newFirstname";
        String newSurname = "newSurname";
        savedDeveloper.setEmail(newEmail);
        savedDeveloper.setFirstName(newFirstname);
        savedDeveloper.setSurname(newSurname);

        // when
        Developer updatedDeveloper = userManager.update(savedDeveloper)
                                                .get();

        // then
        assertThat(updatedDeveloper.getEmail()).isEqualTo(newEmail);
        assertThat(updatedDeveloper.getFirstName()).isEqualTo(newFirstname);
        assertThat(updatedDeveloper.getSurname()).isEqualTo(newSurname);
    }

    @Test
    public void shouldThrowExceptionWhenUserDoesNotExist() throws AlreadyExistException, NotExistException {
        // given
        Developer developer = createDeveloper();

        // when
        Throwable throwable = catchThrowable(() -> userManager.update(developer));

        // then
        assertThat(throwable).isInstanceOf(NotExistException.class);
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM AdminEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM PasswordEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM DeveloperEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Developer createDeveloper() {
        Developer developer = new Developer();
        developer.setFirstName(FIRSTNAME + nextUniqueValue);
        developer.setSurname(SURNAME + nextUniqueValue);
        developer.setUsername(USERNAME + nextUniqueValue);
        developer.setEmail(EMAIL);
        nextUniqueValue++;
        return developer;
    }

    private int findAllUsersSize() {
        return entityManager.createNamedQuery(DeveloperEntity.FIND_ALL, DeveloperEntity.class)
                            .getResultList()
                            .size();
    }
}