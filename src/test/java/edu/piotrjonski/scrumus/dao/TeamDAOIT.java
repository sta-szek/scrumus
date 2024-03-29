package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.TeamEntity;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Project;
import edu.piotrjonski.scrumus.domain.Team;
import edu.piotrjonski.scrumus.utils.TestUtils;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class TeamDAOIT {

    public static final String NAME = "abcdge";
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static int nextUniqueValue = 0;

    @Inject
    private TeamDAO teamDAO;

    @Inject
    private ProjectDAO projectDAO;

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
        Team team = createTeam();

        // when
        teamDAO.saveOrUpdate(team);

        // then
        assertThat(findAll().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnTrueIfExist() {
        // given
        Team team = createTeam();
        int entityId = teamDAO.saveOrUpdate(team)
                              .get()
                              .getId();

        // when
        boolean result = teamDAO.exist(entityId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotExist() {
        // given

        // when
        boolean result = teamDAO.exist(1);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldUpdate() {
        // given
        String updatedName = "UpdatedName";
        Team team = createTeam();
        team = teamDAO.mapToDomainModelIfNotNull(entityManager.merge(teamDAO.mapToDatabaseModelIfNotNull(team)));
        team.setName(updatedName);

        // when
        team = teamDAO.saveOrUpdate(team)
                      .get();

        // then
        assertThat(team.getName()).isEqualTo(updatedName);
    }

    @Test
    public void shouldDelete() {
        // given
        Team team = createTeam();
        team = teamDAO.mapToDomainModelIfNotNull(entityManager.merge(teamDAO.mapToDatabaseModelIfNotNull(team)));

        // when
        teamDAO.delete(team.getId());
        int allUsers = findAll().size();

        // then
        assertThat(allUsers).isEqualTo(0);
    }

    @Test
    public void shouldFindAll() {
        // given
        Team team1 = createTeam();
        Team team2 = createTeam();
        Team team3 = createTeam();

        int id1 = entityManager.merge(teamDAO.mapToDatabaseModelIfNotNull(team1))
                               .getId();
        int id2 = entityManager.merge(teamDAO.mapToDatabaseModelIfNotNull(team2))
                               .getId();
        int id3 = entityManager.merge(teamDAO.mapToDatabaseModelIfNotNull(team3))
                               .getId();

        team1.setId(id1);
        team2.setId(id2);
        team3.setId(id3);

        // when
        List<Team> teams = teamDAO.findAll();

        // then
        assertThat(teams).hasSize(3)
                         .contains(team1)
                         .contains(team2)
                         .contains(team3);
    }

    @Test
    public void shouldFindByKey() {
        // given
        Team team1 = createTeam();
        Team team2 = createTeam();
        int id = entityManager.merge(teamDAO.mapToDatabaseModelIfNotNull(team1))
                              .getId();
        entityManager.merge(teamDAO.mapToDatabaseModelIfNotNull(team2));
        team1.setId(id);

        // when
        Team team = teamDAO.findById(id)
                           .get();

        // then
        assertThat(team).isEqualTo(team1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Team> user = teamDAO.findById(0);

        // then
        assertThat(user).isEmpty();
    }

    @Test
    public void shouldFindAllDeveloperTeams() {
        // given
        Developer developer = new Developer();
        developer.setEmail("test");
        developer.setFirstName("test");
        developer.setSurname("test");
        developer.setUsername("test");
        developer = developerDAO.saveOrUpdate(developer)
                                .get();
        Team team1 = createTeam();
        Team team2 = createTeam();
        team1.addDeveloper(developer);
        team2.addDeveloper(developer);
        team1 = teamDAO.saveOrUpdate(team1)
                       .get();
        team2 = teamDAO.saveOrUpdate(team2)
                       .get();

        // when
        List<Team> result = teamDAO.findAllDeveloperTeams(developer);

        // then
        assertThat(result).contains(team1)
                          .contains(team2);
    }

    @Test
    public void shouldReturnEmptyListIfDeveloperDoesNotExist() {
        // given

        // when
        List<Team> result = teamDAO.findAllDeveloperTeams(new Developer());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldFindAllProjectTeams() {
        // given
        Project project = createProjects().get(0);
        project = projectDAO.saveOrUpdate(project)
                            .get();
        Team team1 = createTeam();
        Team team2 = createTeam();
        team1.addProject(project);
        team2.addProject(project);
        team1 = teamDAO.saveOrUpdate(team1)
                       .get();
        team2 = teamDAO.saveOrUpdate(team2)
                       .get();

        // when
        List<Team> result = teamDAO.findAllTeamsForProject(project.getKey());

        // then
        assertThat(result).contains(team1)
                          .contains(team2);
    }

    @Test
    public void shouldReturnEmptyListIfProjectDoesNotExist() {
        // given

        // when
        List<Team> result = teamDAO.findAllTeamsForProject("test");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldFindAllNames() {
        // given
        Team team1 = createTeam();
        Team team2 = createTeam();
        String name1 = teamDAO.saveOrUpdate(team1)
                              .get()
                              .getName();
        String name2 = teamDAO.saveOrUpdate(team2)
                              .get()
                              .getName();

        // when
        List<String> result = teamDAO.findAllNames();

        // then
        assertThat(result).contains(name1)
                          .contains(name2);
    }

    @Test
    public void shouldFindTeamByName() {
        // given
        Team team = createTeam();
        team = teamDAO.saveOrUpdate(team)
                      .get();

        // when
        Team result = teamDAO.findByName(team.getName())
                             .get();

        // then
        assertThat(result).isEqualTo(team);
    }

    @Test
    public void shouldReturnEmptyOptionalIfTeamWasNotFound() {
        // given

        // when
        Optional<Team> result = teamDAO.findByName("test");

        // then
        assertThat(result).isEmpty();
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        userTransaction.begin();
        entityManager.joinTransaction();
    }

    private void clearData() throws Exception {
        userTransaction.begin();
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM TeamEntity")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Team createTeam() {
        Team team = new Team();
        team.setName(NAME + nextUniqueValue);
        team.setProjects(createProjects());
        nextUniqueValue++;
        return team;
    }

    private List<Project> createProjects() {
        Project project = new Project();
        project.setName(NAME + nextUniqueValue);
        project.setCreationDate(NOW);
        project.setKey(NAME + nextUniqueValue);
        project = projectDAO.mapToDomainModelIfNotNull(entityManager.merge(projectDAO.mapToDatabaseModelIfNotNull(
                project)));
        return Lists.newArrayList(project);
    }

    private List<TeamEntity> findAll() {
        return entityManager.createQuery("SELECT d FROM TeamEntity d")
                            .getResultList();
    }
}