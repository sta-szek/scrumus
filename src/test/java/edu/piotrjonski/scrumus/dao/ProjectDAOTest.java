package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
import edu.piotrjonski.scrumus.domain.Project;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class ProjectDAOTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ProjectDAO projectDAO = new ProjectDAO();

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void shouldCallCreateNamedQueryWithValidParameters() {
        // given

        // when
        projectDAO.getFindAllQuery();

        // then
        verify(entityManager).createNamedQuery(ProjectEntity.FIND_ALL, ProjectEntity.class);
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        String name = "name";
        LocalDateTime now = LocalDateTime.now();
        String dod = "dod";
        String description = "Description";
        String key = "projectKey";
        Project project = new Project();
        project.setCreationDate(now);
        project.setDefinitionOfDone(dod);
        project.setDescription(description);
        project.setKey(key);
        project.setName(name);

        // when
        ProjectEntity result = projectDAO.mapToDatabaseModel(project);

        // then
        assertThat(result.getCreationDate()).isEqualTo(now);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getDefinitionOfDone()).isEqualTo(dod);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getKey()).isEqualTo(key);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        String name = "name";
        LocalDateTime now = LocalDateTime.now();
        String dod = "dod";
        String description = "Description";
        String key = "projectKey";
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setName(name);
        projectEntity.setCreationDate(now);
        projectEntity.setDefinitionOfDone(dod);
        projectEntity.setDescription(description);
        projectEntity.setKey(key);

        // when
        Project result = projectDAO.mapToDomainModel(projectEntity);

        // then
        assertThat(result.getKey()).isEqualTo(key);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getDefinitionOfDone()).isEqualTo(dod);
        assertThat(result.getCreationDate()).isEqualTo(now);
    }

}