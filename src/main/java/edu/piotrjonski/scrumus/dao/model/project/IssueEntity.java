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
@NamedQueries({@NamedQuery(name = IssueEntity.FIND_ALL, query = IssueEntity.FIND_ALL_QUERY),
               @NamedQuery(name = IssueEntity.FIND_ALL_DEVELOPER_ISSUES, query = IssueEntity.FIND_ALL_DEVELOPER_ISSUES_QUERY),
               @NamedQuery(name = IssueEntity.DELETE_PROJECT_ISSUES, query = IssueEntity.DELETE_PROJECT_ISSUES_QUERY),
               @NamedQuery(name = IssueEntity.FIND_ALL_ISSUES_WITH_ISSUE_TYPE, query = IssueEntity.FIND_ALL_ISSUES_WITH_ISSUE_TYPE_QUERY),
               @NamedQuery(name = IssueEntity.FIND_ALL_ISSUES_WITH_PRIORITY, query = IssueEntity.FIND_ALL_ISSUES_WITH_PRIORITY_QUERY),
               @NamedQuery(name = IssueEntity.FIND_ALL_ISSUE_SUMMARIES, query = IssueEntity.FIND_ALL_ISSUE_SUMMARIES_QUERY),
               @NamedQuery(name = IssueEntity.FIND_ALL_PROJECT_ISSUES, query = IssueEntity.FIND_ALL_PROJECT_ISSUES_QUERY),
               @NamedQuery(name = IssueEntity.FIND_ALL_ISSUES_WITH_STATE, query = IssueEntity.FIND_ALL_ISSUES_WITH_STATE_QUERY)})
public class IssueEntity {

    public static final String FIND_ALL = "findAllIssues";
    public static final String FIND_ALL_PROJECT_ISSUES = "findAllProjectIssues";
    public static final String FIND_ALL_DEVELOPER_ISSUES = "findAllDeveloperIssues";
    public static final String DELETE_PROJECT_ISSUES = "deleteProjectIssues";
    public static final String FIND_ALL_ISSUES_WITH_ISSUE_TYPE = "findAllIssuesWithIssueType";
    public static final String FIND_ALL_ISSUES_WITH_PRIORITY = "findAllIssuesWIthPriority";
    public static final String FIND_ALL_ISSUES_WITH_STATE = "findAllIssuesWIthState";
    public static final String FIND_ALL_ISSUE_SUMMARIES = "findAllIssueSummaries";
    public static final String ID = "id";
    protected static final String FIND_ALL_QUERY = "SELECT i FROM IssueEntity i";
    protected static final String FIND_ALL_PROJECT_ISSUES_QUERY = "SELECT i FROM IssueEntity i WHERE i.projectKey=:" + ProjectEntity.KEY;
    protected static final String FIND_ALL_ISSUE_SUMMARIES_QUERY = "SELECT i.summary FROM IssueEntity i";
    protected static final String DELETE_PROJECT_ISSUES_QUERY = "DELETE FROM IssueEntity i WHERE i.projectKey=:" + ProjectEntity.KEY;
    protected static final String FIND_ALL_DEVELOPER_ISSUES_QUERY =
            "SELECT i FROM IssueEntity i WHERE i.reporter.id=:" + DeveloperEntity.ID + " OR i.assignee.id=:" + DeveloperEntity.ID;
    protected static final String FIND_ALL_ISSUES_WITH_ISSUE_TYPE_QUERY =
            "SELECT i FROM IssueEntity i WHERE i.issueTypeEntity.name=:" + IssueTypeEntity.ISSUE_TYPE_NAME;
    protected static final String FIND_ALL_ISSUES_WITH_PRIORITY_QUERY =
            "SELECT i FROM IssueEntity i WHERE i.priorityEntity.name=:" + PriorityEntity.PRIORITY_NAME;
    protected static final String FIND_ALL_ISSUES_WITH_STATE_QUERY =
            "SELECT i FROM IssueEntity i WHERE i.stateEntity.name=:" + StateEntity.STATE_NAME;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 8, nullable = false, updatable = false)
    private String projectKey;

    @Column(length = 255, nullable = false, unique = true)
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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private StateEntity stateEntity;

    @ManyToOne
    private DeveloperEntity reporter;

    @ManyToOne
    private DeveloperEntity assignee;
}
