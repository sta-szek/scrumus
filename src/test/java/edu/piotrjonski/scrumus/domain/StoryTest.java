package edu.piotrjonski.scrumus.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class StoryTest {

    @Test
    public void shouldAddIssueToStory() {
        // given
        Story story = new Story();
        Issue comment = new Issue();

        // when
        story.addIssue(comment);

        // then
        assertThat(story.getIssues()).contains(comment);
    }

    @Test
    public void shouldRemoveIssueFromStory() {
        // given
        Story story = new Story();
        Issue comment = new Issue();
        story.addIssue(comment);

        // when
        story.removeIssue(comment);

        // then
        assertThat(story.getIssues()).doesNotContain(comment);
    }

}