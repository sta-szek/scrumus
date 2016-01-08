package edu.piotrjonski.scrumus.dao.model.project;

import edu.piotrjonski.scrumus.dao.model.user.TeamEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents project object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "project")
@NamedQueries({@NamedQuery(name = ProjectEntity.FIND_ALL, query = ProjectEntity.FIND_ALL_QUERY),
               @NamedQuery(name = ProjectEntity.FIND_ALL_SPRINTS, query = ProjectEntity.FIND_ALL_SPRINTS_QUERY),
               @NamedQuery(name = ProjectEntity.FIND_ALL_KEYS, query = ProjectEntity.FIND_ALL_KEYS_QUERY),
               @NamedQuery(name = ProjectEntity.FIND_ALL_NAMES, query = ProjectEntity.FIND_ALL_NAMES_QUERY)})
public class ProjectEntity {

    public static final String FIND_ALL = "findAllProjects";
    public static final String FIND_ALL_SPRINTS = "findAllSprintsForProject";
    public static final String FIND_ALL_KEYS = "findAllKeys";
    public static final String FIND_ALL_NAMES = "findAllNames";
    public static final String KEY = "projectKey";
    protected static final String FIND_ALL_QUERY = "SELECT p FROM ProjectEntity p";
    protected static final String FIND_ALL_SPRINTS_QUERY = "SELECT sprint FROM SprintEntity sprint WHERE sprint.projectEntity.key=:" + KEY;
    protected static final String FIND_ALL_KEYS_QUERY = "SELECT p.key FROM ProjectEntity p";
    protected static final String FIND_ALL_NAMES_QUERY = "SELECT p.name FROM ProjectEntity p";


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

    @ManyToMany(fetch = FetchType.LAZY)
    private List<TeamEntity> teamEntities = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BacklogEntity backlogEntity = new BacklogEntity();
}
