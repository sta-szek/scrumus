package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class DeveloperDAOTest {

    @InjectMocks
    private DeveloperDAO developerDAO = new DeveloperDAO();

    @Mock
    private EntityManager entityManager;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void shouldCallCreateNamedQueryWithValidParameters() {
        // given

        // when
        developerDAO.getFindAllQuery();

        // then
        verify(entityManager).createNamedQuery(DeveloperEntity.FIND_ALL, DeveloperEntity.class);
    }

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