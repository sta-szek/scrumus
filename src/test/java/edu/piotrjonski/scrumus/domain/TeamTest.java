package edu.piotrjonski.scrumus.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamTest {

    @Test
    public void shouldAddUserToTeam() {
        // given
        Developer developer = new Developer();
        Team team = new Team();

        // when
        team.addDeveloper(developer);

        // then
        assertThat(team.getDevelopers()).contains(developer);
    }

    @Test
    public void shouldRemoveUserFromTeam() {
        // given
        Developer developer = new Developer();
        Team team = new Team();
        team.addDeveloper(developer);

        // when
        team.removeDeveloper(developer);

        // then
        assertThat(team.getDevelopers()).doesNotContain(developer);
    }

    @Test
    public void shouldAddProject() {
        // given
        Project project = new Project();
        Team team = new Team();

        // when
        team.addProject(project);

        // then
        assertThat(team.getProjects()).contains(project);
    }

    @Test
    public void shouldRemoveProject() {
        // given
        Project project = new Project();
        Team team = new Team();
        team.addProject(project);

        // when
        team.removeProject(project);

        // then
        assertThat(team.getProjects()).doesNotContain(project);
    }

    @Test
    public void shouldReturnTrueIfContainsUser() {
        // given
        Team team = new Team();
        Developer developer = new Developer();
        team.addDeveloper(developer);

        // when
        boolean result = team.containsUser(developer);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfDoesNotContainUser() {
        // given
        Team team = new Team();
        Developer developer = new Developer();

        // when
        boolean result = team.containsUser(developer);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnTrueIfTeamBelongsToProject() {
        // given
        Team team = new Team();
        Project project = new Project();
        team.addProject(project);

        // when
        boolean result = team.belongsTo(project);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfTeamDoesNotBelongToProject() {
        // given
        Team team = new Team();
        Project project = new Project();

        // when
        boolean result = team.belongsTo(project);

        // then
        assertThat(result).isFalse();
    }
}