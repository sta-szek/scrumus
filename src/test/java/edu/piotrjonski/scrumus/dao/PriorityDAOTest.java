package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.PriorityEntity;
import edu.piotrjonski.scrumus.domain.Priority;
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
public class PriorityDAOTest {

    @InjectMocks
    private PriorityDAO priorityDAO = new PriorityDAO();

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
        priorityDAO.getFindAllQuery();

        // then
        verify(entityManager).createNamedQuery(PriorityEntity.FIND_ALL, PriorityEntity.class);
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        String priorityName = "priorityName";
        Priority priority = new Priority();
        priority.setId(id);
        priority.setName(priorityName);

        // when
        PriorityEntity result = priorityDAO.mapToDatabaseModel(priority);

        // then
        assertThat(result.getName()).isEqualTo(priorityName);
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        String priorityName = "priorityName";
        PriorityEntity priority = new PriorityEntity();
        priority.setId(id);
        priority.setName(priorityName);

        // when
        Priority result = priorityDAO.mapToDomainModel(priority);

        // then
        assertThat(result.getName()).isEqualTo(priorityName);
        assertThat(result.getId()).isEqualTo(id);
    }

}