package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.IssueEntity;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class IssueDAOIT {

    public static final String SUMMARY = "abcdge";
    public static final String KEY = "abcdge";
    public static final String DESCRIPTION = "abcdge";
    public static final String DOD = "abcdge";
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static int currentlySavedDeveloperId = 1;
    public static int nextUniqueValue = 0;

    @Inject
    private IssueDAO issueDAO;

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private IssueTypeDAO issueTypeDAO;

    @Inject
    private StateDAO stateDAO;

    @Inject
    private PriorityDAO priorityDAO;

    @Inject
    private UserTransaction userTransaction;

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
        Issue issue = createIssue();

        // when
        issueDAO.saveOrUpdate(issue);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        Issue issue = createIssue();
        int entityId = issueDAO.saveOrUpdate(issue)
                               .get()
                               .getId();

        // when
        boolean result = issueDAO.exist(entityId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = issueDAO.exist(1);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldUpdate() {
        // given
        String updatedName = "UpdatedName";
        Issue issue = createIssue();
        issue = issueDAO.mapToDomainModelIfNotNull(entityManager.merge(issueDAO.mapToDatabaseModelIfNotNull(
                issue)));
        issue.setDescription(updatedName);

        // when
        issue = issueDAO.saveOrUpdate(issue)
                        .get();

        // then
        assertThat(issue.getDescription()).isEqualTo(updatedName);
    }

    @Test
    public void shouldDelete() {
        // given
        Issue issue = createIssue();
        issue = issueDAO.mapToDomainModelIfNotNull(entityManager.merge(issueDAO.mapToDatabaseModelIfNotNull(
                issue)));

        // when
        issueDAO.delete(issue.getId());
        int allUsers = findAll().size();

        // then
        assertThat(allUsers).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Issue issue1 = createIssue();
        Issue issue2 = createIssue();
        Issue issue3 = createIssue();

        int id1 = entityManager.merge(issueDAO.mapToDatabaseModelIfNotNull(issue1))
                               .getId();
        int id2 = entityManager.merge(issueDAO.mapToDatabaseModelIfNotNull(issue2))
                               .getId();
        int id3 = entityManager.merge(issueDAO.mapToDatabaseModelIfNotNull(issue3))
                               .getId();

        issue1.setId(id1);
        issue2.setId(id2);
        issue3.setId(id3);

        // when
        List<Issue> issues = issueDAO.findAll();

        // then
        assertThat(issues).hasSize(3)
                          .contains(issue1)
                          .contains(issue2)
                          .contains(issue3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Issue issue1 = createIssue();
        Issue issue2 = createIssue();
        int id = entityManager.merge(issueDAO.mapToDatabaseModelIfNotNull(issue1))
                              .getId();
        entityManager.merge(issueDAO.mapToDatabaseModelIfNotNull(issue2));
        issue1.setId(id);

        // when
        Issue issue = issueDAO.findById(id)
                              .get();

        // then
        assertThat(issue).isEqualTo(issue1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Issue> user = issueDAO.findById(0);

        // then
        assertThat(user).isEmpty();
    }

    @Test
    public void shouldDeleteIssuesFromProject() {
        // given
        Issue issue1 = createIssue();
        Issue issue2 = createIssue();
        Issue issue3 = createIssue();
        issue3.setProjectKey("a");
        issueDAO.saveOrUpdate(issue1);
        issueDAO.saveOrUpdate(issue2);
        issueDAO.saveOrUpdate(issue3);

        // when
        issueDAO.deleteIssuesFromProject(issue1.getProjectKey());
        List<IssueEntity> result = findAll();

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    public void shouldFindAllIssuesWithGivenIssueType() {
        // given
        Issue issue1 = createIssue();
        Issue issue2 = createIssue();
        issueDAO.saveOrUpdate(issue1);
        issueDAO.saveOrUpdate(issue2);

        // when
        List<Issue> result = issueDAO.findAllIssuesWithIssueType(issue1.getIssueType()
                                                                       .getName());

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    public void shouldFindAllIssuesWithGivenState() {
        // given
        Issue issue1 = createIssue();
        Issue issue2 = createIssue();
        issueDAO.saveOrUpdate(issue1);
        issueDAO.saveOrUpdate(issue2);

        // when
        List<Issue> result = issueDAO.findAllIssuesWithState(issue1.getState()
                                                                   .getName());

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    public void shouldFindAllIssuesWithGivenPriority() {
        // given
        Issue issue1 = createIssue();
        Issue issue2 = createIssue();
        issueDAO.saveOrUpdate(issue1);
        issueDAO.saveOrUpdate(issue2);

        // when
        List<Issue> result = issueDAO.findAllIssuesWithIssueType(issue1.getPriority()
                                                                       .getName());

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    public void shouldReturnTrueIfIssueTypeIsInUse() {
        // given
        Issue issue = createIssue();
        issueDAO.saveOrUpdate(issue);

        // when
        boolean result = issueDAO.isIssueTypeInUse(issue.getIssueType()
                                                        .getName());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfIssueTypeIsNotInUse() {
        // given
        Issue issue = createIssue();
        issueDAO.saveOrUpdate(issue);

        // when
        boolean result = issueDAO.isIssueTypeInUse("x");

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnTrueIfStateIsInUse() {
        // given
        Issue issue = createIssue();
        issueDAO.saveOrUpdate(issue);

        // when
        boolean result = issueDAO.isStateInUse(issue.getState()
                                                    .getName());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfStateIsNotInUse() {
        // given
        Issue issue = createIssue();
        issueDAO.saveOrUpdate(issue);

        // when
        boolean result = issueDAO.isStateInUse("x");

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnTrueIfPriorityIsInUse() {
        // given
        Issue issue = createIssue();
        issueDAO.saveOrUpdate(issue);

        // when
        boolean result = issueDAO.isPriorityInUse(issue.getPriority()
                                                       .getName());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfPriorityIsNotInUse() {
        // given
        Issue issue = createIssue();
        issueDAO.saveOrUpdate(issue);

        // when
        boolean result = issueDAO.isPriorityInUse("x");

        // then
        assertThat(result).isFalse();
    }


    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
        Developer developer = new Developer();
        developer.setEmail("Email" + nextUniqueValue);
        developer.setFirstName("firstname" + nextUniqueValue);
        developer.setSurname("surname" + nextUniqueValue);
        developer.setUsername("username" + nextUniqueValue);
        currentlySavedDeveloperId = developerDAO.saveOrUpdate(developer)
                                                .get()
                                                .getId();

        IssueType issueType = new IssueType();
        issueType.setId(0);
        issueType.setName("name" + nextUniqueValue);

        Priority priority = new Priority();
        priority.setName("name" + nextUniqueValue);

        State state = new State();
        state.setName("name" + nextUniqueValue);

        priorityDAO.saveOrUpdate(priority);
        stateDAO.saveOrUpdate(state);
        issueTypeDAO.saveOrUpdate(issueType);
        nextUniqueValue++;
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM IssueEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM DeveloperEntity ")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Issue createIssue() {
        Issue issue = new Issue();
        issue.setCreationDate(NOW);
        issue.setProjectKey(KEY);
        issue.setDescription(DESCRIPTION);
        issue.setDefinitionOfDone(DOD);
        IssueType issueType = new IssueType();
        issueType.setId(1);
        issueType.setName("name0");
        Priority priority = new Priority();
        priority.setId(1);
        priority.setName("name0");
        State state = new State();
        state.setId(1);
        state.setName("name0");
        issue.setPriority(priority);
        issue.setIssueType(issueType);
        issue.setReporterId(currentlySavedDeveloperId);
        issue.setAssigneeId(currentlySavedDeveloperId);
        issue.setSummary(SUMMARY + nextUniqueValue);
        issue.setState(state);
        nextUniqueValue++;
        return issue;
    }

    private List<IssueEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM IssueEntity d")
                            .getResultList();
    }
}