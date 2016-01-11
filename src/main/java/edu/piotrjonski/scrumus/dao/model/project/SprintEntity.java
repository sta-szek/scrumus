package edu.piotrjonski.scrumus.dao.model.project;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents sprint datastore object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "sprint")
@NamedQueries({@NamedQuery(name = SprintEntity.FIND_ALL, query = SprintEntity.FIND_ALL_QUERY),
               @NamedQuery(name = SprintEntity.FIND_ALL_STORIES_FOR_SPRINT, query = SprintEntity.FIND_ALL_STORIES_FOR_SPRINT_QUERY),
               @NamedQuery(name = SprintEntity.DELETE_SPRINTS_FROM_PROJECT, query = SprintEntity.DELETE_SPRINTS_FROM_PROJECT_QUERY)})
public class SprintEntity {

    public static final String FIND_ALL = "findAllSprints";
    public static final String FIND_ALL_STORIES_FOR_SPRINT = "findAllSprintStories";
    public static final String DELETE_SPRINTS_FROM_PROJECT = "deleteSprintsFromProject";
    public static final String ID = "id";
    protected static final String FIND_ALL_QUERY = "SELECT p FROM SprintEntity p";
    protected static final String FIND_ALL_STORIES_FOR_SPRINT_QUERY = "SELECT story FROM StoryEntity story WHERE story.sprint.id=:" + ID;
    protected static final String DELETE_SPRINTS_FROM_PROJECT_QUERY =
            "DELETE FROM SprintEntity s WHERE s.project.key=:" + ProjectEntity.KEY;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(length = 4096, nullable = true)
    private String definitionOfDone;

    @Embedded
    private TimeRange timeRange;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RetrospectiveEntity retrospectiveEntity;

    @ManyToOne(optional = false)
    private ProjectEntity project;
}
