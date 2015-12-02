package edu.piotrjonski.scrumus.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents issue type object.
 */
@Data
@NoArgsConstructor
public class IssueType {

    private int id;
    private String name;
}
