package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents password object.
 */
@Data
@NoArgsConstructor
public class Role {
    private int id;
    private String name;
    private List<Developer> developers;
}
