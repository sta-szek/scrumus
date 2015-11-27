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
@Table(name = "developer")
@NamedQueries({@NamedQuery(name = DeveloperEntity.FIND_ALL, query = DeveloperEntity.FIND_ALL_QUERY),
               @NamedQuery(name = DeveloperEntity.DELETE_BY_ID, query = DeveloperEntity.DELETE_BY_ID_QUERY)})
public class DeveloperEntity {

    public static final String FIND_ALL = "findAllDevelopers";
    public static final String DELETE_BY_ID = "deleteDeveloperById";
    public static final String ID = "id";

    protected static final String FIND_ALL_QUERY = "SELECT d FROM DeveloperEntity d";
    protected static final String DELETE_BY_ID_QUERY = "DELETE FROM DeveloperEntity d WHERE d.id=:" + ID;


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

    @ManyToMany(mappedBy = "developerEntities")
    private List<TeamEntity> teamEntities;
}