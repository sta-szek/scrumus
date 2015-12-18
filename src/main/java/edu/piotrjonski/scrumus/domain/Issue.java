package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents issue object.
 */
@Data
@NoArgsConstructor
public class Issue {
    private int id;
    private String projectKey;
    private String summary;
    private String description;
    private String definitionOfDone;
    private LocalDateTime creationDate = LocalDateTime.now();
    private List<Comment> comments = new ArrayList<>();
    private IssueType issueType;
    private Priority priority;
    private int reporterId;
    private int assigneeId;
}
