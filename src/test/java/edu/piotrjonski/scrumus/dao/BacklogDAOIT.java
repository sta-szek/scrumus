package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.domain.Backlog;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Issue;
import edu.piotrjonski.scrumus.domain.IssueType;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class BacklogDAOIT {

    @Inject
    private BacklogDAO backlogDAO;

    @Inject
    private IssueDAO issueDAO;

    @Inject
    private IssueTypeDAO issueTypeDAO;

    @Inject
    private DeveloperDAO developerDAO;

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
        Backlog backlog = createBacklog();

        // when
        backlogDAO.saveOrUpdate(backlog);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldUpdate() {
        // given
        IssueType issueType = new IssueType(1, "task");

        Issue issue = new Issue();
        issue.setKey("testkey");
        issue.setSummary("summary");
        issue.setIssueType(issueType);

        Developer developer = new Developer(1, "firstname", "a", "b", "c");
        developer = developerDAO.saveOrUpdate(developer)
                                .get();

        issue.setReporterId(developer.getId());

        issueTypeDAO.saveOrUpdate(issueType);
        issue = issueDAO.saveOrUpdate(issue)
                        .get();

        Backlog backlog = createBacklog();
        backlog = backlogDAO.mapToDomainModelIfNotNull(entityManager.merge(backlogDAO.mapToDatabaseModelIfNotNull(
                backlog)));
        backlog.setIssues(Lists.newArrayList(issue));

        // when
        backlog = backlogDAO.saveOrUpdate(backlog)
                            .get();

        // then
        assertThat(backlog.getIssues()).contains(issue);
    }

    @Test
    public void shouldDelete() {
        // given
        Backlog backlog = createBacklog();
        backlog = backlogDAO.mapToDomainModelIfNotNull(entityManager.merge(backlogDAO.mapToDatabaseModelIfNotNull(
                backlog)));

        // when
        backlogDAO.delete(backlog.getId());
        int allBacklogs = findAll().size();

        // then
        assertThat(allBacklogs).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Backlog backlog1 = createBacklog();
        Backlog backlog2 = createBacklog();
        Backlog backlog3 = createBacklog();

        int id1 = entityManager.merge(backlogDAO.mapToDatabaseModelIfNotNull(backlog1))
                               .getId();
        int id2 = entityManager.merge(backlogDAO.mapToDatabaseModelIfNotNull(backlog2))
                               .getId();
        int id3 = entityManager.merge(backlogDAO.mapToDatabaseModelIfNotNull(backlog3))
                               .getId();

        backlog1.setId(id1);
        backlog2.setId(id2);
        backlog3.setId(id3);

        // when
        List<Backlog> backlogs = backlogDAO.findAll();

        // then
        assertThat(backlogs).hasSize(3)
                            .contains(backlog1)
                            .contains(backlog2)
                            .contains(backlog3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Backlog backlog1 = createBacklog();
        Backlog backlog2 = createBacklog();
        int id = entityManager.merge(backlogDAO.mapToDatabaseModelIfNotNull(backlog1))
                              .getId();
        entityManager.merge(backlogDAO.mapToDatabaseModelIfNotNull(backlog2));
        backlog1.setId(id);

        // when
        Backlog backlog = backlogDAO.findByKey(id)
                                    .get();

        // then
        assertThat(backlog).isEqualTo(backlog1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Backlog> user = backlogDAO.findByKey(0);

        // then
        assertThat(user).isEmpty();
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM BacklogEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM IssueEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM IssueTypeEntity ")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM DeveloperEntity ")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Backlog createBacklog() {
        return new Backlog();
    }

    private List<Backlog> findAll() {
        return entityManager.createQuery("SELECT d FROM BacklogEntity d")
                            .getResultList();
    }
}