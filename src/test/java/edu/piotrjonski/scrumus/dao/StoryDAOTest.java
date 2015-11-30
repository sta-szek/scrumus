package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.IssueEntity;
import edu.piotrjonski.scrumus.dao.model.project.StoryEntity;
import edu.piotrjonski.scrumus.domain.Issue;
import edu.piotrjonski.scrumus.domain.Story;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class StoryDAOTest {

    @Mock
    private IssueDAO issueDAO;

    @InjectMocks
    private StoryDAO storyDAO;

    @Before
    public void before() {
        initMocks(this);
        doReturn(Lists.newArrayList(new Issue())).when(issueDAO)
                                                 .mapToDomainModel(anyList());
        doReturn(Lists.newArrayList(new IssueEntity())).when(issueDAO)
                                                       .mapToDatabaseModel(anyList());
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        String name = "name";
        int points = 1;
        String dod = "dod";
        Story story = new Story();
        story.setDefinitionOfDone(dod);
        story.setId(id);
        story.setIssues(Lists.newArrayList(new Issue()));
        story.setName(name);
        story.setPoints(points);
        story.setDefinitionOfDone(dod);
        story.setId(id);

        // when
        StoryEntity result = storyDAO.mapToDatabaseModel(story);

        // then
        assertThat(result.getDefinitionOfDone()).isEqualTo(dod);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getIssueEntities()).hasSize(1);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getPoints()).isEqualTo(points);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        String name = "name";
        int points = 1;
        String dod = "dod";
        StoryEntity story = new StoryEntity();
        story.setDefinitionOfDone(dod);
        story.setId(id);
        story.setIssueEntities(Lists.newArrayList(new IssueEntity()));
        story.setName(name);
        story.setPoints(points);
        story.setDefinitionOfDone(dod);
        story.setId(id);

        // when
        Story result = storyDAO.mapToDomainModel(story);

        // then
        assertThat(result.getDefinitionOfDone()).isEqualTo(dod);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getIssues()).hasSize(1);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getPoints()).isEqualTo(points);
    }

}