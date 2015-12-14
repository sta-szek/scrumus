package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.CommentEntity;
import edu.piotrjonski.scrumus.dao.model.project.IssueEntity;
import edu.piotrjonski.scrumus.dao.model.project.IssueTypeEntity;
import edu.piotrjonski.scrumus.dao.model.project.PriorityEntity;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.*;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class IssueDAOTest {

    private static final int DEVELOPER_ID = 15;

    @Spy
    private CommentDAO commentDAO = new CommentDAO();

    @Spy
    private DeveloperDAO developerDAO = new DeveloperDAO();

    @Spy
    private IssueTypeDAO issueTypeDAO = new IssueTypeDAO();

    @Spy
    private PriorityDAO priorityDAO = new PriorityDAO();

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private IssueDAO issueDAO;

    @Before
    public void before() {
        initMocks(this);
        Whitebox.setInternalState(commentDAO, "developerDAO", developerDAO);
        Developer developer = new Developer();
        developer.setId(DEVELOPER_ID);
        doReturn(Optional.of(developer)).when(developerDAO)
                                        .findByKey(DEVELOPER_ID);
    }

    @Test
    public void shouldCallCreateNamedQueryWithValidParameters() {
        // given

        // when
        issueDAO.getFindAllQuery();

        // then
        verify(entityManager).createNamedQuery(IssueEntity.FIND_ALL, IssueEntity.class);
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        Comment comment = new Comment();
        comment.setDeveloperId(DEVELOPER_ID);
        int id = 1;
        String summary = "summary";
        String dod = "dod";
        String key = "key";
        String description = "description";
        LocalDateTime now = LocalDateTime.now();
        Issue issue = new Issue();
        issue.setAssigneeId(DEVELOPER_ID);
        issue.setComments(Lists.newArrayList(comment));
        issue.setCreationDate(now);
        issue.setKey(key);
        issue.setDescription(description);
        issue.setDefinitionOfDone(dod);
        issue.setId(id);
        issue.setIssueType(new IssueType());
        issue.setPriority(new Priority());
        issue.setReporterId(DEVELOPER_ID);
        issue.setSummary(summary);

        // when
        IssueEntity result = issueDAO.mapToDatabaseModel(issue);

        // then
        assertThat(result.getCommentEntities()).hasSize(1);
        assertThat(result.getCreationDate()).isEqualTo(now);
        assertThat(result.getDefinitionOfDone()).isEqualTo(dod);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getIssueTypeEntity()).isNotNull();
        assertThat(result.getPriorityEntity()).isNotNull();
        assertThat(result.getKey()).isEqualTo(key);
        assertThat(result.getSummary()).isEqualTo(summary);
        assertThat(result.getReporter()
                         .getId()).isEqualTo(DEVELOPER_ID);
        assertThat(result.getAssignee()
                         .getId()).isEqualTo(DEVELOPER_ID);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        CommentEntity commentEntity = new CommentEntity();
        DeveloperEntity developerEntity = new DeveloperEntity();
        developerEntity.setId(DEVELOPER_ID);
        commentEntity.setDeveloperEntity(developerEntity);
        int id = 1;
        String summary = "summary";
        String dod = "dod";
        String key = "key";
        String description = "description";
        LocalDateTime now = LocalDateTime.now();
        IssueEntity issueEntity = new IssueEntity();
        issueEntity.setAssignee(createDeveloperEntity());
        issueEntity.setCommentEntities(Lists.newArrayList(commentEntity));
        issueEntity.setCreationDate(now);
        issueEntity.setKey(key);
        issueEntity.setDescription(description);
        issueEntity.setDefinitionOfDone(dod);
        issueEntity.setId(id);
        issueEntity.setIssueTypeEntity(new IssueTypeEntity());
        issueEntity.setPriorityEntity(new PriorityEntity());
        issueEntity.setReporter(createDeveloperEntity());
        issueEntity.setSummary(summary);

        // when
        Issue result = issueDAO.mapToDomainModel(issueEntity);

        // then
        assertThat(result.getAssigneeId()).isEqualTo(DEVELOPER_ID);
        assertThat(result.getComments()).hasSize(1);
        assertThat(result.getCreationDate()).isEqualTo(now);
        assertThat(result.getDefinitionOfDone()).isEqualTo(dod);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getIssueType()).isNotNull();
        assertThat(result.getPriority()).isNotNull();
        assertThat(result.getKey()).isEqualTo(key);
        assertThat(result.getReporterId()).isEqualTo(DEVELOPER_ID);
        assertThat(result.getSummary()).isEqualTo(summary);
    }

    private DeveloperEntity createDeveloperEntity() {
        DeveloperEntity developerEntity = new DeveloperEntity();
        developerEntity.setId(DEVELOPER_ID);
        return developerEntity;
    }

}