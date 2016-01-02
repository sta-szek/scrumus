package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.ProjectDAO;
import edu.piotrjonski.scrumus.dao.SprintDAO;
import edu.piotrjonski.scrumus.dao.model.project.TimeRange;
import edu.piotrjonski.scrumus.domain.Project;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(Arquillian.class)
public class SprintManagerIT {
    public static final String NAME = "name";
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final String DESCRIPTION = "Descritpion";
    public static int nextUniqueValue = 0;
    private Project lastProject;

    @Inject
    private SprintDAO sprintDAO;

    @Inject
    private SprintManager sprintManager;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return UtilsTest.createDeployment();
    }

    @Before
    public void dropAllSprintsAndStartTransaction() throws Exception {
        clearData();
        startTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        userTransaction.commit();
    }

    @Test
    public void shouldCreateSprint() throws AlreadyExistException {
        // given
        Sprint sprint = createSprint();

        // when
        Sprint savedSprint = sprintManager.createSprint(sprint)
                                          .get();
        boolean result = sprintDAO.exist(savedSprint.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldThrowExceptionIfSprintAlreadyExist() throws AlreadyExistException {
        // given
        Sprint sprint = createSprint();
        Sprint savedSprint = sprintManager.createSprint(sprint)
                                          .get();

        // when
        Throwable result = catchThrowable(() -> sprintManager.createSprint(savedSprint));

        // then
        assertThat(result).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void shouldFindSprint() throws AlreadyExistException {
        // given
        Sprint sprint = createSprint();
        Sprint savedSprint = sprintManager.createSprint(sprint)
                                          .get();

        // when
        Sprint result = sprintManager.findSprint(savedSprint.getId())
                                     .get();

        // then
        assertThat(result).isEqualTo(savedSprint);
    }

    @Test
    public void shouldFindStoryWithNoIdIfStoryDoesNotExist() throws AlreadyExistException {
        // given

        // when
        Optional<Sprint> result = sprintManager.findSprint(0);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldFindAllSprintsForProject() throws AlreadyExistException {
        // given
        Sprint sprint1 = createSprint();
        Sprint sprint2 = createSprint();
        sprint1 = sprintManager.createSprint(sprint1)
                               .get();
        sprint2 = sprintManager.createSprint(sprint2)
                               .get();

        // when
        List<Sprint> result = sprintManager.findAllSprintsForProject(lastProject.getKey());

        // then
        assertThat(result).hasSize(2)
                          .contains(sprint1)
                          .contains(sprint2);
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
        lastProject = projectDAO.saveOrUpdate(createProject())
                                .get();
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
        sprint.setName("name" + nextUniqueValue);
        TimeRange timeRange = new TimeRange();
        timeRange.setEndDate(LocalDateTime.now());
        timeRange.setStartDate(LocalDateTime.now());
        sprint.setTimeRange(timeRange);
        sprint.setProjectKey(lastProject.getKey());
        return sprint;
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
}