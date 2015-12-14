package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.dao.model.user.PasswordEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Password;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class PasswordDAOTest {

    private static final int DEVELOPER_ID = 15;

    @Spy
    private DeveloperDAO developerDAO = new DeveloperDAO();

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PasswordDAO passwordDAO;

    @Before
    public void before() {
        initMocks(this);
        Developer developer = new Developer();
        developer.setId(DEVELOPER_ID);
        doReturn(Optional.of(developer)).when(developerDAO)
                                        .findByKey(DEVELOPER_ID);
    }

    @Test
    public void shouldCallCreateNamedQueryWithValidParameters() {
        // given

        // when
        passwordDAO.getFindAllQuery();

        // then
        verify(entityManager).createNamedQuery(PasswordEntity.FIND_ALL, PasswordEntity.class);
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        String passwordString = "passwordString";
        Password password = new Password();
        password.setDeveloperId(DEVELOPER_ID);
        password.setPassword(passwordString);
        password.setId(id);

        // when
        PasswordEntity result = passwordDAO.mapToDatabaseModel(password);

        // then

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getPassword()).isEqualTo(passwordString);
        assertThat(result.getDeveloperEntity()
                         .getId()).isEqualTo(DEVELOPER_ID);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        String passwordString = "passwordString";
        PasswordEntity password = new PasswordEntity();
        password.setDeveloperEntity(createDeveloperEntity());
        password.setPassword(passwordString);
        password.setId(id);

        // when
        Password result = passwordDAO.mapToDomainModel(password);

        // then

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getPassword()).isEqualTo(passwordString);
        assertThat(result.getDeveloperId()).isEqualTo(DEVELOPER_ID);
    }

    private DeveloperEntity createDeveloperEntity() {
        DeveloperEntity developerEntity = new DeveloperEntity();
        developerEntity.setId(DEVELOPER_ID);
        return developerEntity;
    }

}