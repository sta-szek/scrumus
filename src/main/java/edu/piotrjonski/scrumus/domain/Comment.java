package edu.piotrjonski.scrumus.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents comment object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private int id;
    private String commentBody;
    private LocalDateTime creationDate = LocalDateTime.now();
    private int developerId;
}
