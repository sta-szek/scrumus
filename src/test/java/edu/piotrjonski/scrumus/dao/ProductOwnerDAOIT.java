package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.ProductOwnerEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.ProductOwner;
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
public class ProductOwnerDAOIT {

    private int lastDeveloperId = 0;

    @Inject
    private ProductOwnerDAO productOwnerDAO;

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return TestUtils.createDeployment();
    }

    @Before
    public void dropAllProductOwnersAndStartTransaction() throws Exception {
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
        ProductOwner productOwner = createProductOwner();

        // when
        productOwnerDAO.saveOrUpdate(productOwner);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        ProductOwner productOwner = createProductOwner();
        int entityId = productOwnerDAO.saveOrUpdate(productOwner)
                                      .get()
                                      .getId();

        // when
        boolean result = productOwnerDAO.exist(entityId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = productOwnerDAO.exist(1);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldDelete() {
        // given
        ProductOwner productOwner = createProductOwner();
        productOwner = productOwnerDAO.mapToDomainModelIfNotNull(entityManager.merge(productOwnerDAO.mapToDatabaseModelIfNotNull(
                productOwner)));

        // when
        productOwnerDAO.delete(productOwner.getId());
        int allUsers = findAll().size();

        // then
        assertThat(allUsers).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        ProductOwner productOwner = createProductOwner();

        int id1 = entityManager.merge(productOwnerDAO.mapToDatabaseModelIfNotNull(productOwner))
                               .getId();

        productOwner.setId(id1);

        // when
        List<ProductOwner> productOwners = productOwnerDAO.findAll();

        // then
        assertThat(productOwners).hasSize(1)
                                 .contains(productOwner);
    }

    @Test
    public void shouldFindByKey() {
        // given
        ProductOwner productOwner1 = createProductOwner();
        int id = entityManager.merge(productOwnerDAO.mapToDatabaseModelIfNotNull(productOwner1))
                              .getId();
        productOwner1.setId(id);

        // when
        ProductOwner productOwner = productOwnerDAO.findById(id)
                                                   .get();

        // then
        assertThat(productOwner).isEqualTo(productOwner1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<ProductOwner> user = productOwnerDAO.findById(0);

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
        entityManager.createQuery("DELETE FROM ProductOwnerEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM DeveloperEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private ProductOwner createProductOwner() {
        ProductOwner productOwner = new ProductOwner();
        productOwner.setDeveloper(developerDAO.findById(lastDeveloperId)
                                              .get());
        return productOwner;
    }

    private List<ProductOwnerEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM ProductOwnerEntity d")
                            .getResultList();
    }

}