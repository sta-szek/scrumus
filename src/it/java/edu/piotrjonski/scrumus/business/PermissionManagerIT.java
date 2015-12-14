package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.AdminDAO;
import edu.piotrjonski.scrumus.dao.DeveloperDAO;
import edu.piotrjonski.scrumus.dao.ProjectDAO;
import edu.piotrjonski.scrumus.dao.TeamDAO;
import edu.piotrjonski.scrumus.dao.model.user.AdminEntity;
import edu.piotrjonski.scrumus.domain.Admin;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Project;
import edu.piotrjonski.scrumus.domain.Team;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class PermissionManagerIT {

    public static final String EMAIL = "jako@company.com";
    public static final String USERNAME = "jako";
    public static final String SURNAME = "Kowalski";
    public static final String FIRSTNAME = "Jan";
    public static int nextUniqueValue = 0;

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private AdminDAO adminDAO;

    @Inject
    private TeamDAO teamDAO;

    @Inject
    private ProjectDAO projectDAO;

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
    public void dropAllDevelopersAndStartTransaction() throws Exception {
        clearData();
        startTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        userTransaction.commit();
    }

    @Test
    public void shouldReturnTrueIfUserIsAdmin() {
        // given
        Developer developer = developerDAO.saveOrUpdate(createDeveloper())
                                          .get();
        adminDAO.saveOrUpdate(createAdmin(developer));

        // when
        boolean result = permissionManager.isAdmin(developer);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfUserIsNotAdmin() {
        // given
        Developer developer = createDeveloper();
        developer.setId(1);

        // when
        boolean result = permissionManager.isAdmin(developer);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldGrantAdminPermission() {
        // given
        Developer developer = developerDAO.saveOrUpdate(createDeveloper())
                                          .get();

        // when
        permissionManager.grantAdminPermission(developer);
        int result = findAllAdminsSize();

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void shouldNotGrantAdminPermissionIfUserDoesNotExist() {
        // given
        Developer developer = createDeveloper();

        // when
        permissionManager.grantAdminPermission(developer);
        int result = findAllAdminsSize();

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void shouldRemoveAdminPermission() {
        // given
        Developer developer = developerDAO.saveOrUpdate(createDeveloper())
                                          .get();
        adminDAO.saveOrUpdate(createAdmin(developer));

        // when
        permissionManager.removeAdminPermission(developer);
        int result = findAllAdminsSize();

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void shouldAddTeamToProject() {
        // given
        Team team = createTeam();
        Project project = createProject();
        Team savedTeam = teamDAO.saveOrUpdate(team)
                                .get();
        Project savedProject = projectDAO.saveOrUpdate(project)
                                         .get();
        // when
        permissionManager.addTeamToProject(savedTeam, savedProject);
        List<Project> result = teamDAO.findById(savedTeam.getId())
                                      .get()
                                      .getProjects();
        // then
        assertThat(result).contains(savedProject);
    }

    @Test
    public void shouldRemoveTeamFromProject() {
        // given
        Team team = createTeam();
        Project project = createProject();
        Team savedTeam = teamDAO.saveOrUpdate(team)
                                .get();
        Project savedProject = projectDAO.saveOrUpdate(project)
                                         .get();
        permissionManager.addTeamToProject(savedTeam, savedProject);

        // when
        permissionManager.removeTeamFromProject(savedTeam, savedProject);
        List<Project> result = teamDAO.findById(savedTeam.getId())
                                      .get()
                                      .getProjects();
        // then
        assertThat(result).doesNotContain(savedProject);
    }

    private Project createProject() {
        Project project = new Project();
        project.setCreationDate(LocalDateTime.now());
        project.setKey("projKey");
        project.setName("name");
        return project;
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
        entityManager.createQuery("DELETE FROM TeamEntity ")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM DeveloperEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM ProjectEntity ")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Developer createDeveloper() {
        Developer developer = new Developer();
        developer.setFirstName(FIRSTNAME + nextUniqueValue);
        developer.setSurname(SURNAME + nextUniqueValue);
        developer.setUsername(USERNAME + nextUniqueValue);
        developer.setEmail(EMAIL + nextUniqueValue);
        nextUniqueValue++;
        return developer;
    }

    private Team createTeam() {
        Team team = new Team();
        team.setName("name");
        return team;
    }

    private Admin createAdmin(Developer developer) {
        Admin admin = new Admin();
        admin.setDeveloper(developer);
        return admin;
    }

    private int findAllAdminsSize() {
        return entityManager.createNamedQuery(AdminEntity.FIND_ALL, AdminEntity.class)
                            .getResultList()
                            .size();
    }

}