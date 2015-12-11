package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.dao.model.user.ProductOwnerEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.ProductOwner;
import edu.piotrjonski.scrumus.domain.Project;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class ProductOwnerDAOTest {

    @Spy
    private DeveloperDAO developerDAO = new DeveloperDAO();

    @Spy
    private ProjectDAO projectDAO = new ProjectDAO();

    @InjectMocks
    private ProductOwnerDAO productOwnerDAO;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        ProductOwner productOwner = new ProductOwner();
        productOwner.setId(id);
        productOwner.setDeveloper(new Developer());
        productOwner.setProject(new Project());

        // when
        ProductOwnerEntity result = productOwnerDAO.mapToDatabaseModel(productOwner);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDeveloperEntity()).isNotNull();
        assertThat(result.getProjectEntity()).isNotNull();
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        ProductOwnerEntity productOwnerEntity = new ProductOwnerEntity();
        productOwnerEntity.setId(id);
        productOwnerEntity.setDeveloperEntity(new DeveloperEntity());
        productOwnerEntity.setProjectEntity(new ProjectEntity());

        // when
        ProductOwner result = productOwnerDAO.mapToDomainModel(productOwnerEntity);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDeveloper()).isNotNull();
        assertThat(result.getProject()).isNotNull();
    }

}