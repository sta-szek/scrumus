package edu.piotrjonski.scrumus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents project datastore object.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@NamedQueries({@NamedQuery(name = Project.FIND_BY_ID,
                           query = "SELECT project FROM Project project WHERE Project.id = :id"),
               @NamedQuery(name = Project.FIND_BY_KEY,
                           query = "SELECT project FROM Project project WHERE Project.key = :key")})
public class Project {

    public static final String FIND_BY_ID = "findById";
    public static final String FIND_BY_KEY = "findByKey";
    public static final String FIND_BY_ID_PARAMETER = "id";
    public static final String FIND_BY_KEY_PARAMETER = "key";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 8, nullable = false, unique = true)
    private String key;

    @Column(nullable = false, length = 255, unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Sprint> sprints;

}
