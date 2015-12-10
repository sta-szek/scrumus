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
@NamedQueries({@NamedQuery(name = AdminEntity.FIND_ALL, query = AdminEntity.FIND_ALL_QUERY),
               @NamedQuery(name = AdminEntity.FIND_BY_DEVELOPER_ID, query = AdminEntity.FIND_BY_DEVELOPER_ID_QUERY)})
public class AdminEntity {

    public static final String FIND_ALL = "findAllAdmins";
    public static final String FIND_BY_DEVELOPER_ID = "findAdminByDeveloperId";
    public static final String ID = "id";
    protected static final String FIND_ALL_QUERY = "SELECT d FROM AdminEntity d";
    protected static final String FIND_BY_DEVELOPER_ID_QUERY = "SELECT d FROM AdminEntity d WHERE d.developerEntity.id=:" + ID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private DeveloperEntity developerEntity;
}
