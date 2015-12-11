package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents password object.
 */
@Data
@NoArgsConstructor
public class Password {
    private int id;
    private String password;
    private int developerId;
}
