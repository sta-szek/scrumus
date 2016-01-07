package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.CommentEntity;
import edu.piotrjonski.scrumus.domain.Comment;
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
public class CommentDAOIT {

    public static final String COMMENT_BODY = "commentbody";
    public static final LocalDateTime CREATION_DATE = LocalDateTime.now();
    public static int nextUniqueValue = 1;

    @Inject
    private CommentDAO commentDAO;

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
        Comment comment = createComment();

        // when
        commentDAO.saveOrUpdate(comment);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        Comment comment = createComment();
        int entityId = commentDAO.saveOrUpdate(comment)
                                 .get()
                                 .getId();

        // when
        boolean result = commentDAO.exist(entityId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = commentDAO.exist(1);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldUpdate() {
        // given
        String commentBody = "updatedBody";
        Comment comment = createComment();
        comment = commentDAO.mapToDomainModelIfNotNull(entityManager.merge(commentDAO.mapToDatabaseModelIfNotNull(
                comment)));
        comment.setCommentBody(commentBody);

        // when
        comment = commentDAO.saveOrUpdate(comment)
                            .get();

        // then
        assertThat(comment.getCommentBody()).isEqualTo(commentBody);
    }

    @Test
    public void shouldDelete() {
        // given
        Comment comment = createComment();
        comment = commentDAO.mapToDomainModelIfNotNull(entityManager.merge(commentDAO.mapToDatabaseModelIfNotNull(
                comment)));

        // when
        commentDAO.delete(comment.getId());
        int allComments = findAll().size();

        // then
        assertThat(allComments).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Comment comment1 = createComment();
        Comment comment2 = createComment();
        Comment comment3 = createComment();

        int id1 = entityManager.merge(commentDAO.mapToDatabaseModelIfNotNull(comment1))
                               .getId();
        int id2 = entityManager.merge(commentDAO.mapToDatabaseModelIfNotNull(comment2))
                               .getId();
        int id3 = entityManager.merge(commentDAO.mapToDatabaseModelIfNotNull(comment3))
                               .getId();

        comment1.setId(id1);
        comment2.setId(id2);
        comment3.setId(id3);

        // when
        List<Comment> comments = commentDAO.findAll();

        // then
        assertThat(comments).hasSize(3)
                            .contains(comment1)
                            .contains(comment2)
                            .contains(comment3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Comment comment1 = createComment();
        Comment comment2 = createComment();
        int id = entityManager.merge(commentDAO.mapToDatabaseModelIfNotNull(comment1))
                              .getId();
        entityManager.merge(commentDAO.mapToDatabaseModelIfNotNull(comment2));
        comment1.setId(id);

        // when
        Comment comment = commentDAO.findById(id)
                                    .get();

        // then
        assertThat(comment).isEqualTo(comment1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Comment> user = commentDAO.findById(0);

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
        entityManager.createQuery("DELETE FROM CommentEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Comment createComment() {
        Comment comment = new Comment();
        comment.setCommentBody(COMMENT_BODY + nextUniqueValue);
        comment.setCreationDate(CREATION_DATE);
        nextUniqueValue++;
        return comment;
    }

    private List<CommentEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM CommentEntity d")
                            .getResultList();
    }
}