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
@NamedQueries({@NamedQuery(name = RoleEntity.FIND_ALL, query = RoleEntity.FIND_ALL_QUERY),
               @NamedQuery(name = RoleEntity.FIND_BY_NAME, query = RoleEntity.FIND_BY_NAME_QUERY)})
public class RoleEntity {

    public static final String FIND_ALL = "findAllRoles";
    public static final String FIND_BY_NAME = "findRoleByName";
    public static final String NAME = "name";
    protected static final String FIND_ALL_QUERY = "SELECT r FROM RoleEntity r";
    protected static final String FIND_BY_NAME_QUERY = "SELECT r FROM RoleEntity r WHERE r.roleType=:" + NAME;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType roleType;

    @ManyToMany
    private List<DeveloperEntity> developerEntities;
}
