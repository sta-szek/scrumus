package edu.piotrjonski.scrumus.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents issue type object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueType {

    private int id;
    private String name;
}
