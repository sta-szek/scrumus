package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.AdminEntity;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Admin;
import edu.piotrjonski.scrumus.domain.Developer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class AdminDAOTest {

    @Spy
    private DeveloperDAO developerDAO = new DeveloperDAO();

    @InjectMocks
    private AdminDAO adminDAO;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        Admin admin = new Admin();
        admin.setId(id);
        admin.setDeveloper(new Developer());

        // when
        AdminEntity result = adminDAO.mapToDatabaseModel(admin);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDeveloperEntity()).isNotNull();
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setId(id);
        adminEntity.setDeveloperEntity(new DeveloperEntity());

        // when
        Admin result = adminDAO.mapToDomainModel(adminEntity);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDeveloper()).isNotNull();
    }

}