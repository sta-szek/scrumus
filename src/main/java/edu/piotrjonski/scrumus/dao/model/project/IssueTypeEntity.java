package edu.piotrjonski.scrumus.dao.model.project;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents issue type object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "issue_type")
@NamedQueries({@NamedQuery(name = IssueTypeEntity.FIND_ALL, query = IssueTypeEntity.FIND_ALL_QUERY)})
public class IssueTypeEntity {

    public static final String FIND_ALL = "findAllIssueTypes";
    protected static final String FIND_ALL_QUERY = "SELECT i FROM IssueTypeEntity i";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 32, unique = true, nullable = false)
    private String name;
}
