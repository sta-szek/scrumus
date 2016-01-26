package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Represents password object.
 */
@Data
@NoArgsConstructor
public class Password implements Serializable {
    private int id;
    private String password;
    private int developerId;
}
