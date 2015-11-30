package edu.piotrjonski.scrumus.dao.model.project;

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
@Table(name = "sprint")
@NamedQueries({@NamedQuery(name = SprintEntity.FIND_ALL, query = SprintEntity.FIND_ALL_QUERY),
               @NamedQuery(name = SprintEntity.DELETE_BY_ID, query = SprintEntity.DELETE_BY_KEY_QUERY)})
public class SprintEntity {

    public static final String FIND_ALL = "findAllSprints";
    public static final String FIND_ALL_QUERY = "SELECT p FROM SprintEntity p";
    public static final String DELETE_BY_ID = "deleteSprintByKey";
    public static final String ID = "id";
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StoryEntity> stories;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RetrospectiveEntity retrospectiveEntity;

}
