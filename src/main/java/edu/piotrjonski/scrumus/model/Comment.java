package edu.piotrjonski.scrumus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents comment datastore object.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 4096, nullable = false)
    private String commentBody;

    @Column(nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();


}
