package edu.piotrjonski.scrumus.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Represents issue type object.
 */
@Data
@NoArgsConstructor
public class Priority implements Serializable {
    private int id;
    private String name;
}
