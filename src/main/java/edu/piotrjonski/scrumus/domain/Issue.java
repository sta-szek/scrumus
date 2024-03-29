package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents issue object.
 */
@Data
@NoArgsConstructor
public class Issue implements Serializable {
    private int id;
    private String projectKey;
    private String summary;
    private String description;
    private String definitionOfDone;
    private LocalDateTime creationDate = LocalDateTime.now();
    private List<Comment> comments = new ArrayList<>();
    private IssueType issueType;
    private Priority priority;
    private State state;
    private int reporterId;
    private int assigneeId;

    public void addComment(final Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }
}
