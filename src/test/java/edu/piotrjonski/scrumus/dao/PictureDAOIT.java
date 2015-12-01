package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.domain.Picture;
import edu.piotrjonski.scrumus.utils.TestUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class PictureDAOIT {

    public static final String NAME = "name";
    public static final int ID = 1;
    public static final byte[] DATA = new byte[5];
    public static int nextUniqueValue = 0;

    @Inject
    private PictureDAO pictureDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return TestUtils.createDeployment();
    }

    @Before
    public void dropAllUsersAndStartTransaction() throws Exception {
        clearData();
        startTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        userTransaction.commit();
    }

    @Test
    public void shouldSave() {
        // given
        Picture picture = createPicture();

        // when
        pictureDAO.saveOrUpdate(picture);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldUpdate() {
        // given
        String updatedName = "UpdatedName";
        Picture picture = createPicture();
        picture = pictureDAO.mapToDomainModelIfNotNull(entityManager.merge(pictureDAO.mapToDatabaseModelIfNotNull(
                picture)));
        picture.setName(updatedName);

        // when
        picture = pictureDAO.saveOrUpdate(picture)
                            .get();

        // then
        assertThat(picture.getName()).isEqualTo(updatedName);
    }

    @Test
    public void shouldDelete() {
        // given
        Picture picture = createPicture();
        picture = pictureDAO.mapToDomainModelIfNotNull(entityManager.merge(pictureDAO.mapToDatabaseModelIfNotNull(
                picture)));

        // when
        pictureDAO.delete(picture.getId());
        int allPictures = findAll().size();

        // then
        assertThat(allPictures).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Picture picture1 = createPicture();
        Picture picture2 = createPicture();
        Picture picture3 = createPicture();

        int id1 = entityManager.merge(pictureDAO.mapToDatabaseModelIfNotNull(picture1))
                               .getId();
        int id2 = entityManager.merge(pictureDAO.mapToDatabaseModelIfNotNull(picture2))
                               .getId();
        int id3 = entityManager.merge(pictureDAO.mapToDatabaseModelIfNotNull(picture3))
                               .getId();

        picture1.setId(id1);
        picture2.setId(id2);
        picture3.setId(id3);

        // when
        List<Picture> pictures = pictureDAO.findAll();

        // then
        assertThat(pictures).hasSize(3)
                            .contains(picture1)
                            .contains(picture2)
                            .contains(picture3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Picture picture1 = createPicture();
        Picture picture2 = createPicture();
        int id = entityManager.merge(pictureDAO.mapToDatabaseModelIfNotNull(picture1))
                              .getId();
        entityManager.merge(pictureDAO.mapToDatabaseModelIfNotNull(picture2));
        picture1.setId(id);

        // when
        Picture picture = pictureDAO.findByKey(id)
                                    .get();

        // then
        assertThat(picture).isEqualTo(picture1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Picture> user = pictureDAO.findByKey(0);

        // then
        assertThat(user).isEmpty();
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM PictureEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Picture createPicture() {
        Picture picture = new Picture();
        picture.setName(NAME + nextUniqueValue);
        picture.setData(DATA);
        picture.setId(ID + nextUniqueValue);
        nextUniqueValue++;
        return picture;
    }

    private List<Picture> findAll() {
        return entityManager.createQuery("SELECT d FROM PictureEntity d")
                            .getResultList();
    }
}