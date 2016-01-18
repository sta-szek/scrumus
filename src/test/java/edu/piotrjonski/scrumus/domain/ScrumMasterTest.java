package edu.piotrjonski.scrumus.domain;


import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ScrumMasterTest {

    @Test
    public void shouldReturnTrueIfScrumMasterHasTeam() {
        // given
        ScrumMaster scrumMaster = new ScrumMaster();
        Team team = new Team();
        scrumMaster.addTeam(team);

        // when
        boolean result = scrumMaster.hasAnyTeam();

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfScrumMasterDoesNotHaveTeam() {
        // given
        ScrumMaster scrumMaster = new ScrumMaster();

        // when
        boolean result = scrumMaster.hasAnyTeam();

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldAddTeam() {
        // given
        ScrumMaster scrumMaster = new ScrumMaster();
        Team team = new Team();

        // when
        scrumMaster.addTeam(team);
        List<Team> result = scrumMaster.getTeams();

        // then
        assertThat(result).contains(team);
    }

    @Test
    public void shouldRemoveTeam() {
        // given
        ScrumMaster scrumMaster = new ScrumMaster();
        Team team = new Team();
        scrumMaster.addTeam(team);

        // when
        scrumMaster.removeTeam(team);
        List<Team> result = scrumMaster.getTeams();

        // then
        assertThat(result).doesNotContain(team);
    }
}