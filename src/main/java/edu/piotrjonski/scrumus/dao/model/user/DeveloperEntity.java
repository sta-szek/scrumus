package edu.piotrjonski.scrumus.dao.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents user object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "developer")
@NamedQueries({@NamedQuery(name = DeveloperEntity.FIND_ALL, query = DeveloperEntity.FIND_ALL_QUERY),
               @NamedQuery(name = DeveloperEntity.FIND_BY_MAIL, query = DeveloperEntity.FIND_BY_EMAIL_QUERY),
               @NamedQuery(name = DeveloperEntity.FIND_ALL_USERNAMES, query = DeveloperEntity.FIND_ALL_USERNAMES_QUERY),
               @NamedQuery(name = DeveloperEntity.FIND_ALL_EMAILS, query = DeveloperEntity.FIND_ALL_EMAILS_QUERY),
               @NamedQuery(name = DeveloperEntity.FIND_BY_USERNAME, query = DeveloperEntity.FIND_BY_USERNAME_QUERY)})
public class DeveloperEntity {

    public static final String FIND_ALL = "findAllDevelopers";
    public static final String FIND_ALL_USERNAMES = "findAllUsernames";
    public static final String FIND_ALL_EMAILS = "findAllEmails";
    public static final String FIND_BY_MAIL = "findByMail";
    public static final String FIND_BY_USERNAME = "findByUserName";
    public static final String ID = "id";
    public static final String EMAIL = "id";
    public static final String USERNAME = "username";
    protected static final String FIND_ALL_QUERY = "SELECT d FROM DeveloperEntity d";
    protected static final String FIND_BY_EMAIL_QUERY = "SELECT d FROM DeveloperEntity d WHERE d.email=:" + EMAIL;
    protected static final String FIND_ALL_USERNAMES_QUERY = "SELECT d.username FROM DeveloperEntity d";
    protected static final String FIND_ALL_EMAILS_QUERY = "SELECT d.email FROM DeveloperEntity d";
    protected static final String FIND_BY_USERNAME_QUERY = "SELECT d FROM DeveloperEntity d WHERE d.username=:" + USERNAME;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 30, unique = false)
    private String firstName;

    @Column(nullable = false, length = 30, unique = false)
    private String surname;

    @Column(nullable = false, length = 20, unique = true)
    private String username;

    @Column(nullable = false, length = 40, unique = true)
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PictureEntity pictureEntity;

}
