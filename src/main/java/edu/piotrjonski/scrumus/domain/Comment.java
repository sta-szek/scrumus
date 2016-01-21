package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents comment object.
 */
@Data
@NoArgsConstructor
public class Comment implements Serializable {
    private int id;
    private String commentBody;
    private LocalDateTime creationDate = LocalDateTime.now();
    private int developerId;
}
