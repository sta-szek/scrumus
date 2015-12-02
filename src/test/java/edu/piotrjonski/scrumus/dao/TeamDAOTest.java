package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.dao.model.user.TeamEntity;
import edu.piotrjonski.scrumus.domain.Project;
import edu.piotrjonski.scrumus.domain.Team;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class TeamDAOTest {

    @Spy
    private ProjectDAO projectDAO = new ProjectDAO();

    @InjectMocks
    private TeamDAO teamDAO;

    @Before
    public void before() {
        initMocks(this);
        doReturn(Lists.newArrayList(createDomainProject())).when(projectDAO)
                                                           .mapToDomainModel(any(List.class));

        doReturn(Lists.newArrayList(createDatabaseProject())).when(projectDAO)
                                                             .mapToDatabaseModel(any(List.class));
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        String name = "name";
        Team team = new Team();
        team.setId(id);
        team.setName(name);
        Project project = new Project();
        team.setProjects(Lists.newArrayList(project));

        // when
        TeamEntity result = teamDAO.mapToDatabaseModel(team);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getProjectEntities()).hasSize(1);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        String name = "name";
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setId(id);
        teamEntity.setName(name);
        teamEntity.setDeveloperEntities(Lists.newArrayList(new DeveloperEntity()));
        teamEntity.setProjectEntities(Lists.newArrayList(new ProjectEntity()));

        // when
        Team result = teamDAO.mapToDomainModel(teamEntity);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getProjects()).hasSize(1);
    }

    private Project createDomainProject() {
        Project project = new Project();
        return project;
    }

    private ProjectEntity createDatabaseProject() {
        return new ProjectEntity();
    }
}