package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.BacklogEntity;
import edu.piotrjonski.scrumus.dao.model.project.IssueEntity;
import edu.piotrjonski.scrumus.domain.Backlog;
import edu.piotrjonski.scrumus.domain.Issue;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

@RunWith(PowerMockRunner.class)
public class BacklogDAOTest {

    @Spy
    private CommentDAO commentDAO = new CommentDAO();

    @Spy
    private IssueTypeDAO issueTypeDAO = new IssueTypeDAO();

    @Spy
    private IssueDAO issueDAO = new IssueDAO();

    @InjectMocks
    private BacklogDAO backlogDAO = new BacklogDAO();

    @Before
    public void before() {
        initMocks(this);
        setInternalState(issueDAO, "commentDAO", commentDAO);
        setInternalState(issueDAO, "issueTypeDAO", issueTypeDAO);
        doReturn(Lists.newArrayList(new IssueEntity())).when(issueDAO)
                                                       .mapToDomainModel(anyList());
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        Backlog backlog = new Backlog();
        int id = 1;
        backlog.setId(id);
        backlog.setIssues(createIssues());

        // when
        BacklogEntity result = backlogDAO.mapToDatabaseModel(backlog);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getIssueEntities()).hasSize(1);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        BacklogEntity backlog = new BacklogEntity();
        int id = 1;
        backlog.setId(id);
        backlog.setIssueEntities(issueDAO.mapToDatabaseModel(createIssues()));

        // when
        Backlog result = backlogDAO.mapToDomainModel(backlog);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getIssues()).hasSize(1);
    }

    private List<Issue> createIssues() {
        return Lists.newArrayList(new Issue());
    }
}