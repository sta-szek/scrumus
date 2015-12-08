package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.IssueTypeEntity;
import edu.piotrjonski.scrumus.domain.IssueType;
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
public class IssueTypeDAOIT {
    public static final String NAME = "abcdge";
    public static int nextUniqueValue = 0;

    @Inject
    private IssueTypeDAO issueTypeDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return UtilsTest.createDeployment();
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
        IssueType issueType = createIssueType();

        // when
        issueTypeDAO.saveOrUpdate(issueType);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldUpdate() {
        // given
        String updatedName = "UpdatedName";
        IssueType issueType = createIssueType();
        issueType = issueTypeDAO.mapToDomainModelIfNotNull(entityManager.merge(issueTypeDAO.mapToDatabaseModelIfNotNull(
                issueType)));
        issueType.setName(updatedName);

        // when
        issueType = issueTypeDAO.saveOrUpdate(issueType)
                                .get();

        // then
        assertThat(issueType.getName()).isEqualTo(updatedName);
    }

    @Test
    public void shouldDelete() {
        // given
        IssueType issueType = createIssueType();
        issueType = issueTypeDAO.mapToDomainModelIfNotNull(entityManager.merge(issueTypeDAO.mapToDatabaseModelIfNotNull(
                issueType)));

        // when
        issueTypeDAO.delete(issueType.getId());
        int allUsers = findAll().size();

        // then
        assertThat(allUsers).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        IssueType issueType1 = createIssueType();
        IssueType issueType2 = createIssueType();
        IssueType issueType3 = createIssueType();

        int id1 = entityManager.merge(issueTypeDAO.mapToDatabaseModelIfNotNull(issueType1))
                               .getId();
        int id2 = entityManager.merge(issueTypeDAO.mapToDatabaseModelIfNotNull(issueType2))
                               .getId();
        int id3 = entityManager.merge(issueTypeDAO.mapToDatabaseModelIfNotNull(issueType3))
                               .getId();

        issueType1.setId(id1);
        issueType2.setId(id2);
        issueType3.setId(id3);

        // when
        List<IssueType> issueTypes = issueTypeDAO.findAll();

        // then
        assertThat(issueTypes).hasSize(3)
                              .contains(issueType1)
                              .contains(issueType2)
                              .contains(issueType3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        IssueType issueType1 = createIssueType();
        IssueType issueType2 = createIssueType();
        int id = entityManager.merge(issueTypeDAO.mapToDatabaseModelIfNotNull(issueType1))
                              .getId();
        entityManager.merge(issueTypeDAO.mapToDatabaseModelIfNotNull(issueType2));
        issueType1.setId(id);

        // when
        IssueType issueType = issueTypeDAO.findByKey(id)
                                          .get();

        // then
        assertThat(issueType).isEqualTo(issueType1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<IssueType> user = issueTypeDAO.findByKey(0);

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
        entityManager.createQuery("DELETE FROM IssueTypeEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private IssueType createIssueType() {
        IssueType issueType = new IssueType();
        issueType.setName(NAME + nextUniqueValue);
        nextUniqueValue++;
        return issueType;
    }

    private List<IssueTypeEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM IssueTypeEntity d")
                            .getResultList();
    }
}