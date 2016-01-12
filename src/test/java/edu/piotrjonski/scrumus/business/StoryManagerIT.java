package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.*;
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
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(Arquillian.class)
public class StoryManagerIT {
    public static final String NAME = "name";
    public static final String PROJECT_KEY = "projKey";
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final String DESCRIPTION = "Descritpion";
    public static int nextUniqueValue = 0;
    private Sprint lastSprint;
    private Project lastProject;
    private Developer lastDeveloper;
    private Backlog lastBacklog;
    private Issue lastIssue;
    private State lastState;

    private IssueType lastIssueType;

    private Priority lastPriority;

    @Inject
    private StoryDAO storyDAO;

    @Inject
    private SprintDAO sprintDAO;

    @Inject
    private StoryManager storyManager;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private BacklogDAO backlogDAO;

    @Inject
    private PriorityDAO priorityDAO;

    @Inject
    private StateDAO stateDAO;

    @Inject
    private IssueDAO issueDAO;

    @Inject
    private IssueTypeDAO issueTypeDAO;

    @Inject
    private UserManager userManager;

    @Inject
    private ProjectManager projectManager;
    @Inject
    private UserTransaction userTransaction;
    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return TestUtils.createDeployment();
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
        Story savedStory = storyManager.createStory(story)
                                       .get();
        boolean result = storyDAO.exist(savedStory.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldThrowExceptionIfSprintAlreadyExist() throws AlreadyExistException {
        // given
        Story story = createStory();
        Story savedStory = storyManager.createStory(story)
                                       .get();

        // when
        Throwable result = catchThrowable(() -> storyManager.createStory(savedStory));

        // then
        assertThat(result).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void shouldFindStory() throws AlreadyExistException {
        // given
        Story story = createStory();
        Story savedStory = storyManager.createStory(story)
                                       .get();

        // when
        Story result = storyManager.findStory(savedStory.getId())
                                   .get();

        // then
        assertThat(result).isEqualTo(savedStory);
    }

    @Test
    public void shouldFindStoryWithNoIdIfStoryDoesNotExist() throws AlreadyExistException {
        // given

        // when
        Optional<Story> result = storyManager.findStory(0);

        // then
        assertThat(result).isEmpty();
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
        story1 = storyManager.createStory(story1)
                             .get();
        story2 = storyManager.createStory(story2)
                             .get();
        story3 = storyManager.createStory(story3)
                             .get();

        // when
        List<Story> result = storyManager.findAllStoriesForSprint(lastSprint.getId());

        // then
        assertThat(result).hasSize(2)
                          .contains(story1)
                          .contains(story2)
                          .doesNotContain(story3);
    }

    @Test
    public void shouldAddIssueToStoryAndRemoveFromBacklog() throws AlreadyExistException {
        // given
        Story story = storyManager.createStory(createStory())
                                  .get();
        lastBacklog.addIssue(lastIssue);
        backlogDAO.saveOrUpdate(lastBacklog);

        // when
        storyManager.addIssueToStory(lastIssue, story);
        List<Issue> storyIssues = storyManager.findStory(story.getId())
                                              .get()
                                              .getIssues();
        List<Issue> backlogIssues = backlogDAO.findBacklogForProject(PROJECT_KEY)
                                              .get()
                                              .getIssues();

        // then
        assertThat(storyIssues).contains(lastIssue);
        assertThat(backlogIssues).doesNotContain(lastIssue);
    }

    @Test
    public void shouldRemoveIssueFromStoryAndAddToBacklog() throws AlreadyExistException {
        // given
        Story story = storyManager.createStory(createStory())
                                  .get();
        story.addIssue(lastIssue);
        storyDAO.saveOrUpdate(story);

        // when
        storyManager.removeIssueFromStory(lastIssue, story);
        List<Issue> storyIssues = storyManager.findStory(story.getId())
                                              .get()
                                              .getIssues();
        List<Issue> backlogIssues = backlogDAO.findBacklogForProject(PROJECT_KEY)
                                              .get()
                                              .getIssues();

        // then
        assertThat(storyIssues).doesNotContain(lastIssue);
        assertThat(backlogIssues).contains(lastIssue);
    }

    private void startTransaction()
            throws
            SystemException,
            NotSupportedException,
            AlreadyExistException,
            UnsupportedEncodingException,
            NoSuchAlgorithmException,
            CreateUserException {
        userTransaction.begin();
        entityManager.joinTransaction();
        lastIssueType = issueTypeDAO.saveOrUpdate(createIssueType())
                                    .get();
        lastDeveloper = userManager.create(createDeveloper())
                                   .get();
        lastProject = projectManager.create(createProject())
                                    .get();
        lastPriority = priorityDAO.saveOrUpdate(createPriority())
                                  .get();
        lastState = stateDAO.saveOrUpdate(createState())
                            .get();

        lastSprint = sprintDAO.saveOrUpdate(createSprint())
                              .get();
        lastIssue = issueDAO.saveOrUpdate(createIssue())
                            .get();
        lastBacklog = backlogDAO.findBacklogForProject(lastProject.getKey())
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
        entityManager.createQuery("DELETE FROM BacklogEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM IssueEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM IssueTypeEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM PriorityEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM StateEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM DeveloperEntity ")
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
        project.setKey(PROJECT_KEY);
        project.setDescription(DESCRIPTION + nextUniqueValue);
        nextUniqueValue++;
        return project;
    }

    private Developer createDeveloper() {
        Developer developer = new Developer();
        developer.setEmail("email");
        developer.setFirstName("email");
        developer.setSurname("email");
        developer.setUsername("email");
        return developer;
    }

    private IssueType createIssueType() {
        IssueType issueType = new IssueType();
        issueType.setName("task" + nextUniqueValue);
        return issueType;
    }

    private Priority createPriority() {
        Priority priority = new Priority();
        priority.setName("name" + nextUniqueValue);
        return priority;
    }

    private Issue createIssue() {
        Issue issue = new Issue();
        issue.setAssigneeId(lastDeveloper.getId());
        issue.setProjectKey(PROJECT_KEY);
        issue.setState(lastState);
        issue.setReporterId(lastDeveloper.getId());
        issue.setIssueType(lastIssueType);
        issue.setPriority(lastPriority);
        issue.setSummary("summary");
        nextUniqueValue++;
        return issue;
    }

    private State createState() {
        State state = new State();
        state.setName("name" + nextUniqueValue);
        return state;
    }
}