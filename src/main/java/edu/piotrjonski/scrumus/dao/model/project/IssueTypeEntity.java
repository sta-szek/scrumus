package edu.piotrjonski.scrumus.dao.model.project;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents issue type object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "issue_type")
@NamedQueries({@NamedQuery(name = IssueTypeEntity.FIND_ALL, query = IssueTypeEntity.FIND_ALL_QUERY),
               @NamedQuery(name = IssueTypeEntity.DELETE_BY_ID, query = IssueTypeEntity.DELETE_BY_ID_QUERY)})
public class IssueTypeEntity {

    public static final String FIND_ALL = "findAllIssueTypes";
    public static final String DELETE_BY_ID = "deleteIssueTypeById";
    public static final String ID = "id";

    protected static final String FIND_ALL_QUERY = "SELECT i FROM IssueTypeEntity i";
    protected static final String DELETE_BY_ID_QUERY = "DELETE FROM IssueTypeEntity i WHERE i.id=:" + ID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 32, unique = true, nullable = false)
    private String name;
}
