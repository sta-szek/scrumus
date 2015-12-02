package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.TimeRange;
import edu.piotrjonski.scrumus.domain.Sprint;
import edu.piotrjonski.scrumus.domain.Story;
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
        Story story = createStory();

        // when
        storyDAO.saveOrUpdate(story);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldFindStoriesForSprint() {
        // given
        Story story1 = new Story(0, "name1", "dod1", 1, null, currentSprintId);
        Story story2 = new Story(0, "name2", "dod2", 2, null, currentSprintId);
        Story story3 = new Story(0, "name3", "dod3", 3, null, currentSprintId);
        Story story4 = new Story(0, "name4", "dod4", 4, null, currentSprintId);

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
        Story story = storyDAO.findByKey(id)
                              .get();

        // then
        assertThat(story).isEqualTo(story1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Story> user = storyDAO.findByKey(0);

        // then
        assertThat(user).isEmpty();
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
        TimeRange timeRange = new TimeRange();
        timeRange.setEndDate(LocalDateTime.now());
        timeRange.setStartDate(LocalDateTime.now());
        Sprint sprint = new Sprint(0, "name", "dod", timeRange, 0);
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

    private List<Story> findAll() {
        return entityManager.createQuery("SELECT d FROM StoryEntity d")
                            .getResultList();
    }
}