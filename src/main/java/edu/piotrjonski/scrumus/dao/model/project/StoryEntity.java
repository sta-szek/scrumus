package edu.piotrjonski.scrumus.dao.model.project;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents story object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "story")
@NamedQueries({@NamedQuery(name = StoryEntity.FIND_ALL, query = StoryEntity.FIND_ALL_QUERY),
               @NamedQuery(name = StoryEntity.DELETE_STORIES_FROM_PROJECT, query = StoryEntity.DELETE_STORIES_FROM_PROJECT_QUERY)})
public class StoryEntity {

    public static final String FIND_ALL = "findAllStories";
    public static final String DELETE_STORIES_FROM_PROJECT = "deleteStoriesFromProject";
    protected static final String FIND_ALL_QUERY = "SELECT p FROM StoryEntity p";
    protected static final String DELETE_STORIES_FROM_PROJECT_QUERY =
            "DELETE FROM StoryEntity s WHERE s.sprint IN (SELECT sp FROM SprintEntity sp WHERE sp.project.key=:" + ProjectEntity.KEY + ")";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 4096, nullable = true)
    private String definitionOfDone;

    @Column
    private int points;

    @OneToMany(fetch = FetchType.EAGER)
    private List<IssueEntity> issueEntities;

    @ManyToOne(optional = false)
    private SprintEntity sprint;
}
