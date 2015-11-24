package edu.piotrjonski.scrumus.dao.model.project;

import edu.piotrjonski.scrumus.dao.model.user.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents project object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({@NamedQuery(name = Project.FIND_ALL, query = Project.FIND_ALL_QUERY),
               @NamedQuery(name = Project.DELETE_BY_KEY, query = Project.DELETE_BY_KEY_QUERY)})
@Entity
public class Project {

    public static final String FIND_ALL = "findAllProjects";
    public static final String FIND_ALL_QUERY = "SELECT p FROM Project p";
    public static final String DELETE_BY_KEY = "deleteProjectByKey";
    public static final String KEY = "key";
    protected static final String DELETE_BY_KEY_QUERY = "DELETE FROM Project p WHERE p.key=:" + KEY;

    @Id
    @Column(length = 8, nullable = false, unique = true)
    private String key;

    @Column(length = 255, nullable = false, unique = true)
    private String name;

    @Column(length = 4096, nullable = true)
    private String description;

    @Column(length = 4096, nullable = true)
    private String definitionOfDone;

    @Column(nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sprint> sprints;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Team> teams;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Backlog backlog = new Backlog();
}
