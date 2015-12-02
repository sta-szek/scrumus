package edu.piotrjonski.scrumus.dao.model.project;

import edu.piotrjonski.scrumus.dao.model.user.TeamEntity;
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
@Entity
@Table(name = "project")
@NamedQueries({@NamedQuery(name = ProjectEntity.FIND_ALL, query = ProjectEntity.FIND_ALL_QUERY),
               @NamedQuery(name = ProjectEntity.DELETE_BY_KEY, query = ProjectEntity.DELETE_BY_KEY_QUERY)})
public class ProjectEntity {

    public static final String FIND_ALL = "findAllProjects";
    public static final String FIND_ALL_QUERY = "SELECT p FROM ProjectEntity p";
    public static final String DELETE_BY_KEY = "deleteProjectByKey";
    public static final String KEY = "key";
    protected static final String DELETE_BY_KEY_QUERY = "DELETE FROM ProjectEntity p WHERE p.key=:" + KEY;

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
    private List<SprintEntity> sprintEntities;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<TeamEntity> teamEntities;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BacklogEntity backlogEntity = new BacklogEntity();
}
