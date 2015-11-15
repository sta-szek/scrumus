package edu.piotrjonski.scrumus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents project datastore object.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({@NamedQuery(name = Project.FIND_BY_ID, query = Project.FIND_BY_ID_QUERY),
               @NamedQuery(name = Project.FIND_BY_KEY, query = Project.FIND_BY_KEY_QUERY)})
public class Project {

    public static final String FIND_BY_ID = "findById";
    public static final String FIND_BY_ID_PARAMETER = "id";
    public static final String FIND_BY_ID_QUERY = "SELECT project FROM Project project WHERE Project.id = :id";
    public static final String FIND_BY_KEY = "findByKey";
    public static final String FIND_BY_KEY_PARAMETER = "key";
    public static final String FIND_BY_KEY_QUERY = "SELECT project FROM Project project WHERE Project.key = :key";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 8, nullable = false, unique = true)
    private String key;

    @Column(nullable = false, length = 255, unique = true)
    private String name;

    @Column(length = 4096, nullable = true)
    private String description;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Sprint> sprints;

    @Column(nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

}
