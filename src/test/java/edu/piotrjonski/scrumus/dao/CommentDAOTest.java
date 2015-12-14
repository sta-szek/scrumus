package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.CommentEntity;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Comment;
import edu.piotrjonski.scrumus.domain.Developer;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class CommentDAOTest {

    private static final int DEVELOPER_ID = 2;

    @Spy
    private DeveloperDAO developerDAO = new DeveloperDAO();

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CommentDAO commentDAO;

    @Before
    public void before() {
        initMocks(this);
        doReturn(Lists.newArrayList(createDomainDeveloper())).when(developerDAO)
                                                             .mapToDomainModel(any(List.class));

        doReturn(Optional.of(createDatabaseDeveloper())).when(developerDAO)
                                                        .findById(anyObject());

        doReturn(createDatabaseDeveloper()).when(developerDAO)
                                           .mapToDatabaseModelIfNotNull(anyObject());
    }

    @Test
    public void shouldCallCreateNamedQueryWithValidParameters() {
        // given

        // when
        commentDAO.getFindAllQuery();

        // then
        verify(entityManager).createNamedQuery(CommentEntity.FIND_ALL, CommentEntity.class);
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        String commentBody = "comment body";
        LocalDateTime now = LocalDateTime.now();
        Comment comment = new Comment();
        comment.setId(id);
        comment.setDeveloperId(DEVELOPER_ID);
        comment.setCreationDate(now);
        comment.setCommentBody(commentBody);

        // when
        CommentEntity result = commentDAO.mapToDatabaseModel(comment);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getCommentBody()).isEqualTo(commentBody);
        assertThat(result.getCreationDate()).isEqualTo(now);
        assertThat(result.getDeveloperEntity()
                         .getId()).isEqualTo(DEVELOPER_ID);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        String commentBody = "comment body";
        LocalDateTime now = LocalDateTime.now();
        CommentEntity comment = new CommentEntity();
        comment.setId(id);
        comment.setDeveloperEntity(createDatabaseDeveloper());
        comment.setCreationDate(now);
        comment.setCommentBody(commentBody);

        // when
        Comment result = commentDAO.mapToDomainModel(comment);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getCommentBody()).isEqualTo(commentBody);
        assertThat(result.getCreationDate()).isEqualTo(now);
        assertThat(result.getDeveloperId()).isEqualTo(DEVELOPER_ID);
    }

    private Developer createDomainDeveloper() {
        return new Developer();
    }

    private DeveloperEntity createDatabaseDeveloper() {
        DeveloperEntity developerEntity = new DeveloperEntity();
        developerEntity.setId(DEVELOPER_ID);
        return developerEntity;
    }
}