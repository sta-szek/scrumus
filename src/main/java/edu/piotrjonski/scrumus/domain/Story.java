package edu.piotrjonski.scrumus.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<Issue> issues;
    private int sprintId;
}
