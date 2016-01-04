package edu.piotrjonski.scrumus.dao.model.security;

import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
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
@NamedQueries({@NamedQuery(name = PasswordEntity.FIND_ALL, query = PasswordEntity.FIND_ALL_QUERY),
               @NamedQuery(name = PasswordEntity.FIND_USER_PASSWORD, query = PasswordEntity.FIND_USER_PASSWORD_QUERY)})
public class PasswordEntity {

    public static final String FIND_ALL = "findAllPasswords";
    public static final String FIND_USER_PASSWORD = "findUserPassword";
    protected static final String FIND_ALL_QUERY = "SELECT d FROM PasswordEntity d";
    protected static final String FIND_USER_PASSWORD_QUERY =
            "SELECT d FROM PasswordEntity d WHERE d.developerEntity.id=:" + DeveloperEntity.ID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 1024, unique = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private DeveloperEntity developerEntity;
}
