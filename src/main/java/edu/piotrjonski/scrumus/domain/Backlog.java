package edu.piotrjonski.scrumus.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents backlog object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Backlog {
    private int id;
    private List<Issue> issues;

}
