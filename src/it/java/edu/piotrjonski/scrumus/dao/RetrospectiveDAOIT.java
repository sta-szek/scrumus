package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveEntity;
import edu.piotrjonski.scrumus.domain.Retrospective;
import edu.piotrjonski.scrumus.domain.RetrospectiveItem;
import edu.piotrjonski.scrumus.domain.RetrospectiveItemType;
import edu.piotrjonski.scrumus.utils.UtilsTest;
import org.assertj.core.util.Lists;
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
public class RetrospectiveDAOIT {

    public static final String COMMENT_BODY = "retrospectivebody";
    public static int nextUniqueValue = 1;

    @Inject
    private RetrospectiveDAO retrospectiveDAO;

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
        Retrospective retrospective = new Retrospective();

        // when
        retrospectiveDAO.saveOrUpdate(retrospective);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldUpdate() {
        // given
        String retrospectiveBody = "updatedBody";
        Retrospective retrospective = new Retrospective();
        retrospective = retrospectiveDAO.mapToDomainModelIfNotNull(entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(
                retrospective)));
        retrospective.setDescription(retrospectiveBody);

        // when
        retrospective = retrospectiveDAO.saveOrUpdate(retrospective)
                                        .get();

        // then
        assertThat(retrospective.getDescription()).isEqualTo(retrospectiveBody);
    }

    @Test
    public void shouldDelete() {
        // given
        Retrospective retrospective = new Retrospective();
        retrospective = retrospectiveDAO.mapToDomainModelIfNotNull(entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(
                retrospective)));

        // when
        retrospectiveDAO.delete(retrospective.getId());
        int allRetrospectives = findAll().size();

        // then
        assertThat(allRetrospectives).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Retrospective retrospective1 = createRetrospective();
        Retrospective retrospective2 = createRetrospective();
        Retrospective retrospective3 = createRetrospective();

        int id1 = entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(retrospective1))
                               .getId();
        int id2 = entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(retrospective2))
                               .getId();
        int id3 = entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(retrospective3))
                               .getId();

        retrospective1.setId(id1);
        retrospective2.setId(id2);
        retrospective3.setId(id3);

        // when
        List<Retrospective> retrospectives = retrospectiveDAO.findAll();

        // then
        assertThat(retrospectives).hasSize(3)
                                  .contains(retrospective1)
                                  .contains(retrospective2)
                                  .contains(retrospective3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Retrospective retrospective1 = createRetrospective();
        Retrospective retrospective2 = createRetrospective();
        int id = entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(retrospective1))
                              .getId();
        entityManager.merge(retrospectiveDAO.mapToDatabaseModelIfNotNull(retrospective2));
        retrospective1.setId(id);

        // when
        Retrospective retrospective = retrospectiveDAO.findByKey(id)
                                                      .get();

        // then
        assertThat(retrospective).isEqualTo(retrospective1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Retrospective> user = retrospectiveDAO.findByKey(0);

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
        List<RetrospectiveEntity> resultList = entityManager.createQuery("SELECT r FROM RetrospectiveEntity r", RetrospectiveEntity.class)
                                                            .getResultList();
        resultList.forEach(x -> entityManager.remove(x));
        userTransaction.commit();
        entityManager.clear();
    }

    private Retrospective createRetrospective() {
        Retrospective retrospective = new Retrospective();
        retrospective.setDescription(COMMENT_BODY + nextUniqueValue);
        retrospective.setRetrospectiveItems(createRetrospectiveItems());
        nextUniqueValue++;
        return retrospective;
    }

    private List<RetrospectiveEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM RetrospectiveEntity d")
                            .getResultList();
    }

    private List<RetrospectiveItem> createRetrospectiveItems() {
        RetrospectiveItem retrospectiveItem1 = new RetrospectiveItem();
        RetrospectiveItem retrospectiveItem2 = new RetrospectiveItem();
        RetrospectiveItem retrospectiveItem3 = new RetrospectiveItem();
        RetrospectiveItem retrospectiveItem4 = new RetrospectiveItem();
        retrospectiveItem1.setRate(1);
        retrospectiveItem1.setDescription("desc1");
        retrospectiveItem1.setRetrospectiveItemType(RetrospectiveItemType.MINUS);
        retrospectiveItem2.setRate(5);
        retrospectiveItem2.setDescription("desc1");
        retrospectiveItem2.setRetrospectiveItemType(RetrospectiveItemType.PLUS);
        retrospectiveItem3.setRate(7);
        retrospectiveItem3.setDescription("desc1");
        retrospectiveItem3.setRetrospectiveItemType(RetrospectiveItemType.PLUS);
        retrospectiveItem4.setRate(2);
        retrospectiveItem4.setDescription("desc1");
        retrospectiveItem4.setRetrospectiveItemType(RetrospectiveItemType.MINUS);

        return Lists.newArrayList(retrospectiveItem1, retrospectiveItem2, retrospectiveItem3, retrospectiveItem4);
    }
}