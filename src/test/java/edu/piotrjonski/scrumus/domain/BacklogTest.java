package edu.piotrjonski.scrumus.domain;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BacklogTest {

    @Test
    public void shouldAddIssueToBacklog() {
        // given
        Issue issue = new Issue();
        Backlog backlog = new Backlog();

        // when
        backlog.addIssue(issue);

        // then
        assertThat(backlog.getIssues()).contains(issue);
    }

    @Test
    public void shouldRemoveIssueFromBacklog() {
        // given
        Issue issue = new Issue();
        Backlog backlog = new Backlog();
        backlog.addIssue(issue);

        // when
        backlog.removeIssue(issue);

        // then
        assertThat(backlog.getIssues()).doesNotContain(issue);
    }
}