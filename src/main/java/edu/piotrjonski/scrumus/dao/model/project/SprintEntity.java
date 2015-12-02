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
               @NamedQuery(name = SprintEntity.DELETE_BY_ID, query = SprintEntity.DELETE_BY_KEY_QUERY),
               @NamedQuery(name = SprintEntity.FIND_ALL_STORIES, query = SprintEntity.FIND_ALL_STORIES_QUERY)})
public class SprintEntity {

    public static final String FIND_ALL = "findAllSprints";
    public static final String FIND_ALL_QUERY = "SELECT p FROM SprintEntity p";
    public static final String DELETE_BY_ID = "deleteSprintByKey";
    public static final String FIND_ALL_STORIES = "findAllSprintStories";
    public static final String ID = "id";
    public static final String FIND_ALL_STORIES_QUERY = "SELECT story FROM StoryEntity story WHERE story.sprint.id=:" + ID;
    protected static final String DELETE_BY_KEY_QUERY = "DELETE FROM SprintEntity p WHERE p.id=:" + ID;

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

}
