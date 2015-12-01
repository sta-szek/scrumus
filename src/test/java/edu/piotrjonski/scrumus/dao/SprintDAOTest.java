package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveEntity;
import edu.piotrjonski.scrumus.dao.model.project.SprintEntity;
import edu.piotrjonski.scrumus.dao.model.project.TimeRange;
import edu.piotrjonski.scrumus.domain.Retrospective;
import edu.piotrjonski.scrumus.domain.Sprint;
import edu.piotrjonski.scrumus.domain.Story;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class SprintDAOTest {

    public static final TimeRange TIME_RANGE = new TimeRange(LocalDateTime.now(),
                                                             LocalDateTime.now()
                                                                          .plusDays(14));
    @Mock
    private StoryDAO storyDAO;

    @Mock
    private RetrospectiveDAO retrospectiveDAO;

    @InjectMocks
    private SprintDAO sprintDAO;

    @Before
    public void before() {
        initMocks(this);
        doReturn(Lists.newArrayList(new Story())).when(storyDAO)
                                                 .findStoriesForSprint(any(Integer.class));
        doReturn(createRetrospectiveEntity()).when(retrospectiveDAO)
                                             .mapToDatabaseModelIfNotNull(anyObject());
        doReturn(Optional.of(createRetrospective())).when(retrospectiveDAO)
                                                    .findByKey(anyObject());
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        String name = "name";
        String dod = "dod";
        Sprint sprint = new Sprint();
        sprint.setDefinitionOfDone(dod);
        sprint.setId(id);
        sprint.setName(name);
        sprint.setRetrospectiveId(1);
        sprint.setTimeRange(TIME_RANGE);

        // when
        SprintEntity result = sprintDAO.mapToDatabaseModel(sprint);

        // then
        assertThat(result.getDefinitionOfDone()).isEqualTo(dod);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getRetrospectiveEntity()
                         .getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getTimeRange()).isEqualTo(TIME_RANGE);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        String name = "name";
        String dod = "dod";
        SprintEntity sprint = new SprintEntity();
        sprint.setDefinitionOfDone(dod);
        sprint.setId(id);
        sprint.setName(name);
        sprint.setRetrospectiveEntity(createRetrospectiveEntity());
        sprint.setTimeRange(TIME_RANGE);

        // when
        Sprint result = sprintDAO.mapToDomainModel(sprint);

        // then
        assertThat(result.getDefinitionOfDone()).isEqualTo(dod);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getRetrospectiveId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getTimeRange()).isEqualTo(TIME_RANGE);
    }

    private RetrospectiveEntity createRetrospectiveEntity() {
        RetrospectiveEntity retrospectiveEntity = new RetrospectiveEntity();
        retrospectiveEntity.setId(1);
        return retrospectiveEntity;
    }

    private Retrospective createRetrospective() {
        Retrospective retrospective = new Retrospective();
        retrospective.setId(1);
        return retrospective;
    }

}