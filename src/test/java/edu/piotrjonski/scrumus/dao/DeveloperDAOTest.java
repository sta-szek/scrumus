package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.domain.Developer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.gradle.archive.importer.embedded.EmbeddedGradleImporter;
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
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(Arquillian.class)
public class DeveloperDAOTest {

    public static final String EMAIL = "jako@company.com";
    public static final String USERNAME = "jako";
    public static final String SURNAME = "Kowalski";
    public static final String JAN = "Jan";
    public static int nextUniqueValue = 0;

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(EmbeddedGradleImporter.class, "scrumus-arquillian-tests.war")
                                   .forThisProjectDirectory()
                                   .importBuildOutput()
                                   .as(WebArchive.class);

        return war;
    }

    @Before
    public void dropAllDevelopersAndStartTransaction() throws Exception {
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
        Developer developer = createDeveloper();

        // when
        developerDAO.saveOrUpdate(developer);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldUpdate() {
        // given
        String updatedDeveloperName = "UpdatedDeveloper";
        Developer developer = createDeveloper();
        developer = developerDAO.mapToDomainModel(entityManager.merge(developerDAO.mapToDatabaseModel(developer)));
        developer.setFirstName(updatedDeveloperName);

        // when
        developer = developerDAO.saveOrUpdate(developer)
                                .get();

        // then
        assertThat(developer.getFirstName()).isEqualTo(updatedDeveloperName);
    }

    @Test
    public void shouldDelete() {
        // given
        Developer developer = createDeveloper();
        developer = developerDAO.mapToDomainModel(entityManager.merge(developerDAO.mapToDatabaseModel(developer)));

        // when
        developerDAO.delete(developer.getId());
        int allDevelopers = findAll().size();

        // then
        assertThat(allDevelopers).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Developer developer1 = createDeveloper();
        Developer developer2 = createDeveloper();
        Developer developer3 = createDeveloper();

        int id1 = entityManager.merge(developerDAO.mapToDatabaseModel(developer1))
                               .getId();
        int id2 = entityManager.merge(developerDAO.mapToDatabaseModel(developer2))
                               .getId();
        int id3 = entityManager.merge(developerDAO.mapToDatabaseModel(developer3))
                               .getId();

        developer1.setId(id1);
        developer2.setId(id2);
        developer3.setId(id3);

        // when
        List<Developer> developers = developerDAO.findAll();

        // then
        assertThat(developers).hasSize(3)
                              .contains(developer1)
                              .contains(developer2)
                              .contains(developer3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Developer developer1 = createDeveloper();
        Developer developer2 = createDeveloper();
        int id = entityManager.merge(developerDAO.mapToDatabaseModel(developer1))
                              .getId();
        entityManager.merge(developerDAO.mapToDatabaseModel(developer2));
        developer1.setId(id);

        // when
        Developer developer = developerDAO.findByKey(id)
                                          .get();

        // then
        assertThat(developer).isEqualTo(developer1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Developer> developer = developerDAO.findByKey(0);

        // then
        assertThat(developer).isEmpty();
    }

    @Test
    public void shouldThrowExceptionWhenKeyIsNull() {
        // given
        Object key = null;

        // when
        Throwable thrown = catchThrowable(() -> {
            developerDAO.findByKey(key);
        });

        // then
        assertThat(thrown).hasCauseInstanceOf(javax.validation.ConstraintViolationException.class)
                          .hasMessageContaining("The developer key must not be null!");
    }

    @Test
    public void shouldThrowExceptionWhenDeveloperIsNull() {
        // given
        Developer developer = null;

        // when
        Throwable thrown = catchThrowable(() -> developerDAO.saveOrUpdate(developer));

        // then
        assertThat(thrown).hasCauseInstanceOf(javax.validation.ConstraintViolationException.class)
                          .hasMessageContaining("The developer must not be null!");
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM Developer")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Developer createDeveloper() {
        Developer developer = new Developer();
        developer.setFirstName(JAN + nextUniqueValue);
        developer.setSurname(SURNAME + nextUniqueValue);
        developer.setUsername(USERNAME + nextUniqueValue);
        developer.setEmail(EMAIL + nextUniqueValue);
        nextUniqueValue++;
        return developer;
    }

    private List<Developer> findAll() {
        return entityManager.createQuery("SELECT d FROM Developer d")
                            .getResultList();
    }


}