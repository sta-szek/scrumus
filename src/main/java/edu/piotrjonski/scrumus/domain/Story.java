package edu.piotrjonski.scrumus.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents story object.
 */
@Data
@NoArgsConstructor
public class Story {
    private int id;
    private String name;
    private String definitionOfDone;
    private int points;
    private List<Issue> issues = new ArrayList<>();
    private int sprintId;

    public void addIssue(final Issue issue) {
        issues.add(issue);
    }

    public void removeIssue(final Issue issue) {
        issues.remove(issue);
    }
}
