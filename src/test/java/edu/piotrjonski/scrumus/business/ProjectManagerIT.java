package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.BacklogDAO;
import edu.piotrjonski.scrumus.dao.ProjectDAO;
import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
import edu.piotrjonski.scrumus.domain.Backlog;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(Arquillian.class)
public class ProjectManagerIT {
    public static final String EMAIL = "jako@company.com";
    public static final String USERNAME = "jako";
    public static final String SURNAME = "Kowalski";
    public static final String FIRSTNAME = "Jan";
    public static int nextUniqueValue = 0;

    @Inject
    private BacklogDAO backlogDAO;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private ProjectManager projectManager;

    @Inject
    private PermissionManager permissionManager;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return UtilsTest.createDeployment();
    }

    @Before
    public void dropAllProjectsAndStartTransaction() throws Exception {
        clearData();
        startTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        userTransaction.commit();
    }

    @Test
    public void shouldCreateProject() throws AlreadyExistException {
        // given
        Project project = createProject();

        // when
        Project savedProject = projectManager.create(project);
        boolean result = projectDAO.exist(savedProject.getKey());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldThrowExceptionWhenProjectAlreadyExist() throws AlreadyExistException {
        // given
        Project project = createProject();
        Project savedProject = projectManager.create(project);

        // when
        Throwable throwable = catchThrowable(() -> projectManager.create(savedProject));

        // then
        assertThat(throwable).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void shouldDeleteProject() throws AlreadyExistException {
        // given
        Project project = createProject();
        String key = projectManager.create(project)
                                   .getKey();

        // when
        projectManager.delete(key);
        int result = findAllProjectsSize();

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void shouldCreateBacklogForProject() throws AlreadyExistException {
        // given
        Project project = createProject();
        Project savedProject = projectManager.create(project);

        // when
        Backlog result = backlogDAO.findBacklogForProject(savedProject.getKey())
                                   .get();

        // then
        assertThat(result).isNotNull();
    }

    @Test
    public void shouldFindProject() {
        // given
        Project project = createProject();
        Project savedProject = projectDAO.saveOrUpdate(project)
                                         .get();
        String key = savedProject.getKey();
        // when
        Project result = projectManager.findProject(key);

        // then
        assertThat(result).isEqualTo(savedProject);
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM AdminEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM ProjectEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Project createProject() {
        Project project = new Project();
        project.setKey("proj" + nextUniqueValue);
        project.setName("name" + nextUniqueValue);
        project.setCreationDate(LocalDateTime.now());
        project.setDescription("desc");
        project.setDefinitionOfDone("dod");
        nextUniqueValue++;
        return project;
    }

    private int findAllProjectsSize() {
        return entityManager.createNamedQuery(ProjectEntity.FIND_ALL, ProjectEntity.class)
                            .getResultList()
                            .size();
    }
}