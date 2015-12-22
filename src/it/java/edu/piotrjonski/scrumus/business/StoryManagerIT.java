package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.ProjectDAO;
import edu.piotrjonski.scrumus.dao.SprintDAO;
import edu.piotrjonski.scrumus.dao.StoryDAO;
import edu.piotrjonski.scrumus.dao.model.project.TimeRange;
import edu.piotrjonski.scrumus.domain.Project;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(Arquillian.class)
public class StoryManagerIT {
    public static final String NAME = "name";
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final String DESCRIPTION = "Descritpion";
    public static int nextUniqueValue = 0;
    private Project lastProject;
    private Sprint lastSprint;

    @Inject
    private StoryDAO storyDAO;

    @Inject
    private SprintDAO sprintDAO;

    @Inject
    private StoryManager storyManager;

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
    public void dropAllStoriesAndStartTransaction() throws Exception {
        clearData();
        startTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        userTransaction.commit();
    }

    @Test
    public void shouldCreateStory() throws AlreadyExistException {
        // given
        Story story = createStory();

        // when
        Story savedStory = storyManager.createStory(story);
        boolean result = storyDAO.exist(savedStory.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldThrowExceptionIfSprintAlreadyExist() throws AlreadyExistException {
        // given
        Story story = createStory();
        Story savedStory = storyManager.createStory(story);

        // when
        Throwable result = catchThrowable(() -> storyManager.createStory(savedStory));

        // then
        assertThat(result).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void shouldFindStory() throws AlreadyExistException {
        // given
        Story story = createStory();
        Story savedStory = storyManager.createStory(story);

        // when
        Story result = storyManager.findStory(savedStory.getId());

        // then
        assertThat(result).isEqualTo(savedStory);
    }

    @Test
    public void shouldFindStoryWithNoIdIfStoryDoesNotExist() throws AlreadyExistException {
        // given

        // when
        Story result = storyManager.findStory(0);

        // then
        assertThat(result.getId()).isEqualTo(0);
    }

    @Test
    public void shouldFindAllStoriesForProject() throws AlreadyExistException {
        // given
        Sprint sprint = createSprint();
        sprint = sprintDAO.saveOrUpdate(sprint)
                          .get();
        Story story1 = createStory();
        Story story2 = createStory();
        Story story3 = createStory();
        story3.setSprintId(sprint.getId());
        story1 = storyManager.createStory(story1);
        story2 = storyManager.createStory(story2);
        story3 = storyManager.createStory(story3);

        // when
        List<Story> result = storyManager.findAllStoriesForSprint(lastSprint.getId());

        // then
        assertThat(result).hasSize(2)
                          .contains(story1)
                          .contains(story2)
                          .doesNotContain(story3);
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
        entityManager.createQuery("DELETE FROM StoryEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM SprintEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM ProjectEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Story createStory() {
        Story story = new Story();
        story.setDefinitionOfDone("dod");
        story.setName("name" + nextUniqueValue);
        story.setSprintId(lastSprint.getId());
        TimeRange timeRange = new TimeRange();
        timeRange.setEndDate(LocalDateTime.now());
        timeRange.setStartDate(LocalDateTime.now());
        nextUniqueValue++;
        return story;
    }

    private Sprint createSprint() {
        Sprint sprint = new Sprint();
        sprint.setDefinitionOfDone("dod");
        sprint.setName("name" + nextUniqueValue);
        sprint.setProjectKey(lastProject.getKey());
        TimeRange timeRange = new TimeRange();
        timeRange.setEndDate(LocalDateTime.now());
        timeRange.setStartDate(LocalDateTime.now());
        sprint.setTimeRange(timeRange);
        nextUniqueValue++;
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