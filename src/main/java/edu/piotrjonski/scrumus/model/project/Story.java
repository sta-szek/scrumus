package edu.piotrjonski.scrumus.model.project;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents story object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 4096, nullable = true)
    private String definitionOfDone;

    @Column(nullable = false)
    private int points;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Issue> issues;
}
