package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.TeamDAO;
import edu.piotrjonski.scrumus.dao.model.user.TeamEntity;
import edu.piotrjonski.scrumus.domain.Developer;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(Arquillian.class)
public class TeamManagerIT {

    public static int nextUniqueValue = 0;

    @Inject
    private TeamDAO teamDAO;

    @Inject
    private UserManager userManager;

    @Inject
    private TeamManager teamManager;

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        return UtilsTest.createDeployment();
    }

    @Before
    public void dropAllTeamsAndStartTransaction() throws Exception {
        clearData();
        startTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        userTransaction.commit();
    }

    @Test
    public void shouldCreateTeam() throws AlreadyExistException {
        // given
        Team team = createTeam();

        // when
        Team savedTeam = teamManager.create(team);
        boolean result = teamDAO.exist(savedTeam.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldAddUserToTeam() throws AlreadyExistException {
        // given
        Team team = createTeam();
        Developer developer = createDeveloper();
        Team savedTeam = teamManager.create(team);
        Developer savedDeveloper = userManager.create(developer);

        // when
        teamManager.addUserToTeam(savedDeveloper, savedTeam);
        List<Developer> result = teamDAO.findByKey(savedTeam.getId())
                                        .get()
                                        .getDevelopers();
        // then
        assertThat(result).contains(savedDeveloper);
    }

    @Test
    public void shouldRemoveUserFromTeam() throws AlreadyExistException {
        // given
        Team team = createTeam();
        Developer developer = createDeveloper();
        Team savedTeam = teamManager.create(team);
        Developer savedDeveloper = userManager.create(developer);
        teamManager.addUserToTeam(savedDeveloper, savedTeam);

        // when
        teamManager.removeUserFromTeam(savedDeveloper, savedTeam);
        List<Developer> result = teamDAO.findByKey(savedTeam.getId())
                                        .get()
                                        .getDevelopers();
        // then
        assertThat(result).doesNotContain(savedDeveloper);
    }

    @Test
    public void shouldThrowExceptionWhenTeamAlreadyExist() throws AlreadyExistException {
        // given
        Team team = createTeam();
        Team savedTeam = teamManager.create(team);

        // when
        Throwable throwable = catchThrowable(() -> teamManager.create(savedTeam));

        // then
        assertThat(throwable).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void shouldDeleteTeam() throws AlreadyExistException {
        // given
        Team team = createTeam();
        int id = teamManager.create(team)
                            .getId();

        // when
        teamManager.delete(id);
        int result = findAllTeamsSize();

        // then
        assertThat(result).isEqualTo(0);
    }

    private Developer createDeveloper() {
        return new Developer();
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
        team.setName("name" + nextUniqueValue);
        nextUniqueValue++;
        return team;
    }

    private int findAllTeamsSize() {
        return entityManager.createNamedQuery(TeamEntity.FIND_ALL, TeamEntity.class)
                            .getResultList()
                            .size();
    }
}