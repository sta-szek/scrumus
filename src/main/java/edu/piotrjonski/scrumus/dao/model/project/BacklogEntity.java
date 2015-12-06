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
@NamedQueries({@NamedQuery(name = BacklogEntity.FIND_ALL, query = BacklogEntity.FIND_ALL_QUERY)})
public class BacklogEntity {

    public static final String FIND_ALL = "findAllBacklogs";
    public static final String FIND_ALL_QUERY = "SELECT p FROM BacklogEntity p";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany
    private List<IssueEntity> issueEntities;

}
