package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.AdminEntity;
import edu.piotrjonski.scrumus.domain.Admin;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class AdminDAOIT {

    private int lastDeveloperId = 0;

    @Inject
    private AdminDAO adminDAO;

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return TestUtils.createDeployment();
    }

    @Before
    public void dropAllAdminsAndStartTransaction() throws Exception {
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
        Admin admin = createAdmin();

        // when
        adminDAO.saveOrUpdate(admin);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldFindByDeveloperId() {
        // given
        Admin admin = createAdmin();
        Optional<Admin> savedAdmin = adminDAO.saveOrUpdate(admin);

        // when
        Optional<Admin> result = adminDAO.findByDeveloperId(lastDeveloperId);

        // then
        assertThat(result).isEqualTo(savedAdmin);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        Admin admin = createAdmin();
        int entityId = adminDAO.saveOrUpdate(admin)
                               .get()
                               .getId();

        // when
        boolean result = adminDAO.exist(entityId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = adminDAO.exist(1);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnAdminIfExistByDeveloperId() {
        // given
        Admin admin = createAdmin();
        Admin expectedResult = adminDAO.saveOrUpdate(admin)
                                       .get();

        // when
        Optional<Admin> result = adminDAO.findByDeveloperId(lastDeveloperId);

        // then
        assertThat(result.get()).isEqualTo(expectedResult);
    }

    @Test
    public void shouldReturnEmptyOptionalIfNotExistByDeveloperId() {
        // given

        // when
        Optional<Admin> result = adminDAO.findByDeveloperId(1);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldDelete() {
        // given
        Admin admin = createAdmin();
        admin = adminDAO.mapToDomainModelIfNotNull(entityManager.merge(adminDAO.mapToDatabaseModelIfNotNull(
                admin)));

        // when
        adminDAO.delete(admin.getId());
        int allUsers = findAll().size();

        // then
        assertThat(allUsers).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Admin admin = createAdmin();

        int id1 = entityManager.merge(adminDAO.mapToDatabaseModelIfNotNull(admin))
                               .getId();

        admin.setId(id1);

        // when
        List<Admin> admins = adminDAO.findAll();

        // then
        assertThat(admins).hasSize(1)
                          .contains(admin);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Admin admin1 = createAdmin();
        int id = entityManager.merge(adminDAO.mapToDatabaseModelIfNotNull(admin1))
                              .getId();
        admin1.setId(id);

        // when
        Admin admin = adminDAO.findById(id)
                              .get();

        // then
        assertThat(admin).isEqualTo(admin1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Admin> user = adminDAO.findById(0);

        // then
        assertThat(user).isEmpty();
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
        Developer developer = new Developer();
        developer.setEmail("ds");
        developer.setFirstName("ds");
        developer.setSurname("ds");
        developer.setUsername("ds");
        Developer developer1 = developerDAO.saveOrUpdate(developer)
                                           .get();
        lastDeveloperId = developer1.getId();
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

    private Admin createAdmin() {
        Admin admin = new Admin();
        admin.setDeveloper(developerDAO.findById(lastDeveloperId)
                                       .get());
        return admin;
    }

    private List<AdminEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM AdminEntity d")
                            .getResultList();
    }

}