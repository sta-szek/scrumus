package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.*;
import edu.piotrjonski.scrumus.domain.*;
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
public class IssueManagerIT {

    public static final String PROJECT_KEY = "projKey";
    public static int nextUniqueValue = 0;

    private Project lastProject;
    private Developer lastDeveloper;
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
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return UtilsTest.createDeployment();
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
        Issue savedIssue = issueManager.create(issue, lastProject);
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
        Issue savedIssue = issueManager.create(issue, lastProject);

        // when
        Throwable throwable = catchThrowable(() -> issueManager.create(savedIssue, lastProject));

        // then
        assertThat(throwable).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void shouldChangePriorityIfUserIsProductOwner() {
        // given
        Priority priority = new Priority();
        priority.setName("changedPriority");
        Issue issue = createIssue();

        priority = priorityDAO.saveOrUpdate(priority)
                              .get();
        issue = issueDAO.saveOrUpdate(issue)
                        .get();

        ProductOwner productOwner = new ProductOwner();
        productOwner.setDeveloper(lastDeveloper);
        productOwner.setProject(lastProject);

        productOwnerDAO.saveOrUpdate(productOwner);

        // when
        issueManager.changePriority(issue, priority, lastDeveloper);
        String result = issueDAO.findById(issue.getId())
                                .get()
                                .getPriority()
                                .getName();

        // then
        assertThat(result).isEqualTo(priority.getName());
    }

    @Test
    public void shouldUpdateIssue() throws Exception {
        // given
        Issue issue = createIssue();
        issue = issueManager.create(issue, lastProject);

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

    private Developer createDeveloper() {
        Developer developer = new Developer();
        developer.setEmail("email");
        developer.setFirstName("email");
        developer.setSurname("email");
        developer.setUsername("email");
        return developer;
    }

    private void startTransaction() throws SystemException, NotSupportedException, AlreadyExistException {
        userTransaction.begin();
        entityManager.joinTransaction();
        Developer developer = createDeveloper();
        Project project = createProject();
        IssueType issueType = createIssueType();
        Priority priority = createPriority();

        lastIssueType = issueTypeDAO.saveOrUpdate(issueType)
                                    .get();
        lastDeveloper = userManager.create(developer);
        lastProject = projectManager.create(project);
        lastPriority = priorityDAO.saveOrUpdate(priority)
                                  .get();
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

    private Project createProject() {
        Project project = new Project();
        project.setCreationDate(LocalDateTime.now());
        project.setKey(PROJECT_KEY);
        project.setName("NAME");
        return project;
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
        entityManager.createQuery("DELETE FROM DeveloperEntity ")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Issue createIssue() {
        Issue issue = new Issue();
        issue.setAssigneeId(lastDeveloper.getId());
        issue.setProjectKey(PROJECT_KEY);
        issue.setReporterId(lastDeveloper.getId());
        issue.setIssueType(lastIssueType);
        issue.setPriority(lastPriority);
        issue.setSummary("summary");
        nextUniqueValue++;
        return issue;
    }

}