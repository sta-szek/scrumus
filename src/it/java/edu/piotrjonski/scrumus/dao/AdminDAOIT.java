package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.domain.Admin;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class AdminDAOIT {

    public static int nextUniqueValue = 0;

    @Inject
    private AdminDAO adminDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return UtilsTest.createDeployment();
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
        Admin admin1 = createAdmin();
        Admin admin2 = createAdmin();
        Admin admin3 = createAdmin();

        int id1 = entityManager.merge(adminDAO.mapToDatabaseModelIfNotNull(admin1))
                               .getId();
        int id2 = entityManager.merge(adminDAO.mapToDatabaseModelIfNotNull(admin2))
                               .getId();
        int id3 = entityManager.merge(adminDAO.mapToDatabaseModelIfNotNull(admin3))
                               .getId();

        admin1.setId(id1);
        admin2.setId(id2);
        admin3.setId(id3);

        // when
        List<Admin> admins = adminDAO.findAll();

        // then
        assertThat(admins).hasSize(3)
                              .contains(admin1)
                              .contains(admin2)
                              .contains(admin3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Admin admin1 = createAdmin();
        Admin admin2 = createAdmin();
        int id = entityManager.merge(adminDAO.mapToDatabaseModelIfNotNull(admin1))
                              .getId();
        entityManager.merge(adminDAO.mapToDatabaseModelIfNotNull(admin2));
        admin1.setId(id);

        // when
        Admin admin = adminDAO.findByKey(id)
                                          .get();

        // then
        assertThat(admin).isEqualTo(admin1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Admin> user = adminDAO.findByKey(0);

        // then
        assertThat(user).isEmpty();
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
        userTransaction.commit();
        entityManager.clear();
    }

    private Admin createAdmin() {
        Admin admin = new Admin();
        admin.setDeveloper(new Developer());
        nextUniqueValue++;
        return admin;
    }

    private List<Admin> findAll() {
        return entityManager.createQuery("SELECT d FROM AdminEntity d")
                            .getResultList();
    }

}