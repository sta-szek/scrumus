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
        team.addUserToTeam(developer);

        // then
        assertThat(team.getDevelopers()).contains(developer);
    }

    @Test
    public void shouldRemoveUserFromTeam() {
        // given
        Developer developer = new Developer();
        Team team = new Team();
        team.addUserToTeam(developer);

        // when
        team.removeUserFromTeam(developer);

        // then
        assertThat(team.getDevelopers()).doesNotContain(developer);
    }
}