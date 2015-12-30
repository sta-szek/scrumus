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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class CommentManagerIT {

    public static final String COMMENT_BODY = "projKey";

    private Developer lastDeveloper;
    private Retrospective lastRetrospective;
    private IssueType lastIssueType;
    private Issue lastIssue;
    private Priority lastPriority;
    private State lastState;

    @Inject
    private PriorityDAO priorityDAO;

    @Inject
    private StateDAO stateDAO;

    @Inject
    private IssueTypeDAO issueTypeDAO;

    @Inject
    private IssueDAO issueDAO;

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private CommentManager commentManager;

    @Inject
    private RetrospectiveDAO retrospectiveDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return UtilsTest.createDeployment();
    }

    @Before
    public void dropAllCommentsAndStartTransaction() throws Exception {
        clearData();
        startTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        userTransaction.commit();
    }

    @Test
    public void shouldAddCommentToIssue() {
        // given
        Comment comment = createComment();

        // when
        Comment savedComment = commentManager.addCommentToIssue(comment, lastIssue)
                                             .get();
        List<Comment> result = issueDAO.findById(lastIssue.getId())
                                       .get()
                                       .getComments();

        // then
        assertThat(result).contains(savedComment);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenIssueDoesNotExist() {
        // given

        // when
        Optional<Comment> result = commentManager.addCommentToIssue(createComment(), createIssue());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldRemoveCommentFromIssue() {
        // given
        Comment comment = createComment();
        Comment savedComment = commentManager.addCommentToIssue(comment, lastIssue)
                                             .get();

        // when
        commentManager.removeCommentFromIssue(savedComment, lastIssue);
        List<Comment> result = issueDAO.findById(lastIssue.getId())
                                       .get()
                                       .getComments();

        // then
        assertThat(result).doesNotContain(savedComment);
    }

    @Test
    public void shouldAddCommentToRetrospective() {
        // given
        Comment comment = createComment();

        // when
        Comment savedComment = commentManager.addCommentToRetrospective(comment, lastRetrospective)
                                             .get();
        List<Comment> result = retrospectiveDAO.findById(lastRetrospective.getId())
                                               .get()
                                               .getComments();

        // then
        assertThat(result).contains(savedComment);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenRetrospectiveDoesNotExist() {
        // given

        // when
        Optional<Comment> result = commentManager.addCommentToRetrospective(createComment(), createRetrospective());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldRemoveCommentFromRetrospective() {
        // given
        Comment comment = createComment();
        Comment savedComment = commentManager.addCommentToRetrospective(comment, lastRetrospective)
                                             .get();

        // when
        commentManager.removeCommentFromRetrospective(savedComment, lastRetrospective);
        List<Comment> result = retrospectiveDAO.findById(lastRetrospective.getId())
                                               .get()
                                               .getComments();

        // then
        assertThat(result).doesNotContain(savedComment);
    }

    private Retrospective createRetrospective() {
        Retrospective retrospective = new Retrospective();
        retrospective.setDescription("desc");
        return retrospective;
    }

    private void startTransaction() throws SystemException, NotSupportedException, AlreadyExistException {
        userTransaction.begin();
        entityManager.joinTransaction();

        lastDeveloper = developerDAO.saveOrUpdate(createDeveloper())
                                    .get();
        lastPriority = priorityDAO.saveOrUpdate(createPriority())
                                  .get();
        lastState = stateDAO.saveOrUpdate(createState())
                            .get();
        lastIssueType = issueTypeDAO.saveOrUpdate(createIssueType())
                                    .get();
        lastRetrospective = retrospectiveDAO.saveOrUpdate(createRetrospective())
                                            .get();
        lastIssue = issueDAO.saveOrUpdate(createIssue())
                            .get();
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM RetrospectiveEntity ")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM IssueEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM IssueTypeEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM PriorityEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM StateEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM CommentEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM DeveloperEntity ")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Developer createDeveloper() {
        Developer developer = new Developer();
        developer.setEmail("email");
        developer.setFirstName("email");
        developer.setSurname("email");
        developer.setUsername("email");
        return developer;
    }

    private Comment createComment() {
        Comment comment = new Comment();
        comment.setDeveloperId(lastDeveloper.getId());
        comment.setCommentBody(COMMENT_BODY);
        comment.setCreationDate(LocalDateTime.now());
        return comment;
    }

    private Issue createIssue() {
        Issue issue = new Issue();
        issue.setAssigneeId(lastDeveloper.getId());
        issue.setProjectKey("key");
        issue.setReporterId(lastDeveloper.getId());
        issue.setIssueType(lastIssueType);
        issue.setPriority(lastPriority);
        issue.setState(lastState);
        issue.setSummary("summary");
        return issue;
    }

    private IssueType createIssueType() {
        IssueType issueType = new IssueType();
        issueType.setName("task");
        return issueType;
    }

    private Priority createPriority() {
        Priority priority = new Priority();
        priority.setName("name");
        return priority;
    }

    private State createState() {
        State state = new State();
        state.setName("name");
        return state;
    }

}