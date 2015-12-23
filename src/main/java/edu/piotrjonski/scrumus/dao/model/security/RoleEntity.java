package edu.piotrjonski.scrumus.dao.model.security;

import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents password object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "role")
@NamedQueries({@NamedQuery(name = RoleEntity.FIND_ALL, query = RoleEntity.FIND_ALL_QUERY)})
public class RoleEntity {

    public static final String FIND_ALL = "findAllRoles";
    protected static final String FIND_ALL_QUERY = "SELECT r FROM RoleEntity r";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 32, unique = false)
    private String name;

    @ManyToMany
    private List<DeveloperEntity> developerEntities;
}
