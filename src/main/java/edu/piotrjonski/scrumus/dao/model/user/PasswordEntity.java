package edu.piotrjonski.scrumus.dao.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents password object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "password")
@NamedQueries({@NamedQuery(name = PasswordEntity.FIND_ALL, query = PasswordEntity.FIND_ALL_QUERY)})
public class PasswordEntity {

    public static final String FIND_ALL = "findAllPasswords";
    protected static final String FIND_ALL_QUERY = "SELECT d FROM PasswordEntity d";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 1024, unique = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private DeveloperEntity developerEntity;
}
