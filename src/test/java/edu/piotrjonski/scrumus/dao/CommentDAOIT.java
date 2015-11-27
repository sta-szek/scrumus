package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.domain.Comment;
import edu.piotrjonski.scrumus.domain.Developer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.gradle.archive.importer.embedded.EmbeddedGradleImporter;
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
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class CommentDAOIT {

    public static final String COMMENT_BODY = "commentbody";
    public static final int ID = 1;
    public static final int DEVELOPER_ID = 1;
    public static final LocalDateTime CREATION_DATE = LocalDateTime.now();
    public static int nextUniqueValue = 1;

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private CommentDAO commentDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(EmbeddedGradleImporter.class, "scrumus-arquillian-tests.war")
                         .forThisProjectDirectory()
                         .importBuildOutput()
                         .as(WebArchive.class);
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
    @Transactional
    public void shouldSave() {
        // given
        int developerId = developerDAO.saveOrUpdate(createDeveloper())
                                      .get()
                                      .getId();
        Comment comment = createComment();
        comment.setDeveloperId(developerId);

        // when
        commentDAO.saveOrUpdate(comment);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    @Transactional
    public void shouldUpdate() {
        // given
        String commentBody = "updatedBody";
        int developerId = developerDAO.saveOrUpdate(createDeveloper())
                                      .get()
                                      .getId();
        Comment comment = createComment();
        comment.setDeveloperId(developerId);
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
    @Transactional
    public void shouldDelete() {
        // given
        int developerId = developerDAO.saveOrUpdate(createDeveloper())
                                      .get()
                                      .getId();
        Comment comment = createComment();
        comment.setDeveloperId(developerId);
        comment = commentDAO.mapToDomainModelIfNotNull(entityManager.merge(commentDAO.mapToDatabaseModelIfNotNull(
                comment)));

        // when
        commentDAO.delete(comment.getId());
        int allComments = findAll().size();

        // then
        assertThat(allComments).isEqualTo(0);
    }

    @Test
    @Transactional
    public void shouldFindAll() {
        // given

        int developerId = developerDAO.saveOrUpdate(createDeveloper())
                                      .get()
                                      .getId();
        Comment comment1 = createComment();
        Comment comment2 = createComment();
        Comment comment3 = createComment();
        comment1.setDeveloperId(developerId);
        comment2.setDeveloperId(developerId);
        comment3.setDeveloperId(developerId);

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
    @Transactional
    public void shouldFindByKey() {
        // given
        int developerId = developerDAO.saveOrUpdate(createDeveloper())
                                      .get()
                                      .getId();
        Comment comment1 = createComment();
        Comment comment2 = createComment();
        comment1.setDeveloperId(developerId);
        comment2.setDeveloperId(developerId);
        int id = entityManager.merge(commentDAO.mapToDatabaseModelIfNotNull(comment1))
                              .getId();
        entityManager.merge(commentDAO.mapToDatabaseModelIfNotNull(comment2));
        comment1.setId(id);

        // when
        Comment comment = commentDAO.findByKey(id)
                                    .get();

        // then
        assertThat(comment).isEqualTo(comment1);
    }

    @Test
    @Transactional
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Comment> user = commentDAO.findByKey(0);

        // then
        assertThat(user).isEmpty();
    }

    private Developer createDeveloper() {
        Developer developer = new Developer();
        developer.setEmail("email" + nextUniqueValue);
        developer.setFirstName("firstname" + nextUniqueValue);
        developer.setSurname("surname" + nextUniqueValue);
        developer.setUsername("username" + nextUniqueValue);
        return developer;
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
        comment.setId(ID + nextUniqueValue);
        nextUniqueValue++;
        return comment;
    }

    private List<Comment> findAll() {
        return entityManager.createQuery("SELECT d FROM CommentEntity d")
                            .getResultList();
    }
}