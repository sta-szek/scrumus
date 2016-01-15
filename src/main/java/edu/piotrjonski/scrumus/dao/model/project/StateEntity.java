package edu.piotrjonski.scrumus.dao.model.project;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "state")
@NamedQueries({@NamedQuery(name = StateEntity.FIND_ALL, query = StateEntity.FIND_ALL_QUERY),
               @NamedQuery(name = StateEntity.FIND_ALL_NAMES, query = StateEntity.FIND_ALL_NAMES_QUERY)})
public class StateEntity {

    public static final String FIND_ALL = "findAllStates";
    public static final String STATE_NAME = "stateName";
    public static final String FIND_ALL_NAMES = "findAllStateNames";
    protected static final String FIND_ALL_QUERY = "SELECT p FROM StateEntity p";
    protected static final String FIND_ALL_NAMES_QUERY = "SELECT p.name FROM StateEntity p";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 32, unique = true, nullable = false)
    private String name;
}
