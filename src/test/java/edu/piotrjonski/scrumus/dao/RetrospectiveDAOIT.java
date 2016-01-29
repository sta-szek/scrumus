package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveEntity;
import edu.piotrjonski.scrumus.dao.model.project.TimeRange;
import edu.piotrjonski.scrumus.domain.*;
import edu.piotrjonski.scrumus.utils.TestUtils;
import org.assertj.core.util.Lists;
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
public class RetrospectiveDAOIT {

    private static final String COMMENT_BODY = "retrospectivebody";
    private static final String PROJ_KEY = "projKey";
    private static int nextUniqueValue = 1;
    private Project lastProject;
    private Sprint lastSprint;

    @Inject
    private RetrospectiveDAO retrospectiveDAO;

    @Inject
    private UserTransaction userTransaction;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private SprintDAO sprintDAO;

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
        Retrospective retrospective = new Retrospective();

        // when
        retrospectiveDAO.saveOrUpdate(retrospective);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        Retrospective retrospective = createRetrospective();
        int entityId = retrospectiveDAO.saveOrUpdate(retrospective)
                                       .get()
                                       .getId();

        // when
        boolean result = retrospectiveDAO.exist(entityId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = retrospectiveDAO.exist(1);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldUpdate() {
        // given
        String retrospectiveBody = "updatedBody";
        Retrospective retrospective = new Retrospective();
        retrospective = retrospectiveDAO.mapToDomainModelIfNotNull(entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(
                retrospective)));
        retrospective.setDescription(retrospectiveBody);

        // when
        retrospective = retrospectiveDAO.saveOrUpdate(retrospective)
                                        .get();

        // then
        assertThat(retrospective.getDescription()).isEqualTo(retrospectiveBody);
    }

    @Test
    public void shouldDelete() {
        // given
        Retrospective retrospective = new Retrospective();
        retrospective = retrospectiveDAO.mapToDomainModelIfNotNull(entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(
                retrospective)));

        // when
        retrospectiveDAO.delete(retrospective.getId());
        int allRetrospectives = findAll().size();

        // then
        assertThat(allRetrospectives).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Retrospective retrospective1 = createRetrospective();
        Retrospective retrospective2 = createRetrospective();
        Retrospective retrospective3 = createRetrospective();

        int id1 = entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(retrospective1))
                               .getId();
        int id2 = entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(retrospective2))
                               .getId();
        int id3 = entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(retrospective3))
                               .getId();

        retrospective1.setId(id1);
        retrospective2.setId(id2);
        retrospective3.setId(id3);

        // when
        List<Retrospective> retrospectives = retrospectiveDAO.findAll();

        // then
        assertThat(retrospectives).hasSize(3)
                                  .contains(retrospective1)
                                  .contains(retrospective2)
                                  .contains(retrospective3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Retrospective retrospective1 = createRetrospective();
        Retrospective retrospective2 = createRetrospective();
        int id = entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(retrospective1))
                              .getId();
        entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(retrospective2));
        retrospective1.setId(id);

        // when
        Retrospective retrospective = retrospectiveDAO.findById(id)
                                                      .get();

        // then
        assertThat(retrospective).isEqualTo(retrospective1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Retrospective> user = retrospectiveDAO.findById(0);

        // then
        assertThat(user).isEmpty();
    }

    @Test
    public void shouldFindRetrospectiveForSprint() {
        // given
        Retrospective retrospective = createRetrospective();
        retrospective = retrospectiveDAO.saveOrUpdate(retrospective)
                                        .get();
        lastSprint.setRetrospectiveId(retrospective.getId());
        sprintDAO.saveOrUpdate(lastSprint);

        // when
        Retrospective result = retrospectiveDAO.findRetrospectiveForSprint(lastSprint.getId())
                                               .get();
        // then
        assertThat(result).isEqualTo(retrospective);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenSprintDoesNotHaveRetrospective() {
        // given

        // when
        Optional<Retrospective> result = retrospectiveDAO.findRetrospectiveForSprint(lastSprint.getId());

        // then
        assertThat(result).isEmpty();
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
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

    private Retrospective createRetrospective() {
        Retrospective retrospective = new Retrospective();
        retrospective.setDescription(COMMENT_BODY + nextUniqueValue);
        retrospective.setRetrospectiveItems(createRetrospectiveItems());
        nextUniqueValue++;
        return retrospective;
    }

    private List<RetrospectiveEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM RetrospectiveEntity d")
                            .getResultList();
    }

    private List<RetrospectiveItem> createRetrospectiveItems() {
        RetrospectiveItem retrospectiveItem1 = new RetrospectiveItem();
        RetrospectiveItem retrospectiveItem2 = new RetrospectiveItem();
        RetrospectiveItem retrospectiveItem3 = new RetrospectiveItem();
        RetrospectiveItem retrospectiveItem4 = new RetrospectiveItem();
        retrospectiveItem1.setDescription("desc1");
        retrospectiveItem1.setRetrospectiveItemType(RetrospectiveItemType.MINUS);
        retrospectiveItem2.setDescription("desc1");
        retrospectiveItem2.setRetrospectiveItemType(RetrospectiveItemType.PLUS);
        retrospectiveItem3.setDescription("desc1");
        retrospectiveItem3.setRetrospectiveItemType(RetrospectiveItemType.PLUS);
        retrospectiveItem4.setDescription("desc1");
        retrospectiveItem4.setRetrospectiveItemType(RetrospectiveItemType.MINUS);

        return Lists.newArrayList(retrospectiveItem1, retrospectiveItem2, retrospectiveItem3, retrospectiveItem4);
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