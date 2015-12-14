package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.IssueTypeEntity;
import edu.piotrjonski.scrumus.domain.IssueType;
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
public class IssueTypeDAOTest {

    @InjectMocks
    private IssueTypeDAO issueTypeDAO = new IssueTypeDAO();

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
        issueTypeDAO.getFindAllQuery();

        // then
        verify(entityManager).createNamedQuery(IssueTypeEntity.FIND_ALL, IssueTypeEntity.class);
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        String issueTypeName = "issueTypeName";
        IssueType issueType = new IssueType();
        issueType.setId(id);
        issueType.setName(issueTypeName);

        // when
        IssueTypeEntity result = issueTypeDAO.mapToDatabaseModel(issueType);

        // then
        assertThat(result.getName()).isEqualTo(issueTypeName);
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        String issueTypeName = "issueTypeName";
        IssueTypeEntity issueType = new IssueTypeEntity();
        issueType.setId(id);
        issueType.setName(issueTypeName);

        // when
        IssueType result = issueTypeDAO.mapToDomainModel(issueType);

        // then
        assertThat(result.getName()).isEqualTo(issueTypeName);
        assertThat(result.getId()).isEqualTo(id);
    }
}