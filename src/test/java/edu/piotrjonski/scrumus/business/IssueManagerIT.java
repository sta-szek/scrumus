package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.*;
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
public class IssueManagerIT {

    public static final String PROJECT_KEY = "projKey";
    public static int nextUniqueValue = 0;

    private Project lastProject;
    private Developer lastDeveloper;
    private State lastState;
    private IssueType lastIssueType;
    private Priority lastPriority;

    @Inject
    private ProjectManager projectManager;

    @Inject
    private IssueDAO issueDAO;

    @Inject
    private IssueTypeDAO issueTypeDAO;

    @Inject
    private BacklogDAO backlogDAO;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private UserManager userManager;

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private ProductOwnerDAO productOwnerDAO;

    @Inject
    private IssueManager issueManager;

    @Inject
    private PriorityDAO priorityDAO;

    @Inject
    private StateDAO stateDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return TestUtils.createDeployment();
    }

    @Before
    public void dropAllIssuesAndStartTransaction() throws Exception {
        clearData();
        startTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        userTransaction.commit();
    }

    @Test
    public void shouldCreateIssue() throws Exception {
        // given
        Issue issue = createIssue();

        // when
        Issue savedIssue = issueManager.create(issue, lastProject)
                                       .get();
        boolean result = issueDAO.exist(savedIssue.getId());
        List<Issue> issues = backlogDAO.findBacklogForProject(lastProject.getKey())
                                       .get()
                                       .getIssues();


        // then
        assertThat(result).isTrue();
        assertThat(savedIssue.getProjectKey()).isEqualTo(PROJECT_KEY);
        assertThat(issues).contains(savedIssue);
    }

    @Test
    public void shouldThrowExceptionWhenIssueAlreadyExist() throws Exception {
        // given
        Issue issue = createIssue();
        Issue savedIssue = issueManager.create(issue, lastProject)
                                       .get();

        // when
        Throwable throwable = catchThrowable(() -> issueManager.create(savedIssue, lastProject));

        // then
        assertThat(throwable).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void shouldUpdateIssue() throws Exception {
        // given
        Issue issue = createIssue();
        issue = issueManager.create(issue, lastProject)
                            .get();

        String newDesc = "newDesc";
        String dod = "dod";
        String summary = "summary";
        issue.setDescription(newDesc);
        issue.setDefinitionOfDone(dod);
        issue.setSummary(summary);

        // when
        Issue result = issueManager.update(issue)
                                   .get();

        // then
        assertThat(result.getDescription()).isEqualTo(newDesc);
        assertThat(result.getDefinitionOfDone()).isEqualTo(dod);
        assertThat(result.getSummary()).isEqualTo(summary);
    }

    @Test
    public void shouldCreatePriorityIfNotExist() throws AlreadyExistException {
        // given
        String name = "Major";
        Priority priority = new Priority();
        priority.setName(name);

        // when
        Priority result = issueManager.createPriority(priority)
                                      .get();

        // then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getId()).isNotZero();
    }

    @Test
    public void shouldThrowExceptionIfPriorityAlreadyExist() {
        // given
        Priority priority = new Priority();
        priority.setName("name");
        Priority savedPriority = priorityDAO.saveOrUpdate(priority)
                                            .get();

        // when
        Throwable throwable = catchThrowable(() -> issueManager.createPriority(savedPriority));

        // then
        assertThat(throwable).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void shouldCreateStatusIfNotExist() throws AlreadyExistException {
        // given
        String name = "To Do";
        State state = new State();
        state.setName(name);

        // when
        State result = issueManager.createState(state)
                                   .get();

        // then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getId()).isNotZero();
    }

    @Test
    public void shouldThrowExceptionIfStatusAlreadyExist() {
        // given
        State state = new State();
        state.setName("state");
        State savedState = stateDAO.saveOrUpdate(state)
                                   .get();

        // when
        Throwable throwable = catchThrowable(() -> issueManager.createState(savedState));

        // then
        assertThat(throwable).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void shouldDeletePriorityIfExist() throws NotExistException {
        // given
        Priority priority = new Priority();
        priority.setName("name");
        priority = priorityDAO.saveOrUpdate(priority)
                              .get();

        // when
        issueManager.deletePriority(priority);
        Optional<Priority> result = priorityDAO.findById(priority.getId());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldThrowExceptionIfPriorityDoesNotExistWhenDelete() {
        // given

        // when
        Throwable throwable = catchThrowable(() -> issueManager.deletePriority(new Priority()));

        // then
        assertThat(throwable).isInstanceOf(NotExistException.class);
    }

    @Test
    public void shouldDeleteStateIfExist() throws NotExistException {
        // given
        State state = new State();
        state.setName("name");
        state = stateDAO.saveOrUpdate(state)
                        .get();

        // when
        issueManager.deleteState(state);
        Optional<State> result = stateDAO.findById(state.getId());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldThrowExceptionIfStateDoesNotExistWhenDelete() {
        // given

        // when
        Throwable throwable = catchThrowable(() -> issueManager.deleteState(createState()));

        // then
        assertThat(throwable).isInstanceOf(NotExistException.class);
    }

    @Test
    public void shouldThrowExceptionIfStateDoesNotExistWhenEdit() {
        // given

        // when
        Throwable throwable = catchThrowable(() -> issueManager.updateState(new State()));

        // then
        assertThat(throwable).isInstanceOf(NotExistException.class);
    }

    @Test
    public void shouldThrowExceptionIfPriorityDoesNotExistWhenEdit() {
        // given

        // when
        Throwable throwable = catchThrowable(() -> issueManager.updatePriority(new Priority()));

        // then
        assertThat(throwable).isInstanceOf(NotExistException.class);
    }

    @Test
    public void shouldEditState() throws NotExistException {
        // given
        State state = new State();
        state.setName("oldName");
        state = stateDAO.saveOrUpdate(state)
                        .get();
        String newName = "newName";
        state.setName(newName);

        // when
        State result = issueManager.updateState(state)
                                   .get();

        // then
        assertThat(result.getName()).isEqualTo(newName);
    }

    @Test
    public void shouldEditPriority() throws NotExistException {
        // given
        Priority priority = new Priority();
        priority.setName("oldName");
        priority = priorityDAO.saveOrUpdate(priority)
                              .get();
        String newName = "newName";
        priority.setName(newName);

        // when
        Priority result = issueManager.updatePriority(priority)
                                      .get();

        // then
        assertThat(result.getName()).isEqualTo(newName);
    }

    private void startTransaction()
            throws SystemException, NotSupportedException, AlreadyExistException, UnsupportedEncodingException, NoSuchAlgorithmException {
        userTransaction.begin();
        entityManager.joinTransaction();
        Developer developer = createDeveloper();
        Project project = createProject();
        IssueType issueType = createIssueType();
        Priority priority = createPriority();

        lastIssueType = issueTypeDAO.saveOrUpdate(issueType)
                                    .get();
        lastDeveloper = userManager.create(developer)
                                   .get();
        lastProject = projectManager.create(project)
                                    .get();
        lastPriority = priorityDAO.saveOrUpdate(priority)
                                  .get();
        lastState = stateDAO.saveOrUpdate(createState())
                            .get();
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

    private State createState() {
        State state = new State();
        state.setName("name" + nextUniqueValue);
        return state;
    }

    private Project createProject() {
        Project project = new Project();
        project.setCreationDate(LocalDateTime.now());
        project.setKey(PROJECT_KEY);
        project.setName("NAME");
        return project;
    }

    private Issue createIssue() {
        Issue issue = new Issue();
        issue.setAssigneeId(lastDeveloper.getId());
        issue.setProjectKey(PROJECT_KEY);
        issue.setReporterId(lastDeveloper.getId());
        issue.setIssueType(lastIssueType);
        issue.setPriority(lastPriority);
        issue.setSummary("summary");
        issue.setState(lastState);
        nextUniqueValue++;
        return issue;
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM ProductOwnerEntity")
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

}