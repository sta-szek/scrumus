package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.domain.Project;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectDAOTest {

    private ProjectDAO projectDAO = new ProjectDAO();

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        String name = "name";
        LocalDateTime now = LocalDateTime.now();
        String dod = "dod";
        String description = "Description";
        String key = "key";
        Project project = new Project();
        project.setCreationDate(now);
        project.setDefinitionOfDone(dod);
        project.setDescription(description);
        project.setKey(key);
        project.setName(name);

        // when
        edu.piotrjonski.scrumus.dao.model.project.Project result = projectDAO.mapToDatabaseModel(project);

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
        String key = "key";
        edu.piotrjonski.scrumus.dao.model.project.Project project = new edu.piotrjonski.scrumus.dao.model.project
                .Project();
        project.setName(name);
        project.setCreationDate(now);
        project.setDefinitionOfDone(dod);
        project.setDescription(description);
        project.setKey(key);

        // when
        Project result = projectDAO.mapToDomainModel(project);

        // then
        assertThat(result.getKey()).isEqualTo(key);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getDefinitionOfDone()).isEqualTo(dod);
        assertThat(result.getCreationDate()).isEqualTo(now);
    }

}