package edu.piotrjonski.scrumus.dao.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents user object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQueries({@NamedQuery(name = Developer.FIND_ALL, query = Developer.FIND_ALL_QUERY)})
public class Developer {

    public static final String FIND_ALL = "findAll";

    protected static final String FIND_ALL_QUERY = "SELECT d FROM Developer d";

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

    @OneToOne
    private Picture picture;

    @ManyToMany(mappedBy = "developers")
    private List<Team> teams;
}
