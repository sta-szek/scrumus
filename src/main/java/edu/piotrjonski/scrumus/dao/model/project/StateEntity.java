package edu.piotrjonski.scrumus.dao.model.project;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "state")
@NamedQueries({@NamedQuery(name = StateEntity.FIND_ALL, query = StateEntity.FIND_ALL_QUERY)})
public class StateEntity {

    public static final String FIND_ALL = "findAllStates";
    protected static final String FIND_ALL_QUERY = "SELECT p FROM StateEntity p";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 32, unique = true, nullable = false)
    private String name;
}
