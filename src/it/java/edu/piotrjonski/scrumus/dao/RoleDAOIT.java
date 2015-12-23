package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.security.RoleEntity;
import edu.piotrjonski.scrumus.domain.Role;
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
public class RoleDAOIT {

    public static final String NAME = "name";
    public static final int ID = 1;
    public static final byte[] DATA = new byte[5];
    public static int nextUniqueValue = 0;

    @Inject
    private RoleDAO roleDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return UtilsTest.createDeployment();
    }

    @Before
    public void dropAllRolesAndStartTransaction() throws Exception {
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
        Role role = createRole();

        // when
        roleDAO.saveOrUpdate(role);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        Role role = createRole();
        int entityId = roleDAO.saveOrUpdate(role)
                                 .get()
                                 .getId();

        // when
        boolean result = roleDAO.exist(entityId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = roleDAO.exist(1);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldUpdate() {
        // given
        String updatedName = "UpdatedName";
        Role role = createRole();
        role = roleDAO.mapToDomainModelIfNotNull(entityManager.merge(roleDAO.mapToDatabaseModelIfNotNull(
                role)));
        role.setName(updatedName);

        // when
        role = roleDAO.saveOrUpdate(role)
                            .get();

        // then
        assertThat(role.getName()).isEqualTo(updatedName);
    }

    @Test
    public void shouldDelete() {
        // given
        Role role = createRole();
        role = roleDAO.mapToDomainModelIfNotNull(entityManager.merge(roleDAO.mapToDatabaseModelIfNotNull(
                role)));

        // when
        roleDAO.delete(role.getId());
        int allRoles = findAll().size();

        // then
        assertThat(allRoles).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Role role1 = createRole();
        Role role2 = createRole();
        Role role3 = createRole();

        int id1 = entityManager.merge(roleDAO.mapToDatabaseModelIfNotNull(role1))
                               .getId();
        int id2 = entityManager.merge(roleDAO.mapToDatabaseModelIfNotNull(role2))
                               .getId();
        int id3 = entityManager.merge(roleDAO.mapToDatabaseModelIfNotNull(role3))
                               .getId();

        role1.setId(id1);
        role2.setId(id2);
        role3.setId(id3);

        // when
        List<Role> roles = roleDAO.findAll();

        // then
        assertThat(roles).hasSize(3)
                            .contains(role1)
                            .contains(role2)
                            .contains(role3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Role role1 = createRole();
        Role role2 = createRole();
        int id = entityManager.merge(roleDAO.mapToDatabaseModelIfNotNull(role1))
                              .getId();
        entityManager.merge(roleDAO.mapToDatabaseModelIfNotNull(role2));
        role1.setId(id);

        // when
        Role role = roleDAO.findById(id)
                                    .get();

        // then
        assertThat(role).isEqualTo(role1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Role> user = roleDAO.findById(0);

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
        entityManager.createQuery("DELETE FROM RoleEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Role createRole() {
        Role role = new Role();
        role.setName(NAME + nextUniqueValue);
        role.setId(ID + nextUniqueValue);
        nextUniqueValue++;
        return role;
    }

    private List<RoleEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM RoleEntity d")
                            .getResultList();
    }
}