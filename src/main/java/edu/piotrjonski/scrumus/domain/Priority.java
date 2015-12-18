package edu.piotrjonski.scrumus.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents issue type object.
 */
@Data
@NoArgsConstructor
public class Priority {
    private int id;
    private String name;
}
