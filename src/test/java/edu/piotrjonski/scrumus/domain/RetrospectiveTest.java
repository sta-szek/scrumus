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

    @Test
    public void shouldAddRetrospectiveItemToRetrospective() {
        // given
        Retrospective retrospective = new Retrospective();
        RetrospectiveItem retrospectiveItem = new RetrospectiveItem();

        // when
        retrospective.addRetrospectiveItem(retrospectiveItem);

        // then
        assertThat(retrospective.getRetrospectiveItems()).contains(retrospectiveItem);
    }

    @Test
    public void shouldRemoveRetrospectiveItemFromRetrospective() {
        // given
        Retrospective retrospective = new Retrospective();
        RetrospectiveItem retrospectiveItem = new RetrospectiveItem();
        retrospective.addRetrospectiveItem(retrospectiveItem);

        // when
        retrospective.removeRetrospectiveItem(retrospectiveItem);

        // then
        assertThat(retrospective.getRetrospectiveItems()).doesNotContain(retrospectiveItem);
    }

}