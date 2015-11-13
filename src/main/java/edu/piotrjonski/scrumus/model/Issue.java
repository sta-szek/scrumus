package edu.piotrjonski.scrumus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents issue datastore object.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 16, nullable = false, unique = true)
    private String key;

    @Column(length = 255, nullable = false)
    private String summary;

    @Column(length = 4096, nullable = true)
    private String description;

}
