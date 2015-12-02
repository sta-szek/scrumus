package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.TimeRange;
import edu.piotrjonski.scrumus.domain.Sprint;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class SprintDAOIT {

    @Inject
    private SprintDAO sprintDAO;

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
        Sprint sprint = createSprint();

        // when
        sprintDAO.saveOrUpdate(sprint);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldUpdate() {
        // given
        String updatedName = "UpdatedName";
        Sprint sprint = createSprint();
        sprint = sprintDAO.mapToDomainModelIfNotNull(entityManager.merge(sprintDAO.mapToDatabaseModelIfNotNull(
                sprint)));
        sprint.setName(updatedName);

        // when
        sprint = sprintDAO.saveOrUpdate(sprint)
                          .get();

        // then
        assertThat(sprint.getName()).isEqualTo(updatedName);
    }

    @Test
    public void shouldDelete() {
        // given
        Sprint sprint = createSprint();
        sprint = sprintDAO.mapToDomainModelIfNotNull(entityManager.merge(sprintDAO.mapToDatabaseModelIfNotNull(
                sprint)));

        // when
        sprintDAO.delete(sprint.getId());
        int allUsers = findAll().size();

        // then
        assertThat(allUsers).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Sprint sprint1 = createSprint();
        Sprint sprint2 = createSprint();
        Sprint sprint3 = createSprint();

        int id1 = entityManager.merge(sprintDAO.mapToDatabaseModelIfNotNull(sprint1))
                               .getId();
        int id2 = entityManager.merge(sprintDAO.mapToDatabaseModelIfNotNull(sprint2))
                               .getId();
        int id3 = entityManager.merge(sprintDAO.mapToDatabaseModelIfNotNull(sprint3))
                               .getId();

        sprint1.setId(id1);
        sprint2.setId(id2);
        sprint3.setId(id3);

        // when
        List<Sprint> sprints = sprintDAO.findAll();

        // then
        assertThat(sprints).hasSize(3)
                           .contains(sprint1)
                           .contains(sprint2)
                           .contains(sprint3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Sprint sprint1 = createSprint();
        Sprint sprint2 = createSprint();
        int id = entityManager.merge(sprintDAO.mapToDatabaseModelIfNotNull(sprint1))
                              .getId();
        entityManager.merge(sprintDAO.mapToDatabaseModelIfNotNull(sprint2));
        sprint1.setId(id);

        // when
        Sprint sprint = sprintDAO.findByKey(id)
                                 .get();

        // then
        assertThat(sprint).isEqualTo(sprint1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Sprint> user = sprintDAO.findByKey(0);

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
        entityManager.createQuery("DELETE FROM SprintEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Sprint createSprint() {
        Sprint sprint = new Sprint();
        sprint.setDefinitionOfDone("dod");
        sprint.setName("name");
        TimeRange timeRange = new TimeRange();
        timeRange.setStartDate(LocalDateTime.now());
        timeRange.setEndDate(LocalDateTime.now()
                                          .plusDays(14));
        sprint.setTimeRange(timeRange);
        return sprint;
    }

    private List<Sprint> findAll() {
        return entityManager.createQuery("SELECT d FROM SprintEntity d")
                            .getResultList();
    }
}