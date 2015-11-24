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

@RunWith(Arquillian.class)
public class DeveloperDAOIT {

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
        return ShrinkWrap.create(EmbeddedGradleImporter.class, "scrumus-arquillian-tests.war")
                         .forThisProjectDirectory()
                         .importBuildOutput()
                         .as(WebArchive.class);
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
        Developer developer = createDevloper();

        // when
        developerDAO.saveOrUpdate(developer);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldUpdate() {
        // given
        String updatedUserName = "UpdatedUser";
        Developer developer = createDevloper();
        developer = developerDAO.mapToDomainModel(entityManager.merge(developerDAO.mapToDatabaseModel(developer)));
        developer.setFirstName(updatedUserName);

        // when
        developer = developerDAO.saveOrUpdate(developer)
                                .get();

        // then
        assertThat(developer.getFirstName()).isEqualTo(updatedUserName);
    }

    @Test
    public void shouldDelete() {
        // given
        Developer developer = createDevloper();
        developer = developerDAO.mapToDomainModel(entityManager.merge(developerDAO.mapToDatabaseModel(developer)));

        // when
        developerDAO.delete(developer.getId());
        int allUsers = findAll().size();

        // then
        assertThat(allUsers).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Developer developer1 = createDevloper();
        Developer developer2 = createDevloper();
        Developer developer3 = createDevloper();

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
        Developer developer1 = createDevloper();
        Developer developer2 = createDevloper();
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
        Optional<Developer> user = developerDAO.findByKey(0);

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
        entityManager.createQuery("DELETE FROM Developer")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Developer createDevloper() {
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