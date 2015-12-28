package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.SprintEntity;
import edu.piotrjonski.scrumus.dao.model.project.TimeRange;
import edu.piotrjonski.scrumus.domain.Project;
import edu.piotrjonski.scrumus.domain.Retrospective;
import edu.piotrjonski.scrumus.domain.Sprint;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class SprintDAOIT {

    public static final String NAME = "name";
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final String DESCRIPTION = "Descritpion";
    public static int nextUniqueValue = 0;
    private Project lastProject;
    private Retrospective lastRetrospective;

    @Inject
    private SprintDAO sprintDAO;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private RetrospectiveDAO retrospectiveDAO;

    @Inject
    private UserTransaction userTransaction;
    @PersistenceContext
    private EntityManager entityManager;

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
        Sprint sprint = createSprint();

        // when
        sprintDAO.saveOrUpdate(sprint);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        Sprint sprint = createSprint();
        int entityId = sprintDAO.saveOrUpdate(sprint)
                                .get()
                                .getId();

        // when
        boolean result = sprintDAO.exist(entityId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = sprintDAO.exist(1);

        // then
        assertThat(result).isFalse();
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
        Sprint sprint = sprintDAO.findById(id)
                                 .get();

        // then
        assertThat(sprint).isEqualTo(sprint1);
    }

    @Test
    public void shouldFindAllSprintsForProject() {
        // given
        Project project1 = createProject();
        project1 = projectDAO.saveOrUpdate(project1)
                             .get();
        nextUniqueValue++;
        Project project2 = createProject();
        project2 = projectDAO.saveOrUpdate(project2)
                             .get();
        String projectKey1 = project1.getKey();
        String projectKey2 = project2.getKey();
        List<Sprint> sprints1 = createSprints(projectKey1, 4);
        List<Sprint> sprints2 = createSprints(projectKey2, 4);

        // when
        List<Sprint> result = sprintDAO.findAllSprints(projectKey1);

        // then
        assertThat(result).containsAll(sprints1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Sprint> user = sprintDAO.findById(0);

        // then
        assertThat(user).isEmpty();
    }

    @Test
    public void shouldReturnTrueIfSprintHasRetrospective() {
        // given
        Sprint sprint = sprintDAO.saveOrUpdate(createSprint())
                                 .get();
        sprint.setRetrospectiveId(lastRetrospective.getId());
        sprintDAO.saveOrUpdate(sprint);

        // when
        boolean result = sprintDAO.hasRetrospective(sprint.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfSprintDoesNotHaveRetrospective() {
        // given
        Sprint sprint = sprintDAO.saveOrUpdate(createSprint())
                                 .get();

        // when
        boolean result = sprintDAO.hasRetrospective(sprint.getId());

        // then
        assertThat(result).isFalse();
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
        lastProject = projectDAO.saveOrUpdate(createProject())
                                .get();
        lastRetrospective = retrospectiveDAO.saveOrUpdate(createRetrospective())
                                            .get();
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM SprintEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM RetrospectiveEntity ")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM ProjectEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Sprint createSprint() {
        Sprint sprint = new Sprint();
        sprint.setDefinitionOfDone("dod");
        sprint.setName("name");
        sprint.setProjectKey(lastProject.getKey());
        TimeRange timeRange = new TimeRange();
        timeRange.setStartDate(LocalDateTime.now());
        timeRange.setEndDate(LocalDateTime.now()
                                          .plusDays(14));
        sprint.setTimeRange(timeRange);
        return sprint;
    }

    private List<SprintEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM SprintEntity d")
                            .getResultList();
    }

    private List<Sprint> createSprints(String projectKey, int amount) {
        List<Sprint> sprints = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Sprint sprint = new Sprint();
            TimeRange timeRange = new TimeRange();
            timeRange.setEndDate(LocalDateTime.now()
                                              .plusDays(i));
            timeRange.setStartDate(LocalDateTime.now()
                                                .plusDays(i));
            sprint.setDefinitionOfDone("dod" + i);
            sprint.setName(projectKey + i);
            sprint.setTimeRange(timeRange);
            sprint.setProjectKey(projectKey);
            Sprint savedSprint = sprintDAO.saveOrUpdate(sprint)
                                          .get();
            sprints.add(savedSprint);
        }
        return sprints;
    }

    private Project createProject() {
        Project project = new Project();
        project.setName(NAME + nextUniqueValue);
        project.setCreationDate(NOW);
        project.setKey(NAME + nextUniqueValue);
        project.setDescription(DESCRIPTION + nextUniqueValue);
        nextUniqueValue++;
        return project;
    }

    private Retrospective createRetrospective() {
        Retrospective retrospective = new Retrospective();
        retrospective.setDescription("desc");
        return retrospective;
    }
}