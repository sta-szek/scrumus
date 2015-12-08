package edu.piotrjonski.scrumus.dao.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents admin object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "admin")
@NamedQueries({@NamedQuery(name = AdminEntity.FIND_ALL, query = AdminEntity.FIND_ALL_QUERY)})
public class AdminEntity {

    public static final String FIND_ALL = "findAllDevelopers";
    protected static final String FIND_ALL_QUERY = "SELECT d FROM DeveloperEntity d";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private DeveloperEntity developerEntity;
}
