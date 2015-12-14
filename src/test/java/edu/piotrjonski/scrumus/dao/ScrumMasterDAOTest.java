package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.dao.model.user.ScrumMasterEntity;
import edu.piotrjonski.scrumus.dao.model.user.TeamEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.ScrumMaster;
import edu.piotrjonski.scrumus.domain.Team;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

@RunWith(PowerMockRunner.class)
public class ScrumMasterDAOTest {

    @Spy
    private DeveloperDAO developerDAO = new DeveloperDAO();

    @Spy
    private TeamDAO teamDAO = new TeamDAO();

    @Spy
    private ProjectDAO projectDAO = new ProjectDAO();

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ScrumMasterDAO scrumMasterDAO;

    @Before
    public void before() {
        initMocks(this);
        setInternalState(teamDAO, "projectDAO", projectDAO);
        setInternalState(teamDAO, "developerDAO", developerDAO);
    }

    @Test
    public void shouldCallCreateNamedQueryWithValidParameters() {
        // given

        // when
        scrumMasterDAO.getFindAllQuery();

        // then
        verify(entityManager).createNamedQuery(ScrumMasterEntity.FIND_ALL, ScrumMasterEntity.class);
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        ScrumMaster scrumMaster = new ScrumMaster();
        scrumMaster.setId(id);
        scrumMaster.setDeveloper(new Developer());
        scrumMaster.setTeams(Lists.newArrayList(new Team()));

        // when
        ScrumMasterEntity result = scrumMasterDAO.mapToDatabaseModel(scrumMaster);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDeveloperEntity()).isNotNull();
        assertThat(result.getTeamEntities()).hasSize(1);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        ScrumMasterEntity scrumMasterEntity = new ScrumMasterEntity();
        scrumMasterEntity.setId(id);
        scrumMasterEntity.setDeveloperEntity(new DeveloperEntity());
        scrumMasterEntity.setTeamEntities(Lists.newArrayList(new TeamEntity()));

        // when
        ScrumMaster result = scrumMasterDAO.mapToDomainModel(scrumMasterEntity);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDeveloper()).isNotNull();
        assertThat(result.getTeams()).hasSize(1);
    }

}