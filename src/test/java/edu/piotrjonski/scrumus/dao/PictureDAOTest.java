package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.user.PictureEntity;
import edu.piotrjonski.scrumus.domain.Picture;
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
public class PictureDAOTest {

    @InjectMocks
    private PictureDAO pictureDAO = new PictureDAO();

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
        pictureDAO.getFindAllQuery();

        // then
        verify(entityManager).createNamedQuery(PictureEntity.FIND_ALL, PictureEntity.class);
    }

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        byte[] data = new byte[5];
        int id = 1;
        String name = "name";
        Picture picture = new Picture();
        picture.setData(data);
        picture.setId(id);
        picture.setName(name);

        // when
        PictureEntity result = pictureDAO.mapToDatabaseModel(picture);

        // then
        assertThat(result.getData()).isEqualTo(data);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(name);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        byte[] data = new byte[5];
        int id = 1;
        String name = "name";
        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setData(data);
        pictureEntity.setId(id);
        pictureEntity.setName(name);

        // when
        Picture result = pictureDAO.mapToDomainModel(pictureEntity);

        // then
        assertThat(result.getData()).isEqualTo(data);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(name);
    }
}