package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.ScrumMasterEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.ScrumMaster;
import edu.piotrjonski.scrumus.utils.UtilsTest;
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
public class ScrumMasterDAOIT {

    private int lastDeveloperId = 0;

    @Inject
    private ScrumMasterDAO scrumMasterDAO;

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return UtilsTest.createDeployment();
    }

    @Before
    public void dropAllScrumMastersAndStartTransaction() throws Exception {
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
        ScrumMaster scrumMaster = createScrumMaster();

        // when
        scrumMasterDAO.saveOrUpdate(scrumMaster);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        ScrumMaster scrumMaster = createScrumMaster();
        int entityId = scrumMasterDAO.saveOrUpdate(scrumMaster)
                                     .get()
                                     .getId();

        // when
        boolean result = scrumMasterDAO.exist(entityId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = scrumMasterDAO.exist(1);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldDelete() {
        // given
        ScrumMaster scrumMaster = createScrumMaster();
        scrumMaster = scrumMasterDAO.mapToDomainModelIfNotNull(entityManager.merge(scrumMasterDAO.mapToDatabaseModelIfNotNull(
                scrumMaster)));

        // when
        scrumMasterDAO.delete(scrumMaster.getId());

        // then
        assertThat(findAll().size()).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        ScrumMaster scrumMaster = createScrumMaster();

        int id1 = entityManager.merge(scrumMasterDAO.mapToDatabaseModelIfNotNull(scrumMaster))
                               .getId();

        scrumMaster.setId(id1);

        // when
        List<ScrumMaster> scrumMasters = scrumMasterDAO.findAll();

        // then
        assertThat(scrumMasters).hasSize(1)
                                .contains(scrumMaster);
    }

    @Test
    public void shouldFindByKey() {
        // given
        ScrumMaster scrumMaster1 = createScrumMaster();
        int id = entityManager.merge(scrumMasterDAO.mapToDatabaseModelIfNotNull(scrumMaster1))
                              .getId();
        scrumMaster1.setId(id);

        // when
        ScrumMaster scrumMaster = scrumMasterDAO.findByKey(id)
                                                .get();

        // then
        assertThat(scrumMaster).isEqualTo(scrumMaster1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<ScrumMaster> user = scrumMasterDAO.findByKey(0);

        // then
        assertThat(user).isEmpty();
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
        Developer developer = new Developer();
        developer.setEmail("ds");
        developer.setFirstName("ds");
        developer.setSurname("ds");
        developer.setUsername("ds");
        Developer developer1 = developerDAO.saveOrUpdate(developer)
                                           .get();
        lastDeveloperId = developer1.getId();
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM ScrumMasterEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM DeveloperEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private ScrumMaster createScrumMaster() {
        ScrumMaster scrumMaster = new ScrumMaster();
        scrumMaster.setDeveloper(developerDAO.findByKey(lastDeveloperId)
                                             .get());
        return scrumMaster;
    }

    private List<ScrumMasterEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM ScrumMasterEntity d")
                            .getResultList();
    }

}