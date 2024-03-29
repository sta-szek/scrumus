package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveEntity;
import edu.piotrjonski.scrumus.dao.model.project.SprintEntity;
import edu.piotrjonski.scrumus.dao.model.project.TimeRange;
import edu.piotrjonski.scrumus.domain.Project;
import edu.piotrjonski.scrumus.domain.Retrospective;
import edu.piotrjonski.scrumus.domain.Sprint;
import edu.piotrjonski.scrumus.domain.Story;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

@RunWith(PowerMockRunner.class)
public class SprintDAOTest {

    public static final String PROJ_KEY = "projKey";
    private static TimeRange timeRange;

    @Spy
    private StoryDAO storyDAO = new StoryDAO();

    @Spy
    private RetrospectiveDAO retrospectiveDAO = new RetrospectiveDAO();

    @Spy
    private ProjectDAO projectDAO = new ProjectDAO();

    @Mock
    private EntityManager entityManager;

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
                                                    .findById(anyObject());
        doReturn(Optional.of(createProject())).when(projectDAO)
                                              .findById(PROJ_KEY);

        setInternalState(projectDAO, "entityManager", entityManager);

        doReturn(new SprintEntity()).when(entityManager)
                                    .find(SprintEntity.class, 1);

        timeRange = new TimeRange();
        timeRange.setEndDate(LocalDateTime.now());
        timeRange.setStartDate(LocalDateTime.now());
    }

    @Test
    public void shouldCallCreateNamedQueryWithValidParameters() {
        // given

        // when
        sprintDAO.getFindAllQuery();

        // then
        verify(entityManager).createNamedQuery(SprintEntity.FIND_ALL, SprintEntity.class);
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
        sprint.setTimeRange(timeRange);
        sprint.setProjectKey(PROJ_KEY);

        // when
        SprintEntity result = sprintDAO.mapToDatabaseModel(sprint);

        // then
        assertThat(result.getDefinitionOfDone()).isEqualTo(dod);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getRetrospectiveEntity()
                         .getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getTimeRange()
                         .getEndDate()).isEqualTo(timeRange.getEndDate());
        assertThat(result.getTimeRange()
                         .getStartDate()).isEqualTo(timeRange.getStartDate());
        assertThat(result.getProject()
                         .getKey()).isEqualTo(PROJ_KEY);
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
        sprint.setTimeRange(timeRange);
        sprint.setProject(createProjectEntity());

        // when
        Sprint result = sprintDAO.mapToDomainModel(sprint);

        // then
        assertThat(result.getDefinitionOfDone()).isEqualTo(dod);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getRetrospectiveId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getTimeRange()).isEqualTo(timeRange);
        assertThat(result.getProjectKey()).isEqualTo(PROJ_KEY);
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

    private Project createProject() {
        Project project = new Project();
        project.setKey(PROJ_KEY);
        return project;
    }

    private ProjectEntity createProjectEntity() {
        ProjectEntity project = new ProjectEntity();
        project.setKey(PROJ_KEY);
        return project;
    }

}