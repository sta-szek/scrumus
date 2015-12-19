package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.DeveloperDAO;
import edu.piotrjonski.scrumus.dao.model.user.AdminEntity;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Developer;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(Arquillian.class)
public class UserManagerIT {
    public static final String EMAIL = "jako@company.com";
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
        return UtilsTest.createDeployment();
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
    public void shouldCreateDeveloper() throws AlreadyExistException {
        // given
        Developer developer = createDeveloper();

        // when
        Developer savedDeveloper = userManager.create(developer);
        boolean result = developerDAO.exist(savedDeveloper.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldThrowExceptionWhenUserAlreadyExist() throws AlreadyExistException {
        // given
        Developer developer = createDeveloper();
        Developer savedDeveloper = userManager.create(developer);

        // when
        Throwable throwable = catchThrowable(() -> userManager.create(savedDeveloper));

        // then
        assertThat(throwable).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void shouldDeleteUser() throws AlreadyExistException {
        // given
        Developer developer = createDeveloper();
        int id = userManager.create(developer)
                            .getId();

        // when
        userManager.delete(id);
        int result = findAllUsersSize();

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void shouldGrantAdminPermission() {
        // given
        Developer developer = developerDAO.saveOrUpdate(createDeveloper())
                                          .get();

        // when
        userManager.grantAdminPermission(developer);
        int result = findAllAdminsSize();

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void shouldNotGrantAdminPermissionIfUserDoesNotExist() {
        // given
        Developer developer = createDeveloper();

        // when
        userManager.grantAdminPermission(developer);
        int result = findAllAdminsSize();

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void shouldUpdateUserIfExist() throws AlreadyExistException, NotExistException {
        // given
        Developer developer = createDeveloper();
        Developer savedDeveloper = userManager.create(developer);

        String newEmail = "newEmail";
        String newFirstname = "newFirstname";
        String newSurname = "newSurname";
        savedDeveloper.setEmail(newEmail);
        savedDeveloper.setFirstName(newFirstname);
        savedDeveloper.setSurname(newSurname);

        // when
        Developer updatedDeveloper = userManager.update(savedDeveloper);

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
        developer.setEmail(EMAIL + nextUniqueValue);
        nextUniqueValue++;
        return developer;
    }

    private int findAllAdminsSize() {
        return entityManager.createNamedQuery(AdminEntity.FIND_ALL, AdminEntity.class)
                            .getResultList()
                            .size();
    }

    private int findAllUsersSize() {
        return entityManager.createNamedQuery(DeveloperEntity.FIND_ALL, DeveloperEntity.class)
                            .getResultList()
                            .size();
    }
}