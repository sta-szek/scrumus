package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.security.RoleEntity;
import edu.piotrjonski.scrumus.dao.model.security.RoleType;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Role;
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
public class RoleDAOIT {

    public static final String NAME = "name";
    public static final int ID = 1;
    public static final String EMAIL = "jako@company.com";
    public static final String USERNAME = "jako";
    public static final String SURNAME = "Kowalski";
    public static final String FIRSTNAME = "Jan";
    public static int nextUniqueValue = 0;
    @Inject
    private RoleDAO roleDAO;

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
    public void shouldReturnTrueIfExistById() {
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
    public void shouldReturnFalseIfDoesNotExistById() {
        // given

        // when
        boolean result = roleDAO.exist(1);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnTrueIfExistByRoleType() {
        // given
        Role role = createRole();
        roleDAO.saveOrUpdate(role);

        // when
        boolean result = roleDAO.existByRoleType(RoleType.ADMIN);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExistByRoleType() {
        // given

        // when
        boolean result = roleDAO.existByRoleType(null);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnTrueIfExistByRoleTypeAndDeveloperId() {
        // given
        Role role = createRole();
        Developer developer = developerDAO.saveOrUpdate(createDeveloper())
                                          .get();

        role.addDeveloper(developer);
        roleDAO.saveOrUpdate(role);

        // when
        boolean result = roleDAO.existByRoleTypeAndDeveloperId(RoleType.ADMIN, developer.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExistByRoleTypeAndDeveloperId() {
        // given

        // when
        boolean result = roleDAO.existByRoleTypeAndDeveloperId(RoleType.ADMIN, 0);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldUpdate() {
        // given
        Role role = createRole();
        role = roleDAO.mapToDomainModelIfNotNull(entityManager.merge(roleDAO.mapToDatabaseModelIfNotNull(
                role)));
        role.setRoleType(RoleType.DEVELOPER);

        // when
        role = roleDAO.saveOrUpdate(role)
                      .get();

        // then
        assertThat(role.getRoleType()).isEqualTo(RoleType.DEVELOPER);
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
        role2.setRoleType(RoleType.DEVELOPER);
        Role role3 = createRole();
        role3.setRoleType(RoleType.PRODUCT_OWNER);

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
        role2.setRoleType(RoleType.DEVELOPER);
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
    public void shouldFindByName() {
        // given
        Role role1 = createRole();
        Role role2 = createRole();
        role2.setRoleType(RoleType.DEVELOPER);
        int id = entityManager.merge(roleDAO.mapToDatabaseModelIfNotNull(role1))
                              .getId();
        entityManager.merge(roleDAO.mapToDatabaseModelIfNotNull(role2));
        role1.setId(id);

        // when
        Role role = roleDAO.findRoleByRoleType(role1.getRoleType())
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
        entityManager.createQuery("DELETE FROM DeveloperEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Role createRole() {
        Role role = new Role();
        role.setRoleType(RoleType.ADMIN);
        role.setId(ID + nextUniqueValue);
        nextUniqueValue++;
        return role;
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

    private List<RoleEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM RoleEntity d")
                            .getResultList();
    }
}