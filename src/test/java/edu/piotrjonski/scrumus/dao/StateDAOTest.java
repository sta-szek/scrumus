package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.StateEntity;
import edu.piotrjonski.scrumus.domain.State;
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
public class StateDAOTest {

    @InjectMocks
    private StateDAO stateDAO = new StateDAO();

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
        stateDAO.getFindAllQuery();

        // then
        verify(entityManager).createNamedQuery(StateEntity.FIND_ALL, StateEntity.class);
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        String stateName = "stateName";
        State state = new State();
        state.setId(id);
        state.setName(stateName);

        // when
        StateEntity result = stateDAO.mapToDatabaseModel(state);

        // then
        assertThat(result.getName()).isEqualTo(stateName);
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        String stateName = "stateName";
        StateEntity state = new StateEntity();
        state.setId(id);
        state.setName(stateName);

        // when
        State result = stateDAO.mapToDomainModel(state);

        // then
        assertThat(result.getName()).isEqualTo(stateName);
        assertThat(result.getId()).isEqualTo(id);
    }
}