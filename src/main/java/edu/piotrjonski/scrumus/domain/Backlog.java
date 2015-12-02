package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents backlog object.
 */
@Data
@NoArgsConstructor
public class Backlog {
    private int id;
    private List<Issue> issues = new ArrayList<>();

}
