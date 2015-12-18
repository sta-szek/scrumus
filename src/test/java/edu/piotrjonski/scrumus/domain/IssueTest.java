package edu.piotrjonski.scrumus.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IssueTest {

    @Test
    public void shouldAddCommentToIssue() {
        // given
        Issue issue = new Issue();
        Comment comment = new Comment();

        // when
        issue.addComment(comment);

        // then
        assertThat(issue.getComments()).contains(comment);
    }

    @Test
    public void shouldRemoveCommentFromIssue() {
        // given
        Issue issue = new Issue();
        Comment comment = new Comment();
        issue.addComment(comment);

        // when
        issue.removeComment(comment);

        // then
        assertThat(issue.getComments()).doesNotContain(comment);
    }
}