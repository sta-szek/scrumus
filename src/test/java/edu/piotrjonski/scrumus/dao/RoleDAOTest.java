package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.security.RoleEntity;
import edu.piotrjonski.scrumus.dao.model.security.RoleType;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Role;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class RoleDAOTest {

    @Spy
    private DeveloperDAO developerDAO = new DeveloperDAO();

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private RoleDAO roleDAO;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void shouldCallCreateNamedQueryWithValidParameters() {
        // given

        // when
        roleDAO.getFindAllQuery();

        // then
        verify(entityManager).createNamedQuery(RoleEntity.FIND_ALL, RoleEntity.class);
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        Role role = new Role();
        role.setId(id);
        role.setDevelopers(createDomainDevelopers());
        role.setRoleType(RoleType.ADMIN);

        // when
        RoleEntity result = roleDAO.mapToDatabaseModel(role);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getRoleType()).isEqualTo(RoleType.ADMIN);
        assertThat(result.getDeveloperEntities()).hasSize(1);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        String name = "name";
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(id);
        roleEntity.setDeveloperEntities(createDatabaseDevelopers());
        roleEntity.setRoleType(RoleType.ADMIN);

        // when
        Role result = roleDAO.mapToDomainModel(roleEntity);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getRoleType()).isEqualTo(RoleType.ADMIN);
        assertThat(result.getDevelopers()).hasSize(1);
    }

    private List<Developer> createDomainDevelopers() {
        return Lists.newArrayList(new Developer());
    }

    private List<DeveloperEntity> createDatabaseDevelopers() {
        return Lists.newArrayList(new DeveloperEntity());
    }
}