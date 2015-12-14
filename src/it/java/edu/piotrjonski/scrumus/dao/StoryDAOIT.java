package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.StoryEntity;
import edu.piotrjonski.scrumus.dao.model.project.TimeRange;
import edu.piotrjonski.scrumus.domain.Sprint;
import edu.piotrjonski.scrumus.domain.Story;
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

@RunWith(Arquillian.class)
public class StoryDAOIT {

    @Inject
    private StoryDAO storyDAO;

    @Inject
    private SprintDAO sprintDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    private int currentSprintId;

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
        Story story = createStory();

        // when
        storyDAO.saveOrUpdate(story);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        Story story = createStory();
        int entityId = storyDAO.saveOrUpdate(story)
                               .get()
                               .getId();

        // when
        boolean result = storyDAO.exist(entityId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = storyDAO.exist(1);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldFindStoriesForSprint() {
        // given
        String name = "bame";
        Story story1 = new Story();
        Story story2 = new Story();
        Story story3 = new Story();
        Story story4 = new Story();
        story1.setName(name);
        story2.setName(name);
        story3.setName(name);
        story4.setName(name);
        story1.setSprintId(currentSprintId);
        story2.setSprintId(currentSprintId);
        story3.setSprintId(currentSprintId);
        story4.setSprintId(currentSprintId);

        story1 = storyDAO.saveOrUpdate(story1)
                         .get();
        story2 = storyDAO.saveOrUpdate(story2)
                         .get();
        story3 = storyDAO.saveOrUpdate(story3)
                         .get();
        story4 = storyDAO.saveOrUpdate(story4)
                         .get();

        // when
        List<Story> result = storyDAO.findStoriesForSprint(currentSprintId);

        // then
        assertThat(result).hasSize(4)
                          .contains(story1)
                          .contains(story2)
                          .contains(story3)
                          .contains(story4);
    }

    @Test
    public void shouldUpdate() {
        // given
        String updatedName = "UpdatedName";
        Story story = createStory();
        story = storyDAO.mapToDomainModelIfNotNull(entityManager.merge(storyDAO.mapToDatabaseModelIfNotNull(
                story)));
        story.setName(updatedName);

        // when
        story = storyDAO.saveOrUpdate(story)
                        .get();

        // then
        assertThat(story.getName()).isEqualTo(updatedName);
    }

    @Test
    public void shouldDelete() {
        // given
        Story story = createStory();
        story = storyDAO.mapToDomainModelIfNotNull(entityManager.merge(storyDAO.mapToDatabaseModelIfNotNull(
                story)));

        // when
        storyDAO.delete(story.getId());
        int allUsers = findAll().size();

        // then
        assertThat(allUsers).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Story story1 = createStory();
        Story story2 = createStory();
        Story story3 = createStory();

        int id1 = entityManager.merge(storyDAO.mapToDatabaseModelIfNotNull(story1))
                               .getId();
        int id2 = entityManager.merge(storyDAO.mapToDatabaseModelIfNotNull(story2))
                               .getId();
        int id3 = entityManager.merge(storyDAO.mapToDatabaseModelIfNotNull(story3))
                               .getId();

        story1.setId(id1);
        story2.setId(id2);
        story3.setId(id3);

        // when
        List<Story> storys = storyDAO.findAll();

        // then
        assertThat(storys).hasSize(3)
                          .contains(story1)
                          .contains(story2)
                          .contains(story3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Story story1 = createStory();
        Story story2 = createStory();
        int id = entityManager.merge(storyDAO.mapToDatabaseModelIfNotNull(story1))
                              .getId();
        entityManager.merge(storyDAO.mapToDatabaseModelIfNotNull(story2));
        story1.setId(id);

        // when
        Story story = storyDAO.findById(id)
                              .get();

        // then
        assertThat(story).isEqualTo(story1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Story> user = storyDAO.findById(0);

        // then
        assertThat(user).isEmpty();
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
        TimeRange timeRange = new TimeRange();
        timeRange.setEndDate(LocalDateTime.now());
        timeRange.setStartDate(LocalDateTime.now());
        Sprint sprint = new Sprint();
        sprint.setName("name");
        sprint.setTimeRange(timeRange);
        currentSprintId = sprintDAO.saveOrUpdate(sprint)
                                   .get()
                                   .getId();
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM StoryEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM SprintEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Story createStory() {
        Story story = new Story();
        story.setDefinitionOfDone("dod");
        story.setName("name");
        story.setPoints(1);
        story.setSprintId(currentSprintId);
        return story;
    }

    private List<StoryEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM StoryEntity d")
                            .getResultList();
    }
}