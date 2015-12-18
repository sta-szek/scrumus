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
@Table(name = "priority")
@NamedQueries({@NamedQuery(name = PriorityEntity.FIND_ALL, query = PriorityEntity.FIND_ALL_QUERY)})
public class PriorityEntity {

    public static final String FIND_ALL = "findAllPriorities";
    protected static final String FIND_ALL_QUERY = "SELECT p FROM PriorityEntity p";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 32, unique = true, nullable = false)
    private String name;
}
