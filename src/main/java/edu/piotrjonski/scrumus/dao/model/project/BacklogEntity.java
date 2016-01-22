package edu.piotrjonski.scrumus.dao.model.project;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents backlog object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "backlog")
@NamedQueries({@NamedQuery(name = BacklogEntity.FIND_ALL, query = BacklogEntity.FIND_ALL_QUERY),
               @NamedQuery(name = BacklogEntity.FIND_BY_PROJECT_KEY, query = BacklogEntity.FIND_BY_PROJECT_KEY_QUERY)})
public class BacklogEntity {

    public static final String FIND_ALL = "findAllBacklogs";
    public static final String KEY = "id";
    public static final String FIND_ALL_QUERY = "SELECT p FROM BacklogEntity p";
    public static final String FIND_BY_PROJECT_KEY = "findBacklogByProjectKey";
    protected static final String FIND_BY_PROJECT_KEY_QUERY =
            "SELECT d.backlogEntity FROM ProjectEntity d WHERE d.key=:" + ProjectEntity.KEY;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(fetch = FetchType.LAZY)
    private List<IssueEntity> issueEntities;

}
