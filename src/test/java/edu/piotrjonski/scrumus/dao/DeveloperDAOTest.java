package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeveloperDAOTest {

    private DeveloperDAO developerDAO = new DeveloperDAO();

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        String surname = "surname";
        String username = "username";
        int id = 1;
        String firstname = "firstname";
        String email = "email";
        Developer developer = new Developer();
        developer.setEmail(email);
        developer.setFirstName(firstname);
        developer.setId(id);
        developer.setSurname(surname);
        developer.setUsername(username);

        // when
        DeveloperEntity result = developerDAO.mapToDatabaseModel(developer);

        // then
        assertThat(result.getSurname()).isEqualTo(surname);
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getFirstName()).isEqualTo(firstname);
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        String surname = "surname";
        String username = "username";
        int id = 1;
        String firstname = "firstname";
        String email = "email";
        DeveloperEntity developerEntity = new DeveloperEntity();
        developerEntity.setEmail(email);
        developerEntity.setFirstName(firstname);
        developerEntity.setId(id);
        developerEntity.setSurname(surname);
        developerEntity.setUsername(username);

        // when
        Developer result = developerDAO.mapToDomainModel(developerEntity);

        // then
        assertThat(result.getSurname()).isEqualTo(surname);
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getFirstName()).isEqualTo(firstname);
        assertThat(result.getEmail()).isEqualTo(email);
    }

}