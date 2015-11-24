package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.domain.Project;
import edu.piotrjonski.scrumus.domain.Team;
import org.assertj.core.util.Lists;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class TeamDAOIT {

    public static final String NAME = "name";
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static int nextUniqueValue = 0;

    @Inject
    private TeamDAO teamDAO;

    @Inject
    private ProjectDAO projectDAO;

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
    public void shouldUpdate() {
        // given
        String updatedName = "UpdatedName";
        Team team = createTeam();
        team = teamDAO.mapToDomainModel(entityManager.merge(teamDAO.mapToDatabaseModel(team)));
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
        team = teamDAO.mapToDomainModel(entityManager.merge(teamDAO.mapToDatabaseModel(team)));

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

        int id1 = entityManager.merge(teamDAO.mapToDatabaseModel(team1))
                               .getId();
        int id2 = entityManager.merge(teamDAO.mapToDatabaseModel(team2))
                               .getId();
        int id3 = entityManager.merge(teamDAO.mapToDatabaseModel(team3))
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
        int id = entityManager.merge(teamDAO.mapToDatabaseModel(team1))
                              .getId();
        entityManager.merge(teamDAO.mapToDatabaseModel(team2));
        team1.setId(id);

        // when
        Team team = teamDAO.findByKey(id)
                           .get();

        // then
        assertThat(team).isEqualTo(team1);
    }

    @Test
    public void shouldFindEmptyOptional() {
        // given

        // when
        Optional<Team> user = teamDAO.findByKey(0);

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
        entityManager.createQuery("DELETE FROM Team")
                     .executeUpdate();
        userTransaction.commit();
        entityManager.clear();
    }

    private Team createTeam() {
        Team team = new Team();
        team.setName(NAME + nextUniqueValue);
        team.setProjects(createProjects());
        return team;
    }

    private List<Project> createProjects() {
        Project project = new Project();
        project.setName(NAME + nextUniqueValue);
        project.setCreationDate(NOW);
        project.setKey(NAME + nextUniqueValue);
        project = projectDAO.mapToDomainModel(entityManager.merge(projectDAO.mapToDatabaseModel(project)));
        return Lists.newArrayList(project);
    }

    private List<Team> findAll() {
        return entityManager.createQuery("SELECT d FROM Team d")
                            .getResultList();
    }
}