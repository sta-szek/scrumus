package edu.piotrjonski.scrumus.dao.model.project;

import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents issue object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "issue")
@NamedQueries({@NamedQuery(name = IssueEntity.FIND_ALL, query = IssueEntity.FIND_ALL_QUERY)})
public class IssueEntity {

    public static final String FIND_ALL = "findAllIssues";
    protected static final String FIND_ALL_QUERY = "SELECT i FROM IssueEntity i";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 16, nullable = false, unique = true)
    private String key;

    @Column(length = 255, nullable = false)
    private String summary;

    @Column(length = 4096, nullable = true)
    private String description;

    @Column(length = 4096, nullable = true)
    private String definitionOfDone;

    @Column(nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CommentEntity> commentEntities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private IssueTypeEntity issueTypeEntity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private PriorityEntity priorityEntity;

    @ManyToOne(optional = false)
    private DeveloperEntity reporter;

    @ManyToOne
    private DeveloperEntity assignee;
}
