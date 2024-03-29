package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.*;
import edu.piotrjonski.scrumus.dao.model.security.RoleType;
import edu.piotrjonski.scrumus.dao.model.user.AdminEntity;
import edu.piotrjonski.scrumus.domain.*;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private ProductOwnerDAO productOwnerDAO;

    @Inject
    private PermissionManager permissionManager;

    @Inject
    private UserTransaction userTransaction;

    @Inject
    private RoleDAO roleDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return TestUtils.createDeployment();
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
        Role role = roleDAO.saveOrUpdate(createAdminRole())
                           .get();
        role.addDeveloper(developer);
        roleDAO.saveOrUpdate(role);

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
        Role role = roleDAO.saveOrUpdate(createAdminRole())
                           .get();
        role.addDeveloper(developer);
        roleDAO.saveOrUpdate(role);

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
    public void shouldNotAddTeamToProjectIfTeamDoesNotExist() {
        // given
        Team team = createTeam();
        Project project = createProject();
        Project savedProject = projectDAO.saveOrUpdate(project)
                                         .get();
        // when
        permissionManager.addTeamToProject(team, savedProject);
        Optional<Team> result = teamDAO.findById(team.getId());
        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldNotAddTeamToProjectIfProjectDoesNotExist() {
        // given
        Team team = createTeam();
        Project project = createProject();
        Team savedTeam = teamDAO.saveOrUpdate(team)
                                .get();
        // when
        permissionManager.addTeamToProject(savedTeam, project);
        List<Project> result = teamDAO.findById(savedTeam.getId())
                                      .get()
                                      .getProjects();
        // then
        assertThat(result).doesNotContain(project);
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

    @Test
    public void shouldReturnTrueIfIsProductOwner() {
        // given
        Project project = createProject();
        Developer developer = createDeveloper();
        developer = developerDAO.saveOrUpdate(developer)
                                .get();
        project = projectDAO.saveOrUpdate(project)
                            .get();

        ProductOwner productOwner = new ProductOwner();
        productOwner.setDeveloper(developer);
        productOwner.setProject(project);

        productOwnerDAO.saveOrUpdate(productOwner);

        Role role = roleDAO.saveOrUpdate(createProductOwnerRole())
                           .get();
        role.addDeveloper(developer);
        roleDAO.saveOrUpdate(role);

        // when
        boolean result = permissionManager.isProductOwner(project, developer);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfIsNotProductOwner() {
        // given
        Project project = createProject();
        Developer developer = createDeveloper();
        developer = developerDAO.saveOrUpdate(developer)
                                .get();
        project = projectDAO.saveOrUpdate(project)
                            .get();
        // when
        boolean result = permissionManager.isProductOwner(project, developer);

        // then
        assertThat(result).isFalse();
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
        entityManager.createQuery("DELETE FROM RoleEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM AdminEntity")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM TeamEntity ")
                     .executeUpdate();
        entityManager.createQuery("DELETE FROM ProductOwnerEntity")
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

    private Role createAdminRole() {
        Role role = new Role();
        role.setRoleType(RoleType.ADMIN);
        return role;
    }

    private Role createProductOwnerRole() {
        Role role = new Role();
        role.setRoleType(RoleType.PRODUCT_OWNER);
        return role;
    }

    private int findAllAdminsSize() {
        return entityManager.createNamedQuery(AdminEntity.FIND_ALL, AdminEntity.class)
                            .getResultList()
                            .size();
    }

}