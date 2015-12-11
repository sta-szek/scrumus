package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
import edu.piotrjonski.scrumus.domain.Project;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class ProjectDAOIT {

    public static final String NAME = "name";
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final String DESCRIPTION = "Descritpion";
    public static int nextUniqueValue = 0;

    @Inject
    private ProjectDAO projectDAO;

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
        Project project = createProject();

        // when
        projectDAO.saveOrUpdate(project);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        Project project = createProject();
        String entityKey = projectDAO.saveOrUpdate(project)
                                     .get()
                                     .getKey();

        // when
        boolean result = projectDAO.exist(entityKey);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = projectDAO.exist("das");

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldUpdate() {
        // given
        String updatedName = "UpdatedName";
        Project project = createProject();
        project = projectDAO.mapToDomainModelIfNotNull(entityManager.merge(projectDAO.mapToDatabaseModelIfNotNull(
                project)));
        project.setName(updatedName);

        // when
        project = projectDAO.saveOrUpdate(project)
                            .get();

        // then
        assertThat(project.getName()).isEqualTo(updatedName);
    }

    @Test
    public void shouldDelete() {
        // given
        Project project = createProject();
        project = projectDAO.mapToDomainModelIfNotNull(entityManager.merge(projectDAO.mapToDatabaseModelIfNotNull(
                project)));

        // when
        projectDAO.delete(project.getKey());
        int allUsers = findAll().size();

        // then
        assertThat(allUsers).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Project project1 = createProject();
        Project project2 = createProject();
        Project project3 = createProject();

        String id1 = entityManager.merge(projectDAO.mapToDatabaseModelIfNotNull(project1))
                                  .getKey();
        String id2 = entityManager.merge(projectDAO.mapToDatabaseModelIfNotNull(project2))
                                  .getKey();
        String id3 = entityManager.merge(projectDAO.mapToDatabaseModelIfNotNull(project3))
                                  .getKey();

        project1.setKey(id1);
        project2.setKey(id2);
        project3.setKey(id3);

        // when
        List<Project> projects = projectDAO.findAll();

        // then
        assertThat(projects).hasSize(3)
                            .contains(project1)
                            .contains(project2)
                            .contains(project3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Project project1 = createProject();
        Project project2 = createProject();
        String id = entityManager.merge(projectDAO.mapToDatabaseModelIfNotNull(project1))
                                 .getKey();
        entityManager.merge(projectDAO.mapToDatabaseModelIfNotNull(project2));
        project1.setKey(id);

        // when
        Project project = projectDAO.findByKey(id)
                                    .get();

        // then
        assertThat(project).isEqualTo(project1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Project> user = projectDAO.findByKey("");

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
        entityManager.createQuery("DELETE FROM ProjectEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Project createProject() {
        Project project = new Project();
        project.setName(NAME + nextUniqueValue);
        project.setCreationDate(NOW);
        project.setKey(NAME + nextUniqueValue);
        project.setDescription(DESCRIPTION + nextUniqueValue);
        nextUniqueValue++;
        return project;
    }

    private List<ProjectEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM ProjectEntity d")
                            .getResultList();
    }
}