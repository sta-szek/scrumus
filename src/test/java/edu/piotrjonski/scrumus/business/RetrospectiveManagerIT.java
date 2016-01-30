package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.IssueTypeDAO;
import edu.piotrjonski.scrumus.dao.ProjectDAO;
import edu.piotrjonski.scrumus.dao.RetrospectiveDAO;
import edu.piotrjonski.scrumus.dao.SprintDAO;
import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveEntity;
import edu.piotrjonski.scrumus.dao.model.project.TimeRange;
import edu.piotrjonski.scrumus.domain.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(Arquillian.class)
public class RetrospectiveManagerIT {

    private static final String PROJ_KEY = "projKey";

    private Sprint lastSprint;
    private Retrospective lastRetrospective;
    private Project lastProject;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private IssueTypeDAO issueTypeDAO;

    @Inject
    private SprintDAO sprintDAO;

    @Inject
    private RetrospectiveManager retrospectiveManager;

    @Inject
    private RetrospectiveDAO retrospectiveDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return TestUtils.createDeployment();
    }

    @Before
    public void dropAllRetrospectivesAndStartTransaction() throws Exception {
        clearData();
        startTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        userTransaction.commit();
    }

    @Test
    public void shouldCreateRetrospectiveForSprint() throws AlreadyExistException {
        // given
        Retrospective retrospective = createRetrospective();

        // when
        retrospectiveManager.createRetrospectiveForSprint(retrospective, lastSprint);
        boolean result = sprintDAO.hasRetrospective(lastSprint.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldThrowExceptionIfRetrospectiveAlreadyExist() {
        // given
        Retrospective retrospective = createRetrospective();
        Retrospective savedRetrospective = retrospectiveDAO.saveOrUpdate(retrospective)
                                                           .get();

        // when
        Throwable result = catchThrowable(() -> retrospectiveManager.createRetrospectiveForSprint(savedRetrospective, lastSprint));

        // then
        assertThat(result).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void shouldThrowExceptionIfSprintAlreadyHaveRetrospective() throws AlreadyExistException {
        // given
        Retrospective retrospective1 = createRetrospective();
        retrospectiveManager.createRetrospectiveForSprint(retrospective1, lastSprint);
        Retrospective retrospective2 = createRetrospective();

        // when
        Throwable result = catchThrowable(() -> retrospectiveManager.createRetrospectiveForSprint(retrospective2, lastSprint));

        // then
        assertThat(result).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void shouldFindRetrospective() {
        // given
        Retrospective retrospective = createRetrospective();
        retrospective = retrospectiveDAO.saveOrUpdate(retrospective)
                                        .get();

        // when
        Retrospective result = retrospectiveManager.findRetrospective(retrospective.getId())
                                                   .get();

        // then
        assertThat(result).isEqualTo(retrospective);
    }

    @Test
    public void shouldAddItemToRetrospective() throws NotExistException {
        // given
        Retrospective retrospective = retrospectiveDAO.saveOrUpdate(createRetrospective())
                                                      .get();
        RetrospectiveItem retrospectiveItem = new RetrospectiveItem();
        retrospectiveItem.setRetrospectiveItemType(RetrospectiveItemType.MINUS);
        retrospectiveItem.setDescription("desc");

        // when
        retrospectiveManager.addRetrospectiveItemToRetrospective(retrospectiveItem, retrospective);
        List<RetrospectiveItem> result = retrospectiveManager.findRetrospective(retrospective.getId())
                                                             .get()
                                                             .getRetrospectiveItems();

        // then
        assertThat(result).contains(retrospectiveItem);
    }

    @Test
    public void shouldRemoveItemFromRetrospective() throws NotExistException {
        // given
        RetrospectiveItem retrospectiveItem = new RetrospectiveItem();
        retrospectiveItem.setRetrospectiveItemType(RetrospectiveItemType.MINUS);
        retrospectiveItem.setDescription("desc");
        Retrospective retrospective = createRetrospective();
        retrospective.addRetrospectiveItem(retrospectiveItem);

        Retrospective savedRetrospective = retrospectiveDAO.saveOrUpdate(retrospective)
                                                           .get();

        // when
        retrospectiveManager.removeRetrospectiveItemFromRetrospective(retrospectiveItem, savedRetrospective);
        List<RetrospectiveItem> result = retrospectiveManager.findRetrospective(savedRetrospective.getId())
                                                             .get()
                                                             .getRetrospectiveItems();

        // then
        assertThat(result).doesNotContain(retrospectiveItem);
    }

    @Test
    public void shouldThrowExceptionIfRetrospectiveDoesNotExistWhenAddingItem() {
        // given
        Retrospective retrospective = createRetrospective();
        RetrospectiveItem retrospectiveItem = new RetrospectiveItem();

        // when
        Throwable result = catchThrowable(() -> retrospectiveManager.addRetrospectiveItemToRetrospective(retrospectiveItem,
                                                                                                         retrospective));
        // then
        assertThat(result).isInstanceOf(NotExistException.class);
    }

    @Test
    public void shouldThrowExceptionIfRetrospectiveDoesNotExistWhenRemovingItem() {
        // given
        Retrospective retrospective = createRetrospective();
        RetrospectiveItem retrospectiveItem = new RetrospectiveItem();

        // when
        Throwable result = catchThrowable(() -> retrospectiveManager.removeRetrospectiveItemFromRetrospective(retrospectiveItem,
                                                                                                              retrospective));
        // then
        assertThat(result).isInstanceOf(NotExistException.class);
    }

    private Retrospective createRetrospective() {
        Retrospective retrospective = new Retrospective();
        retrospective.setDescription("desc");
        return retrospective;
    }

    private void startTransaction() throws SystemException, NotSupportedException, AlreadyExistException {
        userTransaction.begin();
        entityManager.joinTransaction();
        lastRetrospective = retrospectiveDAO.saveOrUpdate(createRetrospective())
                                            .get();
        lastProject = projectDAO.saveOrUpdate(createProject())
                                .get();
        lastSprint = sprintDAO.saveOrUpdate(createSprint())
                              .get();
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM SprintEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM ProjectEntity")
                     .executeUpdate();
        entityManager.createQuery("SELECT r FROM RetrospectiveEntity r", RetrospectiveEntity.class)
                     .getResultList()
                     .forEach(entityManager::remove);
        userTransaction.commit();
        entityManager.clear();
    }

    private Sprint createSprint() {
        Sprint sprint = new Sprint();
        sprint.setDefinitionOfDone("dod");
        sprint.setProjectKey(lastProject.getKey());
        sprint.setName("name");
        TimeRange timeRange = new TimeRange();
        timeRange.setStartDate(LocalDateTime.now());
        timeRange.setEndDate(LocalDateTime.now());
        sprint.setTimeRange(timeRange);
        return sprint;
    }

    private Project createProject() {
        Project project = new Project();
        project.setKey(PROJ_KEY);
        project.setName("name");
        return project;
    }

}