package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.CommentEntity;
import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveEntity;
import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveItemEmbeddable;
import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveItemTypeEnum;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Comment;
import edu.piotrjonski.scrumus.domain.Retrospective;
import edu.piotrjonski.scrumus.domain.RetrospectiveItem;
import edu.piotrjonski.scrumus.domain.RetrospectiveItemType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

@RunWith(PowerMockRunner.class)
public class RetrospectiveDAOTest {

    public static final int RATE = 1;
    public static final String ITEM_DESC = "item desc";
    public static final RetrospectiveItemType MINUS = RetrospectiveItemType.MINUS;
    public static final RetrospectiveItemTypeEnum MINUS1 = RetrospectiveItemTypeEnum.MINUS;
    public static final LocalDateTime NOW = LocalDateTime.now();

    @Mock
    private DeveloperDAO developerDAO;

    @Spy
    private CommentDAO commentDAO = new CommentDAO();

    @InjectMocks
    private RetrospectiveDAO retrospectiveDAO;

    @Before
    public void before() {
        initMocks(this);
        setInternalState(commentDAO, "developerDAO", developerDAO);
        doReturn(Optional.of(createDeveloperEntity())).when(developerDAO)
                                                      .findByKey(anyObject());
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
        retrospective.setCommentEntities(newArrayList(createComment()));

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
        assertThat(result.getCommentEntities()).containsOnly(createComment());
        assertThat(result.getRetrospectiveItems()).containsOnly(createDomainRetrospectiveItem());
    }

    private RetrospectiveItem createDomainRetrospectiveItem() {
        return new RetrospectiveItem(RATE, ITEM_DESC, MINUS);
    }

    private RetrospectiveItemEmbeddable createDatabaseRetrospectiveItem() {
        return new RetrospectiveItemEmbeddable(RATE, ITEM_DESC, MINUS1);
    }

    private Comment createComment() {
        return new Comment(RATE, ITEM_DESC, NOW, 1);
    }

    private CommentEntity createDatabaseComment() {
        DeveloperEntity developerEntity = createDeveloperEntity();
        return new CommentEntity(RATE, ITEM_DESC, NOW, developerEntity);
    }

    private DeveloperEntity createDeveloperEntity() {
        return new DeveloperEntity(1, "firstname", "surname", "username", "email", null, null);
    }
}