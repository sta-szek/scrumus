package edu.piotrjonski.scrumus.dao;

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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class DeveloperDAOIT {

    public static final String EMAIL = "jako@company.com";
    public static final String USERNAME = "jako";
    public static final String SURNAME = "Kowalski";
    public static final String FIRSTNAME = "Jan";
    public static int nextUniqueValue = 0;

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
    public void dropAllDevelopersAndStartTransaction() throws Exception {
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
        Developer developer = createDeveloper();

        // when
        developerDAO.saveOrUpdate(developer);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        Developer developer = createDeveloper();
        int entityId = developerDAO.saveOrUpdate(developer)
                                   .get()
                                   .getId();

        // when
        boolean result = developerDAO.exist(entityId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = developerDAO.exist(1);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldUpdate() {
        // given
        String updatedUserName = "UpdatedUser";
        Developer developer = createDeveloper();
        developer = developerDAO.mapToDomainModelIfNotNull(entityManager.merge(developerDAO.mapToDatabaseModelIfNotNull(
                developer)));
        developer.setFirstName(updatedUserName);

        // when
        developer = developerDAO.saveOrUpdate(developer)
                                .get();

        // then
        assertThat(developer.getFirstName()).isEqualTo(updatedUserName);
    }

    @Test
    public void shouldDelete() {
        // given
        Developer developer = createDeveloper();
        developer = developerDAO.mapToDomainModelIfNotNull(entityManager.merge(developerDAO.mapToDatabaseModelIfNotNull(
                developer)));

        // when
        developerDAO.delete(developer.getId());
        int allUsers = findAll().size();

        // then
        assertThat(allUsers).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Developer developer1 = createDeveloper();
        Developer developer2 = createDeveloper();
        Developer developer3 = createDeveloper();

        int id1 = entityManager.merge(developerDAO.mapToDatabaseModelIfNotNull(developer1))
                               .getId();
        int id2 = entityManager.merge(developerDAO.mapToDatabaseModelIfNotNull(developer2))
                               .getId();
        int id3 = entityManager.merge(developerDAO.mapToDatabaseModelIfNotNull(developer3))
                               .getId();

        developer1.setId(id1);
        developer2.setId(id2);
        developer3.setId(id3);

        // when
        List<Developer> developers = developerDAO.findAll();

        // then
        assertThat(developers).hasSize(3)
                              .contains(developer1)
                              .contains(developer2)
                              .contains(developer3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Developer developer1 = createDeveloper();
        Developer developer2 = createDeveloper();
        int id = entityManager.merge(developerDAO.mapToDatabaseModelIfNotNull(developer1))
                              .getId();
        entityManager.merge(developerDAO.mapToDatabaseModelIfNotNull(developer2));
        developer1.setId(id);

        // when
        Developer developer = developerDAO.findById(id)
                                          .get();

        // then
        assertThat(developer).isEqualTo(developer1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Developer> user = developerDAO.findById(0);

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

    private List<DeveloperEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM DeveloperEntity d")
                            .getResultList();
    }

}