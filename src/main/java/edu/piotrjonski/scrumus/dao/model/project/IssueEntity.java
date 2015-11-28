package edu.piotrjonski.scrumus.dao.model.project;

import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Entity
@Table(name = "issue")
@NamedQueries({@NamedQuery(name = IssueEntity.FIND_ALL, query = IssueEntity.FIND_ALL_QUERY),
               @NamedQuery(name = IssueEntity.DELETE_BY_ID, query = IssueEntity.DELETE_BY_ID_QUERY)})
public class IssueEntity {

    public static final String FIND_ALL = "findAllIssues";
    public static final String DELETE_BY_ID = "deleteIssueById";
    public static final String ID = "id";

    protected static final String FIND_ALL_QUERY = "SELECT i FROM IssueEntity i";
    protected static final String DELETE_BY_ID_QUERY = "DELETE FROM IssueEntity i WHERE i.id=:" + ID;

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

    @OneToOne
    private DeveloperEntity reporter;

    @ManyToOne(optional = false)
    private DeveloperEntity assignee;
}
