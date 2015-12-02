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
               @NamedQuery(name = BacklogEntity.DELETE_BY_ID, query = BacklogEntity.DELETE_BY_KEY_QUERY)})
public class BacklogEntity {

    public static final String FIND_ALL = "findAllBacklogs";
    public static final String FIND_ALL_QUERY = "SELECT p FROM BacklogEntity p";
    public static final String DELETE_BY_ID = "deleteBacklogByKey";
    public static final String ID = "id";
    protected static final String DELETE_BY_KEY_QUERY = "DELETE FROM BacklogEntity p WHERE p.id=:" + ID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany
    private List<IssueEntity> issueEntities;

}
