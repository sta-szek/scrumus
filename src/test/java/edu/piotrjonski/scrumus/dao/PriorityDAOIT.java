package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.PriorityEntity;
import edu.piotrjonski.scrumus.domain.Priority;
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
public class PriorityDAOIT {
    public static final String NAME = "abcdge";
    public static int nextUniqueValue = 0;

    @Inject
    private PriorityDAO priorityDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return TestUtils.createDeployment();
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
        Priority priority = createPriority();

        // when
        priorityDAO.saveOrUpdate(priority);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        Priority priority = createPriority();
        int entityId = priorityDAO.saveOrUpdate(priority)
                                  .get()
                                  .getId();

        // when
        boolean result = priorityDAO.exist(entityId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = priorityDAO.exist(1);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldUpdate() {
        // given
        String updatedName = "UpdatedName";
        Priority priority = createPriority();
        priority = priorityDAO.mapToDomainModelIfNotNull(entityManager.merge(priorityDAO.mapToDatabaseModelIfNotNull(
                priority)));
        priority.setName(updatedName);

        // when
        priority = priorityDAO.saveOrUpdate(priority)
                              .get();

        // then
        assertThat(priority.getName()).isEqualTo(updatedName);
    }

    @Test
    public void shouldDelete() {
        // given
        Priority priority = createPriority();
        priority = priorityDAO.mapToDomainModelIfNotNull(entityManager.merge(priorityDAO.mapToDatabaseModelIfNotNull(
                priority)));

        // when
        priorityDAO.delete(priority.getId());
        int allUsers = findAll().size();

        // then
        assertThat(allUsers).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Priority priority1 = createPriority();
        Priority priority2 = createPriority();
        Priority priority3 = createPriority();

        int id1 = entityManager.merge(priorityDAO.mapToDatabaseModelIfNotNull(priority1))
                               .getId();
        int id2 = entityManager.merge(priorityDAO.mapToDatabaseModelIfNotNull(priority2))
                               .getId();
        int id3 = entityManager.merge(priorityDAO.mapToDatabaseModelIfNotNull(priority3))
                               .getId();

        priority1.setId(id1);
        priority2.setId(id2);
        priority3.setId(id3);

        // when
        List<Priority> prioritys = priorityDAO.findAll();

        // then
        assertThat(prioritys).hasSize(3)
                             .contains(priority1)
                             .contains(priority2)
                             .contains(priority3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Priority priority1 = createPriority();
        Priority priority2 = createPriority();
        int id = entityManager.merge(priorityDAO.mapToDatabaseModelIfNotNull(priority1))
                              .getId();
        entityManager.merge(priorityDAO.mapToDatabaseModelIfNotNull(priority2));
        priority1.setId(id);

        // when
        Priority priority = priorityDAO.findById(id)
                                       .get();

        // then
        assertThat(priority).isEqualTo(priority1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Priority> user = priorityDAO.findById(0);

        // then
        assertThat(user).isEmpty();
    }

    @Test
    public void shouldFindAllNames() {
        // given
        Priority priority1 = createPriority();
        Priority priority2 = createPriority();
        String name1 = priorityDAO.saveOrUpdate(priority1)
                                  .get()
                                  .getName();
        String name2 = priorityDAO.saveOrUpdate(priority2)
                                  .get()
                                  .getName();

        // when
        List<String> result = priorityDAO.findAllNames();

        // then
        assertThat(result).contains(name1)
                          .contains(name2);
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM PriorityEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Priority createPriority() {
        Priority priority = new Priority();
        priority.setName(NAME + nextUniqueValue);
        nextUniqueValue++;
        return priority;
    }

    private List<PriorityEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM PriorityEntity d")
                            .getResultList();
    }
}