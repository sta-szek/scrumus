package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.CommentEntity;
import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveEntity;
import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveItemEmbeddable;
import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveItemTypeEnum;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

@RunWith(PowerMockRunner.class)
public class RetrospectiveDAOTest {

    public static final int RATE = 1;
    public static final String ITEM_DESC = "item desc";
    public static final RetrospectiveItemType MINUS = RetrospectiveItemType.MINUS;
    public static final RetrospectiveItemTypeEnum MINUS1 = RetrospectiveItemTypeEnum.MINUS;
    public static final LocalDateTime NOW = LocalDateTime.now();

    @Spy
    private DeveloperDAO developerDAO = new DeveloperDAO();

    @Spy
    private CommentDAO commentDAO = new CommentDAO();

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private RetrospectiveDAO retrospectiveDAO;

    @Before
    public void before() {
        initMocks(this);
        setInternalState(commentDAO, "developerDAO", developerDAO);
        doReturn(Optional.of(createDeveloper())).when(developerDAO)
                                                .findById(anyObject());
    }

    @Test
    public void shouldCallCreateNamedQueryWithValidParameters() {
        // given

        // when
        retrospectiveDAO.getFindAllQuery();

        // then
        verify(entityManager).createNamedQuery(RetrospectiveEntity.FIND_ALL, RetrospectiveEntity.class);
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        String retrospectiveDescription = "retrospective desc";
        Retrospective retrospective = new Retrospective();
        retrospective.setId(id);
        retrospective.setRetrospectiveItems(newArrayList(createDomainRetrospectiveItem()));
        retrospective.setDescription(retrospectiveDescription);
        retrospective.setComments(newArrayList(createComment()));

        // when
        RetrospectiveEntity result = retrospectiveDAO.mapToDatabaseModel(retrospective);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDescription()).isEqualTo(retrospectiveDescription);
        assertThat(result.getCommentEntities()).hasSize(1);
        assertThat(result.getRetrospectiveItemEmbeddables()).containsOnly(createDatabaseRetrospectiveItem());
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        String retrospectiveDescription = "retrospective desc";
        RetrospectiveEntity retrospective = new RetrospectiveEntity();
        retrospective.setId(id);
        retrospective.setRetrospectiveItemEmbeddables(newArrayList(createDatabaseRetrospectiveItem()));
        retrospective.setDescription(retrospectiveDescription);
        retrospective.setCommentEntities(newArrayList(createDatabaseComment()));

        // when
        Retrospective result = retrospectiveDAO.mapToDomainModel(retrospective);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDescription()).isEqualTo(retrospectiveDescription);
        assertThat(result.getComments()).containsOnly(createComment());
        assertThat(result.getRetrospectiveItems()).containsOnly(createDomainRetrospectiveItem());
    }

    private RetrospectiveItem createDomainRetrospectiveItem() {
        RetrospectiveItem retrospectiveItem = new RetrospectiveItem();
        retrospectiveItem.setRate(RATE);
        retrospectiveItem.setDescription(ITEM_DESC);
        retrospectiveItem.setRetrospectiveItemType(RetrospectiveItemType.MINUS);
        return retrospectiveItem;
    }

    private RetrospectiveItemEmbeddable createDatabaseRetrospectiveItem() {
        RetrospectiveItemEmbeddable retrospectiveItemEmbeddable = new RetrospectiveItemEmbeddable();
        retrospectiveItemEmbeddable.setRate(RATE);
        retrospectiveItemEmbeddable.setDescription(ITEM_DESC);
        retrospectiveItemEmbeddable.setRetrospectiveItemTypeEnum(MINUS1);
        return retrospectiveItemEmbeddable;
    }

    private Comment createComment() {
        Comment comment = new Comment();
        comment.setCommentBody(ITEM_DESC);
        comment.setDeveloperId(1);
        comment.setCreationDate(NOW);
        comment.setId(RATE);
        return comment;
    }

    private CommentEntity createDatabaseComment() {
        DeveloperEntity developerEntity = createDeveloperEntity();
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(RATE);
        commentEntity.setCommentBody(ITEM_DESC);
        commentEntity.setCreationDate(NOW);
        commentEntity.setDeveloperEntity(developerEntity);
        return commentEntity;
    }

    private DeveloperEntity createDeveloperEntity() {
        DeveloperEntity developerEntity = new DeveloperEntity();
        developerEntity.setEmail("email");
        developerEntity.setFirstName("firstname");
        developerEntity.setId(1);
        developerEntity.setPictureEntity(null);
        developerEntity.setSurname("surname");
        developerEntity.setUsername("username");
        return developerEntity;
    }

    private Developer createDeveloper() {
        Developer developer = new Developer();
        developer.setEmail("email");
        developer.setFirstName("firstname");
        developer.setId(1);
        developer.setSurname("surname");
        developer.setUsername("username");
        return developer;
    }
}