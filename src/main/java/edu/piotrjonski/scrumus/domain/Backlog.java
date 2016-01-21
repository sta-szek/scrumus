package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents backlog object.
 */
@Data
@NoArgsConstructor
public class Backlog implements Serializable {
    private int id;
    private List<Issue> issues = new ArrayList<>();

    public void addIssue(Issue issue) {
        issues.add(issue);
    }

    public void removeIssue(Issue issue) {
        issues.remove(issue);
    }
}
