package edu.piotrjonski.scrumus.model.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents sprint datastore object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(length = 4096, nullable = true)
    private String definitionOfDone;

    @Embedded
    private TimeRange timeRange;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Story> stories;

}
