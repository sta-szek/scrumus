package edu.piotrjonski.scrumus.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class RetrospectiveTest {

    @Test
    public void shouldAddCommentToRetrospective() {
        // given
        Retrospective retrospective = new Retrospective();
        Comment comment = new Comment();

        // when
        retrospective.addComment(comment);

        // then
        assertThat(retrospective.getComments()).contains(comment);
    }

    @Test
    public void shouldRemoveCommentFromRetrospective() {
        // given
        Retrospective retrospective = new Retrospective();
        Comment comment = new Comment();
        retrospective.addComment(comment);

        // when
        retrospective.removeComment(comment);

        // then
        assertThat(retrospective.getComments()).doesNotContain(comment);
    }

}