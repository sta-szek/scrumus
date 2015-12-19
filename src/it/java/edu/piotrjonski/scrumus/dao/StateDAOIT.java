package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.StateEntity;
import edu.piotrjonski.scrumus.domain.State;
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
public class StateDAOIT {
    public static final String NAME = "abcdge";
    public static int nextUniqueValue = 0;

    @Inject
    private StateDAO stateDAO;

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
        State state = createState();

        // when
        stateDAO.saveOrUpdate(state);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        State state = createState();
        int entityId = stateDAO.saveOrUpdate(state)
                               .get()
                               .getId();

        // when
        boolean result = stateDAO.exist(entityId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = stateDAO.exist(1);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldUpdate() {
        // given
        String updatedName = "UpdatedName";
        State state = createState();
        state = stateDAO.mapToDomainModelIfNotNull(entityManager.merge(stateDAO.mapToDatabaseModelIfNotNull(
                state)));
        state.setName(updatedName);

        // when
        state = stateDAO.saveOrUpdate(state)
                        .get();

        // then
        assertThat(state.getName()).isEqualTo(updatedName);
    }

    @Test
    public void shouldDelete() {
        // given
        State state = createState();
        state = stateDAO.mapToDomainModelIfNotNull(entityManager.merge(stateDAO.mapToDatabaseModelIfNotNull(
                state)));

        // when
        stateDAO.delete(state.getId());
        int allUsers = findAll().size();

        // then
        assertThat(allUsers).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        State state1 = createState();
        State state2 = createState();
        State state3 = createState();

        int id1 = entityManager.merge(stateDAO.mapToDatabaseModelIfNotNull(state1))
                               .getId();
        int id2 = entityManager.merge(stateDAO.mapToDatabaseModelIfNotNull(state2))
                               .getId();
        int id3 = entityManager.merge(stateDAO.mapToDatabaseModelIfNotNull(state3))
                               .getId();

        state1.setId(id1);
        state2.setId(id2);
        state3.setId(id3);

        // when
        List<State> states = stateDAO.findAll();

        // then
        assertThat(states).hasSize(3)
                          .contains(state1)
                          .contains(state2)
                          .contains(state3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        State state1 = createState();
        State state2 = createState();
        int id = entityManager.merge(stateDAO.mapToDatabaseModelIfNotNull(state1))
                              .getId();
        entityManager.merge(stateDAO.mapToDatabaseModelIfNotNull(state2));
        state1.setId(id);

        // when
        State state = stateDAO.findById(id)
                              .get();

        // then
        assertThat(state).isEqualTo(state1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<State> user = stateDAO.findById(0);

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
        entityManager.createQuery("DELETE FROM StateEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private State createState() {
        State state = new State();
        state.setName(NAME + nextUniqueValue);
        nextUniqueValue++;
        return state;
    }

    private List<StateEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM StateEntity d")
                            .getResultList();
    }
}