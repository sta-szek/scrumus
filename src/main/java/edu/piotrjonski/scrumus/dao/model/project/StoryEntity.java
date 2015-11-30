package edu.piotrjonski.scrumus.dao.model.project;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents story object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "story")
@NamedQueries({@NamedQuery(name = StoryEntity.FIND_ALL, query = StoryEntity.FIND_ALL_QUERY),
               @NamedQuery(name = StoryEntity.DELETE_BY_ID, query = StoryEntity.DELETE_BY_KEY_QUERY)})
public class StoryEntity {

    public static final String FIND_ALL = "findAllStories";
    public static final String FIND_ALL_QUERY = "SELECT p FROM StoryEntity p";
    public static final String DELETE_BY_ID = "deleteStoryByKey";
    public static final String ID = "id";
    protected static final String DELETE_BY_KEY_QUERY = "DELETE FROM StoryEntity p WHERE p.id=:" + ID;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 4096, nullable = true)
    private String definitionOfDone;

    @Column(nullable = false)
    private int points;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<IssueEntity> issueEntities;
}
